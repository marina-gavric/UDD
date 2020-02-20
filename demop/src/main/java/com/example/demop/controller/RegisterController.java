package com.example.demop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidationException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demop.model.FormFieldsDTO;
import com.example.demop.model.FormSubmissionDTO;
import com.example.demop.model.TaskDTO;
import com.example.demop.model.User;
import com.example.demop.services.UserService;

@Controller
@RequestMapping("/register")

@CrossOrigin(origins = "http://localhost:4200")
public class RegisterController {
	@Autowired
	IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	FormService formService;
	
	@Autowired
	UserService userService;

	@GetMapping(path = "/startProcess", produces = "application/json")
    public @ResponseBody FormFieldsDTO getData() {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		//startujemo proces sa id RegProces
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("RegProcess");
        
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		task.setAssignee("user");
		taskService.saveTask(task);
		
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		
        return new FormFieldsDTO(task.getId(), pi.getId(), properties);
    }	
	@GetMapping(path = "/nextTask/{processId}", produces = "application/json")
    public @ResponseBody FormFieldsDTO getNextTask(@PathVariable String processId) {

		System.out.println("Usao u get nexxt task "+ processId);
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).list();
		List<TaskDTO> taskDTOList = new ArrayList<TaskDTO>();
		if(tasks.size()==0){
			System.out.println("Prazna lista");
		}
		for(Task T: tasks){
			System.out.println("Dodaje task "+T.getName());
			taskDTOList.add(new TaskDTO(T.getId(), T.getName(), T.getAssignee()));
		}
		
		Task nextTask = tasks.get(0);
		if(nextTask.getAssignee()==null || !nextTask.getAssignee().equals("demo")){
			nextTask.setAssignee("user");
			taskService.saveTask(nextTask);
		}
		System.out.println("Assigne je "+nextTask.getAssignee());
		TaskFormData tfd = formService.getTaskFormData(nextTask.getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		
        return new FormFieldsDTO(nextTask.getId(), processId, properties);
    }
	//funkcija koju listu koja ima clan (id:surname,value:gavric) pretvara
	//u mapu koja izgleda (id:value)->(surname:gavric)
	private HashMap<String, Object> mapListToDto(List<FormSubmissionDTO> list)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(FormSubmissionDTO temp : list){
			map.put(temp.getFieldId(), temp.getFieldValue());
		}
		
		return map;
	}
	@GetMapping(path = "/getTasksUser/{username}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDTO>> get(@PathVariable String username) {
		System.out.println("Usao u getTasksUser");
		System.out.println(username);
		User user = userService.findUserByUsername(username);
		List<TaskDTO> dtos = new ArrayList<TaskDTO>();
		List<User> allUsers = userService.getAll();
		if(user==null){
			System.out.println("Null je ");
		}
        for(User u : allUsers){
        	System.out.println("Username je "+u.getUsername());
        	if(u.getUsername().equals(username)){
        		user=u;
        		System.out.println("Pronadjen usesr");
        	}
        }
		if(user!=null){
			System.out.println("User nije null");
		}
		System.out.println("User nije null");
		List<Task> tasks = new ArrayList<Task>();
		if(user.getType().equals("admin")){
			System.out.println("User je admin");
			tasks.addAll(taskService.createTaskQuery().processDefinitionKey("RegProcess").taskAssignee("demo").list());
			tasks.addAll(taskService.createTaskQuery().processDefinitionKey("MagazineProcess").taskAssignee("demo").list());
			tasks.addAll(taskService.createTaskQuery().processDefinitionKey("textProcess").taskAssignee("demo").list());
		}else{
			System.out.println("User nije admin");
			tasks.addAll(taskService.createTaskQuery().processDefinitionKey("RegProcess").taskAssignee(username).list());
			tasks.addAll(taskService.createTaskQuery().processDefinitionKey("MagazineProcess").taskAssignee(username).list());
			tasks.addAll(taskService.createTaskQuery().processDefinitionKey("textProcess").taskAssignee(username).list());
		}
		for (Task task : tasks) {
			System.out.println("Dodat task sa imenom "+task.getName() + " a assigne "+task.getAssignee());
			TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
			dtos.add(t);
		}
		
        return new ResponseEntity(dtos,  HttpStatus.OK);
    }
	
	@PostMapping(path = "/post/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity post(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDto(formData);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		for(FormSubmissionDTO item: formData){
			String fieldId = item.getFieldId();
			System.out.println("Id polja je "+fieldId);
			System.out.println("Vrednost polja je "+item.getFieldValue());
			if(!fieldId.equals("title") &&  !fieldId.equals("reviserFlag") && !fieldId.equals("branches")){
				if(item.getFieldValue()==null || item.getFieldValue().equals("") || item.getFieldValue().isEmpty()==true){
					System.out.println("Ima greske");	
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}
		}
		
		try{
			runtimeService.setVariable(processInstanceId, "registrationData", formData);
			formService.submitTaskForm(taskId, map);
	     	
			
			    
		}catch(FormFieldValidationException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		System.out.println("Zavrsio");
		return new ResponseEntity<>(HttpStatus.OK);
    }
	@PostMapping(value="/activateAccount/{taskId}/username/{username}",
			produces ="application/json")
	public @ResponseBody ResponseEntity activateUser(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId,@PathVariable String username){
	System.out.println("U activeUser controler username je "+username);
	HashMap<String, Object> map = this.mapListToDto(formData);
	Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
	String processInstanceId = task.getProcessInstanceId();
	boolean activated = false;
	for(FormSubmissionDTO item: formData){
		String fieldId = item.getFieldId();
		System.out.println("Id polja je "+fieldId);
		System.out.println("Vrednost polja je "+item.getFieldValue());
		if(fieldId.equals("activeUser")){
			if(item.getFieldValue().equals("true")){
				activated = true;
				System.out.println("Aktivan je");
			}
		}
	}

	List<User> allUsers = userService.getAll();
	User user = userService.findUserByUsername(username);
	
	user.setActive(activated);
    userService.save(user);
    try{
		runtimeService.setVariable(processInstanceId, "activeFlag", activated);
		
		formService.submitTaskForm(taskId, map);
     	
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		
		    
	}catch(FormFieldValidationException e){
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	return new ResponseEntity<>(HttpStatus.OK);

	}

	@PostMapping(path = "/approveReviewer/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity approveReviewer(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId) {
		System.out.println("Usao u approveReviewer");
		HashMap<String, Object> map = this.mapListToDto(formData);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		boolean approved = false;
		for(FormSubmissionDTO item: formData){
			String fieldId = item.getFieldId();
			System.out.println("Id polja je "+fieldId);
			System.out.println("Vrednost polja je "+item.getFieldValue());
			if(fieldId.equals("approve")){
				if(item.getFieldValue().equals("true")){
					approved = true;
				}
			}
		}
		
		try{
			runtimeService.setVariable(processInstanceId, "approvedFlag", approved);
			
			formService.submitTaskForm(taskId, map);
	     	
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			
			    
		}catch(FormFieldValidationException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
    }

	@GetMapping(path = "/loadTask/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDTO loadNextTask(@PathVariable String taskId) {
		System.out.println("Usao u load task");
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		System.out.println("Naziv taska "+task.getName());

		TaskFormData tfd = formService.getTaskFormData(taskId);
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
        return new FormFieldsDTO(taskId, task.getProcessInstanceId(), properties);
    }

}
