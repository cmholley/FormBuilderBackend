package dash.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import dash.pojo.User;

/**
 * Implementation of User dao layer.
 * 
 * @author Tyler.swensen@gmail.com
 *
 */
@Component("userDao")
public class UserDaoJPA2Impl implements UserDao {

	@PersistenceContext(unitName = "dashPersistence")
	private EntityManager entityManager;

	@Override
	public List<UserEntity> getUsers(String orderByInsertionDate) {
		String sqlString = null;
		if (orderByInsertionDate != null) {
			sqlString = "SELECT u FROM UserEntity u" + " ORDER BY u.insertionDate " + orderByInsertionDate;
		} else {
			sqlString = "SELECT u FROM UserEntity u";
		}
		TypedQuery<UserEntity> query = entityManager.createQuery(sqlString, UserEntity.class);

		return query.getResultList();
	}

	@Override
	public List<UserEntity> getRecentUsers(int numberOfDaysToLookBack) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone("UTC+6"));
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -numberOfDaysToLookBack);// substract the
																// number of
																// days to look
																// back
		Date dateToLookBackAfter = calendar.getTime();

		String qlString = "SELECT u FROM UserEntity u where u.insertionDate > :dateToLookBackAfter ORDER BY u.insertionDate DESC";
		TypedQuery<UserEntity> query = entityManager.createQuery(qlString, UserEntity.class);
		query.setParameter("dateToLookBackAfter", dateToLookBackAfter, TemporalType.DATE);

		return query.getResultList();
	}

	@Override
	public UserEntity getUserById(Long id) {

		try {
			String qlString = "SELECT u FROM UserEntity u WHERE u.id = ?1";
			TypedQuery<UserEntity> query = entityManager.createQuery(qlString, UserEntity.class);
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public UserEntity getUserByName(String name) {

		try {
			String qlString = "SELECT u FROM UserEntity u WHERE u.username = ?1";
			TypedQuery<UserEntity> query = entityManager.createQuery(qlString, UserEntity.class);
			query.setParameter(1, name);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public String getRoleByName(String username) {

		try {
			String qlString = "SELECT u.authority FROM AuthorityEntity u  WHERE u.username= ?1";
			TypedQuery<String> query = entityManager.createQuery(qlString, String.class);
			query.setParameter(1, username);

			return query.getSingleResult();
		} catch (NoResultException e) {
			Logger logger = LoggerFactory.getLogger(this.getClass());
			logger.error("Exception thrown in " + this.getClass().getName(), e);
			return null;
		}

	}

	@Override
	public void deleteUserById(User userPojo) {

		UserEntity user = entityManager.find(UserEntity.class, userPojo.getId());
		entityManager.remove(user);

	}

	@Override
	public Long createUser(UserEntity user) {

		user.setInsertionDate(new Date());
		entityManager.persist(user);
		entityManager.flush();// force insert to receive the id of the user
		// Give admin over new user to the new user

		return user.getId();
	}

	@Override
	public void updateUser(UserEntity user) {
		// TODO think about partial update and full update
		entityManager.merge(user);
	}

	public void updateUserRole(String role, String username) {
		String qlString = "SELECT u FROM AuthorityEntity u WHERE u.username = ?1";
		TypedQuery<AuthorityEntity> query = entityManager.createQuery(qlString, AuthorityEntity.class);
		query.setParameter(1, username);

		AuthorityEntity authority = query.getSingleResult();
		authority.setAuthority(role);
		entityManager.merge(authority);
	}

	@Override
	public void deleteUsers() {
		Query query = entityManager.createNativeQuery("TRUNCATE TABLE users");
		query.executeUpdate();
	}

	@Override
	public int getNumberOfUsers() {
		try {
			String qlString = "SELECT COUNT(*) FROM users";
			TypedQuery<UserEntity> query = entityManager.createQuery(qlString, UserEntity.class);

			return query.getFirstResult();
		} catch (NoResultException e) {
			return 0;
		}
	}
}
