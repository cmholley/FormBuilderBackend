package dash.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.stereotype.Component;

import dash.pojo.Study;

/**
 * This is an example of a JPA implementation of the DAO layer for a simple object
 * 
 * @author Tyler.swensen@gmail.com
 *
 */
@Component("studyDao")
public class StudyDaoJPA2Impl implements StudyDao {
	@PersistenceContext(unitName = "dashPersistence")
	private EntityManager entityManager;

	@Override
	public List<StudyEntity> getStudies(int numberOfStudies, Long startIndex) {
		String sqlString = null;

		sqlString = "SELECT u FROM StudyEntity u WHERE u.id < ?1 ORDER BY u.time_stamp_sample DESC";

		TypedQuery<StudyEntity> query = entityManager.createQuery(sqlString,
				StudyEntity.class);
		if (startIndex == 0)
			startIndex = Long.MAX_VALUE;
		query.setParameter(1, startIndex);
		query.setMaxResults(numberOfStudies);

		return query.getResultList();
	}

	@Override
	public StudyEntity getStudyById(Long id) {

		try {
			String qlString = "SELECT u FROM StudyEntity u WHERE u.id = ?1";
			TypedQuery<StudyEntity> query = entityManager.createQuery(qlString,
					StudyEntity.class);
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void deleteStudyById(Study studyPojo) {

		StudyEntity study = entityManager
				.find(StudyEntity.class, studyPojo.getId());
		entityManager.remove(study);

	}

	@Override
	public Long createStudy(StudyEntity study) {

		study.setInsertionDate(new Date());
		entityManager.persist(study);

		// Give admin over new study to the new study

		return study.getId();
	}

	@Override
	public void updateStudy(StudyEntity study) {
		// TODO think about partial update and full update
		entityManager.merge(study);
	}

	@Override
	public void deleteStudies() {
		Query query = entityManager.createNativeQuery("TRUNCATE TABLE study");
		query.executeUpdate();
	}

	@Override
	public int getNumberOfStudies() {
		try {
			String qlString = "SELECT COUNT(*) FROM study";
			TypedQuery<StudyEntity> query = entityManager.createQuery(qlString,
					StudyEntity.class);

			return query.getFirstResult();
		} catch (NoResultException e) {
			return 0;
		}
	}

	@Override
	public List<StudyEntity> getStudiesForForm(long formId) {
		String sqlString = "SELECT u FROM StudyEntity u WHERE u.formId = :formId ORDER BY u.insertionDate DESC";
		TypedQuery<StudyEntity> query = entityManager.createQuery(sqlString,
				StudyEntity.class);
		query.setParameter("formId", formId);
		List<StudyEntity> studies = query.getResultList();
		return studies;
	}

	@Override
	public List<StudyEntity> getTodaysStudies() {
		String sqlString = "SELECT u FROM StudyEntity u WHERE u.startDate BETWEEN :startDate AND :endDate";
		TypedQuery<StudyEntity> query = entityManager.createQuery(sqlString,
				StudyEntity.class);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR, 0);
		Date endDate = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date startDate = cal.getTime();
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<StudyEntity> studies = query.getResultList();
		return studies;
	}

	@Override
	public void insertExpirationTime(Long id, Date expirationDate) {
		Query query = entityManager.createNativeQuery("INSERT INTO expiration_times (study_id, expiration_time) "
				+ "VALUES (?, ?)");
		query.setParameter(1, id);
		query.setParameter(2, expirationDate);
		query.executeUpdate();
	}

	@Override
	public List<Long> getExpiredStudies() {
		Configuration configuration = new Configuration().configure();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.buildServiceRegistry();
		SessionFactory sessionFactory = configuration
				.buildSessionFactory(serviceRegistry);
		Session session = sessionFactory.openSession();
		String queryString = "SELECT study_id FROM expiration_times WHERE expiration_time < NOW()";
		SQLQuery query = session.createSQLQuery(queryString);
		List<Long> studyIds = new ArrayList<Long>();
		for(Object object : query.list()){
			studyIds.add(new Long((int)(object)));
		}
		return studyIds;
	}

	@Override
	public List<Long> getUsersForActiveStudy(Long studyId) {
		Configuration configuration = new Configuration().configure();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.buildServiceRegistry();
		SessionFactory sessionFactory = configuration
				.buildSessionFactory(serviceRegistry);
		Session session = sessionFactory.openSession();
		String queryString = "SELECT user_id FROM active_studies WHERE activeStudies_KEY = :studyId";
		SQLQuery query = session.createSQLQuery(queryString);
		query.setParameter("studyId", studyId);
		List<Long> userIds = new ArrayList<Long>();
		for(Object object : query.list()){
			userIds.add(new Long((int)(object)));
		}
		return userIds;
	}

	@Override
	public void removeExpiredStudy(Long study) {
		Query query = entityManager.createNativeQuery("DELETE FROM expiration_times WHERE study_id = :studyId");
		query.setParameter("studyId", study);
		query.executeUpdate();
	}
	
	
}
