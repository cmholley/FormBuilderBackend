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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dash.dao.StudyDao;
import dash.dao.StudyEntity;
import dash.errorhandling.AppException;
import dash.filters.AppConstants;
import dash.helpers.NullAwareBeanUtilsBean;
import dash.pojo.Form;
import dash.pojo.Study;
import dash.pojo.User;
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
@Component("studyService")
public class StudyServiceDbAccessImpl extends ApplicationObjectSupport
		implements StudyService {

	@Autowired
	StudyDao studyDao;

	@Autowired
	UserService userService;
	
	@Autowired
	private GenericAclController<Study> aclController;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private SimpleMailMessage templateMessage;

	/********************* Create related methods implementation ***********************/
	@Override
	@Transactional
	public Long createStudy(Study study, Form form)
			throws AppException {

		long studyId = studyDao
				.createStudy(new StudyEntity(study));
		study.setId(studyId);
		aclController.createACL(study);
		aclController.createAce(study, CustomPermission.READ);
		aclController.createAce(study, CustomPermission.WRITE);
		aclController.createAce(study, CustomPermission.DELETE);
		return studyId;
	}

	@Override
	@Transactional
	public void createStudies(List<Study> studies)
			throws AppException {
		for (Study study : studies) {
			Form form = new FormServiceDbAccessImpl()
				.getFormById(study.getFormId());
			createStudy(study, form);
		}
	}

	// ******************** Read related methods implementation
	// **********************
	@Override
	public List<Study> getStudies(int numberOfStudies,
			Long startIndex) throws AppException {

		List<StudyEntity> studies = studyDao
				.getStudies(numberOfStudies, startIndex);
		return getStudiesFromEntities(studies);
	}

	@Override
	public Study getStudyById(Long id) throws AppException {
		StudyEntity studyById = studyDao
				.getStudyById(id);
		if (studyById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404, "The study you requested with id " + id
							+ " was not found in the database",
					"Verify the existence of the study with the id "
							+ id + " in the database",
					AppConstants.DASH_POST_URL);
		}

		return new Study(studyDao.getStudyById(id));
	}

	private List<Study> getStudiesFromEntities(
			List<StudyEntity> studyEntities) {
		List<Study> response = new ArrayList<Study>();
		for (StudyEntity studyEntity : studyEntities) {
			response.add(new Study(studyEntity));
		}

		return response;
	}

	/**
	 *  save uploaded file to new location
	 */
	public void uploadFile(InputStream uploadedInputStream,
			String uploadedFileLocation)
			throws AppException {

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

	// public List<Study> getRecentStudies(int
	// numberOfDaysToLookBack) {
	// List<StudyEntity> recentStudies = studyDao
	// .getRecentStudies(numberOfDaysToLookBack);
	//
	// return getStudiesFromEntities(recentStudies);
	// }

	@Override
	public int getNumberOfStudies() {
		int totalNumber = studyDao.getNumberOfStudies();

		return totalNumber;

	}

	/********************* UPDATE-related methods implementation ***********************/

	@Override
	@Transactional
	public void updateFullyStudy(Study study, Form form)
			throws AppException {

		Study verifyStudyExistenceById = verifyStudyExistenceById(study
				.getId());
		if (verifyStudyExistenceById == null) {
			throw new AppException(
					Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - "
							+ study.getId(), AppConstants.DASH_POST_URL);
		}
		copyAllProperties(verifyStudyExistenceById, study);

		studyDao.updateStudy(new StudyEntity(
				verifyStudyExistenceById));

	}

	private void copyAllProperties(
			Study verifyStudyExistenceById,
			Study study) {
		// If you would like to allow null values use the following line.
		// Reference PostServiceImpl in the VolunteerManagementApp for more
		// details.
		// BeanUtilsBean withNull=new BeanUtilsBean();

		// Assuming the NullAwareBeanUtilsBean is sufficient this code can be
		// used.
		BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
		try {
			notNull.copyProperties(verifyStudyExistenceById,
					study);
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
	public void deleteStudy(Study study, Form form) {
		studyDao.deleteStudyById(study);
		aclController.deleteACL(study);

	}

	@Override
	@Transactional
	// TODO: This shouldn't exist? If it must, then it needs to accept a list of
	// Studies to delete
	public void deleteStudies() {
		studyDao.deleteStudies();
	}

	@Override
	public Study verifyStudyExistenceById(Long id) {
		StudyEntity studyById = studyDao
				.getStudyById(id);
		if (studyById == null) {
			return null;
		} else {
			return new Study(studyById);
		}
	}

	@Override
	@Transactional
	public void updatePartiallyStudy(Study study, Form form)
			throws AppException {
		// do a validation to verify existence of the resource
		Study verifyStudyExistenceById = verifyStudyExistenceById(study
				.getId());
		if (verifyStudyExistenceById == null) {
			throw new AppException(
					Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - "
							+ study.getId(), AppConstants.DASH_POST_URL);
		}
		copyPartialProperties(verifyStudyExistenceById, study);
		studyDao.updateStudy(new StudyEntity(
				verifyStudyExistenceById));

	}

	private void copyPartialProperties(
			Study verifyStudyExistenceById,
			Study study) {

		BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
		try {
			notNull.copyProperties(verifyStudyExistenceById,
					study);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public void deleteUploadFile(String uploadedFileLocation) throws AppException {
		Path path = Paths.get(uploadedFileLocation);
		try {
		    Files.delete(path);
		} catch (NoSuchFileException x) {
			x.printStackTrace();
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"NoSuchFileException thrown, Operation unsuccesful.", "Please ensure the file you are attempting to"
					+ " delete exists at "+path+".", AppConstants.DASH_POST_URL);
			
					
		} catch (DirectoryNotEmptyException x) {
			x.printStackTrace();
			throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
					404,
					"DirectoryNotEmptyException thrown, operation unsuccesful.", "This method should not attempt to delete,"
							+ " This should be considered a very serious error. Occured at "+path+".",
					AppConstants.DASH_POST_URL);
		} catch (IOException x) {
			x.printStackTrace();
			throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
					500,
					"IOException thrown and the designated file was not deleted.", 
					" Permission problems occured at "+path+".",
					AppConstants.DASH_POST_URL);
		}
		
	}


	@Override
	public List<Study> getStudiesForForm(long formId, Form form) {
		List<StudyEntity> studies = studyDao
				.getStudiesForForm(formId);
		return getStudiesFromEntities(studies);
	}

	public void sendStudyNotificationEmail(String email, long formId,
			long studyId) {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setFrom("NOREPLY@Housuggest.org");
		msg.setTo(email);
		msg.setSubject("You have a survey to complete");
		msg.setText("You have a survey to complete. Please go to www.housuggest.org/FormViewer or your"
				+ " EMA App, the survey will be active"); 
		try {
			this.mailSender.send(msg);
		} catch (MailException ex) {
			System.err.println(ex.getMessage()); 
		}
		
	}

	
	@Override
	public void sendTextNotification(String cellPhone, long formId, long studyId) {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setFrom("NOREPLY@Housuggest.org");
		setTextAddresses(cellPhone, msg);
		msg.setSubject("Scheduling Test");
		msg.setText("You have a survey to complete. Please go to www.housuggest.org/FormViewer or your"
				+ " EMA App to fill out the survey."); 
		try {
			this.mailSender.send(msg);
		} catch (MailException ex) {
			System.err.println(ex.getMessage()); 
		}
	}
	
	/**
	 * 
	 * @param cellPhone
	 * @param msg
	 * This private function sets the to field of the message to the various 
	 * emails for sms through email for different carriers. Since each phone number
	 * is unique regardless of carrier, only one should go through using the proper carrier
	 * 
	 */
	private void setTextAddresses(String cellPhone, SimpleMailMessage msg){
		List<String> addresses = new ArrayList<String>();
		addresses.add(cellPhone + "@email.uscc.net");//US Cellular
		addresses.add(cellPhone + "@tms.suncom.com");//SunCom
		addresses.add(cellPhone + "@ptel.net");//Powertel
		addresses.add(cellPhone + "@txt.att.net");//AT&T
		addresses.add(cellPhone + "@message.alltel.com");//Alltel
		addresses.add(cellPhone + "@MyMetroPcs.com");//Metro PCS
		addresses.add(cellPhone + "@tmomail.net");//T-Mobile
		addresses.add(cellPhone + "@vmobl.com");//Virgin Mobile
		addresses.add(cellPhone + "@cingularme.com");//Cingular
		addresses.add(cellPhone + "@messaging.sprintpcs.com");//Sprint
		addresses.add(cellPhone + "@vtext.com");//Verizon
		addresses.add(cellPhone + "@messaging.nextel.com");//Nextel
		msg.setTo(addresses.toArray(new String[0]));
	}

	@Override
	@Transactional
	public void insertExpirationTime(Long id, Long expirationTime) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, expirationTime.intValue());
		Date expirationDate = cal.getTime();
		studyDao.insertExpirationTime(id, expirationDate);
	}

	@Override
	@Transactional
	public void expireStudies(){
		List<Long> expiredStudies = studyDao.getExpiredStudies();
		for(Long study : expiredStudies){
			try {
				expireStudy(study);
			} catch (AppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			studyDao.removeExpiredStudy(study);
		}
	}
	
	private void expireStudy(Long study) throws AppException{
		List<Long> userIds = studyDao.getUsersForActiveStudy(study);
		List<User> users = new ArrayList<User>();
		User userTemp;
		for(Long userId : userIds){
			userTemp = userService.getUserById(userId);
			userTemp.getActiveStudies().remove(study);
			users.add(userTemp);
		}
		for(User user : users){
			userService.updateUserJob(user);
		}
	}
	
}
