package com.leaveweb.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.leaveweb.dto.LeaveTypeRequest;
import com.leaveweb.exception.ResourceNotFoundException;
import com.leaveweb.model.LeaveType;
import com.leaveweb.repository.LeaveTypeRepository;

@Service
public class LeaveTypeService {
    private final LeaveTypeRepository repository;
    public LeaveTypeService(LeaveTypeRepository repository) { this.repository = repository; }
    public List<LeaveType> getAll(boolean activeOnly) { return activeOnly ? repository.findByActiveTrue() : repository.findAll(); }
    public LeaveType get(String id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Leave type not found")); }
    public LeaveType create(LeaveTypeRequest request) { LeaveType type = new LeaveType(); apply(type, request); return repository.save(type); }
    public LeaveType update(String id, LeaveTypeRequest request) { LeaveType type = get(id); apply(type, request); return repository.save(type); }
    public void delete(String id) { repository.delete(get(id)); }
    private void apply(LeaveType type, LeaveTypeRequest request) { type.setName(request.name()); type.setDescription(request.description()); type.setAnnualAllowance(request.annualAllowance()); type.setActive(request.active()); }
}