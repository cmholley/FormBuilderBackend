package dash.pojo;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.beanutils.BeanUtils;

import dash.dao.StudyEntity;
import dash.helpers.DateISO8601Adapter;
import dash.security.IAclObject;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Study implements IAclObject {
	public static enum TIMERANGE{
				MORNING, AFTERNOON, EVENING, NIGHT
	};
	
	@GeneratedValue
	@XmlElement(name = "id")
	private long id;
	
	/** insertion date in the database */
	@XmlElement(name = "insertion_date")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date insertion_date;
	
	@XmlElement(name = "participants")
	private Set<String> participants;
	
	@XmlElement(name = "fixedTimes")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Set<Date> fixedTimes;
	
	@XmlElement(name = "ranges")
	private Set<TIMERANGE> ranges;
	
	@XmlElement(name = "startDate")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date startDate;
	
	@XmlElement(name = "endDate")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date endDate;
	
	@XmlElement(name = "sunday")
	private boolean sunday;
	
	@XmlElement(name = "monday")
	private boolean monday;
	
	@XmlElement(name = "tuesday")
	private boolean tuesday; 
	
	@XmlElement(name = "wednesday")
	private boolean wednesday;
	
	@XmlElement(name = "thursday")
	private boolean thursday; 
	
	@XmlElement(name = "friday")
	private boolean friday; 
	
	@XmlElement(name = "saturday")
	private boolean saturday;
	
	@XmlElement(name = "formId")
	private long formId;

	public Study(StudyEntity studyEntity) {
		try {
			BeanUtils.copyProperties(this, studyEntity);
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}
	}
	
	public Study(Set<String> participants, Set<Date> fixedTimes,
			Set<TIMERANGE> ranges, Date startDate, Date endDate,
			boolean sunday, boolean monday, boolean tuesday, boolean wednesday,
			boolean thursday, boolean friday, boolean saturday, long formId) {
		super();
		this.participants = participants;
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
	
	public Study(){
	}

	public Set<String> generateCronStrings(){
		//CronString Format
		//SECONDS MINUTES HOURS DAYOFMONTH MONTH DAYOFWEEK {YEAR}
		Set<String> cronStrings = null;
		for(Date time : fixedTimes){
			String cronString = "";
			Calendar cal = Calendar.getInstance();
			//cal.setTime(time);
			//cronString += cal.;
			
			
		}
			
		return cronStrings;
	}
	
	public Set<String> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<String> participants) {
		this.participants = participants;
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
		this.endDate= endDate;
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

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	public Date getInsertion_date() {
		return insertion_date;
	}

	public void setInsertion_date(Date insertion_date) {
		this.insertion_date = insertion_date;
	}
	
	
}
 
