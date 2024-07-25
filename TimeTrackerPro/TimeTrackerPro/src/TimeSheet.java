import java.util.Date;

public class TimeSheet {
	private Employee employee;
	private Date shiftStartDate;
	private Date shiftEndDate;
	private Date shiftStartTime;
	private Date shiftEndTime;
	
	
	public TimeSheet(Employee employee, Date shiftStartDate, Date shiftEndDate, Date shiftStartTime, Date shiftEndTime) {
		this.employee = employee;
		this.shiftStartDate = shiftStartDate;
		this.shiftEndDate = shiftEndDate;
		this.shiftStartTime = shiftStartTime;
		this.shiftEndTime = shiftEndTime;
	}
	
	public Employee getEmployee() {
		return this.employee;
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
