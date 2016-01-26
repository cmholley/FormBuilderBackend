package dash.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
 * object resource placeholder for json/xml representation
 *
 * @author tyler.swensen@gmail.com
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FormResponse implements IAclObject {

	private static final long serialVersionUID = -8039686696076337053L;
	
	/** id of the object */
	@Id
	@GeneratedValue
	@Column(name = "id")
	@XmlElement(name = "id")
	private Long id;

	@Column(name = "form_id")
	@XmlElement(name = "form_id")
	private Long form_id;

	@Column(name = "owner_id")
	@XmlElement(name = "owner_id")
	private Long owner_id;

	@Column(name = "study_id")
	@XmlElement(name = "study_id")
	private Long studyId;

	@Column(name = "insertion_date")
	@XmlElement(name = "insertion_date")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date insertion_date;

	@Column(name = "latest_update")
	@XmlElement(name = "latest_update")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date latest_update;

	@Column(name = "is_complete")
	@XmlElement(name = "is_complete")
	private boolean is_complete;

	@Column(name = "document_folder")
	@XmlElement(name = "document_folder")
	private String document_folder;

	@Column(name = "responder_email")
	@XmlElement(name = "responder_email")
	private String responder_email;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "form_response_entries", joinColumns = { @JoinColumn(name = "form_response_id") })
	@XmlElement(name = "entries")
	private Set<Entry> entries = new HashSet<Entry>();

	@Column(name = "send_receipt")
	@XmlElement(name = "send_receipt")
	private boolean send_receipt;

	public FormResponse() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getForm_id() {
		return form_id;
	}

	public void setForm_id(Long form_id) {
		this.form_id = form_id;
	}

	public Long getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(Long owner_id) {
		this.owner_id = owner_id;
	}

	public Date getInsertion_date() {
		return insertion_date;
	}

	public void setInsertion_date(Date insertion_date) {
		this.insertion_date = insertion_date;
	}

	public Date getLatest_update() {
		return latest_update;
	}

	public void setLatest_update(Date latest_update) {
		this.latest_update = latest_update;
	}

	public boolean isIs_complete() {
		return is_complete;
	}

	public void setIs_complete(boolean is_complete) {
		this.is_complete = is_complete;
	}

	public Set<Entry> getEntries() {
		return entries;
	}

	public void setEntries(Set<Entry> entries) {
		this.entries = entries;
	}

	public String getDocument_folder() {
		return document_folder;
	}

	public void setDocument_folder(String document_folder) {
		this.document_folder = document_folder;
	}

	public String getResponderEmail() {
		return responder_email;
	}

	public void setResponderEmail(String responder_email) {
		this.responder_email = responder_email;
	}

	public boolean getSend_receipt() {
		return send_receipt;
	}

	public void setSend_receipt(boolean send_receipt) {
		this.send_receipt = send_receipt;
	}

	public Long getStudyId() {
		return studyId;
	}

	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}