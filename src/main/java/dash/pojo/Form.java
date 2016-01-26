package dash.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	private Date insertion_date;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "questions", joinColumns = { @JoinColumn(name = "form_id") })
	@XmlElement(name = "questions")
	private Set<Question> questions = new HashSet<Question>();

	@Column(name = "enabled")
	@XmlElement(name = "enabled")
	private boolean enabled;

	@Column(name = "public")
	@XmlElement(name = "public")
	private boolean publi;

	@Enumerated(EnumType.STRING)
	@Column(name = "theme")
	@XmlElement(name = "theme")
	private THEME theme = THEME.PLAIN;

	@Column(name = "redirect_to_url")
	@XmlElement(name = "redirect_to_url")
	private boolean redirect_to_url;

	@Column(name = "send_notification")
	@XmlElement(name = "send_notification")
	private boolean send_notification;

	@Column(name = "send_receipt")
	@XmlElement(name = "send_receipt")
	private boolean send_receipt;

	@Column(name = "email_message")
	@XmlElement(name = "email_message") // Message for receipt email
	private String email_message;

	@Column(name = "completed_message")
	@XmlElement(name = "completed_message")
	private String completed_message;

	@Column(name = "redirect_url")
	@XmlElement(name = "redirect_url")
	private String redirect_url;

	@Column(name = "expiration_date")
	@XmlElement(name = "expiration_date")
	private Date expiration_date;

	@Column(name = "closed_message")
	@XmlElement(name = "closed_message")
	private String closed_message;

	private HashMap<String, String> permissions;

	@Column(name = "confirmation_recipient_email")
	@XmlElement(name = "confirmation_recipient_email")
	private String confirmation_recipient_email;
	
	public Form() {
		this.closed_message = "We're sorry, this form is closed";
		this.completed_message = "Thank you for your submission, your response has been recorded";
		this.email_message = "Thank you for completing this form. Your response has been recorded";
		// User user =
		// (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// this.confirmation_recipient_email = user.getUsername();
	}

	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getInsertion_date() {
		return insertion_date;
	}

	public void setInsertion_date(Date insertion_date) {
		this.insertion_date = insertion_date;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean newEnabled) {
		this.enabled = newEnabled;
	}

	public boolean getPubli() {
		return publi;
	}

	public void setPubli(boolean newPubli) {
		this.publi = newPubli;
	}

	public THEME getTheme() {
		return theme;
	}

	public void setTheme(THEME newTheme) {
		this.theme = newTheme;
	}

	public boolean getRedirect_to_url() {
		return redirect_to_url;
	}

	public void setRedirect_to_url(boolean newRedirect_to_url) {
		this.redirect_to_url = newRedirect_to_url;
	}

	public boolean getSend_notification() {
		return send_notification;
	}

	public void setSend_notification(boolean newSend_notification) {
		this.send_notification = newSend_notification;
	}

	public boolean getSend_receipt() {
		return send_receipt;
	}

	public void setSend_receipt(boolean newSend_receipt) {
		this.send_receipt = newSend_receipt;
	}

	public String getEmail_message() {
		return email_message;
	}

	public void setEmail_message(String newEmail_message) {
		this.email_message = newEmail_message;
	}

	public String getCompleted_message() {
		return completed_message;
	}

	public void setCompleted_message(String newCompleted_message) {
		this.completed_message = newCompleted_message;
	}

	public String getRedirect_url() {
		return redirect_url;
	}

	public void setRedirect_url(String newRedirect_url) {
		this.redirect_url = newRedirect_url;
	}

	public Date getExpiration_date() {
		return expiration_date;
	}

	public void setExpiration_date(Date newExpiration_date) {
		this.expiration_date = newExpiration_date;
	}

	public String getClosed_message() {
		return closed_message;
	}

	public void setClosed_message(String newClosed_message) {
		this.closed_message = newClosed_message;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public HashMap<String, String> getPermissions() {
		return permissions;
	}

	public void setPermissions(HashMap<String, String> permissions) {
		this.permissions = permissions;
	}

	public String getConfirmation_recipient_email() {
		return confirmation_recipient_email;
	}

	public void setConfirmation_recipient_email(String confirmation_recipient_email) {
		this.confirmation_recipient_email = confirmation_recipient_email;
	}

	//Tests for the expiration of the particular form at the current time. 
	public boolean isExpired() {
		if (expiration_date == null) {
			return false;
		}
		if (expiration_date.before(new Date())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
