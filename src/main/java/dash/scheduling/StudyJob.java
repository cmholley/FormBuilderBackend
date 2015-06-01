package dash.scheduling;

import java.util.Map;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import dash.dao.UserDaoJPA2Impl;
import dash.dao.UserEntity;
import dash.service.StudyService;
import dash.service.StudyServiceDbAccessImpl;

public class StudyJob implements Job {
	private Set<String> participants;
	private long formId;
	private long studyId;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		UserDaoJPA2Impl userDao = new UserDaoJPA2Impl();
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
		StudyServiceDbAccessImpl studyService = new StudyServiceDbAccessImpl();
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
