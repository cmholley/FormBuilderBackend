package dash.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dash.dao.FormDao;
import dash.dao.FormEntity;
import dash.errorhandling.AppException;
import dash.filters.AppConstants;
import dash.helpers.NullAwareBeanUtilsBean;
import dash.pojo.Form;
import dash.pojo.User;
import dash.security.CustomPermission;
import dash.security.CustomPermissionFactory;
import dash.security.GenericAclController;

/**
 * An Example service layer implementation. Here is where all business logic
 * should be implemented. Create a GenericAclController for each type of object
 * you will be managing permissions for in this service.
 * 
 * @author Tyler.swensen@gmail.com
 *
 */
@Component("formService")
public class FormServiceDbAccessImpl extends ApplicationObjectSupport implements FormService {

	@Autowired
	FormDao formDao;

	@Autowired
	private GenericAclController<Form> aclController;

	/*********************
	 * Create related methods implementation
	 ***********************/
	@Override
	@Transactional
	public Long createForm(Form form) throws AppException {

		long formId = formDao.createForm(new FormEntity(form));
		form.setId(formId);
		aclController.createACL(form);
		aclController.createAce(form, CustomPermission.READ);
		aclController.createAce(form, CustomPermission.WRITE);
		aclController.createAce(form, CustomPermission.DELETE);
		aclController.createAce(form, CustomPermission.CREATE);
		aclController.createAce(form, CustomPermission.DELETE_RESPONSES);
		return formId;
	}

	@Override
	@Transactional
	public void createForms(List<Form> forms) throws AppException {
		for (Form form : forms) {
			createForm(form);
		}
	}

	// ******************** Read related methods implementation
	// **********************
	@Override
	public List<Form> getForms(int numberOfForms, Long startIndex) throws AppException {

		List<FormEntity> forms = formDao.getForms(numberOfForms, startIndex);
		return getFormsFromEntities(forms);
	}

	@Override
	public LinkedHashMap<Form, List<Integer>> getMyForms(int numberOfForms, Long startIndex) throws AppException {

		// A lookup in the ACL tables to compile a list of all objects where the
		// user has
		// the required permission and then do a select query to build a
		// collection
		// of only those objects.
		// return getForms(numberOfForms, startIndex);
		List<Object[]> resultList = formDao.getMyForms(numberOfForms, startIndex);
		LinkedHashMap<Long, List<Integer>> formIds = new LinkedHashMap<Long, List<Integer>>();
		long formId = -1;
		List<Integer> permissionsTemp = new ArrayList<Integer>();
		// This for loop consolidates all the permissions for each form
		// Since the permissions are stored in multiple ACEs, we need to
		// consolidate
		// Them into a list that is matched with just one form
		for (Object[] entry : resultList) {
			if (formId == -1 || formId != ((BigInteger) entry[1]).longValue()) {
				if (formId != -1) {// If -1, this is the first iteration and
									// permissionsTemp should not be added
					formIds.put(formId, permissionsTemp);
				}
				permissionsTemp = new ArrayList<Integer>();// Erases previous
															// permissionsTemp
															// list
			}
			permissionsTemp.add((Integer) entry[0]);
			formId = ((BigInteger) entry[1]).longValue();

		}
		if (formId != -1) {// Ensures that the last form id and permissions list
							// is added. if != -1, then the for loop iterated
							// and the resultList was not empty
			formIds.put(formId, permissionsTemp);
		}
		LinkedHashMap<Form, List<Integer>> forms = new LinkedHashMap<Form, List<Integer>>();
		for (Entry<Long, List<Integer>> entry : formIds.entrySet()) {
			Form form = new Form(formDao.getFormById(entry.getKey()));
			forms.put(form, entry.getValue());
		}
		return forms;
	}

	@Override
	public Form getFormById(Long id) throws AppException {
		FormEntity formById = formDao.getFormById(id);
		if (formById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 404,
					"The form you requested with id " + id + " was not found in the database",
					"Verify the existence of the form with the id " + id + " in the database",
					AppConstants.DASH_POST_URL);
		}

