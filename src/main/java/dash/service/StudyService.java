package dash.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import dash.errorhandling.AppException;
import dash.pojo.Form;
import dash.pojo.Study;

/**
 * Example service interface for a basic object.
 * 
 * This is where you set method/object level permissions using Spring annotations.
 * 
 * @author Tyler.swensen@gmail.com
 *
 */

public interface StudyService {
	
	/*
	 * ******************** Create related methods *********************/
	
	/**
	 * Create a new study and set the current user as owner and manager.
	 * @param study
	 * @return
	 * @throws AppException
	 */
	
	@PreAuthorize("hasPermission(#form, 'WRITE')"
			+ " or hasRole('ROLE_ADMIN')")
	public Long createStudy(Study study, Form form) throws AppException;
	
	@PreAuthorize("hasPermission(#study, 'WRITE') or hasRole('ROLE_ADMIN')")
	public void uploadFile(InputStream uploadedInputStream,
			String uploadedFileLocation) throws AppException;

	/*
	 * Create multiple studies as ROOT, testing purposes only.
	 */
	@PreAuthorize("hasRole('ROLE_ROOT')")
	public void createStudies(List<Study> studies) throws AppException;

	/*
	 * ******************* Read related methods ********************
	 */
	/**
	 *
	 * @param orderByInsertionDate
	 *            - if set, it represents the order by criteria (ASC or DESC)
	 *            for displaying studies
	 * @param numberDaysToLookBack
	 *            - if set, it represents number of days to look back for studies,
	 *            null
	 * @return list with studies corresponding to search criteria
	 * @throws AppException
	 */
	//Enable post filter to restrict read access to a collection
	//@PostFilter("hasPermission(filterObject, 'READ')"
	public List<Study> getStudies(int numberOfStudies, Long startIndex) throws AppException;
	
	/**
	 * Returns a study given its id
	 *
	 * @param id
	 * @return
	 * @throws AppException
	 */
	
	//Enable the following line of code to restrict read access to a single object.
	//@PostAuthrorize("hasPermission(returnObject, 'READ')")
	public Study getStudyById(Long id) throws AppException;
	
	@PreAuthorize("hasPermission(#study, 'WRITE') or hasRole('ROLE_ADMIN')")
	public File getUploadFile(String uploadedFileLocation) throws AppException;

	@PreAuthorize("hasPermission(#form, 'WRITE') or hasRole('ROLE_ADMIN')")
	public List<Study> getStudiesForForm(long formId, Form form);
	
	/*
	 * ******************** Update related methods **********************
	 */
	@PreAuthorize("hasPermission(#study, 'WRITE') or hasPermission(#form, 'WRITE')"
			+ " or hasRole('ROLE_ADMIN')")
	public void updateFullyStudy(Study study, Form form) throws AppException;

	@PreAuthorize("hasPermission(#study, 'WRITE') or hasPermission(#form, 'WRITE') "
			+ "or hasRole('ROLE_ADMIN')")
	public void updatePartiallyStudy(Study study, Form form) throws AppException;

	
	/*
	 * ******************** Delete related methods **********************
	 */

	@PreAuthorize("hasPermission(#study, 'DELETE') or hasPermission(#form, 'WRITE')"
			+ " or hasRole('ROLE_ADMIN')")
	public void deleteStudy(Study study, Form form);
	/** removes all studies
	 * DO NOT USE, IMPROPERLY UPDATES ACL_OBJECT table
	 * Functional but does not destroy old acl's which doesnt hurt anything
	 * but they will take up space if this is commonly used */
	@PreAuthorize("hasRole('ROLE_ROOT')")
	public void deleteStudies();
	
	@PreAuthorize("hasPermission(#study, 'DELETE') or hasRole('ROLE_ADMIN')")
	public void deleteUploadFile(String uploadedFileLocation) throws AppException;
	
	/*
	 * ******************** Helper methods **********************
	 */
	// TODO: This also should not exist, or it should be changed to
	// private/protected. Redundant
	// Could be made a boolean so it was not a security vulnerability
	public Study verifyStudyExistenceById(Long id);

	public int getNumberOfStudies();

	public void sendStudyNotificationEmail(String email, long formId,
			long studyId);

	public void sendTextNotification(String cellPhone, long formId, long studyId);

	public void insertExpirationTime(Long id, Long expirationTime);

	public void expireStudies();
	
	
	

}
