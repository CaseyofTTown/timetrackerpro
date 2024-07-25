import java.util.Date;

public class TimeSheet {
	private String employeeName;
	private int timeSheetId;
	private Date shiftStartDate;
	private Date shiftEndDate;
	private Date shiftStartTime;
	private Date shiftEndTime;
	
	//create a new time sheet, doesnt have the ID, will be created by sqlite
	public TimeSheet(String employeeName, Date shiftStartDate, Date shiftEndDate, Date shiftStartTime, Date shiftEndTime) {
		this.employeeName = employeeName;
		this.shiftStartDate = shiftStartDate;
		this.shiftEndDate = shiftEndDate;
		this.shiftStartTime = shiftStartTime;
		this.shiftEndTime = shiftEndTime;
		this.timeSheetId = -1;
	}
	
	//create a time sheet in memory from db, will have an ID
	public TimeSheet(int timeSheetID, String employeeName, Date shiftStartDate, Date shiftEndDate, Date shiftStartTime, Date shiftEndTime) {
		this.timeSheetId = timeSheetID;
		this.employeeName = employeeName;
		this.shiftStartDate = shiftStartDate;
		this.shiftEndDate = shiftEndDate;
		this.shiftStartTime = shiftStartTime;
		this.shiftEndTime = shiftEndTime;
	}
	
	public String getEmployeeName() {
		return this.employeeName;
	}
	public Date getShiftStartDate() {
		return this.shiftStartDate;
	}
	public Date getShiftEndDate() {
		return this.shiftEndDate;
	}
	public Date getShiftStartTime() {
		return this.shiftStartTime;
	}
	public Date getShiftEndTime() {
		return this.shiftEndDate;
	}
	
	public void setShiftStartDate(Date shiftStartDate) {
		if(this.shiftStartDate != null) {
			this.shiftStartDate = shiftStartDate;
		}
	}

	public void setShiftEndDate(Date shiftEndDate) {
		if(this.shiftEndDate != null);
		this.shiftEndDate = shiftEndDate;
	}
	
	public void setShiftStartTime(Date shiftStartTime) {
		if(this.shiftStartTime != null);
		this.shiftStartTime = shiftStartTime;
	}
	
	public void setShiftEndTime(Date shiftEndTime) {
		if(this.shiftEndTime != null);
		this.shiftEndTime = shiftEndTime;
	}
	
	

	
	

	
}
