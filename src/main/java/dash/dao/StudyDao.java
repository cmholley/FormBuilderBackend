package dash.dao;

import java.util.Date;
import java.util.List;

import dash.pojo.Study;

/**
 * An Example DAO interface for a simple object.
 * 
 * @Author tyler.swensen@gmail.com
 */
public interface StudyDao {

	public List<Study> getStudies(int numberOfStudies, Long startIndex);

	public int getNumberOfStudies();

	/**
	 * Returns a study given its id
	 *
	 * @param id
	 * @return
	 */
	public Study getStudyById(Long id);

	public void deleteStudyById(Study study);

	public Long createStudy(Study study);

	public void updateStudy(Study study);

	/** removes all studies */
	public void deleteStudies();

	public List<Study> getStudiesForForm(long formId);

	public List<Study> getTodaysStudies();

	public void insertExpirationTime(Long id, Date expirationDate);

	public List<Long> getExpiredStudies();

	public List<Long> getUsersForActiveStudy(Long study);

	public void removeExpiredStudy(Long study);

}
