package br.com.ufal.user;

public class Undergraduate extends User {

	public Undergraduate() {
		// TODO Auto-generated constructor stub
	}

	public Undergraduate(String name, String email, String login,
			String password) {
		super(name, email, login, password);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		
		return super.toString() + "\nTipo: Graduando";
		
	}

}
