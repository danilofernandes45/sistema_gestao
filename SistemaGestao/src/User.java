
public class User {
	
	private String name;
	private String email;
	private String login;
	private String password;
	private TypeUser type;
	
	public User() {}
	
	public User(String name, String email, String login, String password, TypeUser type) {
		this.name = name;
		this.email = email;
		this.login = login;
		this.password = password;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public TypeUser getType() {
		return type;
	}
	public void setType(TypeUser type) {
		this.type = type;
	}
}