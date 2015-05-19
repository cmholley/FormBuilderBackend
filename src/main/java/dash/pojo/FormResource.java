package dash.pojo;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dash.errorhandling.AppException;
import dash.service.FormService;
import dash.service.UserService;

/**
 *
 * Service class that handles REST requests. This is where you define your API
 * and what requests will be accepted.
 *
 * @author tyler.swensen@gmail.com
 *
 */
@Component("formResource")
@Path("/forms")
public class FormResource {

	@Autowired
	private FormService formService;
	
	@Autowired
	private UserService userService;
	

	// ************************************* CREATE
	// ************************************

	/**
	 * Adds a new resource (form) from the given json format (at least
	 * formname and password elements are required at the DB level)
	 *
	 * @param form
	 * @return
	 * @throws AppException
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response createForm(Form form)
			throws AppException, JsonParseException, JsonMappingException, IOException
	{
		
		Long createFormId = formService.createForm(form);
		
		
		return Response
				.status(Response.Status.CREATED)
				// 201
				.entity("A new form has been created at index")
				.header("Location", String.valueOf(createFormId))
				.header("ObjectId", String.valueOf(createFormId))
				.build();
	}

	/**
	 * A list of resources (here forms) provided in json format will be
	 * added to the database.
	 *
	 * @param forms
	 * @return
	 * @throws AppException
	 */
	
	/*This service is disabled because it does not appear to be a use case.
	 * 
	 * Before enabling be sure to implement the creation of questions for each form in a secure way.
	 * 
	 * @POST
	@Path("list")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createForms(List<Form> forms)
			throws AppException {
		formService.createForms(forms);
		return Response.status(Response.Status.CREATED)
				// 201
				.entity("List of forms was successfully created")
				.build();
	}*/

	// *************************************
	// READ************************************

	/**
	 * Returns a list of forms via pagination. The order the list is
	 * sorted is set in the DAO implementation. Number of sample objects is the
	 * page size, and start index is the id of the last sample object received.
	 * 
	 * @param numberOfForms
	 * @param startIndex
	 * @return
	 * @throws IOException
	 * @throws AppException
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Form> getForms(
			@QueryParam("numberOfForms") @DefaultValue("25") int numberOfForms,
			@QueryParam("startIndex") @DefaultValue("0") Long startIndex)
			throws IOException, AppException {
		List<Form> forms = formService
				.getForms(numberOfForms, startIndex);
		return forms;
	}
	
	@GET
	@Path("/myForms")
	@Produces({ MediaType.APPLICATION_JSON })
	public LinkedHashMap<String, List<Integer>> getMyForms(
			@QueryParam("numberOfForms") @DefaultValue("25") int numberOfForms,
			@QueryParam("startIndex") @DefaultValue("0") Long startIndex)
			throws IOException, AppException {
		LinkedHashMap<Form, List<Integer>> forms = formService
				.getMyForms(numberOfForms, startIndex);
		LinkedHashMap<String, List<Integer>> returnForms = new LinkedHashMap<String, List<Integer>>();
		ObjectMapper mapper = new ObjectMapper();
		String tempString;
		for(Map.Entry<Form, List<Integer>> entry: forms.entrySet()){
			tempString = mapper.writeValueAsString(entry.getKey());
			returnForms.put(tempString, entry.getValue());
		}
		return returnForms; 
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getFormById(@PathParam("id") Long id,
			@QueryParam("detailed") boolean detailed) throws IOException,
			AppException {
		try{
		Form formById = formService
				.getFormById(id);
		return Response.status(200)
				.entity(new GenericEntity<Form>(formById) {
				}).header("Access-Control-Allow-Headers", "X-extra-header")
				.allow("OPTIONS").build();
		}catch(AccessDeniedException e){
			Form formData = formService.verifyFormExistenceById(id);
			formData.setQuestions(null);			
			return Response.status(Status.UNAUTHORIZED)
					.entity(new GenericEntity<Form>(formData) {
					}).header("Access-Control-Allow-Headers", "X-extra-header")
					.allow("OPTIONS").build();
			
		}
		
	}
	
	
	// ************************************* UPDATE
	// ************************************

	/**
	 * The method offers both Creation and Update resource functionality. If
	 * there is no resource yet at the specified location, then a form
	 * creation is executed and if there is then the resource will be full
	 * updated.
	 *
	 * @param id
	 * @param form
	 * @return
	 * @throws AppException
	 */
	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response putFormById(@PathParam("id") Long id,
			Form form) throws AppException {

		Form formById = formService
				.verifyFormExistenceById(id);

		if (formById == null) {
			// resource not existent yet, and should be created under the
			// specified URI
			Long createFormId = formService
					.createForm(form);
			return Response
					.status(Response.Status.CREATED)
					// 201
					.entity("A new form has been created AT THE LOCATION you specified")
					.header("Location", String.valueOf(createFormId))
					.build();
		} else {
			// resource is existent and a full update should occur
			formService.updateFullyForm(form);
			return Response
					.status(Response.Status.OK)
					// 200
					.entity("The form you specified has been fully updated created AT THE LOCATION you specified")
					.header("Location", String.valueOf(id)).build();
		}
	}

