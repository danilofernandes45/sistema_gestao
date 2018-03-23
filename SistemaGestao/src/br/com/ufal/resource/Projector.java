package br.com.ufal.resource;
import br.com.ufal.user.User;


public class Projector extends Resource {

	public Projector() {
		// TODO Auto-generated constructor stub
	}

	public Projector(String id, User responsible) {
		super(id, responsible);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		
		return super.toString() + "\nTipo : Projetor";
		
	}

}
