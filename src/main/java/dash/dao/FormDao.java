package dash.dao;

import java.util.List;

import dash.pojo.Form;

/**
 * An Example DAO interface for a simple object.
 * 
 * @Author tyler.swensen@gmail.com
 */
public interface FormDao {

	public List<Form> getForms(int numberOfForms, Long startIndex);

	public int getNumberOfForms();

	/**
	 * Returns a form given its id
	 *
	 * @param id
	 * @return
	 */
	public Form getFormById(Long id);

	public void deleteFormById(Form form);

	public Long createForm(Form form);

	public void updateForm(Form form);

	/** removes all forms */
	public void deleteForms();

	public List<Object[]> getMyForms(int numberOfForms, Long startIndex);

	public List<Object[]> getPermissionsForm(long id);
}
