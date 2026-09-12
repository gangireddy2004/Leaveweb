package com.leaveweb.config;

import java.time.Instant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import com.leaveweb.model.LeaveType;
import com.leaveweb.model.Role;
import com.leaveweb.model.User;
import com.leaveweb.model.AccountStatus;
import com.leaveweb.repository.LeaveTypeRepository;
import com.leaveweb.repository.UserRepository;

@Configuration
@Profile("dev")
public class DataInitializer {
    @Value("${app.bootstrap-admin.email:admin@leaveweb.com}") private String adminEmail;
    @Value("${app.bootstrap-admin.password:Admin@12345}") private String adminPassword;
    @Value("${app.bootstrap-admin.full-name:LeaveWeb Admin}") private String adminName;
    @Bean CommandLineRunner seedDevelopmentData(UserRepository users, LeaveTypeRepository types, PasswordEncoder encoder) { return args -> {
        if (users.findByEmailIgnoreCase(adminEmail).isEmpty()) users.save(user("EMP-ADMIN", adminName, adminEmail, adminPassword, "People Operations", Role.ADMIN, encoder));
        if (users.findByEmailIgnoreCase("employee@leaveweb.com").isEmpty()) users.save(user("EMP-1001", "Demo Employee", "employee@leaveweb.com", "Employee@123", "Engineering", Role.EMPLOYEE, encoder));
        if (types.count() == 0) { types.save(type("Casual Leave", "Short personal time away", 12)); types.save(type("Sick Leave", "Health and recovery related absence", 14)); types.save(type("Earned Leave", "Planned annual time away", 24)); types.save(type("Optional Holiday", "Personal choice of company holiday", 3)); }
    }; }
    private User user(String employeeId, String name, String email, String password, String department, Role role, PasswordEncoder encoder) { User user = new User(); user.setEmployeeId(employeeId); user.setFullName(name); user.setEmail(email); user.setPassword(encoder.encode(password)); user.setDepartment(department); user.setRole(role); user.setAccountStatus(AccountStatus.APPROVED); user.setActive(true); user.setCreatedAt(Instant.now()); user.setUpdatedAt(Instant.now()); return user; }
    private LeaveType type(String name, String description, int allowance) { LeaveType type = new LeaveType(); type.setName(name); type.setDescription(description); type.setAnnualAllowance(allowance); type.setActive(true); return type; }
}