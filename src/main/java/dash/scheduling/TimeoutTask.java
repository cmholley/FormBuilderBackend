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
		ApplicationContext springContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContextEvent
						.getServletContext());
		AutowireCapableBeanFactory factory = springContext
				.getAutowireCapableBeanFactory();
		StudyService studyService = (StudyService) factory
				.getBean(StudyService.class);
		studyService.expireStudies();
	}

}
