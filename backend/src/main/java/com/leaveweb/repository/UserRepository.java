package com.leaveweb.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.leaveweb.model.User;
import com.leaveweb.model.AccountStatus;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByEmployeeId(String employeeId);
    List<User> findByAccountStatusOrderByCreatedAtAsc(AccountStatus status);
    long countByAccountStatus(AccountStatus status);
    long countByRoleAndActiveTrue(com.leaveweb.model.Role role);
    long countByDepartmentAndActiveTrue(String department);
}