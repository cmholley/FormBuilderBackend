package dash.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
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

import org.apache.commons.beanutils.BeanUtils;

import dash.pojo.Study;

@Entity
@Table(name = "studies")
public class StudyEntity implements Serializable {
	
	private static final long serialVersionUID = -2453192655669468348L;

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
	private Set<Calendar> fixedTimes = new HashSet<Calendar>();;
	
	@Column(name = "start_Date")
	private Date startDate;
	
	@Column(name = "end_Date")
	private Date endDate;
	
	@Column(name = "duration")
	private Integer duration;
	
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

	public Set<Calendar> getFixedTimes() {
		return fixedTimes;
	}

	public void setFixedTimes(Set<Calendar> fixedTimes) {
		this.fixedTimes = fixedTimes;
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

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
