package dash.pojo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.beanutils.BeanUtils;

import dash.dao.FormEntity;
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
	@XmlElement(name = "Id")
	private long id;
	
	/** insertion date in the database */
	@XmlElement(name = "insertion_date")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date insertion_date;
	
	@XmlElement(name = "participants")
	private List<String> participants;
	
	@XmlElement(name = "fixedTimes")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private List<Date> fixedTimes;
	
	@XmlElement(name = "ranges")
	private List<TIMERANGE> ranges;
	
	@XmlElement(name = "startTime")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date startTime;
	
	@XmlElement(name = "endTime")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date endTime;
	
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
	
	public Study(List<String> participants, List<Date> fixedTimes,
			List<TIMERANGE> ranges, Date startTime, Date endTime,
			boolean sunday, boolean monday, boolean tuesday, boolean wednesday,
			boolean thursday, boolean friday, boolean saturday, long formId) {
		super();
		this.participants = participants;
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
		return participants;
	}

	public void setParticipants(List<String> participants) {
		this.participants = participants;
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

	public void setId(long studyId) {
		this.id = studyId;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getInsertion_date() {
		return insertion_date;
	}

	public void setInsertion_date(Date insertion_date) {
		this.insertion_date = insertion_date;
	}
	
	
}
 
