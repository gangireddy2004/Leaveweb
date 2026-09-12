package com.leaveweb.model;

import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("leave_requests")
public class LeaveRequest {
    @Id private String id;
    private String userId;
    private String leaveTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private long numberOfDays;
    private String reason;
    private String supportingDocument;
    private LeaveStatus status = LeaveStatus.PENDING;
    private Instant appliedAt;
    private Instant reviewedAt;
    private String reviewedBy;
    private String rejectionReason;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getLeaveTypeId() { return leaveTypeId; }
    public void setLeaveTypeId(String leaveTypeId) { this.leaveTypeId = leaveTypeId; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public long getNumberOfDays() { return numberOfDays; }
    public void setNumberOfDays(long numberOfDays) { this.numberOfDays = numberOfDays; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getSupportingDocument() { return supportingDocument; }
    public void setSupportingDocument(String supportingDocument) { this.supportingDocument = supportingDocument; }
    public LeaveStatus getStatus() { return status; }
    public void setStatus(LeaveStatus status) { this.status = status; }
    public Instant getAppliedAt() { return appliedAt; }
    public void setAppliedAt(Instant appliedAt) { this.appliedAt = appliedAt; }
    public Instant getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(Instant reviewedAt) { this.reviewedAt = reviewedAt; }
    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}