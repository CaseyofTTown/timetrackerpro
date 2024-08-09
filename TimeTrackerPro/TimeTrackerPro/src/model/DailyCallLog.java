package model;

import java.util.Date;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class DailyCallLog {
    private int id;
    private Date startDate;
    private Date endDate;
    private String truckUnitNumber;
    private List<Integer> crewMembers;
    private List<AmbulanceCall> ambulanceCalls;

    public DailyCallLog(int id, Date startDate, Date endDate, String truckUnitNumber, int crewMember1, int crewMember2) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.truckUnitNumber = truckUnitNumber;
        this.crewMembers = new ArrayList<>();
        this.ambulanceCalls = new ArrayList<>();
    }

    public void addAmbulanceCall(AmbulanceCall call) {
        ambulanceCalls.add(call);
    }

    public void addCrewMember(int crewMemberId) {
        crewMembers.add(crewMemberId);
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

    public List<Integer> getCrewMembers() {
        return crewMembers;
    }

    public void setCrewMembers(List<Integer> crewMembers) {
        this.crewMembers = crewMembers;
    }

    public List<AmbulanceCall> getAmbulanceCalls() {
        return ambulanceCalls;
    }

    public void setAmbulanceCalls(List<AmbulanceCall> ambulanceCalls) {
        this.ambulanceCalls = ambulanceCalls;
    }
}