package br.com.ufal.user;


public class Researcher extends User {

	public Researcher() {
		// TODO Auto-generated constructor stub
	}

	public Researcher(String name, String email, String login, String password) {
		super(name, email, login, password);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		
		return super.toString() + "\nTipo: Pesquisador";
		
	}

}
