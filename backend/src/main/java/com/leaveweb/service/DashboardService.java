package com.leaveweb.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.leaveweb.model.LeaveStatus;
import com.leaveweb.model.LeaveType;
import com.leaveweb.model.User;
import com.leaveweb.repository.LeaveRequestRepository;

@Service
public class DashboardService {
    private final UserService userService; private final LeaveRequestRepository leaveRepository; private final LeaveTypeService typeService; private final LeaveService leaveService;
    public DashboardService(UserService userService, LeaveRequestRepository leaveRepository, LeaveTypeService typeService, LeaveService leaveService) { this.userService = userService; this.leaveRepository = leaveRepository; this.typeService = typeService; this.leaveService = leaveService; }
    public Map<String, Object> employee(String email) { User user = userService.getByEmail(email); Map<String, Object> result = new LinkedHashMap<>(); List<LeaveType> types = typeService.getAll(true); List<?> leaves = leaveService.getForUser(email); result.put("totalLeaveBalance", types.stream().mapToInt(LeaveType::getAnnualAllowance).sum()); result.put("leaveBalances", types.stream().map(type -> { Map<String, Object> item = new LinkedHashMap<>(); item.put("leaveTypeId", type.getId()); item.put("name", type.getName()); item.put("annualAllowance", type.getAnnualAllowance()); item.put("used", leaveRepository.findByUserIdAndStatusIn(user.getId(), List.of(LeaveStatus.APPROVED)).stream().filter(leave -> leave.getLeaveTypeId().equals(type.getId())).mapToLong(item2 -> item2.getNumberOfDays()).sum()); return item; }).toList()); result.put("pendingRequests", leaveRepository.findByUserIdAndStatusIn(user.getId(), List.of(LeaveStatus.PENDING)).size()); result.put("approvedRequests", leaveRepository.findByUserIdAndStatusIn(user.getId(), List.of(LeaveStatus.APPROVED)).size()); result.put("rejectedRequests", leaveRepository.findByUserIdAndStatusIn(user.getId(), List.of(LeaveStatus.REJECTED)).size()); result.put("recentLeaveRequests", leaves.stream().limit(5).toList()); return result; }
    public Map<String, Object> admin() { Map<String, Object> result = new LinkedHashMap<>(); result.put("totalEmployees", userService.getAll().stream().filter(User::isActive).count()); result.put("pendingLeaves", leaveRepository.countByStatus(LeaveStatus.PENDING)); result.put("approvedLeaves", leaveRepository.countByStatus(LeaveStatus.APPROVED)); result.put("rejectedLeaves", leaveRepository.countByStatus(LeaveStatus.REJECTED)); LocalDate today = LocalDate.now(); result.put("currentEmployeesOnLeave", leaveRepository.countByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(LeaveStatus.APPROVED, today, today)); result.put("leaveStatistics", Map.of("pending", leaveRepository.countByStatus(LeaveStatus.PENDING), "approved", leaveRepository.countByStatus(LeaveStatus.APPROVED), "rejected", leaveRepository.countByStatus(LeaveStatus.REJECTED))); Map<String, Long> departments = new LinkedHashMap<>(); userService.getAll().forEach(user -> departments.merge(user.getDepartment(), 1L, Long::sum)); result.put("departmentStatistics", departments); return result; }
}