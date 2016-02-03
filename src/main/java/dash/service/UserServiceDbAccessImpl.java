package dash.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dash.dao.UserDao;
import dash.dao.ValidationToken;
import dash.errorhandling.AppException;
import dash.filters.AppConstants;
import dash.helpers.NullAwareBeanUtilsBean;
import dash.pojo.User;
import dash.security.UserLoginController;

/**
 * Implementation of the business logic for our User object. Users are also used
 * by SpringSecurity and the ACL's to determine authorization.
 * 
 * @author Tyler.swensen@gmail.com
 *
 */
@Component("userService")
public class UserServiceDbAccessImpl extends ApplicationObjectSupport implements UserService {

	@Autowired
	UserDao userDao;

	@Autowired
	private MutableAclService mutableAclService;

	@Autowired
	private UserLoginController authoritiesController;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SimpleMailMessage templateMessage;

	public static final String userRole = "ROLE_USER";

	/*********************
	 * Create related methods implementation
	 ***********************/
	@Override
	@Transactional
	public Long createUser(User user) throws AppException {

		validateInputForCreation(user);

		// verify existence of resource in the db (feed must be unique)
		User userByName = userDao.getUserByName(user.getUsername());
		if (userByName != null) {
			throw new AppException(Response.Status.CONFLICT.getStatusCode(), 409,
					"User with username already existing in the database with the id " + userByName.getId(),
					"Please verify that the username and password are properly generated", AppConstants.DASH_POST_URL);
		}

		long userId = userDao.createUser(user);
		user.setId(userId);
		authoritiesController.create(user, userRole);
		createUserACL(user, new PrincipalSid(user.getUsername()));
		return userId;
	}

