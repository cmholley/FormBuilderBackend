package dash.scheduling;

import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class StudyJob implements Job {
	private Set<String> participants;
	private int formId;
	private int studyId;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		

	}

	public void setParticipants(Set<String> participants) {
		this.participants = participants;
	}

	public void setFormId(int formId) {
		this.formId = formId;
	}

	public void setStudyId(int studyId) {
		this.studyId = studyId;
	}
	
}
