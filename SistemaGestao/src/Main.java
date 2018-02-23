import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	static final ArrayList<User> users = new ArrayList<>();
	static final ArrayList<Resource> resources = new ArrayList<>();
	static final ArrayList<Activity> activities = new ArrayList<>();
	static final ArrayList<Allocation> allocations = new ArrayList<>();
	
	static User user_logged = null;
	static final ArrayList<Integer> id_alloc_in_process = new ArrayList<>();
	static final ArrayList<Integer> id_allocated = new ArrayList<>();
	static final ArrayList<Integer> id_in_progress = new ArrayList<>();
	
	static final Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		User user = new User("ADM", "adm@ic.ufal.br", "adm", "adm", TypeUser.ADM);
		users.add(user);
		
		Resource resource = new Resource("aud1", TypeRes.AUDITORIUM, user); // Responsible = ADM
		resources.add(resource);
		
		user = new User("Baldoino", "baldoino@ic.ufal.com", "baldoino", "b123", TypeUser.PROFESSOR);
		users.add(user);
		user = new User("João", "joao@ic.ufal.com", "joao", "j123", TypeUser.RESEARCHER);
		users.add(user);
		
		resource = new Resource("lab1", TypeRes.LABORATORY, user); // Responsible = Joao
		resources.add(resource);
		
		login();
		
	}
	
	private static void login() {
		
		String login, passwd;
		User user = null;
		
		while( true ) { 
			System.out.print("Login: ");
			login = input.nextLine();
			System.out.print("Password: ");
			passwd = input.nextLine();
			
			int index = searchUser(login);
			if(index != -1) {
				user = users.get(index);
			}
			if(user != null && passwd.equals(user.getPassword())) {
				user_logged = user;
				displayMenu();
			} else {
				System.out.println("\nLogin ou senha incorretos\n");
			}
			
		}
		
	}
	
	private static ArrayList<Integer> getResourcesToConfirm() {
		
		ArrayList<Integer> resourcesToConfirm = new ArrayList<>();
		
		for(int i : id_allocated) {
			if(resources.get(i).getResponsible().getLogin().equals(user_logged.getLogin())) {
				resourcesToConfirm.add(i);
			}
		}
		
		return resourcesToConfirm;
		
	}
	
	private static void displayMenu() {
		
		ArrayList<Integer> resourcesToConfirm = getResourcesToConfirm();
		boolean isResponsible = false;
		
		if(resourcesToConfirm.size() > 0)
			isResponsible = true;
		
		int option;
		do {
			
			System.out.println("Menu:\n"
					+ "1 - Solicitar alocação de recurso\n"
					+ "2 - Consultar por usuário\n"
					+ "3 - Consultar por recurso");
			
			if(isResponsible)
				System.out.println("4 - Confirmar alocação de recurso");
				
			
			if(user_logged.getType() == TypeUser.ADM) {
				
				if(id_alloc_in_process.size() > 0)
					System.out.println("5 - Confirmar processo de alocação");
				if(id_in_progress.size() > 0)
					System.out.println("6 - Concluir processo de alocação");
				
				System.out.println("7 - Criar usuário\n"
								 + "8 - Excluir usuário\n"
								 + "9 - Criar recurso\n"
								 + "10 - Excluir recurso\n"
								 + "11 - Gerar relatório");
				
			}
			
			System.out.println("0 - Sair");
			
			option = Integer.valueOf(input.nextLine());
			if(option == 1)
				allocateResource();
			else if(option == 2)
				showUser();
			else if(option == 3)
				showResource();
			else if(option == 4 && isResponsible) {
				
				confirmAllocation(resourcesToConfirm);
				resourcesToConfirm = getResourcesToConfirm();
				if(resourcesToConfirm.size() == 0)
					isResponsible = false;
			
			}
			else if (user_logged.getType() == TypeUser.ADM) {
				if(option == 5 && id_alloc_in_process.size() > 0) {
					
					confirmProcess();
					resourcesToConfirm = getResourcesToConfirm();
					if(resourcesToConfirm.size() > 0)
						isResponsible = true;
					
				}
				else if(option == 6 && id_in_progress.size() > 0)
					concludeProcess();
				else if(option == 7)
					createUser();
				else if(option == 8)
					deleteUser();
				else if(option == 9)
					createResource();
				else if(option == 10)
					deleteResource();
				else if(option == 11)
					generateReport();
			}
		}while( option != 0 );
		
		user_logged = null;
		login();
			
		
	}
	
	private static void concludeProcess() {
		changeStatus(id_in_progress, Status.COMPLETED);
	}

	private static void confirmProcess() {
		int cod = changeStatus(id_alloc_in_process, Status.ALLOCATED);
		id_allocated.add(cod);
	}
	
	private static int changeStatus(ArrayList<Integer> list, Status status ) {
		System.out.println("Alocações de recursos pendentes: \n\n"
						 + "Cod - Id do recurso - Nome do usuário");
		System.out.println("--------------------------------------");
		Allocation alloc;
		for(int i=0; i<list.size(); i++) {
			alloc = allocations.get( list.get(i) );
			System.out.println((list.get(i))+" - "+alloc.getResource().getId()+" - "+alloc.getRequester().getName());
		}
		
		System.out.println("\nDigite o código da alocação (Cod)");
		int cod = Integer.valueOf( input.nextLine() );
		
		allocations.get(cod).setStatus(status);
		list.remove(cod);
		
		System.out.println("Feito!\n");
		
		return cod;
		
	}

	private static void showResource() {
		System.out.println("Digite o id do Recurso: ");
		int index = searchResource( input.nextLine() );
		if( index == -1 ) {
			System.out.println("Recurso não encontrado");
			return;
		}
		
		Resource resource = resources.get(index);
		
		System.out.printf("Responsável : "+resource.getResponsible().getName()+"\n"
						 + "Tipo de recurso: ");
		
		switch( resource.getType() ) {
		
			case LABORATORY:
				System.out.println("Laboratório\n");
				break;
			case AUDITORIUM:
				System.out.println("Auditório\n");
				break;
			case CLASSROOM:
				System.out.println("Sala de aula\n");
				break;
			case PROJECTOR:
				System.out.println("Projetor\n");
				break;
			
		}
		
		System.out.println("Alocações:\n");
		boolean hasAllocations = false;
		
		for(Allocation alloc : allocations) {
			if(alloc.getResource().getId().equals(resource.getId())) {
				
				System.out.println("Usuário: "+alloc.getRequester().getName()+"\n"
								 + "Atividade: "+alloc.getActivity().getTitle()+"\n");
				
				printStatus(alloc);
				hasAllocations = true;
					
			}
		}
		if(!hasAllocations)
			System.out.println("[Sem alocações]\n");
			
		
	}

	private static void showUser() {
		System.out.println("Digite o login do usuário: \n");
		int index = searchUser( input.nextLine() );
		if(index == -1) {
			System.out.println("Usuário não encontrado\n");
			return;
		}
		
		User user = users.get(index);
		
		System.out.printf("Nome: "+user.getName()+"\n"
						 + "Email: "+user.getEmail()+"\n"
						 + "Tipo de usuário: ");
		
		switch( user.getType() ) {
		
			case ADM:
				System.out.println("Administrador\n");
				break;
			case UNDERGRADUATE:
				System.out.println("Aluno de graduação\n");
				break;
			case MASTERS:
				System.out.println("Aluno de mestrado\n");
				break;
			case DOCTORATE:
				System.out.println("Aluno de doutorado\n");
				break;
			case PROFESSOR:
				System.out.println("Professor\n");
				break;
			case RESEARCHER:
				System.out.println("Pesquisador\n");
				break;
		
	}
		
		System.out.println("Alocações:\n");
		boolean hasAllocations = false;
		
		for(Allocation alloc : allocations) {
			if(alloc.getRequester().getLogin().equals(user.getLogin())) {
				
				System.out.println("Recurso: "+alloc.getResource().getId()+"\n"
								 + "Atividade: "+alloc.getActivity().getTitle()+"\n");
				
				printStatus(alloc);
				hasAllocations = true;
					
			}
		}
		if(!hasAllocations)
			System.out.println("[Sem alocações]\n");
	}
	
	public static void printStatus(Allocation alloc) {
		
		if( alloc.getStatus() == Status.IN_PROCESS )
			System.out.println("Status: Em processo");
		else if( alloc.getStatus() == Status.ALLOCATED )
			System.out.println("Status: Alocado");
		else if( alloc.getStatus() == Status.IN_PROGRESS )
			System.out.println("Status: Em andamento");
		else
			System.out.println("Status: Concluído");
		
		System.out.println("\n----------------------------\n");
		
	}

	private static void generateReport() {
		
		System.out.println("--------------------RELATÓRIO----------------------\n\n" 
						  + "Número de usuários: "+users.size());
		
		int in_process_size = id_alloc_in_process.size();
		int allocated_size = id_allocated.size();
		int in_progress_size = id_in_progress.size();
		int alloc_size = allocations.size();
		
		int num_class = 0;
		int num_lab = 0;
		int num_pres = 0;
		for(Activity act: activities) {
			
			if(act.getType() == TypeAct.CLASS)
				num_class++;
			else if(act.getType() == TypeAct.PRESANTATION)
				num_pres++;
			else
				num_lab++;
		}
		
		System.out.println("\nNúmero de recursos em processo de alocação: "+in_process_size
						  +"\nNúmero de recursos alocados: "+allocated_size
						  +"\nNúmero de recursos 'em andamento': "+in_progress_size
						  +"\nNúmero de recursos 'concluídos': "+(alloc_size - in_process_size - in_progress_size - allocated_size)
						  +"\nNúmero total de alocações: "+alloc_size
						  +"\n\nNúmero de atividades por tipo:\n"
						  +"\n- Aulas tradicionais: "+num_class
						  +"\n- Laboratórios: "+num_lab
						  +"\n- Apresentações: "+num_pres);
		System.out.println("---------------------------------------------------\n");
		
		
	}

	private static void deleteResource() {
		
		System.out.println("Digite o id do recurso: ");
		int index = searchResource( input.nextLine() );
		
		if(index == -1)
			System.out.println("Recurso não encontrado!");
		else {
			resources.remove(index);
			System.out.println("Feito!\n");
		}
		
	}

	private static void createResource() {
		
		Resource resource = new Resource();
		System.out.println("Digite a identificação (id): ");
		resource.setId( input.nextLine() );
		System.out.println("Digite o tipo: \n"
						 + "1 - Laboratório\n"
						 + "2 - Auditório\n"
						 + "3 - Sala de aula\n"
						 + "4 - Projetor");
		int type = Integer.valueOf( input.nextLine() );
		
		if(type == 1)
			resource.setType(TypeRes.LABORATORY);
		else if(type == 2)
			resource.setType(TypeRes.AUDITORIUM);
		else if(type == 3)
			resource.setType(TypeRes.CLASSROOM);
		else
			resource.setType(TypeRes.PROJECTOR);
		
		User responsible = null;
		boolean unable = true;
		System.out.println("Digite o login do usuário responsável pelo recurso");
		while(responsible == null || unable) {
			int index = searchUser( input.nextLine() );
			
			if(index == -1)
				System.out.println("Usuário não encontrado! Tente novamente.");
			else {
				responsible = users.get(index);
				if(responsible.getType() != TypeUser.ADM && responsible.getType() != TypeUser.PROFESSOR && responsible.getType() != TypeUser.RESEARCHER ) {
					System.out.println("Somente são permitidos serem responsáveis pelo recurso: um professor, um pesquisador ou um adminstrador do sistema. Tente novamente.");
				} else {
					unable = false;
				}
			}

			
		}
		
		resource.setResponsible(responsible);
		
		resources.add(resource);
		
		System.out.println("Feito!\n");
		
	}

	private static void deleteUser() {
		
		System.out.println("Digite o login do usuário: ");
		int index = searchUser( input.nextLine() );
		
		if(index == -1)
			System.out.println("Usuário não encontrado!");
		else {
			users.remove(index);
			System.out.println("Feito!\n");
		}
		
	}

	private static void createUser() {
		
		User user = new User();
		System.out.println("Digite o nome: ");
		user.setName( input.nextLine() );
		System.out.println("Digite o email: ");
		user.setEmail( input.nextLine() );
		System.out.println("Digite o login: ");
		user.setLogin( input.nextLine() );
		System.out.println("Digite a senha: ");
		user.setPassword( input.nextLine() );
		System.out.println("Digite o tipo: \n"
						 + "1 - Administrador\n"
						 + "2 - Professor\n"
						 + "3 - Pesquisador\n"
						 + "4 - Aluno de doutorado\n"
						 + "5 - Aluno de mestrado\n"
						 + "6 - Aluno de graduação");
		int type = Integer.valueOf( input.nextLine() );
		
		if(type == 1)
			user.setType(TypeUser.ADM);
		else if(type == 2)
			user.setType(TypeUser.PROFESSOR);
		else if(type == 3)
			user.setType(TypeUser.RESEARCHER);
		else if(type == 3)
			user.setType(TypeUser.DOCTORATE);
		else if(type == 3)
			user.setType(TypeUser.MASTERS);
		else
			user.setType(TypeUser.UNDERGRADUATE);
		
		users.add(user);
		
		System.out.println("Feito!\n");
		
	}

	private static void confirmAllocation(ArrayList<Integer> resourcesToConfirm) {
		
		int cod = changeStatus(resourcesToConfirm, Status.IN_PROGRESS);
		id_in_progress.add(cod);
		id_allocated.remove(cod);
		
	}

	private static int searchResource(String id) {
		
		int size = resources.size();
		Resource res;
		for(int i=0; i<size; i++) {
			res = resources.get(i);
			if(res.getId().equals(id)) {
				return i;
			}
		}
		
		return -1;
	}

	private static int searchUser(String userLogin) {
		
		int size = users.size();
		User user;
		for(int i=0; i<size; i++) {
			user = users.get(i);
			if(user.getLogin().equals(userLogin))
				return i;
		}
		
		return -1;
		
	}

	public static void allocateResource() {
		
		Allocation newAlloc = new Allocation();
				
		newAlloc.setStatus(Status.IN_PROCESS);
		newAlloc.setRequester(user_logged);
		
		System.out.println("Digite o id do recurso");
		String id = input.nextLine();
		int index = searchResource(id);
		
		if(index == -1) {
			System.out.println("Recurso não encontrado!\n");
			return;
		}
		
		Resource resource = resources.get(index);
			
		newAlloc.setResource(resource);
		
		System.out.println("Digite a data de início [formato dd/mm/aaaa]: ");
		newAlloc.setDateBegin( new Date( input.nextLine() ) );
			
		System.out.println("Digite a hora de início [formato hh:mm]: ");
		newAlloc.setTimeBegin( new Time( input.nextLine() ) );
			
		System.out.println("Digite a data de término [formato dd/mm/aaaa]: ");
		newAlloc.setDateEnd( new Date( input.nextLine() ) );
			
		System.out.println("Digite a hora de término [formato hh:mm]: ");
		newAlloc.setTimeEnd( new Time( input.nextLine() ) );
			
		Activity activity = new Activity();
		System.out.println("Digite o título da atividade a ser realizada: ");
		activity.setTitle( input.nextLine() );
			
		System.out.println("Digite a descrição da atividade: ");
		activity.setDescription( input.nextLine() );
			
		System.out.println("Digite qual o material que será usado na atividade: ");
		activity.setMaterial( input.nextLine() );
			
		System.out.println("Digite quantos participantes terá a atividade");
		int num_part = Integer.valueOf(input.nextLine() );
			
		String userLogin;
		for(int i=0; i<num_part; i++) {
			System.out.println("Digite o login do "+(i+1)+"º participante");
			userLogin = input.nextLine();
			index = searchUser(userLogin);
			
			if(index == -1)
				System.out.println("Usuário não encontrado\n");
			else {
				activity.addParticipants(users.get(index));
			}
		}
			
		System.out.println("Digite a tipo da atividade: \n"
						 + "1 - Aula tradicional\n"
						 + "2 - Apresentação\n"
						 + "3 - Laboratório");
		int type = Integer.valueOf( input.nextLine() );
			
		if(type == 1 && user_logged.getType() == TypeUser.PROFESSOR) 
			activity.setType(TypeAct.CLASS);
		else if(type == 2) 
			activity.setType(TypeAct.PRESANTATION);
		else if(type == 3 && user_logged.getType() == TypeUser.PROFESSOR)
			activity.setType(TypeAct.LABORATORY);
		
		else {
			System.out.println("Atividade não permitida!\n");
			return;
		}
		
		activities.add(activity);
		newAlloc.setActivity(activity);
		allocations.add(newAlloc);
		index = allocations.size() - 1;
		if(user_logged.getType() == TypeUser.ADM)
			id_allocated.add(index);
		else
			id_alloc_in_process.add(index);
		
		System.out.println("\nFeito!\n");
		
		
	}

}
