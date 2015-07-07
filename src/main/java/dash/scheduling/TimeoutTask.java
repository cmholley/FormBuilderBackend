package dash.scheduling;

import java.util.TimerTask;

import javax.servlet.ServletContextEvent;

import org.quartz.Scheduler;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dash.dao.StudyDao;

public class TimeoutTask extends TimerTask {

private ServletContextEvent servletContextEvent;
	
	private Scheduler scheduler;

	public TimeoutTask(ServletContextEvent servletContextEvent, Scheduler scheduler){
		this.servletContextEvent = servletContextEvent;
		this.scheduler = scheduler;
	}
	
	
	
	@Override
	public void run() {	
		ApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext());
		AutowireCapableBeanFactory factory = springContext.getAutowireCapableBeanFactory();
		
		StudyDao studyDao = (StudyDao) factory.getBean(StudyDao.class);
		List<>
	}

}
