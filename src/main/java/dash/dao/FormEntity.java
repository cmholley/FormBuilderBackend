package dash.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
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
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import dash.pojo.Form;
import dash.pojo.User;
import dash.pojo.Form.THEME;
import dash.pojo.Question;

/**
 * This is an example implementation of an entity for a simple object (non-user)
 *
 * @author Tyler.swensen@gmail.com
 *
 */
/**
 * @author Christopher
 *
 */
@Entity
@Table(name="forms")
public class FormEntity implements Serializable {

	private static final long serialVersionUID = -8039686696076337053L;
	
	/** id of the user
	 * Be aware that every object/entity MUST have an id */
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	/** basic_field_sample of the user */
	@Column(name = "name")
	private String name;
	
	@Column(name = "subtitle")
	private String subtitle;

	/** insertion date in the database */
	@Column(name = "insertion_date")
	private Date insertion_date;
	
	@ElementCollection (fetch= FetchType.EAGER)
	@CollectionTable(name = "questions", joinColumns = {@JoinColumn(name="form_id")})
	private Set<Question> questions= new HashSet<Question>();

	@Column(name = "public")
	private boolean publi;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "theme")
	private THEME theme;
	
	@Column(name = "redirect_to_url")
	private boolean redirect_to_url;
	
	@Column(name = "send_notification")
	private boolean send_notification;
	
	@Column(name = "send_receipt")
	private boolean send_receipt; 
	
	@Column(name = "email_message")//Message for receipt email
	private String email_message;
	
	@Column(name = "completed_message")
	private String completed_message;
	
	@Column(name = "redirect_url")
	private String redirect_url;
	
	@Column(name = "expiration_date")
	private Date expiration_date;
	
	@Column(name = "closed_message")
	private String closed_message;
	
	@Column(name = "confirmation_recipient_email")
	private String confirmation_recipient_email;
	
	public FormEntity(){
		this.closed_message = "We're sorry, this form is closed.";
		this.completed_message = "Thank you for your submission, your response has been recorded.";
		this.email_message = "Thank you for completing this form. Your response has been recorded.";
		//User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//this.confirmation_recipient_email = user.getUsername();
	}

<<<<<<< HEAD
	public FormEntity(String name, Set<Question> questions, boolean redirect_to_url,
			boolean enabled, boolean publi, boolean send_notification,
			boolean email_embedded_responses, boolean send_receipt, 
			String email_message, String completed_message, String redirect_url, 
			Date expiration_date, String closed_message, THEME theme) {
=======


	public FormEntity(Long id, String name, String subtitle,
			Date insertion_date, Set<Question> questions, boolean publi,
			boolean enabled, THEME theme, boolean redirect_to_url,
			boolean send_notification, 
			boolean send_receipt, String email_message,
			String completed_message, String redirect_url,
			Date expiration_date, String closed_message, String confirmation_recipient_email) { 
>>>>>>> 555e97c789d228ca3d234cdb1ba92cd55ee93b1f
		super();
		this.name = name;
		this.questions = questions;
		this.redirect_to_url = redirect_to_url;
		this.enabled = enabled;
		this.publi = publi;
		this.send_notification = send_notification;
		this.send_receipt = send_receipt;
<<<<<<< HEAD
		this.email_message = email_message;
		this.completed_message = completed_message;
=======
>>>>>>> 555e97c789d228ca3d234cdb1ba92cd55ee93b1f
		this.redirect_url = redirect_url;
		this.expiration_date = expiration_date;
		this.theme = theme;
		this.confirmation_recipient_email = confirmation_recipient_email;
		this.closed_message = closed_message;
		this.email_message = email_message;
		this.completed_message = completed_message;
	}



	public FormEntity(Form form) {
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



	public Date getInsertion_date() {
		return insertion_date;
	}



	public void setInsertion_date(Date insertion_date) {
		this.insertion_date = insertion_date;
	}



	public Set<Question> getQuestions() {
		return questions;
	}



	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}



	public boolean isPubli() {
		return publi;
	}



	public void setPubli(boolean publi) {
		this.publi = publi;
	}



	public boolean isEnabled() {
		return enabled;
	}



	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	public THEME getTheme() {
		return theme;
	}



	public void setTheme(THEME theme) {
		this.theme = theme;
	}



	public boolean isRedirect_to_url() {
		return redirect_to_url;
	}
<<<<<<< HEAD
	
	public void setRedirect_to_url(boolean newRedirect_to_url){
		this.redirect_to_url = newRedirect_to_url;
	}
	
	public boolean getsend_notification(){
		return send_notification;
	}
	
	public void setsend_notification(boolean newsend_notification){
		this.send_notification = newsend_notification;
	}
	
	
	public boolean getsend_receipt(){
		return send_receipt;
	}
	
	public void setsend_receipt(boolean newsend_receipt){
		this.send_receipt = newsend_receipt;
=======



	public void setRedirect_to_url(boolean redirect_to_url) {
		this.redirect_to_url = redirect_to_url;
	}



	public boolean isSend_notification() {
		return send_notification;
	}



	public void setSend_notification(boolean send_notification) {
		this.send_notification = send_notification;
	}



	public boolean isSend_receipt() {
		return send_receipt;
	}



	public void setSend_receipt(boolean send_receipt) {
		this.send_receipt = send_receipt;
>>>>>>> 555e97c789d228ca3d234cdb1ba92cd55ee93b1f
	}



	public String getEmail_message() {
		return email_message;
	}



	public void setEmail_message(String email_message) {
		this.email_message = email_message;
	}



	public String getCompleted_message() {
		return completed_message;
	}



	public void setCompleted_message(String completed_message) {
		this.completed_message = completed_message;
	}



	public String getRedirect_url() {
		return redirect_url;
	}



	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}



	public Date getExpiration_date() {
		return expiration_date;
	}



	public void setExpiration_date(Date expiration_date) {
		this.expiration_date = expiration_date;
	}



	public String getClosed_message() {
		return closed_message;
	}



	public void setClosed_message(String closed_message) {
		this.closed_message = closed_message;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String getConfirmation_recipient_email() {
		return confirmation_recipient_email;
	}



	public void setConfirmation_recipient_email(String confirmation_recipient_email) {
		this.confirmation_recipient_email = confirmation_recipient_email;
	}

}