		return new Form(formDao.getFormById(id));
	}

	private List<Form> getFormsFromEntities(List<FormEntity> formEntities) {
		List<Form> response = new ArrayList<Form>();
		for (FormEntity formEntity : formEntities) {
			response.add(new Form(formEntity));
		}

		return response;
	}

	// public List<Form> getRecentForms(int numberOfDaysToLookBack) {
	// List<FormEntity> recentForms = formDao
	// .getRecentForms(numberOfDaysToLookBack);
	//
	// return getFormsFromEntities(recentForms);
	// }

	@Override
	public int getNumberOfForms() {
		int totalNumber = formDao.getNumberOfForms();

		return totalNumber;

	}

	/*********************
	 * UPDATE-related methods implementation
	 ***********************/

	@Override
	@Transactional
	public void updateFullyForm(Form form) throws AppException {

		Form verifyFormExistenceById = verifyFormExistenceById(form.getId());
		if (verifyFormExistenceById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - " + form.getId(),
					AppConstants.DASH_POST_URL);
		}
		copyAllProperties(verifyFormExistenceById, form);

		formDao.updateForm(new FormEntity(verifyFormExistenceById));

	}

	private void copyAllProperties(Form verifyFormExistenceById, Form form) {
		// If you would like to allow null values use the following line.
		// Reference PostServiceImpl in the VolunteerManagementApp for more
		// details.
		BeanUtilsBean withNull = new BeanUtilsBean();

		// Assuming the NullAwareBeanUtilsBean is sufficient this code can be
		// used.
		// BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
		try {
			withNull.copyProperties(verifyFormExistenceById, form);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*********************
	 * DELETE-related methods implementation
	 ***********************/

	@Override
	@Transactional
	public void deleteForm(Form form) {

		formDao.deleteFormById(form);
		aclController.deleteACL(form);

	}

	@Override
	@Transactional
	// TODO: This shouldn't exist? If it must, then it needs to accept a list of
	// Forms to delete
	public void deleteForms() {
		formDao.deleteForms();
	}

	@Override
	public Form verifyFormExistenceById(Long id) {
		FormEntity formById = formDao.getFormById(id);
		if (formById == null) {
			return null;
		} else {
			return new Form(formById);
		}
	}

	@Override
	@Transactional
	public void updatePartiallyForm(Form form) throws AppException {
		// do a validation to verify existence of the resource
		Form verifyFormExistenceById = verifyFormExistenceById(form.getId());
		if (verifyFormExistenceById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - " + form.getId(),
					AppConstants.DASH_POST_URL);
		}
		copyPartialProperties(verifyFormExistenceById, form);
		formDao.updateForm(new FormEntity(verifyFormExistenceById));

	}

	private void copyPartialProperties(Form verifyFormExistenceById, Form form) {

		BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
		try {
			notNull.copyProperties(verifyFormExistenceById, form);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*********************
	 * PERMISSION-related methods implementation
	 ***********************/

	/**
	 * Takes a role, and applies the associated permissions with that role
	 * owner: READ, WRITE, CREATE, DELETE, DELETE_RESPONSES 
	 * collaborator: READ, WRITE, CREATE 
	 * response_viewer: READ 
	 * responder: CREATE
	 * @throws AppException 
	 */
	@Override
	public void updatePermission(User user, Form form, String permissionRole) throws AppException {
		//Create factory and PrincipalSid
		CustomPermissionFactory factory = new CustomPermissionFactory();
		PrincipalSid sid = new PrincipalSid(user.getUsername());
		// Switch applies the new one permissions
		switch (permissionRole.toLowerCase()) {
		case ("owner"):
			deleteAllPermissions(user, form);
			aclController.createAce(form, factory.buildFromName("DELETE"), sid);
			aclController.createAce(form, factory.buildFromName("DELETE_RESPONSES"), sid);
			aclController.createAce(form, factory.buildFromName("READ"), sid);
			aclController.createAce(form, factory.buildFromName("WRITE"), sid);
			aclController.createAce(form, factory.buildFromName("CREATE"), sid);
			break;
			// No break because we want it to fall through t the collabrator
			// case also
		case ("collaborator"):
			deleteAllPermissions(user, form);
			aclController.createAce(form, factory.buildFromName("READ"), sid);
			aclController.createAce(form, factory.buildFromName("WRITE"), sid);
			aclController.createAce(form, factory.buildFromName("CREATE"), sid);
			break;
		case ("response_viewer"):
			deleteAllPermissions(user, form);
			aclController.createAce(form, factory.buildFromName("READ"), sid);
			break;
		case ("responder"):
			deleteAllPermissions(user, form);
			aclController.createAce(form, factory.buildFromName("CREATE"), sid);
			break;
		default:
			throw new AppException(
					Response.Status.FORBIDDEN.getStatusCode(),
					403,
					"The permission role you sent is not valid",
					"Please verify the role", AppConstants.DASH_POST_URL);
			
		}
	}
	
	@Override
	public void deleteAllPermissions(User user, Form form){
		//Create factory and PrincipalSid
		CustomPermissionFactory factory = new CustomPermissionFactory();
		PrincipalSid sid = new PrincipalSid(user.getUsername());
		//Delete all existing permissions
		aclController.deleteACE(form, factory.buildFromName("READ"), sid);
		aclController.deleteACE(form, factory.buildFromName("WRITE"), sid);
		aclController.deleteACE(form, factory.buildFromName("CREATE"), sid);
		aclController.deleteACE(form, factory.buildFromName("DELETE"), sid);
		aclController.deleteACE(form, factory.buildFromName("DELETE_RESPONSES"), sid);
	}

	@Override
	public HashMap<String, String> getPermissionsForm(Form form) {
		List<Object[]> resultList = formDao.getPermissionsForm(form.getId());
		LinkedHashMap<String, List<Integer>> permissions = new LinkedHashMap<String, List<Integer>>();
		String username = "";
		List<Integer> permissionsTemp = new ArrayList<Integer>();
		// Generates map with usernames as keys and lists of permissions as
		// values
		for (Object[] entry : resultList) {
			if (username.equals("") || !username.equals((String) entry[1])) {
				if (!username.equals("")) {
					permissions.put(username, permissionsTemp);
				}
				permissionsTemp = new ArrayList<Integer>();
			}
			permissionsTemp.add((Integer) entry[0]);
			username = (String) entry[1];
		}
		if (!username.equals("")) {
			permissions.put(username, permissionsTemp);
		}
		//Assigns a role for every list of masks
		HashMap<String, String> permissionRoles = new LinkedHashMap<String, String>();
		String role;
		for(Entry<String, List<Integer>> entry : permissions.entrySet()){
			role = "ERROR";
			List<Integer> values = entry.getValue();
			List<Integer> comparing = new ArrayList<Integer>();
			comparing.add(4);//CREATE
			if(values.containsAll(comparing))//List just holds 4
				role = "responder";
			comparing.clear();
			comparing.add(1);//READ
			if(values.containsAll(comparing))
				role = "response_viewer";
			comparing.add(4);//Re-add CREATE
			comparing.add(2);//WRITE
			if(values.containsAll(comparing))
				role = "collaborator";
			comparing.add(8);//DELETE
			comparing.add(128);//DELETE_RESPONSES
			if(values.containsAll(comparing))
				role = "owner";
			permissionRoles.put(entry.getKey(), role);
		}
		
		return permissionRoles;
	}
}
