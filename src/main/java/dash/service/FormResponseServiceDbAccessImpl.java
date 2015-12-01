package dash.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dash.dao.FormResponseDao;
import dash.dao.FormResponseEntity;
import dash.errorhandling.AppException;
import dash.filters.AppConstants;
import dash.helpers.NullAwareBeanUtilsBean;
import dash.pojo.Entry;
import dash.pojo.Form;
import dash.pojo.FormResponse;
import dash.pojo.Question;
import dash.security.CustomPermission;
import dash.security.GenericAclController;

/**
 * An Example service layer implementation. Here is where all business logic
 * should be implemented. Create a GenericAclController for each type of object
 * you will be managing permissions for in this service.
 * 
 * @author Tyler.swensen@gmail.com
 *
 */
@Component("formResponseService")
public class FormResponseServiceDbAccessImpl extends ApplicationObjectSupport
		implements FormResponseService {

	@Autowired
	FormResponseDao formResponseDao;

	@Autowired
	FormService formService;

	@Autowired
	private GenericAclController<FormResponse> aclController;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SimpleMailMessage templateMessage;

	/********************* Create related methods implementation ***********************/
	@Override
	@Transactional
	public Long createFormResponse(FormResponse formResponse, Form form)
			throws AppException {

		//Generate empty form response entries for each question
		Set<Entry> entries= new HashSet<Entry>();
		Set<Question> questions= form.getQuestions();
		
		if(questions != null && formResponse.getEntries().isEmpty()){
			for (Question question : questions) {
				Entry entry = new Entry();
				entry.setQuestion_id(question.getQuestion_id());
				entry.setLabel(question.getLabel());
				entries.add(entry);
			}
			formResponse.setEntries(entries);
		}

		long formResponseId = formResponseDao
				.createFormResponse(new FormResponseEntity(formResponse));
		formResponse.setId(formResponseId);
		aclController.createACL(formResponse);
		aclController.createAce(formResponse, CustomPermission.READ);
		aclController.createAce(formResponse, CustomPermission.WRITE);
		aclController.createAce(formResponse, CustomPermission.DELETE);


		return formResponseId;
	}

	@Override
	@Transactional
	public void createFormResponses(List<FormResponse> formResponses)
			throws AppException {
		Form form;
		for (FormResponse formResponse : formResponses) {
			form = formService.getFormById(formResponse.getForm_id());
			createFormResponse(formResponse, form);
		}
	}

	// ******************** Read related methods implementation
	// **********************
	@Override
	public List<FormResponse> getFormResponses(int numberOfFormResponses,
			Long startIndex) throws AppException {

		List<FormResponseEntity> formResponses = formResponseDao
				.getFormResponses(numberOfFormResponses, startIndex);
		return getFormResponsesFromEntities(formResponses);
	}

	public List<FormResponse> getMyFormResponses(int numberOfFormResponses,
			Long startIndex) throws AppException {

		// TODO: Instead of returning the getAll function we should do a lookup
		// in the ACL tables to compile a list of all objects where the user has
		// the required permission and then do a select query to build a
		// collection
		// of only those objects.
		return getFormResponses(numberOfFormResponses, startIndex);
	}

	
	//Sends a plain text email with confirmation. 
	private void sendReceiptEmail(FormResponse formResponse, Form form) {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setFrom("NOREPLY@Housuggest.org");
		msg.setTo(formResponse.getResponderEmail());
		msg.setSubject("Confirmation Receipt for FormBuilder Form: " + form.getName());
		msg.setText(form.getEmail_message()); 
		try {
			this.mailSender.send(msg);
		} catch (MailException ex) {
			System.err.println(ex.getMessage()); //Do we need a front end error if the sending fails?
		}
	}
	
	//Sends an email with text and attachment
	private void sendEmbeddedResponse(FormResponse formResponse, Form form) {

		MimeMessage message = this.mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("NOREPLY@Housuggest.org");
			helper.setFrom(templateMessage.getFrom());
			helper.setTo(form.getConfirmation_recipient_email());
			helper.setSubject("Response Alert for FormBuilder form: " + form.getName());
			helper.setText(generateHTML(formResponse, form), true);
			

		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		mailSender.send(message);
	}

	private String generateHTML(FormResponse formResponse, Form form){
		String html= "";
		html += ("<html><body><table><tr><td valign=\"top\" style=\"background-color:#c8102e;padding:20px 20p	x 20px 20px\"><table cellspacing=\"0\" border=\"0\" cellpadding=\"0\" width=\"100%\"><tbody><tr><td align=\"left\" valign=\"top\"><h2 style=\"font-size:20px;font-weight:bold;margin:10px 0 10px 0;font-family:Arial;color:#ffffff;padding:0\">"
				+ form.getName() + "</h2>");
		if(form.getSubtitle() != null){
			html += ("<h2 style=\"font-size:15px;font-weight:bold;margin:10px 0 10px 0;font-family:Arial;color:#ffffff;padding:0\">"
				+ form.getSubtitle() + "</h2>");
		}
		
		HashMap<Integer, String> htmlList = new HashMap<Integer, String>();
		for(Entry entry : formResponse.getEntries()){
			long questionID = entry.getQuestion_id();
			String component = null;
			String temp;
			int index = 0;
			for(Question question : form.getQuestions()){
				if(questionID == question.getQuestion_id()){
					component = question.getComponent();
					index = question.getIndex();
					break;
				}
			}
			if(component.equals("dateInput")){
				String dateString;
				try{
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
					Date utilDate = format.parse(entry.getValue());
					format = new SimpleDateFormat("MM/dd/yyyy");
					dateString = format.format(utilDate);
				}catch(ParseException e){
					dateString = entry.getValue();
				}
				temp = ("<tr><td align=\"left\" valign=\"top\" width=\"300\" "
						+ "style=\"background-color:#eeeeee;border-bottom:1px solid #cccccc\">"
						+ "<p style=\"font-size:13px;font-weight:bold;margin:14px 0 14px 5px;"
						+ "font-family:Arial;color:#333333;padding:0\">"
						+ entry.getLabel() + "</p></td>"
						+ "<td align=\"left\" valign=\"top\" width=\"300\" "
						+ "style=\"background-color:#eeeeee;border-bottom:1px solid #cccccc\">"
						+ "<p style=\"font-size:13px;font-weight:normal;margin:14px 0 14px 0;"
						+ "font-family:Arial;color:#333333;padding:0\">"
						+ dateString +"</p></td></tr>");
			}
			else if(component.equals("dateTimeInput")){
				String dateString;
				try{
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
					Date utilDate = format.parse(entry.getValue());
					format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
					dateString = format.format(utilDate);
					}catch (ParseException e){
					dateString = entry.getValue();
					}
				temp = ("<tr><td align=\"left\" valign=\"top\" width=\"300\" "
						+ "style=\"background-color:#eeeeee;border-bottom:1px solid #cccccc\">"
						+ "<p style=\"font-size:13px;font-weight:bold;margin:14px 0 14px 5px;"
						+ "font-family:Arial;color:#333333;padding:0\">"
						+ entry.getLabel() + "</p></td>"
						+ "<td align=\"left\" valign=\"top\" width=\"300\" "
						+ "style=\"background-color:#eeeeee;border-bottom:1px solid #cccccc\">"
						+ "<p style=\"font-size:13px;font-weight:normal;margin:14px 0 14px 0;"
						+ "font-family:Arial;color:#333333;padding:0\">"
						+ dateString +"</p></td></tr>");
			}
			else if(component.equals("fileUpload")){
				if(entry.getValue().equals("")){
					temp = ("<tr><td align=\"left\" valign=\"top\" width=\"300\" "
							+ "style=\"background-color:#eeeeee;border-bottom:1px solid #cccccc\">"
							+ "<p style=\"font-size:13px;font-weight:bold;margin:14px 0 14px 5px;"
							+ "font-family:Arial;color:#333333;padding:0\">"
							+ entry.getLabel() + "</p></td>"
							+ "<td align=\"left\" valign=\"top\" width=\"300\" "
							+ "style=\"background-color:#eeeeee;border-bottom:1px solid #cccccc\">"
							+ "<p style=\"font-size:13px;font-weight:normal;margin:14px 0 14px 0;"
							+ "font-family:Arial;color:#333333;padding:0\">"
							+ "No File Uploaded" +"</p></td></tr>");
				}
				else{	
				temp = ("<tr><td align=\"left\" valign=\"top\" width=\"300\" "
						+ "style=\"background-color:#eeeeee;border-bottom:1px solid #cccccc\">"
						+ "<p style=\"font-size:13px;font-weight:bold;margin:14px 0 14px 5px;"
						+ "font-family:Arial;color:#333333;padding:0\">"
						+ entry.getLabel() + "</p></td>"
						+ "<td align=\"left\" valign=\"top\" width=\"300\" "
						+ "style=\"background-color:#eeeeee;border-bottom:1px solid #cccccc\">"
						+ "<p style=\"font-size:13px;font-weight:normal;margin:14px 0 14px 0;"
						+ "font-family:Arial;color:#333333;padding:0\">"
						+ "http://housuggest.org/FormBuilder/#/file/"+ entry.getValue() +"</p></td></tr>");
				}
			}
			else if(component.equals("section")){
				temp = ("<tr><td colspan=\"2\" style=\"background-color:#5c6266;color:#ffffff\">"
						+ "<h3 style=\"font-size:15px;font-weight:bold;margin:14px 14px 14px 10px;"
						+ "font-family:Arial;color:#ffffff;padding:0\">"
						+ entry.getLabel() +"</h3></td></tr>");
			}
			else{
				temp = ("<tr><td align=\"left\" valign=\"top\" width=\"300\" "
						+ "style=\"background-color:#eeeeee;border-bottom:1px solid #cccccc\">"
						+ "<p style=\"font-size:13px;font-weight:bold;margin:14px 0 14px 5px;"
						+ "font-family:Arial;color:#333333;padding:0\">"
						+ entry.getLabel() + "</p></td>"
						+ "<td align=\"left\" valign=\"top\" width=\"300\" "
						+ "style=\"background-color:#eeeeee;border-bottom:1px solid #cccccc\">"
						+ "<p style=\"font-size:13px;font-weight:normal;margin:14px 0 14px 0;"
						+ "font-family:Arial;color:#333333;padding:0\">"
						+ entry.getValue() +"</p></td></tr>");
			}
			htmlList.put(index, temp);
		}
		for(int i = 0; i < htmlList.size(); i++){
			String temp = (String) htmlList.get(i);
			html += temp;
		}
		html += ("</tbody></table></td></tr></tbody></table></body></html>");
		return html;
	}
	
	@Override
	public FormResponse getFormResponseById(Long id, Form form) throws AppException {
		FormResponseEntity formResponseById = formResponseDao
				.getFormResponseById(id);
		if (formResponseById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404, "The formResponse you requested with id " + id
							+ " was not found in the database",
					"Verify the existence of the formResponse with the id "
							+ id + " in the database",
					AppConstants.DASH_POST_URL);
		}

		return new FormResponse(formResponseDao.getFormResponseById(id));
	}

	@Override
	public List<FormResponse> getFormResponsesByFormId(Long id, int numberOfFormResponses, int page, Form form) throws AppException {
		List<FormResponseEntity> formResponsesByFormId = formResponseDao
				.getFormResponsesByFormId(id, numberOfFormResponses, page);
		if (formResponsesByFormId == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404, "The formResponse you requested with id " + id
							+ " was not found in the database",
					"Verify the existence of the formResponse with the id "
							+ id + " in the database",
					AppConstants.DASH_POST_URL);
		}

		return getFormResponsesFromEntities(formResponsesByFormId);
	}

	private List<FormResponse> getFormResponsesFromEntities(
			List<FormResponseEntity> formResponseEntities) {
		List<FormResponse> responses = new ArrayList<FormResponse>();
		for (FormResponseEntity formResponseEntity : formResponseEntities) {
			responses.add(new FormResponse(formResponseEntity));
		}

		return responses;
	}

	/**
	 * save uploaded file to new location
	 */
	public void uploadFile(InputStream uploadedInputStream,
			String uploadedFileLocation) throws AppException {

		try {
			File file = new File(uploadedFileLocation);
			file.getParentFile().mkdirs();
			OutputStream out = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			throw new AppException(
					Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), 500,
					"Could not upload file due to IOException", "\n\n"
							+ e.getMessage(), AppConstants.DASH_POST_URL);
		}

	}

	public File getUploadFile(String uploadedFileLocation) throws AppException {
		return new File(uploadedFileLocation);
	}

	// public List<FormResponse> getRecentFormResponses(int
	// numberOfDaysToLookBack) {
	// List<FormResponseEntity> recentFormResponses = formResponseDao
	// .getRecentFormResponses(numberOfDaysToLookBack);
	//
	// return getFormResponsesFromEntities(recentFormResponses);
	// }

	@Override
	public int getNumberOfFormResponses() {
		int totalNumber = formResponseDao.getNumberOfFormResponses();

		return totalNumber;

	}

	/********************* UPDATE-related methods implementation ***********************/

	@Override
	@Transactional
	public void updateFullyFormResponse(FormResponse formResponse)
			throws AppException {

		FormResponse verifyFormResponseExistenceById = verifyFormResponseExistenceById(formResponse
				.getId());
		if (verifyFormResponseExistenceById == null) {
			throw new AppException(
					Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - "
							+ formResponse.getId(), AppConstants.DASH_POST_URL);
		}
		copyAllProperties(verifyFormResponseExistenceById, formResponse);
		Form form = formService.getFormById(formResponse.getForm_id());	
		
		formResponseDao.updateFormResponse(new FormResponseEntity(
				verifyFormResponseExistenceById));
		if(formResponse.isIs_complete()){
			if (formResponse.getSend_receipt()) //Sends Confirmation Email to Responder
				this.sendReceiptEmail(formResponse, form);
			if(form.getSend_notification())
				this.sendEmbeddedResponse(formResponse, form);
		}
	}

	private void copyAllProperties(
			FormResponse verifyFormResponseExistenceById,
			FormResponse formResponse) {
		// If you would like to allow null values use the following line.
		// Reference PostServiceImpl in the VolunteerManagementApp for more
		// details.
		 BeanUtilsBean withNull=new BeanUtilsBean();

		// Assuming the NullAwareBeanUtilsBean is sufficient this code can be
		// used.
		//BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
		try {
			withNull.copyProperties(verifyFormResponseExistenceById,
					formResponse);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/********************* DELETE-related methods implementation ***********************/

	@Override
	@Transactional
	public void deleteFormResponse(FormResponse formResponse, Form form) {

		formResponseDao.deleteFormResponseById(formResponse);
		aclController.deleteACL(formResponse);

	}

	@Override
	@Transactional
	// TODO: This shouldn't exist? If it must, then it needs to accept a list of
	// FormResponses to delete
	public void deleteFormResponses() {
		formResponseDao.deleteFormResponses();
	}

	@Override
	public FormResponse verifyFormResponseExistenceById(Long id) {
		FormResponseEntity formResponseById = formResponseDao
				.getFormResponseById(id);
		if (formResponseById == null) {
			return null;
		} else {
			return new FormResponse(formResponseById);
		}
	}

	@Override
	@Transactional
	public void updatePartiallyFormResponse(FormResponse formResponse)
			throws AppException {
		// do a validation to verify existence of the resource
		FormResponse verifyFormResponseExistenceById = verifyFormResponseExistenceById(formResponse
				.getId());
		if (verifyFormResponseExistenceById == null) {
			throw new AppException(
					Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - "
							+ formResponse.getId(), AppConstants.DASH_POST_URL);
		}
		copyPartialProperties(verifyFormResponseExistenceById, formResponse);
		formResponseDao.updateFormResponse(new FormResponseEntity(
				verifyFormResponseExistenceById));

	}

	private void copyPartialProperties(
			FormResponse verifyFormResponseExistenceById,
			FormResponse formResponse) {

		BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
		try {
			notNull.copyProperties(verifyFormResponseExistenceById,
					formResponse);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void deleteUploadFile(String uploadedFileLocation)
			throws AppException {
		Path path = Paths.get(uploadedFileLocation);
		try {
			Files.delete(path);
		} catch (NoSuchFileException x) {
			x.printStackTrace();
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404, "NoSuchFileException thrown, Operation unsuccesful.",
					"Please ensure the file you are attempting to"
							+ " delete exists at " + path + ".",
					AppConstants.DASH_POST_URL);

		} catch (DirectoryNotEmptyException x) {
			x.printStackTrace();
			throw new AppException(
					Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
					404,
					"DirectoryNotEmptyException thrown, operation unsuccesful.",
					"This method should not attempt to delete,"
							+ " This should be considered a very serious error. Occured at "
							+ path + ".", AppConstants.DASH_POST_URL);
		} catch (IOException x) {
			x.printStackTrace();
			throw new AppException(
					Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
					500,
					"IOException thrown and the designated file was not deleted.",
					" Permission problems occured at " + path + ".",
					AppConstants.DASH_POST_URL);
		}

	}

	@Override
	public List<String> getFileNames(FormResponse formResponse) {
		List<String> results = new ArrayList<String>();

		File[] files = new File(AppConstants.APPLICATION_UPLOAD_LOCATION_FOLDER
				+ "/" + formResponse.getDocument_folder()).listFiles();
		// If this pathname does not denote a directory, then listFiles()
		// returns null.

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					results.add(file.getName());
				}
			}
		}
		return results;
	}

}
