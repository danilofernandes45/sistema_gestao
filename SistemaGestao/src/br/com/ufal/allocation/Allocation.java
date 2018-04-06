package br.com.ufal.allocation;
import br.com.ufal.activity.Activity;
import br.com.ufal.resource.Resource;
import br.com.ufal.user.User;
import br.com.ufal.util.Date;
import br.com.ufal.util.Status;
import br.com.ufal.util.Time;


public class Allocation {
	
	private Status status;
	private Date dateBegin;
	private Time timeBegin;
	private Date dateEnd;
	private Time timeEnd;
	private User requester;
	private Resource resource;
	private Activity activity;
	
	public Allocation() {}
	
	public Allocation(Status status, Date dateBegin, Time timeBegin, Date dateEnd, Time timeEnd, User requester,
			Resource resource, Activity activity) {
		this.status = status;
		this.dateBegin = dateBegin;
		this.timeBegin = timeBegin;
		this.dateEnd = dateEnd;
		this.timeEnd = timeEnd;
		this.requester = requester;
		this.resource = resource;
		this.activity = activity;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Time getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin(Time timeBegin) {
		this.timeBegin = timeBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Time getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Time timeEnd) {
		this.timeEnd = timeEnd;
	}

	public User getRequester() {
		return requester;
	}

	public void setRequester(User requester) {
		this.requester = requester;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	public String getStringStatus() {
		
		if(this.status == Status.IN_PROCESS)
			return "Em processo";
		if(this.status == Status.ALLOCATED)
			return "Alocado";
		if(this.status == Status.IN_PROGRESS)
			return "Em andamento";
		
		return "Concluido";
		
	}
	
	public String printWithoutResource() {
		
		return "Usuário: "+this.requester.getName()+"\n"
				 + "Atividade: "+this.activity.getTitle()+"\n"
				+ "Status: "+getStringStatus();
		
	}
	
	public String printWithoutRequester() {
		
		return "Recurso: "+this.resource.getId()+"\n"
				 + "Atividade: "+this.activity.getTitle()+"\n"
				+ "Status: "+getStringStatus();
		
	}
	
	
	
}
