import java.util.ArrayList;
public class Activity {
	
	private String title;
	private String description;
	private String material;
	private TypeAct type;
	private ArrayList<User> participants = new ArrayList<>();
	
	public Activity() {}
	
	public Activity(String title, String description, String material, TypeAct type, ArrayList<User> participants) {
		this.title = title;
		this.description = description;
		this.material = material;
		this.type = type;
		this.participants = participants;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public TypeAct getType() {
		return type;
	}
	public void setType(TypeAct type) {
		this.type = type;
	}
	public ArrayList<User> getParticipants() {
		return participants;
	}
	public void setParticipants(ArrayList<User> participants) {
		this.participants = participants;
	}
	public void addParticipants(User user) {
		this.participants.add(user);
	}
}
