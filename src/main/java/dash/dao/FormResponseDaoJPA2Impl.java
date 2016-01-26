package dash.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import dash.pojo.FormResponse;

/**
 * This is an example of a JPA implementation of the DAO layer for a simple
 * object
 * 
 * @author Tyler.swensen@gmail.com
 *
 */
@Component("formResponseDao")
public class FormResponseDaoJPA2Impl implements FormResponseDao {
	@PersistenceContext(unitName = "dashPersistence")
	private EntityManager entityManager;

	@Override
	public List<FormResponse> getFormResponses(int numberOfFormResponses, Long startIndex) {
		String sqlString = null;

		sqlString = "SELECT u FROM FormResponse u WHERE u.id < ?1 ORDER BY u.insertion_date DESC";

		TypedQuery<FormResponse> query = entityManager.createQuery(sqlString, FormResponse.class);
		if (startIndex == 0)
			startIndex = Long.MAX_VALUE;
		query.setParameter(1, startIndex);
		query.setMaxResults(numberOfFormResponses);

		return query.getResultList();
	}

	@Override
	public FormResponse getFormResponseById(Long id) {

		try {
			String qlString = "SELECT u FROM FormResponse u WHERE u.id = ?1";
			TypedQuery<FormResponse> query = entityManager.createQuery(qlString, FormResponse.class);
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<FormResponse> getFormResponsesByFormId(Long id, int numberOfFormResponses, int page) {

		try {
			String qlString = "SELECT u FROM FormResponse u WHERE u.form_id = ?1 ORDER BY u.latest_update DESC";
			TypedQuery<FormResponse> query = entityManager.createQuery(qlString, FormResponse.class);

			query.setFirstResult((page - 1) * numberOfFormResponses);
			query.setParameter(1, id);
			query.setMaxResults(numberOfFormResponses);

			return query.getResultList();

		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void deleteFormResponseById(FormResponse formResponsePojo) {

		FormResponse formResponse = entityManager.find(FormResponse.class, formResponsePojo.getId());
		entityManager.remove(formResponse);

	}

	@Override
	public Long createFormResponse(FormResponse formResponse) {

		formResponse.setInsertion_date(new Date());
		formResponse.setLatest_update(new Date());
		entityManager.persist(formResponse);
		// Give admin over new formResponse to the new formResponse

		return formResponse.getId();
	}

	@Override
	public void updateFormResponse(FormResponse formResponse) {
		// TODO think about partial update and full update
		formResponse.setLatest_update(new Date());
		entityManager.merge(formResponse);
	}

	@Override
	public void deleteFormResponses() {
		Query query = entityManager.createNativeQuery("TRUNCATE TABLE formResponses");
		query.executeUpdate();
	}

	@Override
	public int getNumberOfFormResponses() {
		try {
			String qlString = "SELECT COUNT(*) FROM formResponse";
			TypedQuery<FormResponse> query = entityManager.createQuery(qlString, FormResponse.class);

			return query.getFirstResult();
		} catch (NoResultException e) {
			return 0;
		}
	}
}
