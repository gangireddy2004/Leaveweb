package com.leaveweb.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.leaveweb.model.LeaveRequest;
import com.leaveweb.model.LeaveStatus;

public interface LeaveRequestRepository extends MongoRepository<LeaveRequest, String> {
    List<LeaveRequest> findByUserIdOrderByAppliedAtDesc(String userId);
    List<LeaveRequest> findAllByOrderByAppliedAtDesc();
    List<LeaveRequest> findByUserIdAndStatusIn(String userId, List<LeaveStatus> statuses);
    long countByStatus(LeaveStatus status);
    long countByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(LeaveStatus status, LocalDate date1, LocalDate date2);
}