package br.com.ufal.ui;
import java.util.ArrayList;
import java.util.Scanner;

import br.com.ufal.activity.*;
import br.com.ufal.activity.Class;
import br.com.ufal.allocation.Allocation;
import br.com.ufal.database.Database;
import br.com.ufal.resource.*;
import br.com.ufal.user.*;
import br.com.ufal.util.*;


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
		
		for(Allocation alloc : allocations) 
			System.out.println(alloc.printWithoutResource()+"\n");
			
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
		
		ArrayList<Allocation> allocations = database.getAllocationsByRequester(user);
		
		for(Allocation alloc : allocations) 
			System.out.println(alloc.printWithoutRequester()+"\n");
		
		if(allocations.size() == 0)
			System.out.println("[Sem alocações]\n");
	}

	private void generateReport() {
		
		System.out.println("--------------------RELATÓRIO----------------------\n\n" 
						  + "Número de usuários: "+database.getSizeUsers());
		
		int in_process_size = database.getSizeAllocInProcess();
		int allocated_size = database.getSizeAllocated();
		int in_progress_size = database.getSizeAllocInProgress();
		int alloc_completed = database.getSizeAllocCompleted();
		
		
		System.out.println("\nNúmero de recursos em processo de alocação: "+in_process_size
						  +"\nNúmero de recursos alocados: "+allocated_size
						  +"\nNúmero de recursos 'em andamento': "+in_progress_size
						  +"\nNúmero de recursos 'concluídos': "+alloc_completed
						  +"\nNúmero total de alocações: "+( alloc_completed + in_process_size + in_progress_size + allocated_size )
						  +"\n\nNúmero de atividades por tipo:\n"
						  +"\n- Aulas tradicionais: "+database.getAmountClasses()
						  +"\n- Laboratórios: "+database.getAmountActLaboratories()
						  +"\n- Apresentações: "+database.getAmountPresentations());
		System.out.println("---------------------------------------------------\n");
		
		
	}

	private void deleteResource() {
		
		System.out.println("Digite o id do recurso: ");
		Resource res = database.searchResource( input.nextLine() );
		
		if(res == null)
			System.out.println("Recurso não encontrado!");
		else {
			database.removeResource(res);
			System.out.println("Feito!\n");
		}
		
	}

	private void createResource() {
		
		System.out.println("Digite o tipo: \n"
						 + "1 - Laboratório\n"
						 + "2 - Auditório\n"
						 + "3 - Sala de aula\n"
						 + "4 - Projetor");
		int type = Integer.valueOf( input.nextLine() );
		
		Resource resource;
		if(type == 1)
			resource = new Laboratory();
		else if(type == 2)
			resource = new Auditorium();
		else if(type == 3)
			resource = new Classroom();
		else
			resource = new Projector();
		
		System.out.println("Digite a identificação (id): ");
		resource.setId( input.nextLine() );
		
		User responsible = null;
		boolean unable = true;
		System.out.println("Digite o login do usuário responsável pelo recurso");
		while(responsible == null || unable) {
			responsible = database.searchUser( input.nextLine() );
			
			if(responsible == null)
				System.out.println("Usuário não encontrado! Tente novamente.");
			else {
				if(responsible instanceof Administrator && responsible instanceof Professor && responsible instanceof Researcher ) {
					unable = false;
				} else {
					System.out.println("Somente são permitidos serem responsáveis pelo recurso: um professor, um pesquisador ou um adminstrador do sistema. Tente novamente.");
				}
			}

			
		}
		
		resource.setResponsible(responsible);
		
		database.addResource(resource);
		
		System.out.println("Feito!\n");
		
	}

	private void deleteUser() {
		
		System.out.println("Digite o login do usuário: ");
		User user = database.searchUser( input.nextLine() );
		
		if(user == null)
			System.out.println("Usuário não encontrado!");
		else {
			database.removeUser(user);
			System.out.println("Feito!\n");
		}
		
	}

	private void createUser() {
		
		System.out.println("Digite o tipo: \n"
				 + "1 - Administrador\n"
				 + "2 - Professor\n"
				 + "3 - Pesquisador\n"
				 + "4 - Aluno de doutorado\n"
				 + "5 - Aluno de mestrado\n"
				 + "6 - Aluno de graduação");
		int type = Integer.valueOf( input.nextLine() );
		
		User user;
		if(type == 1)
			user = new Administrator();
		else if(type == 2)
			user = new Professor();
		else if(type == 3)
			user = new Researcher();
		else if(type == 3)
			user = new Doctorate();
		else if(type == 3)
			user = new Masters();
		else
			user = new Undergraduate();
		
		System.out.println("Digite o nome: ");
		user.setName( input.nextLine() );
		System.out.println("Digite o email: ");
		user.setEmail( input.nextLine() );
		System.out.println("Digite o login: ");
		user.setLogin( input.nextLine() );
		System.out.println("Digite a senha: ");
		user.setPassword( input.nextLine() );
		
		database.addUser(user);
		
		System.out.println("Feito!\n");
		
	}

	private void confirmAllocation(ArrayList<Allocation> resourcesToConfirm) {
		
		ArrayList<Allocation> list = database.getAllocated();
		int cod  = selectAllocationToChange(list);
		database.changeToInProgress(list.get(cod));
		
	}

	public void allocateResource() {
		
		Allocation newAlloc = new Allocation();
				
		newAlloc.setRequester(user_logged);
		
		System.out.println("Digite o id do recurso");
		String id = input.nextLine();
		Resource resource = database.searchResource(id);
		
		if(resource == null) {
			System.out.println("Recurso não encontrado!\n");
			return;
		}
			
		newAlloc.setResource(resource);
		
		System.out.println("Digite a data de início [formato dd/mm/aaaa]: ");
		newAlloc.setDateBegin( new Date( input.nextLine() ) );
			
		System.out.println("Digite a hora de início [formato hh:mm]: ");
		newAlloc.setTimeBegin( new Time( input.nextLine() ) );
			
		System.out.println("Digite a data de término [formato dd/mm/aaaa]: ");
		newAlloc.setDateEnd( new Date( input.nextLine() ) );
			
		System.out.println("Digite a hora de término [formato hh:mm]: ");
		newAlloc.setTimeEnd( new Time( input.nextLine() ) );
			
		Activity activity;
		
		System.out.println("Digite a tipo da atividade: \n"
				 + "1 - Aula tradicional\n"
				 + "2 - Apresentação\n"
				 + "3 - Laboratório");
		int type = Integer.valueOf( input.nextLine() );
			
		if(type == 1 && user_logged instanceof Professor) 
			activity = new Class();
		else if(type == 2) 
			activity = new Presentation();
		else if(type == 3 && user_logged instanceof Professor)
			activity = new ActLaboratory();
		else {
			System.out.println("Atividade nao permitida!");
			return;
		}
		
		
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
			User user = database.searchUser(userLogin);
			
			if(user == null){
				System.out.println("Usuário não encontrado\n");
				i--;
			}else {
				activity.addParticipants(user);
			}
		}
		
		database.addActivity(activity);
		newAlloc.setActivity(activity);
		database.addAllocation(newAlloc);
		if(user_logged instanceof Administrator)
			database.changeToAllocated(newAlloc);
		
		System.out.println("\nFeito!\n");
		
		
	}

}