	private void validateInputForCreation(User user) throws AppException {
		if (user.getUsername() == null) {
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 400,
					"Provided data not sufficient for insertion",
					"Please verify that the username is properly generated/set", AppConstants.DASH_POST_URL);
		}
		if (user.getPassword() == null) {
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 400,
					"Provided data not sufficient for insertion",
					"Please verify that the password is properly generated/set", AppConstants.DASH_POST_URL);
		}
		// etc...
	}

	@Override
	@Transactional
	public void createUsers(List<User> users) throws AppException {
		for (User user : users) {
			createUser(user);
		}
	}

	/********************
	 * Read related methods implementation
	 **********************/
	@Override
	public List<User> getUsers(String orderByInsertionDate, Integer numberDaysToLookBack) throws AppException {

		// verify optional parameter numberDaysToLookBack first
		if (numberDaysToLookBack != null) {
			List<User> recentUsers = userDao.getRecentUsers(numberDaysToLookBack);
			return getUsersFromEntities(recentUsers);
		}

		if (isOrderByInsertionDateParameterValid(orderByInsertionDate)) {
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 400,
					"Please set either ASC or DESC for the orderByInsertionDate parameter", null,
					AppConstants.DASH_POST_URL);
		}
		List<User> users = userDao.getUsers(orderByInsertionDate);

		return getUsersFromEntities(users);
	}

	@Override
	public List<User> getMyUser() throws AppException {
		return getUsers(null, null);
	}

	private boolean isOrderByInsertionDateParameterValid(String orderByInsertionDate) {
		return orderByInsertionDate != null
				&& !("ASC".equalsIgnoreCase(orderByInsertionDate) || "DESC".equalsIgnoreCase(orderByInsertionDate));
	}

	@Override
	public User getUserById(Long id) throws AppException {
		User userById = userDao.getUserById(id);
		if (userById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 404,
					"The user you requested with id " + id + " was not found in the database",
					"Verify the existence of the user with the id " + id + " in the database",
					AppConstants.DASH_POST_URL);
		}

		return userDao.getUserById(id);
	}

	private List<User> getUsersFromEntities(List<User> userEntities) {
		List<User> response = new ArrayList<User>();
		for (User User : userEntities) {
			response.add(User);
		}

		return response;
	}

	public List<User> getRecentUsers(int numberOfDaysToLookBack) {
		List<User> recentUsers = userDao.getRecentUsers(numberOfDaysToLookBack);

		return getUsersFromEntities(recentUsers);
	}

	@Override
	public int getNumberOfUsers() {
		int totalNumber = userDao.getNumberOfUsers();

		return totalNumber;

	}

	@Override
	public List<String> getRole(User user) {
		ArrayList<String> tempRole = new ArrayList<String>();
		tempRole.add(userDao.getRoleByName(user.getUsername()));
		return tempRole;
	}

	protected String getUsername() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth.getPrincipal() instanceof UserDetails) {
			return ((UserDetails) auth.getPrincipal()).getUsername();
		} else {
			return auth.getPrincipal().toString();
		}
	}

	/*********************
	 * UPDATE-related methods implementation
	 ***********************/
	@Override
	@Transactional
	public void updateFullyUser(User user) throws AppException {
		// do a validation to verify FULL update with PUT

		User verifyUserExistenceById = verifyUserExistenceById(user.getId());
		if (verifyUserExistenceById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - " + user.getId(),
					AppConstants.DASH_POST_URL);
		}

		copyAllProperties(verifyUserExistenceById, user);
		userDao.updateUser(verifyUserExistenceById);
	}

	/**
	 * Allows for merging bean with object does not ignore null properties.
	 * 
	 * 
	 */
	private void copyAllProperties(User verifyUserExistenceById, User user) {

		BeanUtilsBean withNull = new BeanUtilsBean();
		try {
			withNull.copyProperty(verifyUserExistenceById, "firstName", user.getFirstName());
			withNull.copyProperty(verifyUserExistenceById, "lastName", user.getLastName());
			withNull.copyProperty(verifyUserExistenceById, "city", user.getCity());
			withNull.copyProperty(verifyUserExistenceById, "homePhone", user.getHomePhone());
			withNull.copyProperty(verifyUserExistenceById, "cellPhone", user.getCellPhone());
			withNull.copyProperty(verifyUserExistenceById, "email", user.getEmail());
			withNull.copyProperty(verifyUserExistenceById, "picture", user.getPicturePath());
			withNull.copyProperty(verifyUserExistenceById, "activeStudies", user.getActiveStudies());
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

	private User verifyUserExistenceById(Long id) {
		User userById = userDao.getUserById(id);
		if (userById == null) {
			return null;
		} else {
			return userById;
		}
	}

	@Override
	@Transactional
	public void updatePartiallyUser(User user) throws AppException {
		// do a validation to verify existence of the resource
		User verifyUserExistenceById = verifyUserExistenceById(user.getId());
		if (verifyUserExistenceById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - " + user.getId(),
					AppConstants.DASH_POST_URL);
		}
		copyPartialProperties(verifyUserExistenceById, user);
		userDao.updateUser(verifyUserExistenceById);

	}

	private void copyPartialProperties(User verifyUserExistenceById, User user) {

		BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
		try {
			notNull.copyProperties(verifyUserExistenceById, user);
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

	@Override
	@Transactional
	public void resetPassword(User user) throws AppException {
		User verifyUserExistenceById = verifyUserExistenceById(user.getId());
		if (verifyUserExistenceById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - " + user.getId(),
					AppConstants.DASH_POST_URL);
		} else {
			authoritiesController.passwordReset(user);
		}

	}

	/****************** Methods for Acl *****************/

	// Creates/Updates the ACL of user
	// Is also an example of how to implement class specific ACL helper methods.
	public void createUserACL(User user, Sid recipient) {
		MutableAcl acl;
		ObjectIdentity oid = new ObjectIdentityImpl(User.class, user.getId());

		try {
			acl = (MutableAcl) mutableAclService.readAclById(oid);
		} catch (NotFoundException nfe) {
			acl = mutableAclService.createAcl(oid);
		}
		acl.insertAce(acl.getEntries().size(), BasePermission.READ, recipient, true);
		acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, recipient, true);
		acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, recipient, true);
		mutableAclService.updateAcl(acl);
		acl.setOwner(new PrincipalSid("Root"));
		mutableAclService.updateAcl(acl);

		logger.debug("Added permission " + "Read, Write, Delete" + " for Sid " + recipient + " contact " + user);
	}

	@Override
	@Transactional
	public void setRoleUser(User user) {
		userDao.updateUserRole("ROLE_USER", user.getUsername());
	}

	@Override
	@Transactional
	public void setRoleModerator(User user) {
		userDao.updateUserRole("ROLE_MODERATOR", user.getUsername());
	}

	@Override
	@Transactional
	public void setRoleAdmin(User user) {
		userDao.updateUserRole("ROLE_ADMIN", user.getUsername());
	}

	@Override
	public User getUserByName(String username) {
		List<User> uel = new ArrayList<User>();
		User ue = userDao.getUserByName(username);
		if (ue != null) {
			uel.add(ue);
			return getUsersFromEntities(uel).get(0);
		} else
			return null;
	}

	@Transactional
	@Override
	public void removeActiveStudy(Long removeActiveStudy, User user) {
		Map<Long, Long> activeStudies = user.getActiveStudies();
		activeStudies.remove(removeActiveStudy);
		user.setActiveStudies(activeStudies);
		User verifyUser = this.getUserByName(user.getUsername());
		copyAllProperties(verifyUser, user);
		userDao.updateUser(verifyUser);
	}

	@Transactional
	@Override
	public void updateUserJob(User user) throws AppException {
		User verifyUserExistenceById = verifyUserExistenceById(user.getId());
		if (verifyUserExistenceById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - " + user.getId(),
					AppConstants.DASH_POST_URL);
		}

		copyAllProperties(verifyUserExistenceById, user);
		userDao.updateUser(verifyUserExistenceById);

	}

	@Transactional
	public void requestPasswordReset(User user, UriInfo uri) throws AppException {
		User User = userDao.getUserById(user.getId());
		if (User.isEmailVerified()) {
			String ws = "FormBuilder";
			ValidationToken tokenEntity = new ValidationToken(
					ValidationToken.TOKEN_TYPE.PASSWORD_RESET);
			User.getValidationTokens().add(tokenEntity);
			// Then email the token
			SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
			msg.setSubject("Password Reset Request");
			msg.setTo(User.getUsername());
			msg.setText("Hi, \n\nWe recieved a request to reset you password for " + AppConstants.APPLICATION_NAME + "."
					+ "  To reset your password please click the following link.\n\n"
					+ "http://www.housuggest.org/PasswordReset/#/resetPassword/uid/" + User.getId() + "/token/"
					+ tokenEntity.getToken() + "/uin/" + User.getUsername() + "/ws/" + ws
					+ "\n\n\nThis link is only valid for 2 hours. After that point, please request another password reset. "
					+ "If you did not attempt to reset your password please contact us immediately.");
			try {
				this.mailSender.send(msg);
			} catch (MailException ex) {
				// simply log it and go on...
				throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), 500,
						"The mail server has expirienced a critical error, we were unable to email the user",
						ex.getMessage(), AppConstants.DASH_POST_URL);
			}
		}

	}

	@Override
	@Transactional
	public void requestEmailActivation(User user) throws AppException {
		User User = userDao.getUserById(user.getId());
		String ws = "FormBuilder";
		ValidationToken tokenEntity = new ValidationToken(
				ValidationToken.TOKEN_TYPE.EMAIL_ACTIVATION);
		User.getValidationTokens().add(tokenEntity);
		userDao.updateUser(User);
		// Then email the token
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setSubject("Accout Activation");
		msg.setTo(User.getUsername());
		msg.setText("Hi, \n\nWe recieved a request to create a new account with this email for "
				+ AppConstants.APPLICATION_NAME + "." + " To activate your account please click the following link.\n\n"
				+ "http://www.housuggest.org/PasswordReset/#/emailValidate/uid/" + User.getId() + "/token/"
				+ tokenEntity.getToken() + "/uin/" + User.getUsername() + "/ws/" + ws
				+ "\n\n\nIf you did not attempt to create this account, please contact us immediately.");
		try {
			this.mailSender.send(msg);
		} catch (MailException ex) {
			// simply log it and go on...
			throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), 500,
					"The mail server has expirienced a critical error, we were unable to email the user",
					ex.getMessage(), AppConstants.DASH_POST_URL);
		}
	}

	@Override
	@Transactional
	public Response validateToken(Long id, String token) throws AppException {
		User user = this.getUserById(id);
		String debugInfo = "token failed debug out:";
		for (ValidationToken tokenEntity : user.getValidationTokens()) {
			debugInfo = "<br>tokenmatchcheck=";
			if (tokenEntity.getToken().equals(token)) {
				debugInfo += "true";
			} else {
				debugInfo += "false<br>" + tokenEntity.getToken() + "<br>" + token;
			}
			debugInfo += "  <br>expirationCheck=";
			if (tokenEntity.getExpiration_date().after(new Date())) {
				debugInfo += "true";
			} else {
				debugInfo += "false";
			}
			if (tokenEntity.getToken().equals(token)) {
				switch (tokenEntity.getToken_type()) {
				case PASSWORD_RESET: {
					if (tokenEntity.getExpiration_date().after(new Date())) {
						return Response.status(200).entity("Token is valid! Proceed to password reset...").build();
					} else {
						return Response.status(500).entity("Internal Server Error: Token Type Invalid").build();
					}
				}
				case EMAIL_ACTIVATION: {
					user.setEmailVerified(true);
					userDao.updateUser(user);

					return Response.status(200).entity("Thank you for activating your account!").build();
				}

				default:
					return Response.status(500).entity("Internal Server Error: Token Type Invalid").build();
				}
			}
		}

		return Response.status(500).entity(debugInfo).build();

	}

	public void tokenPasswordReset(Long id, String token, String password) throws AppException {
		User user = this.getUserById(id);
		for (ValidationToken tokenEntity : user.getValidationTokens()) {
			if (tokenEntity.getToken().equals(token) && tokenEntity.getExpiration_date().after(new Date())
					&& tokenEntity.getToken_type() == ValidationToken.TOKEN_TYPE.PASSWORD_RESET) {
				user.setPassword(password);
				this.resetPassword(user);
			}
		}
	}
}
