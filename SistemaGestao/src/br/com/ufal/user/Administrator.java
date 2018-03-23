package br.com.ufal.user;

public class Administrator extends User {

	public Administrator() {
		// TODO Auto-generated constructor stub
	}

	public Administrator(String name, String email, String login,
			String password) {
		super(name, email, login, password);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		
		return super.toString() + "\nTipo: Administrador";
		
	}
	
}
