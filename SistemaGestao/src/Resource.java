
public class Resource {
	private String id;
	private TypeRes type;
	private User responsible;
	
	public Resource() {}
	
	public Resource(String id, TypeRes type, User responsible) {
		super();
		this.id = id;
		this.type = type;
		this.responsible = responsible;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TypeRes getType() {
		return type;
	}

	public void setType(TypeRes type) {
		this.type = type;
	}

	public User getResponsible() {
		return responsible;
	}

	public void setResponsible(User responsible) {
		this.responsible = responsible;
	}
	
	
	
}
