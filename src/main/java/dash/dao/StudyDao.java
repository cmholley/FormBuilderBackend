package dash.dao;

import java.util.List;


import dash.pojo.Study;


/**
 * An Example DAO interface for a simple object.
 * @Author tyler.swensen@gmail.com
 */
public interface StudyDao {
	
	public List<StudyEntity> getStudies(int numberOfStudies, Long startIndex);
	
	public int getNumberOfStudies();

	/**
	 * Returns a study given its id
	 *
	 * @param id
	 * @return
	 */
	public StudyEntity getStudyById(Long id);
	
	public void deleteStudyById(Study study);

	public Long createStudy(StudyEntity study);

	public void updateStudy(StudyEntity study);

	/** removes all studies */
	public void deleteStudies();

	public List<StudyEntity> getStudiesForForm(long formId);

	public List<StudyEntity> getTodaysStudies();

}
