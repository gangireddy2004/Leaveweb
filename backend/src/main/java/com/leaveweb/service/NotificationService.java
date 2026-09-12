package com.leaveweb.service;

import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import com.leaveweb.dto.NotificationResponse;
import com.leaveweb.exception.ResourceNotFoundException;
import com.leaveweb.model.Notification;
import com.leaveweb.model.NotificationType;
import com.leaveweb.repository.NotificationRepository;

@Service
public class NotificationService {
    private final NotificationRepository repository;
    public NotificationService(NotificationRepository repository) { this.repository = repository; }
    public List<NotificationResponse> getForUser(String userId) { return repository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toResponse).toList(); }
    public void markRead(String id, String userId) { Notification notification = getOwned(id, userId); notification.setRead(true); repository.save(notification); }
    public void delete(String id, String userId) { repository.delete(getOwned(id, userId)); }
    public void create(String userId, String title, String message, NotificationType type) { Notification notification = new Notification(); notification.setUserId(userId); notification.setTitle(title); notification.setMessage(message); notification.setType(type); notification.setCreatedAt(Instant.now()); repository.save(notification); }
    private Notification getOwned(String id, String userId) { Notification notification = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notification not found")); if (!notification.getUserId().equals(userId)) throw new ResourceNotFoundException("Notification not found"); return notification; }
    private NotificationResponse toResponse(Notification item) { return new NotificationResponse(item.getId(), item.getTitle(), item.getMessage(), item.getType(), item.isRead(), item.getCreatedAt()); }
}