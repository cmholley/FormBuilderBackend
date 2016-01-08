package dash.pojo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dash.dao.SampleObjectEntity;
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
public class SampleObject implements IAclObject {

	/** id of the object */
	@XmlElement(name = "id")
	private Long id;

	@XmlElement(name = "document_folder")
	private String document_folder;

	/** basic_field_sample of the object */
	@XmlElement(name = "basic_field_sample")
	private String basic_field_sample;

	/** insertion date in the database */
	@XmlElement(name = "insertionDate")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date time_stamp_sample;

	public SampleObject(SampleObjectEntity objectEntity) {
		try {
			BeanUtils.copyProperties(this, objectEntity);
		} catch (IllegalAccessException e) {

			Logger logger = LoggerFactory.getLogger(this.getClass());
			logger.error("Exception thrown in " + this.getClass().getName(), e);
		} catch (InvocationTargetException e) {

			Logger logger = LoggerFactory.getLogger(this.getClass());
			logger.error("Exception thrown in " + this.getClass().getName(), e);
		}
	}

	public SampleObject(String basic_field_sample) {

		this.basic_field_sample = basic_field_sample;

	}

	public SampleObject() {
	}

	public String getBasic_field_sample() {
		return basic_field_sample;
	}

	public void setBasic_field_sample(String basic_field_sample) {
		this.basic_field_sample = basic_field_sample;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDocument_folder() {
		return document_folder;
	}

	public void setDocument_folder(String document_folder) {
		this.document_folder = document_folder;
	}

	public Date getTime_stamp_sample() {
		return time_stamp_sample;
	}

	public void setTime_stamp_sample(Date time_stamp_sample) {
		this.time_stamp_sample = time_stamp_sample;
	}

}
