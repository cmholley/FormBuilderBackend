package dash.pojo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dash.helpers.CalendarISO8601Adapter;
import dash.helpers.DateISO8601Adapter;
import dash.security.IAclObject;

@Entity
@Table(name = "studies")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Study implements IAclObject {

	private static final long serialVersionUID = -2453192655669468348L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	@XmlElement(name = "id")
	private Long id;

	/** insertion date in the database */
	@Column(name = "insertion_date")
	@XmlElement(name = "insertion_date")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date insertionDate;

	@Column(name = "study_name")
	@XmlElement(name = "studyName")
	private String studyName;

	// The number of hours after the study is made active before it expires
	@Column(name = "duration")
	@XmlElement(name = "duration")
	private Long duration;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "participants", joinColumns = { @JoinColumn(name = "study_id") })
	@XmlElement(name = "participants")
	private Set<String> participants;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "fixed_times", joinColumns = { @JoinColumn(name = "study_id") })
	@XmlElement(name = "fixedTimes")
	@XmlJavaTypeAdapter(CalendarISO8601Adapter.class)
	private Set<Calendar> fixedTimes = new HashSet<Calendar>();

	@Column(name = "start_date")
	@XmlElement(name = "startDate")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date startDate;

	@Column(name = "end_date")
	@XmlElement(name = "endDate")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date endDate;

	//We explicitly notify hibernate that the column will be a bit to prevent 
	//Errors during the schema validation
	@Column(name = "sunday", columnDefinition = "BIT", length = 1)
	@XmlElement(name = "sunday")
	private boolean sunday;

	@Column(name = "monday", columnDefinition = "BIT", length = 1)
	@XmlElement(name = "monday")
	private boolean monday;

	@Column(name = "tuesday", columnDefinition = "BIT", length = 1)
	@XmlElement(name = "tuesday")
	private boolean tuesday;

	@Column(name = "wednesday", columnDefinition = "BIT", length = 1)
	@XmlElement(name = "wednesday")
	private boolean wednesday;

	@Column(name = "thursday", columnDefinition = "BIT", length = 1)
	@XmlElement(name = "thursday")
	private boolean thursday;

	@Column(name = "friday", columnDefinition = "BIT", length = 1)
	@XmlElement(name = "friday")
	private boolean friday;

	@Column(name = "saturday", columnDefinition = "BIT", length = 1)
	@XmlElement(name = "saturday")
	private boolean saturday;

	@Column(name = "form_id", columnDefinition = "BIT", length = 1)
	@XmlElement(name = "formId")
	private long formId;

	public Study() {

	}

	// Generates a string that can be used as a Cron Expression for scheduling a
	// job using a Quartz Cron trigger
	public List<String> generateCronStrings() {
		// CronString Format
		// SECONDS MINUTES HOURS DAYOFMONTH MONTH DAYOFWEEK {YEAR}
		List<String> cronStrings = new ArrayList<String>();
		String cronString;
		Calendar cal = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(startDate);
		end.setTime(endDate);
		String dateString = "? ";
		if (start.get(Calendar.MONTH) == end.get(Calendar.MONTH)) {
			dateString += (start.get(Calendar.MONTH) + 1);
		} else {
			dateString += ((start.get(Calendar.MONTH) + 1) + "-" + (end.get(Calendar.MONTH) + 1));
		}
		dateString += " ";
		if (sunday)
			dateString += "1,";
		if (monday)
			dateString += "2,";
		if (tuesday)
			dateString += "3,";
		if (wednesday)
			dateString += "4,";
		if (thursday)
			dateString += "5,";
		if (friday)
			dateString += "6,";
		if (saturday)
			dateString += "7,";
		// Removes the comma at the end of the string left from the last day of
		// week added
		dateString = dateString.substring(0, dateString.length() - 1);
		dateString += " ";
		if (start.get(Calendar.YEAR) == end.get(Calendar.YEAR))
			dateString += start.get(Calendar.YEAR);
		else
			dateString += (start.get(Calendar.YEAR) + "-" + end.get(Calendar.YEAR));
		for (Calendar time : fixedTimes) {
			cal = time;
			cronString = "";
			cronString += cal.get(Calendar.SECOND);
			cronString += " ";
			cronString += cal.get(Calendar.MINUTE);
			cronString += " ";
			cronString += cal.get(Calendar.HOUR_OF_DAY);
			cronString += " ";
			cronString += dateString;
			cronStrings.add(cronString);
		}
		return cronStrings;
	}

	public Set<String> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<String> participants) {
		this.participants = participants;
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

	public long getFormId() {
		return formId;
	}

	public void setFormId(long formId) {
		this.formId = formId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Date getInsertionDate() {
		return insertionDate;
	}

	public void setInsertionDate(Date insertion_date) {
		this.insertionDate = insertion_date;
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
