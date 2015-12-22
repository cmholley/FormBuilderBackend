package dash.service;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import dash.errorhandling.AppException;
import dash.pojo.User;

/**
 *
 * @author plindner, tyler.swensen@gmail.com
 * @see <a
 *      href="http://www.codingpedia.org/ama/spring-mybatis-integration-example/">http://www.codingpedia.org/ama/spring-mybatis-integration-example/</a>
 */
public interface UserService {

	/*
	 * ******************** Create related methods **********************
	 */
	public Long createUser(User user) throws AppException;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void createUsers(List<User> users) throws AppException;

	/*
	 * ******************* Read related methods ********************
	 */
	/**
	 *
	 * @param orderByInsertionDate
	 *            - if set, it represents the order by criteria (ASC or DESC)
	 *            for displaying users
	 * @param numberDaysToLookBack
	 *            - if set, it represents number of days to look back for users,
	 *            null
	 * @return list with users corresponding to search criteria
	 * @throws AppException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<User> getUsers(String orderByInsertionDate,
			Integer numberDaysToLookBack) throws AppException;

	@PostFilter("hasPermission(filterObject, 'READ')")
	public List<User> getMyUser() throws AppException;

	/**
	 * Returns a user given its id
	 *
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public User getUserById(Long id) throws AppException;

	public List<String> getRole(User user);

	/*
	 * ******************** Update related methods **********************
	 */
	@PreAuthorize("hasPermission(#user, 'WRITE') or hasRole('ROLE_ADMIN')")
	public void updateFullyUser(User user) throws AppException;

	@PreAuthorize("hasPermission(#user, 'WRITE') or hasRole('ROLE_ADMIN')")
	public void updatePartiallyUser(User user) throws AppException;

	@PreAuthorize("hasPermission(#user, 'WRITE') or hasRole('ROLE_ADMIN')")
	public void resetPassword(User user) throws AppException;

	@PreAuthorize("hasPermission(#user, 'WRITE') and hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public void setRoleUser(User user);

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void setRoleModerator(User user);

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void setRoleAdmin(User user);
	
	/*
	 * ******************** Password Reset methods **********************
	 */

	public void requestPasswordReset(User user, UriInfo uri) throws AppException;
	
	public Response validateToken(Long id, String token) throws AppException;
	
	public void tokenPasswordReset(Long id, String token, String Password)throws AppException;
	
	/*
	 * ******************** Helper methods **********************
	 */
	public int getNumberOfUsers();

	public User getUserByName(String username);

	public void removeActiveStudy(Long removeActiveStudy, User user);
	
	//This method is insecure and should not be used outside of the StudyJob class
	//It's purpose is to allow the study job to make calls to the service layer
	//Without worrying about the permissions filtering
	public void updateUserJob(User use) throws AppException;
	
	//Generates a new token and sends an email to validate 
	public void requestEmailActivation(User user) throws AppException;
}
