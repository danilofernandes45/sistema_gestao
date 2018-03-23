package br.com.ufal.database;

import java.util.ArrayList;

import br.com.ufal.activity.Activity;
import br.com.ufal.allocation.Allocation;
import br.com.ufal.resource.Resource;
import br.com.ufal.user.User;
import br.com.ufal.util.Status;

public class Database {

	private ArrayList<User> users;
	private ArrayList<Resource> resources;
	private ArrayList<Activity> activities;
	
	private ArrayList<Allocation> alloc_in_process;
	private ArrayList<Allocation> allocated;
	private ArrayList<Allocation> alloc_in_progress;
	private ArrayList<Allocation> alloc_completed;
	
	private static Database database;
	
	private Database() {
		
		users = new ArrayList<>();
		resources = new ArrayList<>();
		activities = new ArrayList<>();
		alloc_in_process = new ArrayList<>();
		allocated = new ArrayList<>();
		alloc_in_progress = new ArrayList<>();
		alloc_completed = new ArrayList<>();
		
	}
	
	public static Database getInstance() {
		
		if( database == null ) {
			database = new Database();
		}
		
		return database;
	}
	
	public ArrayList<Allocation> getAllocationsToConfirm(User user) {
		
		ArrayList<Allocation> allocToConfirm = new ArrayList<>();
		
		for(Allocation alloc : alloc_in_process) {
			
			Resource res = alloc.getResource();
			if(user.equals( res.getResponsible() ))
				allocToConfirm.add(alloc);
			
		}
		
		return allocToConfirm;
		
	}
	
	public User searchUser(String login) {
		
		for(User user : users) {
			if(login.equals( user.getLogin() ))
				return user;
		}
		
		return null;
	}
	
	public Resource searchResource(String id) {
		
		for(Resource res : resources) {
			if(id.equals( res.getId() ))
				return res;
		}
		
		return null;
	}
	
	public ArrayList<Allocation> getAllocationsByResource(Resource res) {
		
		ArrayList<Allocation> allocations = new ArrayList<>();
		
		for(Allocation alloc : alloc_in_process) {
			if(res.equals( alloc.getResource() ))
				allocations.add(alloc);
		}
		
		for(Allocation alloc : allocated) {
			if(res.equals( alloc.getResource() ))
				allocations.add(alloc);
		}
		
		for(Allocation alloc : alloc_in_progress) {
			if(res.equals( alloc.getResource() ))
				allocations.add(alloc);
		}
		
		for(Allocation alloc : alloc_completed) {
			if(res.equals( alloc.getResource() ))
				allocations.add(alloc);
		}
		
		return allocations;
		
	}
	
	public void addUser(User user) {
		users.add(user);
	}
	
	public void addResource(Resource res) {
		resources.add(res);
	}
	
	public void addActivity(Activity act) {
		activities.add(act);
	}
	
	public void addAllocation(Allocation alloc) {
		alloc.setStatus(Status.IN_PROCESS);
		alloc_in_process.add(alloc);
	}
	
	public void changeToAllocated(Allocation alloc) {
		alloc.setStatus(Status.ALLOCATED);
		alloc_in_process.remove(alloc);
		allocated.add(alloc);
	}
	
	public void changeToInProgress(Allocation alloc) {
		alloc.setStatus(Status.IN_PROGRESS);
		allocated.remove(alloc);
		alloc_in_progress.add(alloc);
	}
	
	public void changeToCompleted(Allocation alloc) {
		alloc.setStatus(Status.COMPLETED);
		alloc_in_progress.remove(alloc);
		alloc_completed.add(alloc);
	}

	public boolean hasProcessAllocToConfirm() {
		
		if(alloc_in_process.size() > 0)
			return true;

		return false;
	}
	
	public boolean hasProcessAllocToConclude() {
		
		if(alloc_in_progress.size() > 0)
			return true;

		return false;
	}

	public ArrayList<Allocation> getAllocInProcess() {
		return alloc_in_process;
	}

	public ArrayList<Allocation> getAllocated() {
		return allocated;
	}

	public ArrayList<Allocation> getAllocInProgress() {
		return alloc_in_progress;
	}

	public ArrayList<Allocation> getAllocCompleted() {
		return alloc_completed;
	}

	
}
