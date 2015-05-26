package dash.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
		String sqlString = "SELECT u FROM StudyEntity u WHERE u.formId = ?1 ORDER BY u.insertionDate DESC";
		TypedQuery<StudyEntity> query = entityManager.createQuery(sqlString,
				StudyEntity.class);
		List<StudyEntity> studies = query.getResultList();
		return studies;
	}

	@Override
	public List<StudyEntity> getTodaysStudies() {
		String sqlString = "SELECT u FROM StudyEntity u WHERE u.startdate = :startdate";
		TypedQuery<StudyEntity> query = entityManager.createQuery(sqlString,
				StudyEntity.class);
		query.setParameter("startdate", new Date());
		List<StudyEntity> studies = query.getResultList();
		return studies;
	}
}
