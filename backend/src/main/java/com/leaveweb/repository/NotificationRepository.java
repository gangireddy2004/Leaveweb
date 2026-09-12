package com.leaveweb.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.leaveweb.model.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
}