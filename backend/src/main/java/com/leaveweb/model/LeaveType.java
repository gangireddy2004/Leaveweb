package com.leaveweb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("leave_types")
public class LeaveType {
    @Id private String id;
    private String name;
    private String description;
    private int annualAllowance;
    private boolean active = true;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getAnnualAllowance() { return annualAllowance; }
    public void setAnnualAllowance(int annualAllowance) { this.annualAllowance = annualAllowance; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}