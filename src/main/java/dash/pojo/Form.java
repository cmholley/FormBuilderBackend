package dash.pojo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.beanutils.BeanUtils;

import dash.dao.FormEntity;
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
public class Form implements IAclObject {

	public static enum THEME {
		PLAIN
	};

	/** id of the form */
	@XmlElement(name = "id")
	private Long id;

	/** name of the form */
	@XmlElement(name = "name")
	private String name;

	@XmlElement(name = "subtitle")
	private String subtitle;

	/** insertion date in the database */
	@XmlElement(name = "insertion_date")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date insertion_date;

	@XmlElement(name = "questions")
	private Set<Question> questions = new HashSet<Question>();

	@XmlElement(name = "enabled")
	private boolean enabled;

	@XmlElement(name = "public")
	private boolean publi;

	@XmlElement(name = "theme")
	private THEME theme = THEME.PLAIN;

	@XmlElement(name = "redirect_to_url")
	private boolean redirect_to_url;

	@XmlElement(name = "send_notification")
	private boolean send_notification;

	@XmlElement(name = "send_receipt")
	private boolean send_receipt;

	@XmlElement(name = "email_message")//Message for receipt email
	private String email_message;

	@XmlElement(name = "completed_message")
	private String completed_message;

	@XmlElement(name = "redirect_url")
	private String redirect_url;

	@XmlElement(name = "expiration_date")
	private Date expiration_date;

	@XmlElement(name = "closed_message")
	private String closed_message;

	private HashMap<String, String> permissions;
	
	@XmlElement(name = "confirmation_recipient_email")
	private String confirmation_recipient_email;

	public Form(FormEntity formEntity) {
		try {
			BeanUtils.copyProperties(this, formEntity);
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}
	}

	public Form(String name, Set<Question> questions, boolean redirect_to_url,
			boolean enabled, boolean publi, boolean send_notification, boolean send_receipt,
			String email_message, String completed_message,
			String redirect_url, Date expiration_date, String closed_message,
			THEME theme, String receipt_message, String confirmation_recipient_email) {
		super();
		this.name = name;
		this.questions = questions;
		this.redirect_to_url = redirect_to_url;
		this.enabled = enabled;
		this.publi = publi;
		this.send_notification = send_notification;
		this.send_receipt = send_receipt;
		this.redirect_url = redirect_url;
		this.expiration_date = expiration_date;
		this.theme = theme;
		this.confirmation_recipient_email = confirmation_recipient_email;
		this.closed_message = closed_message;
		this.email_message = email_message;
		this.completed_message = completed_message;	
	}

	public Form() {
		this.closed_message = "We're sorry, this form is closed";
		this.completed_message = "Thank you for your submission, your response has been recorded";
		this.email_message = "Thank you for completing this form. Your response has been recorded"; 
		//User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//this.confirmation_recipient_email = user.getUsername();
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

	public Date getinsertion_date() {
		return insertion_date;
	}

	public void setinsertion_date(Date insertion_date) {
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
}
