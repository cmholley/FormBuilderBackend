package dash.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.beanutils.BeanUtils;

import dash.pojo.User;
import dash.pojo.User.NOTIFICATION_PREFERENCE;

/**
 * User entity
 * @author plindner
 *
 */
@Entity
@Table(name="user_data")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = -8039686696076337053L;

	/** id of the user */
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	/** username of the user */
	@Column(name = "username")
	private String username;

	/** firstname of the user */
	@Column(name = "firstName")
	private String firstName;

	/** lastname of the user */
	@Column(name = "lastName")
	private String lastName;

	/** city of the user */
	@Column(name = "city")
	private String city;

	/** home phone number of the user */
	@Column(name = "homePhone")
	private String homePhone;

	/** cellPhone number of the user */
	@Column(name = "cellPhone")
	private String cellPhone;

	/** email address of the user */
	@Column(name = "email")
	private String email;

	/** path to stored picture of the user */
	@Column(name = "picture")
	private String picturePath;

	@ElementCollection (fetch= FetchType.EAGER)
	@CollectionTable(name = "active_studies", joinColumns = {@JoinColumn(name="user_id")})
	@Column(name = "value")
	private Map<Long, Long> activeStudies = new HashMap<Long, Long>();
	
	@Enumerated(EnumType.STRING)
	@Column (name = "notification_preference")
	private NOTIFICATION_PREFERENCE notificationPreference = NOTIFICATION_PREFERENCE.EMAIL;
	
	/** insertion date in the database */
	@Column(name = "insertion_date")
	private Date insertionDate;

	@Column(name = "is_email_verified")
	private boolean is_email_verified;
	
	@ElementCollection (fetch= FetchType.EAGER)
	@CollectionTable(name = "validation_tokens", joinColumns = {@JoinColumn(name="user_id")})
	private Set<ValidationTokenEntity> validation_tokens = new HashSet<ValidationTokenEntity>();
	
	public UserEntity(){}

	public UserEntity( String username, 
			String firstName,  String lastName,  String city,
			String homePhone,  String cellPhone,  String email,
			String picturePath) {

		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.city = city;
		this.homePhone = homePhone;
		this.cellPhone = cellPhone;
		this.email = email;
		this.picturePath = picturePath;

	}

	public UserEntity(User user) {
		try {
			BeanUtils.copyProperties(this, user);
		} catch ( IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public void setLastName( String lastName) {
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

	public void setId( Long id) {
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

	public void setNotificationPreference(
			NOTIFICATION_PREFERENCE notificationPreference) {
		this.notificationPreference = notificationPreference;
	}

	public boolean isIs_email_verified() {
		return is_email_verified;
	}

	public void setIs_email_verified(boolean is_email_verified) {
		this.is_email_verified = is_email_verified;
	}

	public Set<ValidationTokenEntity> getValidation_tokens() {
		return validation_tokens;
	}

	public void setValidation_tokens(Set<ValidationTokenEntity> validation_tokens) {
		this.validation_tokens = validation_tokens;
	}

}
