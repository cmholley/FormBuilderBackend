package dash.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dash.dao.ValidationToken;
import dash.helpers.DateISO8601Adapter;

/**
 * User resource placeholder for json/xml representation
 *
 * @author plindner
 *
 */
@Entity
@Table(name = "user_data")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {

	public static enum NOTIFICATION_PREFERENCE {
		EMAIL, TEXT, BOTH
	};

	private static final long serialVersionUID = -8039686696076337053L;

	/** id of the user */
	@Id
	@GeneratedValue
	@Column(name = "id")
	@XmlElement(name = "id")
	private Long id;

	/** username of the user */
	@Column(name = "username")
	@XmlElement(name = "username")
	private String username;

	/** password of the user */
	@Column(name = "password")
	@XmlElement(name = "password")
	private String password;

	/** firstname of the user */
	@Column(name = "firstName")
	@XmlElement(name = "firstName")
	private String firstName;

	/** lastname of the user */
	@Column(name = "lastName")
	@XmlElement(name = "lastName")
	private String lastName;

	/** city of the user */
	@Column(name = "city")
	@XmlElement(name = "city")
	private String city;

	/** home phone number of the user */
	@Column(name = "homePhone")
	@XmlElement(name = "homePhone")
	private String homePhone;

	/** cellPhone number of the user */
	@Column(name = "cellPhone")
	@XmlElement(name = "cellPhone")
	private String cellPhone;

	/** email address of the user */
	@Column(name = "email")
	@XmlElement(name = "email")
	private String email;

	/** path to stored picture of the user */
	@Column(name = "picturePath")
	@XmlElement(name = "picturePath")
	private String picturePath;

	//Key is studyId, value is formId
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "active_studies", joinColumns = { @JoinColumn(name = "user_id") })
	@Column(name = "value")
	@XmlElement(name = "activeStudies")
	private Map<Long, Long> activeStudies = new HashMap<Long, Long>();

	@Enumerated(EnumType.STRING)
	@Column(name = "notificationPreference")
	@XmlElement(name = "notificationPreference")
	private NOTIFICATION_PREFERENCE notificationPreference = NOTIFICATION_PREFERENCE.EMAIL;

	/** insertion date in the database */
	@Column(name = "insertionDate")
	@XmlElement(name = "insertionDate")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date insertionDate;

	@Column(name = "is_email_verified")
	@XmlElement(name = "is_email_verified")
	private boolean is_email_verified;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "validation_tokens", joinColumns = { @JoinColumn(name = "user_id") })
	@XmlTransient
	private Set<ValidationToken> validation_tokens = new HashSet<ValidationToken>();


	public User(String username, String password, String firstName, String lastName, String city, String homePhone,
			String cellPhone, String email, String picturePath) {

		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.city = city;
		this.homePhone = homePhone;
		this.cellPhone = cellPhone;
		this.email = email;
		this.picturePath = picturePath;
	}

	public User() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPicture() {
		return picturePath;
	}

	public void setPicture(String picturePath) {
		this.picturePath = picturePath;
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

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public Map<Long, Long> getActiveStudies() {
		return activeStudies;
	}

	public void setActiveStudies(Map<Long, Long> activeStudies) {
		this.activeStudies = activeStudies;
	}

	public NOTIFICATION_PREFERENCE getNotificationPreference() {
		return notificationPreference;
	}

	public void setNotificationPreference(NOTIFICATION_PREFERENCE notificationPreference) {
		this.notificationPreference = notificationPreference;
	}

	public boolean isIs_email_verified() {
		return is_email_verified;
	}

	public void setIs_email_verified(boolean is_email_verified) {
		this.is_email_verified = is_email_verified;
	}

	public Set<ValidationToken> getValidation_tokens() {
		return validation_tokens;
	}

	public void setValidation_tokens(Set<ValidationToken> validation_tokens) {
		this.validation_tokens = validation_tokens;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
