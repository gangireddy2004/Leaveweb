package com.leaveweb.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.leaveweb.dto.LeaveRequestDto;
import com.leaveweb.dto.LeaveResponse;
import com.leaveweb.exception.BusinessException;
import com.leaveweb.exception.ResourceNotFoundException;
import com.leaveweb.model.LeaveRequest;
import com.leaveweb.model.LeaveStatus;
import com.leaveweb.model.LeaveType;
import com.leaveweb.model.NotificationType;
import com.leaveweb.model.Role;
import com.leaveweb.model.User;
import com.leaveweb.repository.LeaveRequestRepository;

@Service
public class LeaveService {
    private static final Logger log = LoggerFactory.getLogger(LeaveService.class);
    private final LeaveRequestRepository repository; private final LeaveTypeService leaveTypeService; private final UserService userService; private final NotificationService notificationService; private final EmailService emailService;
    public LeaveService(LeaveRequestRepository repository, LeaveTypeService leaveTypeService, UserService userService, NotificationService notificationService, EmailService emailService) { this.repository = repository; this.leaveTypeService = leaveTypeService; this.userService = userService; this.notificationService = notificationService; this.emailService = emailService; }

    @Transactional public LeaveResponse create(String email, LeaveRequestDto dto) {
        User user = userService.getByEmail(email); LeaveType type = leaveTypeService.get(dto.leaveTypeId()); validateDates(dto.startDate(), dto.endDate()); long days = daysBetween(dto.startDate(), dto.endDate());
        if (!type.isActive()) throw new BusinessException("Leave type is inactive");
        long used = repository.findByUserIdAndStatusIn(user.getId(), List.of(LeaveStatus.PENDING, LeaveStatus.APPROVED)).stream().filter(item -> item.getLeaveTypeId().equals(type.getId())).mapToLong(LeaveRequest::getNumberOfDays).sum();
        if (used + days > type.getAnnualAllowance()) throw new BusinessException("Leave balance exceeded for " + type.getName());
        LeaveRequest leave = new LeaveRequest(); leave.setUserId(user.getId()); leave.setLeaveTypeId(type.getId()); leave.setStartDate(dto.startDate()); leave.setEndDate(dto.endDate()); leave.setNumberOfDays(days); leave.setReason(dto.reason()); leave.setSupportingDocument(dto.supportingDocument()); leave.setStatus(LeaveStatus.PENDING); leave.setAppliedAt(Instant.now());
        LeaveRequest saved = repository.save(leave); notificationService.create(user.getId(), "Leave request submitted", "Your leave request is waiting for approval.", NotificationType.LEAVE_SUBMITTED); log.info("Leave request {} created for user {}", saved.getId(), user.getId()); return toResponse(saved);
    }
    public List<LeaveResponse> getForUser(String email) { User user = userService.getByEmail(email); return repository.findByUserIdOrderByAppliedAtDesc(user.getId()).stream().map(this::toResponse).toList(); }
    public List<LeaveResponse> getAll() { return repository.findAllByOrderByAppliedAtDesc().stream().map(this::toResponse).toList(); }
    public LeaveResponse getById(String email, String id) { LeaveRequest leave = get(id); User requester = userService.getByEmail(email); if (!requester.getRole().equals(Role.ADMIN) && !leave.getUserId().equals(requester.getId())) throw new AccessDeniedException("You cannot access this leave request"); return toResponse(leave); }
    @Transactional public LeaveResponse update(String email, String id, LeaveRequestDto dto) { LeaveRequest leave = get(id); User user = userService.getByEmail(email); if (!leave.getUserId().equals(user.getId())) throw new AccessDeniedException("You cannot update this leave request"); if (leave.getStatus() != LeaveStatus.PENDING) throw new BusinessException("Only pending requests can be updated"); leaveTypeService.get(dto.leaveTypeId()); validateDates(dto.startDate(), dto.endDate()); leave.setLeaveTypeId(dto.leaveTypeId()); leave.setStartDate(dto.startDate()); leave.setEndDate(dto.endDate()); leave.setNumberOfDays(daysBetween(dto.startDate(), dto.endDate())); leave.setReason(dto.reason()); leave.setSupportingDocument(dto.supportingDocument()); return toResponse(repository.save(leave)); }
    public void delete(String email, String id) { LeaveRequest leave = get(id); User user = userService.getByEmail(email); if (!leave.getUserId().equals(user.getId())) throw new AccessDeniedException("You cannot delete this leave request"); if (leave.getStatus() != LeaveStatus.PENDING) throw new BusinessException("Only pending requests can be deleted"); repository.delete(leave); }
    @Transactional public LeaveResponse cancel(String email, String id) { LeaveRequest leave = get(id); User user = userService.getByEmail(email); if (!leave.getUserId().equals(user.getId())) throw new AccessDeniedException("You cannot cancel this leave request"); if (leave.getStatus() != LeaveStatus.PENDING) throw new BusinessException("Only pending requests can be cancelled"); leave.setStatus(LeaveStatus.CANCELLED); return toResponse(repository.save(leave)); }
    @Transactional public LeaveResponse approve(String adminEmail, String id) { LeaveRequest leave = get(id); if (leave.getStatus() != LeaveStatus.PENDING) throw new BusinessException("Only pending requests can be approved"); User admin = userService.getByEmail(adminEmail); leave.setStatus(LeaveStatus.APPROVED); leave.setReviewedAt(Instant.now()); leave.setReviewedBy(admin.getId()); LeaveRequest saved = repository.save(leave); notificationService.create(leave.getUserId(), "Leave request approved", "Your leave request has been approved.", NotificationType.LEAVE_APPROVED); User employee = userService.getById(leave.getUserId()); emailService.sendLeaveApprovedEmail(employee.getEmail(), "Your leave request has been approved.\nLeave type: " + leaveTypeService.get(leave.getLeaveTypeId()).getName() + "\nDates: " + leave.getStartDate() + " to " + leave.getEndDate() + "\nWorking days: " + leave.getNumberOfDays()); log.info("Leave request {} approved by {}", id, adminEmail); return toResponse(saved); }
    @Transactional public LeaveResponse reject(String adminEmail, String id, String reason) { LeaveRequest leave = get(id); if (leave.getStatus() != LeaveStatus.PENDING) throw new BusinessException("Only pending requests can be rejected"); User admin = userService.getByEmail(adminEmail); leave.setStatus(LeaveStatus.REJECTED); leave.setReviewedAt(Instant.now()); leave.setReviewedBy(admin.getId()); leave.setRejectionReason(reason); LeaveRequest saved = repository.save(leave); notificationService.create(leave.getUserId(), "Leave request rejected", reason, NotificationType.LEAVE_REJECTED); User employee = userService.getById(leave.getUserId()); emailService.sendLeaveRejectedEmail(employee.getEmail(), "Your leave request has been rejected.\nLeave type: " + leaveTypeService.get(leave.getLeaveTypeId()).getName() + "\nDates: " + leave.getStartDate() + " to " + leave.getEndDate() + "\nWorking days: " + leave.getNumberOfDays() + "\nReason: " + reason); log.info("Leave request {} rejected by {}", id, adminEmail); return toResponse(saved); }
    public long daysBetween(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) return 0;
        long workingDays = 0;
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            switch (date.getDayOfWeek()) {
                case SATURDAY, SUNDAY -> { }
                default -> workingDays++;
            }
        }
        return workingDays;
    }
    private void validateDates(LocalDate start, LocalDate end) { if (start.isBefore(LocalDate.now())) throw new BusinessException("Start date cannot be in the past"); if (end.isBefore(start)) throw new BusinessException("End date must be on or after start date"); }
    private LeaveRequest get(String id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Leave request not found")); }
    private LeaveResponse toResponse(LeaveRequest item) { User user = userService.getById(item.getUserId()); LeaveType type = leaveTypeService.get(item.getLeaveTypeId()); return new LeaveResponse(item.getId(), item.getUserId(), user.getFullName(), user.getEmail(), item.getLeaveTypeId(), type.getName(), item.getStartDate(), item.getEndDate(), item.getNumberOfDays(), item.getReason(), item.getSupportingDocument(), item.getStatus(), item.getAppliedAt(), item.getReviewedAt(), item.getReviewedBy(), item.getRejectionReason()); }
}