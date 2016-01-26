package dash.dao;

import java.util.List;

import dash.pojo.User;

/**
 * @author plindner, tyler.swensen@gmail.com
 *
 * @see <a href=
 *      "http://www.codingpedia.org/ama/spring-mybatis-integration-example/">
 *      http://www.codingpedia.org/ama/spring-mybatis-integration-example/</a>
 */
public interface UserDao {

	public List<User> getUsers(String orderByInsertionDate);

	public List<User> getRecentUsers(int numberOfDaysToLookBack);

	public int getNumberOfUsers();

	/**
	 * Returns a user given its id
	 *
	 * @param id
	 * @return
	 */
	public User getUserById(Long id);

	/**
	 * Find user by name
	 *
	 * @param user
	 * @return the user with the name specified or null if not existent
	 */
	public User getUserByName(String name);

	public String getRoleByName(String username);

	public void deleteUserById(User user);

	public Long createUser(User user);

	public void updateUser(User user);

	public void updateUserRole(String role, String username);

	/** removes all users */
	public void deleteUsers();

}
