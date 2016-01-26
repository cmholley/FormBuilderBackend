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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dash.dao.StudyDao;
import dash.pojo.Study;

//This TimerTask is designed to run every day at midnight. 
//It will retrieve all of the studies with a start date
//Of the current day and then initialize the job for them based of off the survey.

public class DailyInitTask extends TimerTask {

	private ServletContextEvent servletContextEvent;

	private Scheduler scheduler;

	public DailyInitTask(ServletContextEvent servletContextEvent, Scheduler scheduler) {
		this.servletContextEvent = servletContextEvent;
		this.scheduler = scheduler;
	}

	@Override
	public void run() {
		//// Gets the current spring context to retrieve the StudyDao bean
		ApplicationContext springContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContextEvent.getServletContext());
		AutowireCapableBeanFactory factory = springContext.getAutowireCapableBeanFactory();

		StudyDao studyDao = (StudyDao) factory.getBean(StudyDao.class);
		
		//Retrieves the list of studies to be executed
		List<Study> todaysStudies = studyDao.getTodaysStudies();
		
		int counter = 0;
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

		String dateString = format.format(date);
		String studyName;
		String groupName;
		
		//Construct a mapping of JobDetails to a list 
		//of triggers that are to be assigned to it
		Map<JobDetail, List<Trigger>> jobs = new HashMap<JobDetail, List<Trigger>>();
		for (Study study : todaysStudies) {
			//Used for the identity of the job. 
			//See Quartz documentation for more information on groups and names
			studyName = (dateString + counter++);
			groupName = ("group_" + dateString);
			//Create the actual job, use .usingJobData() to pass in the formId and studyId
			JobDetail job = JobBuilder.newJob(StudyJob.class).withIdentity(studyName, groupName)
					.usingJobData("formId", study.getFormId()).usingJobData("studyId", study.getId()).build();
			
			//Generate the triggers for the job
			List<String> cronStrings = study.generateCronStrings();
			List<Trigger> triggers = new ArrayList<Trigger>();
			for (String cronString : cronStrings) {
				int count = 0;
				CronTrigger cronTrigger = TriggerBuilder.newTrigger()
						.withIdentity("trigger_study:" + study.getId() + "_" + count++, "triggerStudy" + study.getId())
						.endAt(study.getEndDate()).startAt(study.getStartDate()).withSchedule(CronScheduleBuilder
								.cronSchedule(cronString).withMisfireHandlingInstructionFireAndProceed())
						.build();
				triggers.add(cronTrigger);
			}
			jobs.put(job, triggers);
		}
		try {
			scheduler.scheduleJobs(jobs, true);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			Logger logger = LoggerFactory.getLogger(this.getClass());
			logger.error("Exception thrown in " + this.getClass().getName(), e);
		}

	}

}
