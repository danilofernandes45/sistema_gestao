package br.com.ufal.user;

public class Masters extends User {

	public Masters() {
		// TODO Auto-generated constructor stub
	}

	public Masters(String name, String email, String login, String password) {
		super(name, email, login, password);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		
		return super.toString() + "\nTipo: Mestrando";
		
	}

}
