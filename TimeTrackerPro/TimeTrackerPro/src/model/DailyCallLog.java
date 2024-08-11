package model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class DailyCallLog {
    private int id;
    private Date startDate;
    private Date endDate;
    private String truckUnitNumber;
    private List<String> crewMembers;
    private List<AmbulanceCall> ambulanceCalls;

    public DailyCallLog(int id, Date startDate, Date endDate, String truckUnitNumber) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.truckUnitNumber = truckUnitNumber;
        this.crewMembers = new ArrayList<>();
        this.ambulanceCalls = new ArrayList<>();
    }
    public DailyCallLog(Date startDate, Date endDate, String truckUnitNumber) {
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.truckUnitNumber = truckUnitNumber;
    	this.crewMembers = new ArrayList<>();
    	this.ambulanceCalls = new ArrayList<>();
    }

    public void addAmbulanceCall(AmbulanceCall call) {
        ambulanceCalls.add(call);
    }

    public void addCrewMember(String crewMemberName) {
        crewMembers.add(crewMemberName);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTruckUnitNumber() {
        return truckUnitNumber;
    }

    public void setTruckUnitNumber(String truckUnitNumber) {
        this.truckUnitNumber = truckUnitNumber;
    }

    public List<String> getCrewMembers() {
        return crewMembers;
    }

    public void setCrewMembers(List<String> crewMembers) {
        this.crewMembers = crewMembers;
    }

    public List<AmbulanceCall> getAmbulanceCalls() {
        return ambulanceCalls;
    }

    public void setAmbulanceCalls(List<AmbulanceCall> ambulanceCalls) {
        this.ambulanceCalls = ambulanceCalls;
    }
}