	// PARTIAL update
	@POST
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response partialUpdateForm(@PathParam("id") Long id,
			Form form) throws AppException {
		form.setId(id);
		formService.updatePartiallyForm(form);
		return Response
				.status(Response.Status.OK)
				// 200
				.entity("The form you specified has been successfully updated")
				.build();
	}
	
	@DELETE
	@Path("{id}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deletePost(@PathParam("id") Long id)
			throws AppException {
		Form form = formService.verifyFormExistenceById(id);
		
		
		formService.deleteForm(form);
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("Form successfully removed from database").build();
	}
	
	// *************************************
	// Permissions**************************
	
	@POST
	@Path("{id}/PERMISSION/{username}")
	@Produces({ MediaType.TEXT_HTML })
	public Response updatePermission(@PathParam("username") String username,
			@PathParam("id") Long id, @QueryParam("permissions") List<String> permissions) throws AppException {
		User user = userService.getUserByName(username);
		if(user != null) {
			Form form = formService.getFormById(id);
			formService.updatePermission(user, form, permissions);
			return Response
				.status(Response.Status.OK)
				.entity("PERMISSION UPDATED: User " + user.getUsername()
						+ " given permission " + permissions + " for form "
						+ form.getId()).build();
		} else {
			return Response.
					status(Response.Status.NOT_FOUND)
					.entity("USER NOT FOUND!").build();
		}
	}
	
	@POST
	@Path("{id}/PERMISSIONADD/{user}/{permission}")
	@Produces({ MediaType.TEXT_HTML })
	public Response addPermission(@PathParam("user") Long userId,
			@PathParam("id") Long id, @PathParam("permission") String permission) throws AppException {
		User user = userService.getUserById(userId);
		if(user != null) {
			Form form = formService.getFormById(id);
			formService.addPermission(user, form, permission);
			return Response
					.status(Response.Status.OK)
					.entity("PERMISSION ADDED: User " + user.getUsername()
							+ " given permission " + permission + " for form "
							+ form.getId()).build();
		} else {
			return Response.
					status(Response.Status.NOT_FOUND)
					.entity("USER NOT FOUND!").build();
		}
	}
	
	@DELETE
	@Path("{id}/PERMISSION/{user}/{permission}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deletePermission(@PathParam("user") Long userId,
			@PathParam("id") Long id, @PathParam("permission") String permission) throws AppException {
		User user = userService.getUserById(userId);
		Form form = formService.getFormById(id);
		formService.deletePermission(user, form, permission);
		return Response
				.status(Response.Status.OK)
				.entity("PERMISSION ADDED: User " + user.getUsername()
						+ " given permission " + permission + " for form "
						+ form.getId()).build();
	}
}
