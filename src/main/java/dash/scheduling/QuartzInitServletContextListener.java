package dash.scheduling;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

@WebListener
public class QuartzInitServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		scheduleDailyInitJob(sce);
		scheduleTimeoutJob(sce);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

	private void scheduleTimeoutJob(ServletContextEvent sce) {
		Timer timeoutTimer = new Timer(true);// The timer thread needs to be a
											 // daemon
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, cal.get(Calendar.MINUTE) % 5);
		Date start = cal.getTime();
		timeoutTimer.scheduleAtFixedRate(new TimeoutTask(sce), start,
				TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES));
	}

	private void scheduleDailyInitJob(ServletContextEvent sce) {
		Timer dailyTimer = new Timer(true);// The timer thread needs to be a
											// daemon
		Scheduler scheduler = null;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 5);// Scheduling at 12:05am removes midnight
									// ambiguity
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// cal.add(Calendar.SECOND, 15);
		Date midnightDate = cal.getTime();
		dailyTimer.scheduleAtFixedRate(new DailyInitTask(sce, scheduler),
				midnightDate, TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // Executes
																				// daily
	}

}
