package com.leaveweb.controller;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.leaveweb.dto.NotificationResponse;
import com.leaveweb.service.NotificationService;
import com.leaveweb.service.UserService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService service; private final UserService userService;
    public NotificationController(NotificationService service, UserService userService) { this.service = service; this.userService = userService; }
    @GetMapping public List<NotificationResponse> list(Authentication auth) { return service.getForUser(userService.getByEmail(auth.getName()).getId()); }
    @PutMapping("/{id}/read") public void read(Authentication auth, @PathVariable String id) { service.markRead(id, userService.getByEmail(auth.getName()).getId()); }
    @PatchMapping("/{id}/read") public void patchRead(Authentication auth, @PathVariable String id) { service.markRead(id, userService.getByEmail(auth.getName()).getId()); }
    @DeleteMapping("/{id}") public void delete(Authentication auth, @PathVariable String id) { service.delete(id, userService.getByEmail(auth.getName()).getId()); }
}