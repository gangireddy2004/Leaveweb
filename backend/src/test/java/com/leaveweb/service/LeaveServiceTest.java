package com.leaveweb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import com.leaveweb.dto.LeaveRequestDto;
import com.leaveweb.exception.BusinessException;
import com.leaveweb.model.LeaveRequest;
import com.leaveweb.model.LeaveType;
import com.leaveweb.model.User;
import com.leaveweb.repository.LeaveRequestRepository;

@ExtendWith(MockitoExtension.class)
class LeaveServiceTest {
    @Mock LeaveRequestRepository repository;
    @Mock LeaveTypeService leaveTypeService;
    @Mock UserService userService;
    @Mock NotificationService notificationService;
    @Mock EmailService emailService;
    @InjectMocks private LeaveService service;
    private User user;
    private LeaveType type;

    @BeforeEach void setUp() { user = new User(); user.setId("user-1"); user.setEmail("employee@example.com"); type = new LeaveType(); type.setId("type-1"); type.setName("Annual leave"); type.setAnnualAllowance(10); type.setActive(true); }

    @Test void calculatesWorkingDays() { assertEquals(2, service.daysBetween(LocalDate.of(2026, 9, 11), LocalDate.of(2026, 9, 14))); }
    @Test void excludesWeekendOnlyRange() { assertEquals(0, service.daysBetween(LocalDate.of(2026, 9, 12), LocalDate.of(2026, 9, 13))); }
    @Test void countsWeekdaysAcrossWeekend() { assertEquals(5, service.daysBetween(LocalDate.of(2026, 9, 14), LocalDate.of(2026, 9, 20))); }

    @Test void rejectsEndDateBeforeStartDate() { LeaveRequestDto request = new LeaveRequestDto("type-1", LocalDate.now(), LocalDate.now().minusDays(1), "Travel", null); assertThrows(BusinessException.class, () -> service.create("employee@example.com", request)); }

    @Test void rejectsWhenLeaveBalanceIsExceeded() { when(userService.getByEmail("employee@example.com")).thenReturn(user); when(leaveTypeService.get("type-1")).thenReturn(type); LeaveRequest used = new LeaveRequest(); used.setLeaveTypeId("type-1"); used.setNumberOfDays(10); when(repository.findByUserIdAndStatusIn(any(), any())).thenReturn(List.of(used)); LocalDate monday = LocalDate.now().plusDays((8 - LocalDate.now().getDayOfWeek().getValue()) % 7); LeaveRequestDto request = new LeaveRequestDto("type-1", monday, monday.plusDays(1), "Travel", null); assertThrows(BusinessException.class, () -> service.create("employee@example.com", request)); }
}