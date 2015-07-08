package dash.scheduling;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dash.dao.StudyDao;
import dash.dao.StudyEntity;
import dash.pojo.Study;

//This TimerTask is designed to run every day at midnight. 
//It will retrieve all of the studies with a start date
//Of the current day and then initialize the job for them based of off the survey.

public class DailyInitJob extends TimerTask{
	
	private ServletContextEvent servletContextEvent;
	
	private Scheduler scheduler;

	public DailyInitJob(ServletContextEvent servletContextEvent, Scheduler scheduler){
		this.servletContextEvent = servletContextEvent;
		this.scheduler = scheduler;
	}
	
	
	
	@Override
	public void run() {	
		ApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext());
		AutowireCapableBeanFactory factory = springContext.getAutowireCapableBeanFactory();
		
		StudyDao studyDao = (StudyDao) factory.getBean(StudyDao.class);
		
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
			studyName = (dateString + counter++);
			groupName = ("group_" + dateString);
			JobDetail job = JobBuilder.newJob(StudyJob.class)
					.withIdentity(studyName, groupName)
					.usingJobData("formId", study.getFormId())
					.usingJobData("studyId", study.getId())
					.build();
			List<String>cronStrings = study.generateCronStrings();
			List<Trigger> triggers = new ArrayList<Trigger>();
			for(String cronString : cronStrings){
				int count = 0;
				CronTrigger cronTrigger = TriggerBuilder.newTrigger()
						.withIdentity("trigger_study:" + study.getId() + "_" + count++, 
								"triggerStudy" + study.getId())
						.endAt(study.getEndDate())
						.startAt(study.getStartDate())
						.withSchedule(CronScheduleBuilder.cronSchedule(cronString)
								.withMisfireHandlingInstructionFireAndProceed())
						.build();
				triggers.add(cronTrigger);
			}
			jobs.put(job, triggers);
		}
		try {
			scheduler.scheduleJobs(jobs, true);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
