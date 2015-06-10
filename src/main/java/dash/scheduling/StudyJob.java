package dash.scheduling;

import java.util.Map;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import dash.dao.UserDao;
import dash.dao.UserDaoJPA2Impl;
import dash.dao.UserEntity;
import dash.service.StudyService;
import dash.service.StudyServiceDbAccessImpl;

public class StudyJob extends QuartzJobBean{
	private Set<String> participants;
	private long formId;
	private long studyId;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	StudyService studyService;
	
	@Transactional
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		UserEntity user;
		for(String participant : participants){
			user = userDao.getUserByName(participant) ;
			Map<Long, Long> activeStudies = user.getActiveStudies();
			activeStudies.put(studyId, formId);
			user.setActiveStudies(activeStudies);
			userDao.updateUser(user);
			//TODO: User preferences
			sendNotificationEmail(participant);
		}

	}
	

	private void sendPushNotification(String username){
		
	}
	
	private void sendNotificationEmail(String email){
		studyService.sendStudyNotificationEmail(email, formId, studyId);
	}

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
