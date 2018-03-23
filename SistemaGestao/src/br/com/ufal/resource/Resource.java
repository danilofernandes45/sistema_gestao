package br.com.ufal.resource;
import br.com.ufal.user.User;


public class Resource {
	protected String id;
	protected User responsible;
	
	public Resource() {}
	
	public Resource(String id, User responsible) {
		super();
		this.id = id;
		this.responsible = responsible;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getResponsible() {
		return responsible;
	}

	public void setResponsible(User responsible) {
		this.responsible = responsible;
	}
	
	public String toString() {
		
		return "Id: "+id+"\nResponsavel : "+responsible.getName();
		
	}
	
}
