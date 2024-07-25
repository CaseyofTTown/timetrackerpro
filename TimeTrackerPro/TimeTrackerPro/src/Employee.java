import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Employee {
	private String name;
	private CertificationLevelenum level;
	private String certificationNumber;
	private Date certExpirationDate;
	private List<TimeSheet> timeSheetList;

	// constructor w/o cert number in case driver
	public Employee(String name, CertificationLevelenum level) {
		this.name = name;
		this.level = level;
		this.timeSheetList = new ArrayList<TimeSheet>();
	}

	// constructor w/ cert included
	public Employee(String name, CertificationLevelenum level, String certificationNumber, Date certificationExpDate) {
		this.name = name;
		this.level = level;
		this.certificationNumber = certificationNumber;
		this.certExpirationDate = certificationExpDate;
		this.timeSheetList = new ArrayList<TimeSheet>();
	}
	
	public String getName() {
		return this.name;
	}
	public CertificationLevelenum getCertLevel() {
		return this.level;
	}
	public String getCertificationNumber() {
		return this.certificationNumber;
	}
	public Date getCertExpDate() {
		return this.certExpirationDate;
	}
	public List<TimeSheet> getTimeSheetList() {
		return this.timeSheetList;
	}
	
	public void setName(String name) {
		if(this.name != null);
		this.name = name;
	}
	public void setCertificationLevel(CertificationLevelenum level) {
		if(this.level != null);
		this.level = level;
	}
	public void setCertificationNumber(String number) {
		if(this.certificationNumber != null);
		this.certificationNumber = number;
	}
	public void setCertificationExpDate(Date expDate) {
		if(this.certExpirationDate != null);
		this.certExpirationDate = expDate;
	}
	
	public void addTimeSheet(TimeSheet timeSheet) {
	    // Add the TimeSheet to the list
	    this.timeSheetList.add(timeSheet);

	    // TODO: Add SQL query to insert the TimeSheet into the database
	}

	public void removeTimeSheet(TimeSheet timeSheet) {
	    // Remove the TimeSheet from the list
	    this.timeSheetList.remove(timeSheet);

	    // TODO: Add SQL query to remove the TimeSheet from the database
	}
	
	//function to return 14 days worth of time entries
	public List<TimeSheet> getTimeSheetsInPeriod(Date startDate) {
        // Create a Calendar object
        Calendar calendar = Calendar.getInstance();

        // Set the Calendar's time to the start date
        calendar.setTime(startDate);

        // Add 14 days to get the end date
        calendar.add(Calendar.DATE, 14);
        Date endDate = calendar.getTime();

        // Filter the time sheets TODO test behavior to make sure collection is from tuesday 0800 to 14 days tuesday 0800 
        return this.timeSheetList.stream()
            .filter(timeSheet -> !timeSheet.getShiftStartDate().before(startDate) && !timeSheet.getShiftStartDate().after(endDate))
            .collect(Collectors.toList());
    }


}
