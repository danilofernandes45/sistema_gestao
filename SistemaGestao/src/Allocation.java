
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
	
	
	
	
	
}
