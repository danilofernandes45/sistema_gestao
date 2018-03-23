package br.com.ufal.resource;
import br.com.ufal.user.User;


public class Laboratory extends Resource {

	public Laboratory() {
		// TODO Auto-generated constructor stub
	}

	public Laboratory(String id, User responsible) {
		super(id, responsible);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		
		return super.toString() + "\nTipo : Laboratorio";
		
	}

}
