package dash.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import dash.errorhandling.AppException;
import dash.pojo.Form;
import dash.pojo.User;

/**
 * Example service interface for a basic object.
 * 
 * This is where you set method/object level permissions using Spring annotations.
 * 
 * @author Tyler.swensen@gmail.com
 *
 */

public interface FormService {
	
	/*
	 * ******************** Create related methods *********************/
	
	/**
	 * Create a new form and set the current user as owner and manager.
	 * @param form
	 * @return
	 * @throws AppException
	 */
	public Long createForm(Form form) throws AppException;

	/*
	 * Create multiple forms as ROOT, testing purposes only.
	 */
	@PreAuthorize("hasRole('ROLE_ROOT')")
	public void createForms(List<Form> forms) throws AppException;

	/*
	 * ******************* Read related methods ********************
	 */
	/**
	 *
	 * @param orderByInsertionDate
	 *            - if set, it represents the order by criteria (ASC or DESC)
	 *            for displaying forms
	 * @param numberDaysToLookBack
	 *            - if set, it represents number of days to look back for forms,
	 *            null
	 * @return list with forms corresponding to search criteria
	 * @throws AppException
	 */
	//Enable post filter to restrict read access to a collection
	//@PostFilter("hasPermission(filterObject, 'READ')")
	public List<Form> getForms(int numberOfForms, Long startIndex) throws AppException;
	
	//This filter was a temp fix that has been replaced with an SQL query which 
	//Returns a list of all the forms with the permissions
	//@PostFilter("hasPermission(filterObject, 'WRITE') or hasPermission(filterObject, 'READ')"
	//		+ "or hasPermission(filterObject, 'DELETE')")
	public LinkedHashMap<Form, List<Integer>> getMyForms(int numberOfForms, Long startIndex) throws AppException;
	
	/**
	 * Returns a form given its id
	 *
	 * @param id
	 * @return
	 * @throws AppException
	 */
	
	//Enable the following line of code to restrict read access to a single object.
	// and returnObject.getPubli()==true or returnObject.getEnabled()==true and hasRole('ROLE_USER')
	//This post authorize has been removed. We are preventing responses at response creation rather than at form request
	//@PostAuthorize("hasPermission(returnObject, 'read') or hasRole('ROLE_ADMIN') or returnObject.getEnabled()==true and returnObject.getPubli()==true "
	//		+ "and returnObject.isExpired()==false or returnObject.getEnabled()==true and hasRole('ROLE_USER') and returnObject.isExpired()==false")
	public Form getFormById(Long id) throws AppException;
	

	/*
	 * ******************** Update related methods **********************
	 */
	@PreAuthorize("hasPermission(#form, 'WRITE') or hasRole('ROLE_ADMIN')")
	public void updateFullyForm(Form form) throws AppException;

	@PreAuthorize("hasPermission(#form, 'WRITE') or hasRole('ROLE_ADMIN')")
	public void updatePartiallyForm(Form form) throws AppException;

	/*
	 * ******************** Permission related methods **********************
	 */
	@PreAuthorize("hasPermission(#form, 'DELETE') or hasRole('ROLE_ADMIN')")
	public void updatePermission(User user, Form form, String permissionRole) throws AppException;
	
	@PreAuthorize("hasPermission(#form, 'DELETE') or hasRole('ROLE_ADMIN')")
	public void deleteAllPermissions(User user, Form form);
	
	@PreAuthorize("hasPermission(#form, 'WRITE') or hasRole('ROLE_ADMIN')")
	public HashMap<String, String> getPermissionsForm(Form form);
	
	/*
	 * ******************** Delete related methods **********************
	 */


	@PreAuthorize("hasPermission(#form, 'DELETE') or hasRole('ROLE_ADMIN')")
	public void deleteForm(Form form);
	/** removes all forms
	 * DO NOT USE, IMPROPERLY UPDATES ACL_OBJECT table
	 * Functional but does not destroy old acl's which doesnt hurt anything
	 * but they will take up space if this is commonly used */
	@PreAuthorize("hasRole('ROLE_ROOT')")
	public void deleteForms();
		
	
	/*
	 * ******************** Helper methods **********************
	 */
	// TODO: This also should not exist, or it should be changed to
	// private/protected. Redundant
	// Could be made a boolean so it was not a security vulnerability
	public Form verifyFormExistenceById(Long id);

	public int getNumberOfForms();

}
