package br.com.ufal.resource;
import br.com.ufal.user.User;


public class Auditorium extends Resource {

	public Auditorium() {
		// TODO Auto-generated constructor stub
	}

	public Auditorium(String id, User responsible) {
		super(id, responsible);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		
		return super.toString() + "\nTipo : Auditorio";
		
	}

}
