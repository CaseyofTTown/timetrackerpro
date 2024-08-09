package model;

import java.util.Date;

public class AmbulanceCall {
    private int id;
    private int dailyLogId;
    private Date callDate;
    private String callType;
    private String callCategory;
    private String pickupLocation;
    private String dropoffLocation;
    private int totalMiles;
    private String insurance;
    private int aicEmployeeId;

    public AmbulanceCall(int id, int dailyLogId, Date callDate, String callType, String callCategory, String pickupLocation, String dropoffLocation, int totalMiles, String insurance, int aicEmployeeId) {
        this.id = id;
        this.dailyLogId = dailyLogId;
        this.callDate = callDate;
        this.callType = callType;
        this.callCategory = callCategory;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.totalMiles = totalMiles;
        this.insurance = insurance;
        this.aicEmployeeId = aicEmployeeId;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDailyLogId() {
        return dailyLogId;
    }

    public void setDailyLogId(int dailyLogId) {
        this.dailyLogId = dailyLogId;
    }

    public Date getCallDate() {
        return callDate;
    }

    public void setCallDate(Date callDate) {
        this.callDate = callDate;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallCategory() {
        return callCategory;
    }

    public void setCallCategory(String callCategory) {
        this.callCategory = callCategory;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public int getTotalMiles() {
        return totalMiles;
    }

    public void setTotalMiles(int totalMiles) {
        this.totalMiles = totalMiles;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public int getAicEmployeeId() {
        return aicEmployeeId;
    }

    public void setAicEmployeeId(int aicEmployeeId) {
        this.aicEmployeeId = aicEmployeeId;
    }
}
