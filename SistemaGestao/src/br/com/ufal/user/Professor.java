package br.com.ufal.user;

public class Professor extends User {

	public Professor() {
		// TODO Auto-generated constructor stub
	}

	public Professor(String name, String email, String login, String password) {
		super(name, email, login, password);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		
		return super.toString() + "\nTipo: Professor";
		
	}

}
