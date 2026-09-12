package com.leaveweb.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.leaveweb.service.LeaveService;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
    private final LeaveService leaveService;
    public CalendarController(LeaveService leaveService) { this.leaveService = leaveService; }
    @GetMapping public Map<String, Object> year(Authentication authentication, @RequestParam(defaultValue = "0") int year) {
        int selectedYear = year == 0 ? LocalDate.now().getYear() : year;
        List<?> leaves = leaveService.getForUser(authentication.getName()).stream().filter(leave -> leave.startDate().getYear() == selectedYear || leave.endDate().getYear() == selectedYear).toList();
        Map<String, Object> result = new LinkedHashMap<>(); result.put("year", selectedYear); result.put("leaves", leaves); return result;
    }
}