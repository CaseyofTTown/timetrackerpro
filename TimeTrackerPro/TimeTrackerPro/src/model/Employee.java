package model;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Employee {
	private int id;
	private String name;
	private CertificationLevelenum level;
	private String certificationNumber;
	private Date certExpirationDate;
	private List<TimeSheet> timeSheetList;

	// constructor create new employee w/o cert number in case driver
	public Employee(int id, String name, CertificationLevelenum level) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.timeSheetList = new ArrayList<TimeSheet>();
		this.certificationNumber = null;
		this.certExpirationDate = null; // included so will be null in database, can be upgraded in future
	}

	// constructor create new employee w/ cert included
	public Employee(int id, String name, CertificationLevelenum level, String certificationNumber, Date certificationExpDate) {
		this.id = id;
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
	public int getId() {
		return this.id;
	}

	public Date getCertExpDate() {
		return this.certExpirationDate;
	}

	public List<TimeSheet> getTimeSheetList() {
		return this.timeSheetList;
	}
	public int calculateDaysUntilExpiration() {
	    try {
	        Date expirationDate = this.getCertExpDate();
	        if (expirationDate == null) {
	            throw new Exception("No expiration date available.");
	        }

	        Date currentDate = new Date();
	        long diffInMillies = expirationDate.getTime() - currentDate.getTime();
	        return (int) (diffInMillies / (1000 * 60 * 60 * 24));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1; // Indicate an error
	    }
	}



	public void setName(String name) {
		if (this.name != null)
			;
		this.name = name;
	}

	public void setCertificationLevel(CertificationLevelenum level) {
		if (this.level != null)
			;
		this.level = level;
	}

	public void setCertificationNumber(String number) {
		if (this.certificationNumber != null)
			;
		this.certificationNumber = number;
	}

	public void setCertificationExpDate(Date expDate) {
		if (this.certExpirationDate != null)
			;
		this.certExpirationDate = expDate;
	}

	// functions to add/remove timesheets from memory, does not interact with db
	// here!
	public void addTimeSheet(TimeSheet timeSheet) {
		// Add the TimeSheet to the list
		this.timeSheetList.add(timeSheet);
	}

	public void removeTimeSheet(TimeSheet timeSheet) {
		// Remove the TimeSheet from the list
		this.timeSheetList.remove(timeSheet);
	}

}
