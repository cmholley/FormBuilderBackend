package dash.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Study {
	public static enum TIMERANGE{
				MORNING, AFTERNOON, EVENING, NIGHT
	};
	
	private List<String> Participants;
	
	private List<Date> fixedTimes;
	
	private List<TIMERANGE> ranges;
	
	private Date startTime;
	
	private Date endTime;
	
	private boolean sunday, monday, tuesday, 
		wednesday, thursday, friday, saturday;
	
	private long formId;

	public Study(List<String> participants, List<Date> fixedTimes,
			List<TIMERANGE> ranges, Date startTime, Date endTime,
			boolean sunday, boolean monday, boolean tuesday, boolean wednesday,
			boolean thursday, boolean friday, boolean saturday, long formId) {
		super();
		Participants = participants;
		this.fixedTimes = fixedTimes;
		this.ranges = ranges;
		this.startTime = startTime;
		this.endTime = endTime;
		this.sunday = sunday;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.formId = formId;
	}

	public List<String> getParticipants() {
		return Participants;
	}

	public void setParticipants(List<String> participants) {
		Participants = participants;
	}

	public List<Date> getFixedTimes() {
		return fixedTimes;
	}

	public void setFixedTimes(List<Date> fixedTimes) {
		this.fixedTimes = fixedTimes;
	}

	public List<TIMERANGE> getRanges() {
		return ranges;
	}

	public void setRanges(List<TIMERANGE> ranges) {
		this.ranges = ranges;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean isSunday() {
		return sunday;
	}

	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}

	public boolean isMonday() {
		return monday;
	}

	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	public boolean isTuesday() {
		return tuesday;
	}

	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	public boolean isWednesday() {
		return wednesday;
	}

	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}

	public boolean isThursday() {
		return thursday;
	}

	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	public boolean isFriday() {
		return friday;
	}

	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	public boolean isSaturday() {
		return saturday;
	}

	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	public long getFormId() {
		return formId;
	}

	public void setFormId(long formId) {
		this.formId = formId;
	}
	
	
}
 
