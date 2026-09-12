package com.leaveweb.service;

import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import com.leaveweb.dto.ProfileChangeRequestDto;
import com.leaveweb.dto.ProfileChangeResponse;
import com.leaveweb.exception.BusinessException;
import com.leaveweb.exception.ResourceNotFoundException;
import com.leaveweb.model.NotificationType;
import com.leaveweb.model.ProfileChangeStatus;
import com.leaveweb.model.User;
import com.leaveweb.model.UserProfileChangeRequest;
import com.leaveweb.repository.UserProfileChangeRequestRepository;

@Service
public class ProfileChangeRequestService {
    private final UserProfileChangeRequestRepository repository; private final UserService users; private final NotificationService notifications; private final EmailService email;
    public ProfileChangeRequestService(UserProfileChangeRequestRepository repository, UserService users, NotificationService notifications, EmailService email) { this.repository = repository; this.users = users; this.notifications = notifications; this.email = email; }
    public ProfileChangeResponse create(String emailAddress, ProfileChangeRequestDto dto) { User user = users.getByEmail(emailAddress); UserProfileChangeRequest request = new UserProfileChangeRequest(); request.setUserId(user.getId()); request.setRequestedChanges(dto.requestedChanges()); request.setRequestedAt(Instant.now()); return response(repository.save(request)); }
    public List<ProfileChangeResponse> mine(String emailAddress) { return repository.findByUserIdOrderByRequestedAtDesc(users.getByEmail(emailAddress).getId()).stream().map(this::response).toList(); }
    public List<ProfileChangeResponse> all() { return repository.findAllByOrderByRequestedAtDesc().stream().map(this::response).toList(); }
    public ProfileChangeResponse approve(String id, String adminEmail) { UserProfileChangeRequest request = pending(id); User admin = users.getByEmail(adminEmail); User user = users.getById(request.getUserId()); request.getRequestedChanges().forEach((key, value) -> { if ("fullName".equals(key)) user.setFullName(value); if ("phone".equals(key)) user.setPhone(value); if ("department".equals(key)) user.setDepartment(value); if ("email".equals(key)) user.setEmail(value.toLowerCase()); }); users.save(user); request.setStatus(ProfileChangeStatus.APPROVED); request.setReviewedBy(admin.getId()); request.setReviewedAt(Instant.now()); UserProfileChangeRequest saved = repository.save(request); notifications.create(user.getId(), "Profile updated", "Your profile details have been modified.", NotificationType.PROFILE_APPROVED); email.sendProfileUpdatedEmail(user.getEmail(), "Your LeaveWeb user details have been modified and approved by the administrator."); return response(saved); }
    public ProfileChangeResponse reject(String id, String adminEmail, String reason) { UserProfileChangeRequest request = pending(id); User admin = users.getByEmail(adminEmail); request.setStatus(ProfileChangeStatus.REJECTED); request.setReviewedBy(admin.getId()); request.setReviewedAt(Instant.now()); request.setRejectionReason(reason); UserProfileChangeRequest saved = repository.save(request); notifications.create(request.getUserId(), "Profile changes rejected", "Your requested profile changes were rejected.", NotificationType.PROFILE_REJECTED); return response(saved); }
    private UserProfileChangeRequest pending(String id) { UserProfileChangeRequest request = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Profile change request not found")); if (request.getStatus() != ProfileChangeStatus.PENDING) throw new BusinessException("Only pending profile changes can be reviewed"); return request; }
    private ProfileChangeResponse response(UserProfileChangeRequest request) { return new ProfileChangeResponse(request.getId(), request.getUserId(), users.toResponse(users.getById(request.getUserId())), request.getRequestedChanges(), request.getStatus(), request.getRequestedAt(), request.getReviewedAt(), request.getReviewedBy(), request.getRejectionReason()); }
}