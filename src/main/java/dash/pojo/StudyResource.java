package dash.pojo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dash.errorhandling.AppException;
import dash.filters.AppConstants;
import dash.service.FormService;
import dash.service.FormServiceDbAccessImpl;
import dash.service.StudyService;

/**
 *
 * Service class that handles REST requests. This is where you define your API
 * and what requests will be accepted.
 *
 * @author tyler.swensen@gmail.com
 *
 */
@Component("studyResource")
@Path("/studies")
public class StudyResource {

	@Autowired
	private StudyService studyService;

	@Autowired
	private FormService formService;

	// ************************************* CREATE
	// ************************************

	/**
	 * Adds a new resource (study) from the given json format (at least
	 * studyname and password elements are required at the DB level)
	 *
	 * @param study
	 * @return
	 * @throws AppException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response createStudy(Study study) throws AppException {
		Long createStudyId = studyService.createStudy(study);
		return Response
				.status(Response.Status.CREATED)
				// 201
				.entity("A new study has been created at index")
				.header("Location", String.valueOf(createStudyId))
				.header("ObjectId", String.valueOf(createStudyId)).build();
	}

	/**
	 * A list of resources (here studies) provided in json format will be added
	 * to the database.
	 *
	 * @param studies
	 * @return
	 * @throws AppException
	 */
	@POST
	@Path("list")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createStudies(List<Study> studies) throws AppException {
		studyService.createStudies(studies);
		return Response.status(Response.Status.CREATED)
		// 201
				.entity("List of studies was successfully created").build();
	}

	// *************************************
	// READ************************************

	/**
	 * Returns a list of studies via pagination. The order the list is sorted is
	 * set in the DAO implementation. Number of sample objects is the page size,
	 * and start index is the id of the last sample object received.
	 * 
	 * @param numberOfStudies
	 * @param startIndex
	 * @return
	 * @throws IOException
	 * @throws AppException
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Study> getStudies(
			@QueryParam("numberOfStudies") @DefaultValue("25") int numberOfStudies,
			@QueryParam("startIndex") @DefaultValue("0") Long startIndex)
			throws IOException, AppException {
		List<Study> studies = studyService.getStudies(numberOfStudies,
				startIndex);
		return studies;
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getStudyById(@PathParam("id") Long id,
			@QueryParam("detailed") boolean detailed) throws IOException,
			AppException {
		Study studyById = studyService.getStudyById(id);
		return Response.status(200).entity(new GenericEntity<Study>(studyById) {
		}).header("Access-Control-Allow-Headers", "X-extra-header")
				.allow("OPTIONS").build();
	}

	@GET
	@Path("/getstudiesform/{formId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Study> getStudiesForForm(@PathParam("formId") long formId)
			throws AppException {
		Form form = formService.getFormById(formId);
		List<Study> studies = studyService.getStudiesForForm(formId, form);
		return studies;
	}

	// ************************************* UPDATE
	// ************************************

	/**
	 * The method offers both Creation and Update resource functionality. If
	 * there is no resource yet at the specified location, then a study creation
	 * is executed and if there is then the resource will be full updated.
	 *
	 * @param id
	 * @param study
	 * @return
	 * @throws AppException
	 */
	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response putStudyById(@PathParam("id") Long id, Study study)
			throws AppException {

		Study studyById = studyService.verifyStudyExistenceById(id);

		if (studyById == null) {
			// resource not existent yet, and should be created under the
			// specified URI
			Long createStudyId = studyService.createStudy(study);
			return Response
					.status(Response.Status.CREATED)
					// 201
					.entity("A new study has been created AT THE LOCATION you specified")
					.header("Location", String.valueOf(createStudyId)).build();
		} else {
			// resource is existent and a full update should occur
			studyService.updateFullyStudy(study);
			return Response
					.status(Response.Status.OK)
					// 200
					.entity("The study you specified has been fully updated created AT THE LOCATION you specified")
					.header("Location", String.valueOf(id)).build();
		}
	}

	// PARTIAL update
	@POST
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response partialUpdateStudy(@PathParam("id") Long id, Study study)
			throws AppException {
		study.setId(id);
		studyService.updatePartiallyStudy(study);
		return Response
				.status(Response.Status.OK)
				// 200
				.entity("The study you specified has been successfully updated")
				.build();
	}

	@PUT
	@Path("/updatestudies")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response updateStudies(List<Study> studies) {

		FormServiceDbAccessImpl formService = new FormServiceDbAccessImpl();
		studyService.updateStudies(studies, formService);
		return Response.status(Response.Status.OK)
		// 20
				.entity("List of studies was successfully created").build();
	}

	// ************************************* FILE UPLOAD
	// ************************************

}
