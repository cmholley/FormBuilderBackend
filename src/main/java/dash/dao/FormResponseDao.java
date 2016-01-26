package dash.dao;

import java.util.List;

import dash.pojo.FormResponse;

/**
 * An Example DAO interface for a simple object.
 * 
 * @Author tyler.swensen@gmail.com
 */
public interface FormResponseDao {

	public List<FormResponse> getFormResponses(int numberOfFormResponses, Long startIndex);

	public int getNumberOfFormResponses();

	/**
	 * Returns a response given its id
	 *
	 * @param id
	 * @return
	 */
	public FormResponse getFormResponseById(Long id);

	public List<FormResponse> getFormResponsesByFormId(Long id, int numberOfFormResponses, int page);

	public void deleteFormResponseById(FormResponse formResponse);

	public Long createFormResponse(FormResponse formResponse);

	public void updateFormResponse(FormResponse formResponse);

	/** removes all responses */
	public void deleteFormResponses();

}
