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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dash.pojo.Entry;
import dash.pojo.FormResponse;

/**
 * This is an example implementation of an entity for a simple object (non-user)
 *
 * @author Tyler.swensen@gmail.com
 *
 */
@Entity
@Table(name = "form_responses")
public class FormResponseEntity implements Serializable {

	private static final long serialVersionUID = -8039686696076337053L;

	/**
	 * id of the user Be aware that every object/entity MUST have an id
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "form_id")
	private Long form_id;

	@Column(name = "owner_id")
	private Long owner_id;

	@Column(name = "study_id")
	private Long studyId;

	@Column(name = "insertion_date")
	private Date insertion_date;

	@Column(name = "latest_update")
	private Date latest_update;

	@Column(name = "is_complete")
	private boolean is_complete;

	@Column(name = "document_folder")
	private String document_folder;

	@Column(name = "responder_email")
	private String responder_email;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "form_response_entries", joinColumns = { @JoinColumn(name = "form_response_id") })
	private Set<Entry> entries = new HashSet<Entry>();

	public FormResponseEntity() {
	}

	public FormResponseEntity(FormResponse formResponse) {
		try {
			BeanUtils.copyProperties(this, formResponse);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			Logger logger = LoggerFactory.getLogger(this.getClass());
			logger.error("Exception thrown in " + this.getClass().getName(), e);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			Logger logger = LoggerFactory.getLogger(this.getClass());
			logger.error("Exception thrown in " + this.getClass().getName(), e);
		}
	}

	public FormResponseEntity(Long form_id, Long owner_id, Date insertion_date, Date latest_update, boolean is_complete,
			Set<Entry> entries, String document_folder, String responder_email, boolean send_receipt) {
		super();
		this.form_id = form_id;
		this.owner_id = owner_id;
		this.insertion_date = insertion_date;
		this.latest_update = latest_update;
		this.is_complete = is_complete;
		this.entries = entries;
		this.document_folder = document_folder;
		this.responder_email = responder_email;
	}

	public String getDocument_folder() {
		return document_folder;
	}

	public void setDocument_folder(String document_folder) {
		this.document_folder = document_folder;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Set<Entry> getEntries() {
		return entries;
	}

	public void setEntries(Set<Entry> entries) {
		this.entries = entries;
	}

	public String getResponderEmail() {
		return responder_email;
	}

	public void setResponderEmail(String responder_email) {
		this.responder_email = responder_email;
	}

	public Long getStudyId() {
		return studyId;
	}

	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}

}
