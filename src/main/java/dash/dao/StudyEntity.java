package dash.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.beanutils.BeanUtils;

import dash.pojo.Form;
import dash.pojo.Study;
import dash.pojo.Study.TIMERANGE;

@Entity
@Table(name = "studies")
public class StudyEntity implements Serializable {
	
	private static final Long serialVersionUID = -2453192655669468348L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@Column(name = "study_name")
	private String studyName;
	
	@ElementCollection (fetch= FetchType.EAGER)
	@CollectionTable(name = "participants", joinColumns = {@JoinColumn(name="study_id")})
	private Set<String> Participants;
	
	@ElementCollection (fetch= FetchType.EAGER)
	@CollectionTable(name = "fixed_times", joinColumns = {@JoinColumn(name="study_id")})
	private Set<Date> fixedTimes;
	
	@ElementCollection (fetch= FetchType.EAGER)
	@CollectionTable(name = "ranges", joinColumns = {@JoinColumn(name="study_id")})
	private Set<TIMERANGE> ranges;
	
	@Column(name = "start_Date")
	private Date startDate;
	
	@Column(name = "end_Date")
	private Date endDate;
	
	@Column(name = "expiration_time")
	private Integer expirationTime;
	
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
	
	@Column(name = "form_id")
	private Long formId;
	
	@Column(name = "insertion_Date")
	private Date insertionDate;

	public StudyEntity(Set<String> participants, Set<Date> fixedTimes,
			Set<TIMERANGE> ranges, Date startDate, Date endDate,
			boolean sunday, boolean monday, boolean tuesday, boolean wednesday,
			boolean thursday, boolean friday, boolean saturday, Long formId) {
		super();
		Participants = participants;
		this.fixedTimes = fixedTimes;
		this.ranges = ranges;
		this.startDate = startDate;
		this.endDate = endDate;
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
	
	public StudyEntity(){
		
	}

	public Set<String> getParticipants() {
		return Participants;
	}

	public void setParticipants(Set<String> participants) {
		Participants = participants;
	}

	public Set<Date> getFixedTimes() {
		return fixedTimes;
	}

	public void setFixedTimes(Set<Date> fixedTimes) {
		this.fixedTimes = fixedTimes;
	}

	public Set<TIMERANGE> getRanges() {
		return ranges;
	}

	public void setRanges(Set<TIMERANGE> ranges) {
		this.ranges = ranges;
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

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getInsertionDate() {
		return insertionDate;
	}

	public void setInsertionDate(Date insertionDate) {
		this.insertionDate = insertionDate;
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	public Integer getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Integer expirationTime) {
		this.expirationTime = expirationTime;
	}
	
}
