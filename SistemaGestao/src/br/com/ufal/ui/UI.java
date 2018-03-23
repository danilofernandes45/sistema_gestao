package br.com.ufal.ui;
import java.util.ArrayList;
import java.util.Scanner;

import br.com.ufal.activity.Activity;
import br.com.ufal.allocation.Allocation;
import br.com.ufal.database.Database;
import br.com.ufal.resource.Auditorium;
import br.com.ufal.resource.Laboratory;
import br.com.ufal.resource.Resource;
import br.com.ufal.user.Administrator;
import br.com.ufal.user.Professor;
import br.com.ufal.user.Researcher;
import br.com.ufal.user.User;
import br.com.ufal.util.Date;
import br.com.ufal.util.Status;
import br.com.ufal.util.Time;


public class UI {
	
	private User user_logged;
	private Scanner input;
	private Database database;
	
	public void initialize() {
	
		input = new Scanner(System.in);
		database = Database.getInstance();
		
		User user = new Administrator("ADM", "adm@ic.ufal.br", "adm", "adm");
		database.addUser(user);
		
		Resource resource = new Auditorium("aud1", user); // Responsible = ADM
		database.addResource(resource);
		
		user = new Professor("Baldoino", "baldoino@ic.ufal.com", "baldoino", "b123");
		database.addUser(user);
		user = new Researcher("João", "joao@ic.ufal.com", "joao", "j123");
		database.addUser(user);
		
		resource = new Laboratory("lab1", user); // Responsible = Joao
		database.addResource(resource);
		
		login();
	
	}
	
	private void login() {
		
		String login, passwd;
		User user = null;
		
		while( true ) { 
			System.out.print("Login: ");
			login = input.nextLine();
			System.out.print("Password: ");
			passwd = input.nextLine();
			
			user = database.searchUser(login);
			
			if(user != null && passwd.equals(user.getPassword())) {
				user_logged = user;
				displayMenu();
			} else {
				System.out.println("\nLogin ou senha incorretos\n");
			}
			
		}
		
	}
	
	private void displayMenu() {
		
		ArrayList<Allocation> allocToConfirm = database.getAllocationsToConfirm(user_logged);
		boolean isResponsible = false;
		
		if(allocToConfirm.size() > 0)
			isResponsible = true;
		
		int option;
		do {
			
			System.out.println("Menu:\n"
					+ "1 - Solicitar alocação de recurso\n"
					+ "2 - Consultar por usuário\n"
					+ "3 - Consultar por recurso");
			
			if(isResponsible)
				System.out.println("4 - Confirmar alocação de recurso");
				
			
			if(user_logged instanceof Administrator) {
				
				if( database.hasProcessAllocToConfirm() )
					System.out.println("5 - Confirmar processo de alocação");
				if( database.hasProcessAllocToConclude() )
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
				
				confirmAllocation(allocToConfirm);
				allocToConfirm = database.getAllocationsToConfirm(user_logged);
				if(allocToConfirm.size() == 0)
					isResponsible = false;
			
			}
			else if ( user_logged instanceof Administrator ) {
				if(option == 5 && database.hasProcessAllocToConfirm()) {
					
					confirmProcess();
					allocToConfirm = database.getAllocationsToConfirm(user_logged);
					if(allocToConfirm.size() > 0)
						isResponsible = true;
					
				}
				else if(option == 6 && database.hasProcessAllocToConclude())
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
	
	private void concludeProcess() {
		
		ArrayList<Allocation> list = database.getAllocInProgress();
		int cod  = selectAllocationToChange(list);
		database.changeToCompleted(list.get(cod));
	}

	private void confirmProcess() {
		
		ArrayList<Allocation> list = database.getAllocInProcess();
		int cod  = selectAllocationToChange(list);
		database.changeToAllocated(list.get(cod));
	}
	
	private int selectAllocationToChange(ArrayList<Allocation> list) {
		System.out.println("Alocações de recursos pendentes: \n\n"
						 + "Cod - Id do recurso - Nome do usuário");
		System.out.println("--------------------------------------");
		Allocation alloc;
		for(int i=0; i<list.size(); i++) {
			alloc = list.get(i);
			System.out.println(i+" - "+alloc.getResource().getId()+" - "+alloc.getRequester().getName());
		}
		
		System.out.println("\nDigite o código da alocação (Cod)");
		int cod = Integer.valueOf( input.nextLine() );
		
		return cod;
		
	}

	private void showResource() {
		System.out.println("Digite o id do Recurso: ");
		Resource resource = database.searchResource( input.nextLine() );
		if( resource == null ) {
			System.out.println("Recurso não encontrado");
			return;
		}
		
		System.out.println(resource);
		
		System.out.println("Alocações:\n");
		
		ArrayList<Allocation> allocations = database.getAllocationsByResource(resource);
		
		for(Allocation alloc : allocations) {	//MUDAR AQUI!!!!!!!!
			if(alloc.getResource().getId().equals(resource.getId())) {
				
				System.out.println("Usuário: "+alloc.getRequester().getName()+"\n"
								 + "Atividade: "+alloc.getActivity().getTitle()+"\n");
				
				printStatus(alloc);
				hasAllocations = true;
					
			}
		}
		if(allocations.size() == 0)
			System.out.println("[Sem alocações]\n");
			
		
	}

	private void showUser() {
		
		System.out.println("Digite o login do usuário: \n");
		User user = database.searchUser( input.nextLine() );
		if(user == null) {
			System.out.println("Usuário não encontrado\n");
			return;
		}
		
		System.out.println(user);
		
		System.out.println("Alocações:\n");
		boolean hasAllocations = false;
		
		ArrayList<Allocation> allocations = database.getAllocationsByRequester(user);
		
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

	private void confirmAllocation(ArrayList<Integer> resourcesToConfirm) {
		
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


}
