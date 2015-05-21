package dash.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import org.apache.commons.beanutils.BeanUtils;

import dash.pojo.Form;
import dash.pojo.Study;
import dash.pojo.Study.TIMERANGE;

public class StudyEntity implements Serializable {
	
	private static final long serialVersionUID = -2453192655669468348L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@ElementCollection (fetch= FetchType.EAGER)
	@CollectionTable(name = "participants", joinColumns = {@JoinColumn(name="study_id")})
	private List<String> Participants;
	
	@ElementCollection (fetch= FetchType.EAGER)
	@CollectionTable(name = "fixedTimes", joinColumns = {@JoinColumn(name="study_id")})
	private List<Date> fixedTimes;
	
	@ElementCollection (fetch= FetchType.EAGER)
	@CollectionTable(name = "ranges", joinColumns = {@JoinColumn(name="study_id")})
	private List<TIMERANGE> ranges;
	
	@Column(name = "startTime")
	private Date startTime;
	
	@Column(name = "endTime")
	private Date endTime;
	
	@Column(name = "sunday")
	private boolean sunday;
	
	@Column(name = "monday")
	private boolean monday;
	
	@Column(name = "tuesday")
	private boolean tuesday; 
	
	@Column(name = "wednesday")
	private boolean wednesday;
	
	@Column(name = "thursday")
	private boolean thursday; 
	
	@Column(name = "friday")
	private boolean friday; 
	
	@Column(name = "saturday")
	private boolean saturday;
	
	@Column(name = "formId")
	private long formId;

	public StudyEntity(List<String> participants, List<Date> fixedTimes,
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
	
	public StudyEntity(Study form) {
		try {
			BeanUtils.copyProperties(this, form);
		} catch ( IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
