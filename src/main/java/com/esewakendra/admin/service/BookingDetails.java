package com.esewakendra.admin.service;

public class BookingDetails {
    private int id;
    private String state;
    private String district;
    private String courtComplex;
    private String kendra;
    private String service;
    private String advocateName;
    private String enrollmentNumber;
    private String phoneNumber;
    private String email;
    private String status;
    private String tokenNumber;
    private java.sql.Timestamp bookingTime;
    private boolean isAdvocate;
    private java.sql.Date date;
    private String timeSlot;
    private java.sql.Timestamp modifiedTime;
    private String timeRange;

    // Getter and Setter methods

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCourtComplex() {
        return courtComplex;
    }

    public void setCourtComplex(String courtComplex) {
        this.courtComplex = courtComplex;
    }

    public String getKendra() {
        return kendra;
    }

    public void setKendra(String kendra) {
        this.kendra = kendra;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAdvocateName() {
        return advocateName;
    }

    public void setAdvocateName(String advocateName) {
        this.advocateName = advocateName;
    }

    public String getEnrollmentNumber() {
        return enrollmentNumber;
    }

    public void setEnrollmentNumber(String enrollmentNumber) {
        this.enrollmentNumber = enrollmentNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public java.sql.Timestamp getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(java.sql.Timestamp bookingTime) {
        this.bookingTime = bookingTime;
    }

    public boolean isAdvocate() {
        return isAdvocate;
    }

    public void setIsAdvocate(boolean isAdvocate) {
        this.isAdvocate = isAdvocate;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public java.sql.Timestamp getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(java.sql.Timestamp modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }
}
