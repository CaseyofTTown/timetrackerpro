package model;

import java.util.Date;

public class AmbulanceCall {
	private int id;
	private int dailyLogId;
	private Date callDate;
	private String patientsName;
	private TypeOfCallEnum callCategory;
	private String pickupLocation;
	private String dropoffLocation;
	private int totalMiles;
	private String insurance;
	private String aicEmployee;

	// all fields present
	public AmbulanceCall(int id, int dailyLogId, Date callDate, String patientsName, TypeOfCallEnum callCategory,
			String pickupLocation, String dropoffLocation, int totalMiles, String insurance, String aicEmployee) {
		this.id = id;
		this.dailyLogId = dailyLogId;
		this.callDate = callDate;
		this.patientsName = patientsName;
		this.callCategory = callCategory;
		this.pickupLocation = pickupLocation;
		this.dropoffLocation = dropoffLocation;
		this.totalMiles = totalMiles;
		this.insurance = insurance;
		this.aicEmployee = aicEmployee;
	}

	// refusal for ptName, refusal typeOfCall, aic
	public AmbulanceCall(int id, int dailyLogId, Date callDate, TypeOfCallEnum callCategory, String pickupLocation,
			String aicEmployee) {
		this.id = id;
		this.dailyLogId = dailyLogId;
		this.callDate = callDate;
		this.callCategory = callCategory;
		this.pickupLocation = pickupLocation;
		this.aicEmployee = aicEmployee;

	}

	// for creating object in application (wont have id, sqlite auto generates it)
	// all fields present
	public AmbulanceCall(int dailyLogId, Date callDate, String callType, TypeOfCallEnum callCategory,
			String pickupLocation, String dropoffLocation, int totalMiles, String insurance, String aicEmployee) {

		this.dailyLogId = dailyLogId;
		this.callDate = callDate;
		this.patientsName = callType;
		this.callCategory = callCategory;
		this.pickupLocation = pickupLocation;
		this.dropoffLocation = dropoffLocation;
		this.totalMiles = totalMiles;
		this.insurance = insurance;
		this.aicEmployee = aicEmployee;
	}

	// refusal for ptName, refusal typeOfCall, aic
	public AmbulanceCall(int dailyLogId, Date callDate, TypeOfCallEnum callCategory, String pickupLocation,
			String aicEmployee) {
		this.dailyLogId = dailyLogId;
		this.callDate = callDate;
		this.callCategory = callCategory;
		this.pickupLocation = pickupLocation;
		this.aicEmployee = aicEmployee;

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

	public String getPatientsName() {
		return patientsName;
	}

	public void setPatientsName(String callType) {
		this.patientsName = callType;
	}

	public TypeOfCallEnum getCallCategory() {
		return callCategory;
	}

	public void setCallCategory(TypeOfCallEnum callCategory) {
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

	public String getAicName() {
		return aicEmployee;
	}

	public void setAicEmployeeId(String aicEmployee) {
		this.aicEmployee = aicEmployee;
	}
}
