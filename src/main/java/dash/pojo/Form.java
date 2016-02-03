package dash.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dash.helpers.DateISO8601Adapter;
import dash.security.IAclObject;

/**
 * Form object definition Data representation of a collection of questions to be
 * presented to a user as a single document or Form to fill out.
 *
 * @author tyler.swensen@gmail.com
 *
 */
@Entity
@Table(name = "forms")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Form implements IAclObject, Serializable {

	public static enum THEME {
		PLAIN
	};

	private static final long serialVersionUID = -8039686696076337053L;

	/** id of the form */
	@Id
	@GeneratedValue
	@Column(name = "id")
	@XmlElement(name = "id")
	private Long id;

	/** name of the form */
	@Column(name = "name")
	@XmlElement(name = "name")
	private String name;

	@Column(name = "subtitle")
	@XmlElement(name = "subtitle")
	private String subtitle;

	/** insertion date in the database */
	@Column(name = "insertion_date")
	@XmlElement(name = "insertion_date")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date insertionDate;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "questions", joinColumns = { @JoinColumn(name = "form_id") })
	@XmlElement(name = "questions")
	private Set<Question> questions = new HashSet<Question>();

	//We explicitly notify hibernate that the column will be a bit to prevent 
	//Errors during the schema validation
	@Column(name = "enabled", columnDefinition = "BIT", length = 1)
	@XmlElement(name = "enabled")
	private boolean enabled;

	//We explicitly notify hibernate that the column will be a bit to prevent 
	//Errors during the schema validation
	@Column(name = "public", columnDefinition = "BIT", length = 1)
	@XmlElement(name = "public")
	private boolean publi;

	@Enumerated(EnumType.STRING)
	@Column(name = "theme")
	@XmlElement(name = "theme")
	private THEME theme = THEME.PLAIN;

	//We explicitly notify hibernate that the column will be a bit to prevent 
	//Errors during the schema validation
	@Column(name = "redirect_to_url", columnDefinition = "BIT", length = 1)
	@XmlElement(name = "redirect_to_url")
	private boolean redirectToUrl;

	//We explicitly notify hibernate that the column will be a bit to prevent 
	//Errors during the schema validation
	@Column(name = "send_notification", columnDefinition = "BIT", length = 1)
	@XmlElement(name = "send_notification")
	private boolean sendNotification;

	//We explicitly notify hibernate that the column will be a bit to prevent 
	//Errors during the schema validation
	@Column(name = "send_receipt", columnDefinition = "BIT", length = 1)
	@XmlElement(name = "send_receipt")
	private boolean sendReceipt;

	@Column(name = "email_message")
	@XmlElement(name = "email_message") // Message for receipt email
	private String emailMessage;

	@Column(name = "completed_message")
	@XmlElement(name = "completed_message")
	private String completedMessage;

	@Column(name = "redirect_url")
	@XmlElement(name = "redirect_url")
	private String redirectUrl;

	@Column(name = "expiration_date")
	@XmlElement(name = "expiration_date")
	private Date expirationDate;

	@Column(name = "closed_message")
	@XmlElement(name = "closed_message")
	private String closedMessage;

	private HashMap<String, String> permissions;

	@Column(name = "confirmation_recipient_email")
	@XmlElement(name = "confirmation_recipient_email")
	private String confirmationRecipientEmail;

	public Form() {
		this.closedMessage = "We're sorry, this form is closed";
		this.completedMessage = "Thank you for your submission, your response has been recorded";
		this.emailMessage = "Thank you for completing this form. Your response has been recorded";
		// User user =
		// (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// this.confirmation_recipient_email = user.getUsername();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public Date getInsertionDate() {
		return insertionDate;
	}

	public void setInsertionDate(Date insertionDate) {
		this.insertionDate = insertionDate;
	}

	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isPubli() {
		return publi;
	}

	public void setPubli(boolean publi) {
		this.publi = publi;
	}

	public THEME getTheme() {
		return theme;
	}

	public void setTheme(THEME theme) {
		this.theme = theme;
	}

	public boolean isRedirectToUrl() {
		return redirectToUrl;
	}

	public void setRedirectToUrl(boolean redirectToUrl) {
		this.redirectToUrl = redirectToUrl;
	}

	public boolean isSendNotification() {
		return sendNotification;
	}

	public void setSendNotification(boolean sendNotification) {
		this.sendNotification = sendNotification;
	}

	public boolean isSendReceipt() {
		return sendReceipt;
	}

	public void setSendReceipt(boolean sendReceipt) {
		this.sendReceipt = sendReceipt;
	}

	public String getEmailMessage() {
		return emailMessage;
	}

	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}

	public String getCompletedMessage() {
		return completedMessage;
	}

	public void setCompletedMessage(String completedMessage) {
		this.completedMessage = completedMessage;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getClosedMessage() {
		return closedMessage;
	}

	public void setClosedMessage(String closedMessage) {
		this.closedMessage = closedMessage;
	}

	public HashMap<String, String> getPermissions() {
		return permissions;
	}

	public void setPermissions(HashMap<String, String> permissions) {
		this.permissions = permissions;
	}

	public String getConfirmationRecipientEmail() {
		return confirmationRecipientEmail;
	}

	public void setConfirmationRecipientEmail(String confirmationRecipientEmail) {
		this.confirmationRecipientEmail = confirmationRecipientEmail;
	}


	// Tests for the expiration of the particular form at the current time.
	public boolean isExpired() {
		if (expirationDate == null) {
			return false;
		}
		if (expirationDate.before(new Date())) {
			return true;
		} else {
			return false;
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
