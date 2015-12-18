package dash.scheduling;

import java.util.TimerTask;

import javax.servlet.ServletContextEvent;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dash.service.StudyService;

public class TimeoutTask extends TimerTask {

	private ServletContextEvent servletContextEvent;

	public TimeoutTask(ServletContextEvent servletContextEvent) {
		this.servletContextEvent = servletContextEvent;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Gets the current spring context to retrieve the StudyService bean
		ApplicationContext springContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContextEvent
						.getServletContext());
		//Retrieves the study service bean
		AutowireCapableBeanFactory factory = springContext
				.getAutowireCapableBeanFactory();
		StudyService studyService = (StudyService) factory
				.getBean(StudyService.class);
		
		studyService.expireStudies();
	}

}
