package br.com.ufal.user;

public class Doctorate extends User {

	public Doctorate() {
		// TODO Auto-generated constructor stub
	}

	public Doctorate(String name, String email, String login, String password) {
		super(name, email, login, password);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		
		return super.toString() + "\nTipo: Doutorando";
		
	}
	
}
