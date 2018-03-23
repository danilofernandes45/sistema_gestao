package br.com.ufal.resource;
import br.com.ufal.user.User;


public class Classroom extends Resource {

	public Classroom() {
		// TODO Auto-generated constructor stub
	}

	public Classroom(String id, User responsible) {
		super(id, responsible);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		
		return super.toString() + "\nTipo : Sala de aula";
		
	}

}
