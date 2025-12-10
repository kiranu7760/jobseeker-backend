package com.jobseeker.model;

public class Application {

    private int id;
    private int userId;
    private int jobId;
    private String status;
    private String appliedAt;
    private String notes;
    private String resumePath;


    // Extra fields from JOIN (for displaying job details)
    private String jobTitle;
    private String company;
    private String location;
    private String type;
    private String salary;
    private String userName;
    private String userEmail;

    // --------- Getters & Setters ----------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(String appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    
    public String getResumePath() {
        return resumePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }

}
