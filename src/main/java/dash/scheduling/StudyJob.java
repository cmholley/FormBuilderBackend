package dash.scheduling;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import dash.errorhandling.AppException;
import dash.pojo.Study;
import dash.pojo.User;
import dash.service.StudyService;
import dash.service.UserService;

public class StudyJob extends QuartzJobBean{
	private Set<String> participants;
	private long formId;
	private long studyId;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StudyService studyService;
	
	public void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		Study study = null;
		try {
			study = studyService.getStudyById(studyId);
		} catch (AppException e) {
			e.printStackTrace();
		}
		//If the study has an expiration time, then add it to the list
		if(study.getDuration() != null && study.getDuration() != 0){
			studyService.insertExpirationTime(study.getId(), study.getDuration());
		}
		//Update user activeStudies maps
		participants = new HashSet<String>();
		for(String participant : study.getParticipants()){
			User user = userService.getUserByName(participant);
			if(user.isIs_email_verified()){
				participants.add(participant);
			}
		}
		updateActiveStudies();
	}
	
	private void updateActiveStudies(){
		User user;
		for(String participant : participants){
			user = userService.getUserByName(participant) ;
			Map<Long, Long> activeStudies = user.getActiveStudies();
			activeStudies.put(studyId, formId);
			user.setActiveStudies(activeStudies);
			try {
				userService.updateUserJob(user);
			} catch (AppException e) {
				e.printStackTrace();
			}
			sendNotifications(user);
		}
	}
	
	private void sendNotifications(User user){
		//TODO: User preferences
		switch(user.getNotificationPreference()){
		case EMAIL:
			sendNotificationEmail(user.getUsername());
			break;
		case TEXT:
			sendTextNotification(user.getCellPhone());
			break;
		case BOTH:
			sendTextNotification(user.getCellPhone());
			sendNotificationEmail(user.getUsername());
			break;
		}
	}
	
	private void sendNotificationEmail(String email){
		studyService.sendStudyNotificationEmail(email, formId, studyId);
	}
	
	private void sendTextNotification(String cellPhone){
		studyService.sendTextNotification(cellPhone, formId, studyId);
	}
	//TODO: Use SMS API rather than email
	
	public void setParticipants(Set<String> participants) {
		this.participants = participants;
	}

	public void setFormId(long formId) {
		this.formId = formId;
	}

	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}	
}
