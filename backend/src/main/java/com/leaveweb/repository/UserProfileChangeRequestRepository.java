package com.leaveweb.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.leaveweb.model.UserProfileChangeRequest;

public interface UserProfileChangeRequestRepository extends MongoRepository<UserProfileChangeRequest, String> {
    List<UserProfileChangeRequest> findByUserIdOrderByRequestedAtDesc(String userId);
    List<UserProfileChangeRequest> findAllByOrderByRequestedAtDesc();
}