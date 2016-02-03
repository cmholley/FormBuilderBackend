package dash.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * object resource placeholder for json/xml representation
 *
 * @author tyler.swensen@gmail.com
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Embeddable
public class Entry implements Serializable {

	private static final long serialVersionUID = -3313914953151072441L;

	/** Corresponds to the question.index that this is an answer to */
	@XmlElement(name = "question_id")
	@Column(name = "question_id")
	private Long questionId;

	@GeneratedValue
	@XmlElement(name = "entry_id")
	@Column(name = "entry_id")
	private Long entryId;

	/** Corresponds to the question.label that this is an answer to */
	@XmlElement(name = "label")
	@Column(name = "label")
	private String label = "";

	/** Contains the actual data entered by the person completing the form */
	@XmlElement(name = "value")
	@Column(name = "value")
	private String value = "";

	public Entry() {
	}

	public Entry(Long question_id, String label, String value) {
		super();
		this.questionId = question_id;
		this.label = label;
		this.value = value;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getEntryId() {
		return entryId;
	}

	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
