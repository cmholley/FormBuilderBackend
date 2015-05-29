package dash.scheduling;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import dash.dao.StudyDao;
import dash.dao.StudyDaoJPA2Impl;
import dash.dao.StudyEntity;
import dash.pojo.Study;

//This TimerTask is designed to run every day at midnight. 
//It will retrieve all of the studies with a start date
//Of the current day and then initialize the job for them based of off the survey.

public class DailyInitJob extends TimerTask {

	@Override
	public void run() {
		
		StudyDao studyDao = new StudyDaoJPA2Impl(); 
		List<StudyEntity> todaysStudyEntities = studyDao.getTodaysStudies();
		List<Study>	todaysStudies = new ArrayList<Study>();
		for(StudyEntity studyEntity : todaysStudyEntities){
			todaysStudies.add(new Study(studyEntity));
		}
		int counter = 0;
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		
		String dateString = format.format(date);
		String studyName;
		String groupName;
		Map<JobDetail, List<Trigger>> jobs = new HashMap<JobDetail, List<Trigger>>();
		for(Study study : todaysStudies){
			studyName = (dateString + counter);
			groupName = ("group_" + dateString);
			JobDetail job = JobBuilder.newJob(StudyJob.class)
					.withIdentity(studyName, groupName)
					.usingJobData("formId", study.getFormId())
					.usingJobData("studyId", study.getId())
					.build();
			job.getJobDataMap().put("participants", study.getParticipants());
			List<String>cronStrings = study.generateCronStrings();
			List<Trigger> triggers = new ArrayList<Trigger>();
			for(String cronString : cronStrings){
				int count = 0;
				Trigger cronTrigger = TriggerBuilder.newTrigger()
						.withIdentity("trigger_" + study.getId() + "_" + count, 
								"triggerStudy" + study.getId())
						.withSchedule(CronScheduleBuilder.cronSchedule(cronString))
						.build();
				triggers.add(cronTrigger);
			}
			jobs.put(job, triggers);
		}
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJobs(jobs, true);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
