package com.leaveweb.model;

import java.time.Instant;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("userProfileChangeRequests")
public class UserProfileChangeRequest {
    @Id private String id; private String userId; private Map<String, String> requestedChanges; private ProfileChangeStatus status = ProfileChangeStatus.PENDING; private Instant requestedAt; private Instant reviewedAt; private String reviewedBy; private String rejectionReason;
    public String getId() { return id; } public void setId(String value) { id = value; }
    public String getUserId() { return userId; } public void setUserId(String value) { userId = value; }
    public Map<String, String> getRequestedChanges() { return requestedChanges; } public void setRequestedChanges(Map<String, String> value) { requestedChanges = value; }
    public ProfileChangeStatus getStatus() { return status; } public void setStatus(ProfileChangeStatus value) { status = value; }
    public Instant getRequestedAt() { return requestedAt; } public void setRequestedAt(Instant value) { requestedAt = value; }
    public Instant getReviewedAt() { return reviewedAt; } public void setReviewedAt(Instant value) { reviewedAt = value; }
    public String getReviewedBy() { return reviewedBy; } public void setReviewedBy(String value) { reviewedBy = value; }
    public String getRejectionReason() { return rejectionReason; } public void setRejectionReason(String value) { rejectionReason = value; }
}