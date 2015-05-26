package dash.scheduling;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import dash.dao.StudyDao;
import dash.dao.StudyDaoJPA2Impl;
import dash.dao.StudyEntity;
import dash.pojo.Study;

//This job is designed to run when the servlet is initialized and then again
//Every day at midnight. It will retrieve all of the studies with a start date
//Of the current day and then initialize the job for them based of off the survey.

public class DailyInitJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		StudyDao studyDao = new StudyDaoJPA2Impl(); 
		List<StudyEntity> todaysStudyEntities = studyDao.getTodaysStudies();
		List<Study>	todaysStudies = new ArrayList<Study>();
		for(StudyEntity studyEntity : todaysStudyEntities){
			todaysStudies.add(new Study(studyEntity));
		}
		for(Study study : todaysStudies){
			
		}
	}

}
