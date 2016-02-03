package dash.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dash.filters.AppConstants;
import dash.filters.AppConstants.InputValidation;

/**
 * An embeddable object resource for json/xml representation
 *
 * @author tyler.swensen@gmail.com
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Embeddable
public class Question implements Serializable {

	private static final long serialVersionUID = -8334607009238618323L;

	/**
	 * Defines the type of question, ie. TextInput, MultipleChoice, Checkbox...
	 * ect
	 */
	@XmlElement(name = "component")
	@Column(name = "component")
	private String component;

	@GeneratedValue
	@XmlElement(name = "question_id")
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * Can this question be changed, possibly useful to prevent accidental
	 * removal of a question which is critical to an organization or is a
	 * dependency for an external application
	 */
	//We explicitly notify hibernate that the column will be a bit to prevent 
	//Errors during the schema validation
	@XmlElement(name = "editable")
	@Column(name = "editable", columnDefinition = "BIT", length = 1)
	private boolean editable = true;

	/** The order in which this question appears on the form it belongs to. */
	@XmlElement(name = "index")
	@Column(name = "form_index")
	private int index;

	/**
	 * This is the primary text that identifies the question to both the person
	 * filling out the form and to the user building the form.
	 */
	@XmlElement(name = "label")
	@Column(name = "label")
	private String label = "";

	/** This is displayed as a low profile hint beneath the input area */
	@XmlElement(name = "description")
	@Column(name = "description")
	private String description = "";

	/** A string which appears as ghosted text inside the input area */
	@XmlElement(name = "placeholder")
	@Column(name = "placeholder")
	private String placeholder = "";

	/**
	 * An array of strings which hold possible responses to multiple choice and
	 * checkbox questions.
	 */

	@XmlElement(name = "options")
	@Column(name = "options")
	private String options = "[]";

	/** Must the question have a response in order to submit the form */
	//We explicitly notify hibernate that the column will be a bit to prevent 
	//Errors during the schema validation
	@XmlElement(name = "required")
	@Column(name = "required", columnDefinition = "BIT", length = 1)
	private boolean required;

	/**
	 * Are there any constraints on what text may be submitted as input, ie.
	 * email, number, url ect..
	 */
	@XmlElement(name = "validation")
	@Column(name = "validation")
	private AppConstants.InputValidation validation;

	/** A JSON of misc question settings. */
	@XmlElement(name = "settings")
	@Column(name = "settings")
	private String settings = "{}";

	public Question() {
	}

	public Question(String component, boolean editable, int index, String label, String description, String placeholder,
			String options, boolean required, InputValidation validation) {
		super();
		this.component = component;
		this.editable = editable;
		this.index = index;
		this.label = label;
		this.description = description;
		this.placeholder = placeholder;
		this.options = options;
		this.required = required;
		this.validation = validation;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public Long getQuestion_id() {
		return questionId;
	}

	public void setQuestion_id(Long question_id) {
		this.questionId = question_id;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public AppConstants.InputValidation getValidation() {
		return validation;
	}

	public void setValidation(AppConstants.InputValidation validation) {
		this.validation = validation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
