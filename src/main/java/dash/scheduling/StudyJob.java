package dash.scheduling;

import java.util.Map;
import java.util.Set;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import dash.dao.UserDao;
import dash.dao.UserEntity;
import dash.errorhandling.AppException;
import dash.pojo.Study;
import dash.service.StudyService;

public class StudyJob extends QuartzJobBean{
	private Set<String> participants;
	private long formId;
	private long studyId;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	StudyService studyService;
	
	@Transactional
	public void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		UserEntity user;
		Study study = null;
		try {
			study = studyService.getStudyById(studyId);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		studyService.insertExpirationTime(study.getId(), study.getExpirationTime());
		participants = study.getParticipants();
		for(String participant : participants){
			user = userDao.getUserByName(participant) ;
			Map<Long, Long> activeStudies = user.getActiveStudies();
			activeStudies.put(studyId, formId);
			user.setActiveStudies(activeStudies);
			userDao.updateUser(user);
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

	}
	

	/*private void sendPushNotification(String username){
	}*/
	
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
