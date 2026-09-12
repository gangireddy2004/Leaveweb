package com.leaveweb.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.leaveweb.model.LeaveType;

public interface LeaveTypeRepository extends MongoRepository<LeaveType, String> {
    List<LeaveType> findByActiveTrue();
}