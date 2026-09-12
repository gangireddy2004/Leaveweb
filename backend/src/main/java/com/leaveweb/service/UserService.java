package com.leaveweb.service;

import java.time.Instant;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.leaveweb.dto.RegisterRequest;
import com.leaveweb.dto.UpdateUserRequest;
import com.leaveweb.dto.UserResponse;
import com.leaveweb.exception.BusinessException;
import com.leaveweb.exception.ResourceNotFoundException;
import com.leaveweb.model.Role;
import com.leaveweb.model.AccountStatus;
import com.leaveweb.model.User;
import com.leaveweb.repository.UserRepository;
import com.leaveweb.model.NotificationType;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, NotificationService notificationService) { this.repository = repository; this.passwordEncoder = passwordEncoder; this.notificationService = notificationService; }

    @Override public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmailIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        boolean enabled = user.isActive() && user.getAccountStatus() == AccountStatus.APPROVED;
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPassword()).roles(user.getRole().name()).disabled(!enabled).build();
    }

    @Transactional public User register(RegisterRequest request) {
        if (repository.findByEmailIgnoreCase(request.email()).isPresent() || repository.findByEmployeeId(request.employeeId()).isPresent()) throw new BusinessException("Email or employee ID is already registered");
        User user = new User(); user.setEmployeeId(request.employeeId()); user.setFullName(request.fullName()); user.setEmail(request.email().toLowerCase()); user.setPassword(passwordEncoder.encode(request.password())); user.setPhone(request.phone()); user.setDepartment(request.department()); user.setRole(Role.EMPLOYEE); user.setAccountStatus(AccountStatus.PENDING); user.setActive(true); user.setCreatedAt(Instant.now()); user.setUpdatedAt(Instant.now()); return repository.save(user);
    }

    public User getById(String id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found")); }
    public User getByEmail(String email) { return repository.findByEmailIgnoreCase(email).orElseThrow(() -> new ResourceNotFoundException("User not found")); }
    public List<User> getAll() { return repository.findAll(); }
    public User save(User user) { user.setUpdatedAt(Instant.now()); return repository.save(user); }
    public List<User> getPending() { return repository.findByAccountStatusOrderByCreatedAtAsc(AccountStatus.PENDING); }
    @Transactional public User approve(String id) { User user = getById(id); user.setAccountStatus(AccountStatus.APPROVED); user.setUpdatedAt(Instant.now()); User saved = repository.save(user); notificationService.create(saved.getId(), "Account approved", "Your LeaveWeb account has been approved.", NotificationType.ACCOUNT_APPROVED); return saved; }
    @Transactional public User reject(String id, String reason) { User user = getById(id); user.setAccountStatus(AccountStatus.REJECTED); user.setUpdatedAt(Instant.now()); User saved = repository.save(user); notificationService.create(saved.getId(), "Account rejected", reason == null || reason.isBlank() ? "Your LeaveWeb account has been rejected." : reason, NotificationType.ACCOUNT_REJECTED); return saved; }
    public User update(String id, UpdateUserRequest request) { User user = getById(id); user.setFullName(request.fullName()); user.setEmail(request.email().toLowerCase()); user.setPhone(request.phone()); user.setDepartment(request.department()); if (request.active() != null) user.setActive(request.active()); user.setUpdatedAt(Instant.now()); return repository.save(user); }
    public void deactivate(String id) { User user = getById(id); user.setActive(false); user.setUpdatedAt(Instant.now()); repository.save(user); }
    public UserResponse toResponse(User user) { return new UserResponse(user.getId(), user.getEmployeeId(), user.getFullName(), user.getEmail(), user.getPhone(), user.getDepartment(), user.getRole(), user.getAccountStatus(), user.isActive(), user.getCreatedAt()); }
}