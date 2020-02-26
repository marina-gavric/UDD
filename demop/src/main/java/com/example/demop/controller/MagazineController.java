package com.example.demop.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.example.demop.model.*;
import com.example.demop.security.TokenUtils;
import com.example.demop.services.*;
import com.example.demop.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.text.PDFTextStripper;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.impl.form.type.StringFormType;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidationException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demop.model.FormFieldsDTO;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/magazine")
@CrossOrigin(origins = "http://localhost:4200")
public class MagazineController {

	@Autowired
	IdentityService identityService;
	@Autowired
	MLTService mltService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private SearchService searchService;

	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	FormService formService;

	@Autowired
	ScientificAreaServiceImpl areasService;

	@Autowired
	UserServiceImpl userService;
	@Autowired
	LocationService locationService;

	@Autowired
	MagazineService magazineService;

	@Autowired
	private TokenUtils tokenUtils;

	@Qualifier("restHighLevelCient")
	@Autowired
	RestHighLevelClient restClient;

	@GetMapping(path = "/startMagProcess", produces = "application/json")
	public @ResponseBody FormFieldsDTO getData(@Context HttpServletRequest request) {
		//startujemo proces sa id MagazineProcess
		System.out.println("Startovanje magazin procesa");
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("MagazineProcess");
        User us=(User) request.getAttribute("logged");
        if(us!=null){
        	us.setType("maineditor");
        	userService.save(us);
        }
		String username = Utils.getUsernameFromRequest(request, tokenUtils);
		runtimeService.setVariable(pi.getId(), "mainEditor", username);

		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		task.setAssignee("editor");
		taskService.saveTask(task);
		
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		
        return new FormFieldsDTO(task.getId(), pi.getId(), properties);
    }
	@GetMapping(path = "/startTextProcess", produces = "application/json")
	public @ResponseBody FormFieldsDTO getTextData(@Context HttpServletRequest request) {
		//startujemo proces sa id newText
		System.out.println("Startovanje procesa obrade teksta");
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("textProcess");
		String username = Utils.getUsernameFromRequest(request, tokenUtils);
		runtimeService.setVariable(pi.getId(), "author", username);

		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		task.setAssignee("author");
		taskService.saveTask(task);

		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		List<Magazine> allMagazines = magazineService.getAll();
		for(FormField fp : properties){
			if(fp.getId().equals("magazineList")){
				EnumFormType enumType = (EnumFormType) fp.getType();
				enumType.getValues().clear();
				for(Magazine magazine: allMagazines){
					enumType.getValues().put(magazine.getId().toString(), magazine.getTitle());
				}
				break;
			}
		}
		return new FormFieldsDTO(task.getId(), pi.getId(), properties);
	}
	@PostMapping(path = "/create/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity create(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId) {
		System.out.println("Kreiranje magazina kontroler");
		HashMap<String, Object> map = this.mapListToDto(formData);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		for(FormSubmissionDTO item: formData){
			String fieldId = item.getFieldId();
			System.out.println("Id polja je "+fieldId);
			System.out.println("Vrednost polja je "+item.getFieldValue());
			if(!fieldId.equals("areas") && !fieldId.equals("paying")){
				if(item.getFieldValue()==null || item.getFieldValue().equals("") || item.getFieldValue().isEmpty()==true){
					System.out.println("Prazno je");	
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}
			if(fieldId.equals("areas")){
				if(item.getCategories().size()==0){
					System.out.println("Nema naucnih oblasti");	

					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

				}
			}
		
			
		}
		
		try{
			runtimeService.setVariable(processInstanceId, "magazineData", formData);
			formService.submitTaskForm(taskId, map);
	     	
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			System.out.println("Taskovi");
			for(Task t : tasks){
				System.out.println(t.getName());
			}
			    
		}catch(FormFieldValidationException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		System.out.println("Zavrsio");
		return new ResponseEntity<>(HttpStatus.OK);
    }
	@PostMapping(path = "/choose/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity chooseMagazine(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId) {
		System.out.println("Izbog magazina kontroler");
		HashMap<String, Object> map = this.mapListToDto(formData);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		String magId="";
        String usernameLogged = (String)runtimeService.getVariable(processInstanceId, "author");
        boolean payment = true;
        if(!usernameLogged.equals("")){
            User us=userService.findUserByUsername(usernameLogged);
            if(us.isPayed() == false){
                payment=false;
                System.out.println("Nije platio");
            }else{
				System.out.println("Platio je vec");
			}
        }
        runtimeService.setVariable(processInstanceId, "paid",payment);

        for(FormSubmissionDTO item: formData){
			String fieldId = item.getFieldId();
			System.out.println("Id polja je "+fieldId);
			System.out.println("Vrednost polja je "+item.getFieldValue());
			if(fieldId.equals("magazineList")){
				if(item.getFieldValue().equals("")){
					System.out.println("Nema magazina");
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				magId = item.getFieldValue();
			}
		}
		Magazine mag = magazineService.findMagazineById(Long.parseLong(magId));
		System.out.println("Name of mag "+mag.getTitle());
		boolean opena=true;
		if(mag.isOpenaccess()==true){
			System.out.println("Besplatan je ");
		}else{
			opena=false;
			System.out.println("Nije besplatan");
		}
		try{
			System.out.println("Sacuvao varijablu chosen magazine");
			runtimeService.setVariable(processInstanceId, "chosenMagazine", formData);
			runtimeService.setVariable(processInstanceId, "magOA", opena);

			formService.submitTaskForm(taskId, map);

			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();

		}catch(FormFieldValidationException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		System.out.println("Zavrsio biranje magazina");
		return new ResponseEntity<>(payment, HttpStatus.OK);
	}
	@PostMapping(path = "/saveText/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity saveTextData(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId) {
		System.out.println("Izbog text kontroler");
		HashMap<String, Object> map = this.mapListToDto(formData);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		String idMagazine="";
		List<FormSubmissionDTO> magForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processInstanceId, "chosenMagazine");
		for(FormSubmissionDTO item: magForm){
			if(item.getFieldId().equals("magazineList")){
				idMagazine = item.getFieldValue();
				break;
			}
		}
		Magazine magazine = magazineService.findMagazineById(Long.parseLong(idMagazine));
		String mainEditor = magazine.getMainEditor().getUsername();
		String magId="";
		for(FormSubmissionDTO item: formData){
			String fieldId = item.getFieldId();
			System.out.println(fieldId + " "+ item.getFieldValue());
			if(!fieldId.equals("num")){
				if(item.getFieldValue().equals("")){
					System.out.println("Neko polje je prazno");
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}
			}

		try{
			runtimeService.setVariable(processInstanceId, "textData", formData);
			formService.submitTaskForm(taskId, map);
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		}catch(FormFieldValidationException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		List<Task> taskovi = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		for(Task T: taskovi){
			if(T.getName().equals("CheckingTheme")){
				T.setAssignee(mainEditor);
				taskService.saveTask(T);
			}
    	}
		List<Task> tasks2 = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@PostMapping(path = "/addCA/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity addCoAuthor(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId) {
		System.out.println("In Adding co-author");
		HashMap<String, Object> map = this.mapListToDto(formData);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		System.out.println("Instanca proesa je "+ processInstanceId);
		System.out.println("ID taska je "+taskId);
		System.out.println("Naziv taska je "+task.getName());
		String idMagazine="";
		String mail="";
		List<FormSubmissionDTO> magForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processInstanceId, "chosenMagazine");
		for(FormSubmissionDTO item: magForm){
			if(item.getFieldId().equals("magazineList")){
				idMagazine = item.getFieldValue();
				break;
			}
		}
		Magazine magazine = magazineService.findMagazineById(Long.parseLong(idMagazine));
		String mainEditor = magazine.getMainEditor().getUsername();

		for(FormSubmissionDTO item: formData){
			System.out.println("Ispis polja forme");
			System.out.println("Id "+item.getFieldId() + " value "+item.getFieldValue());
			String fieldId = item.getFieldId();
			if(fieldId.equals("mail_c")){
				if(item.getFieldValue().equals("")){
					System.out.println("Neko polje je prazno");
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				mail = item.getFieldValue();

			}
		}
		boolean flagA = false;
		List<User> allUsers = userService.getAll();
		for(User u : allUsers){
			System.out.println("mail "+u.getMail());
			if(u.getMail().equals(mail)){
				System.out.println("Pronadjen");
				flagA=true;
				break;
			}
		}
		try{
			System.out.println("Setujemo varijable");
			runtimeService.setVariable(processInstanceId, "maki", flagA);
			runtimeService.setVariable(processInstanceId, "addCA", formData);
			System.out.println("Varijable setovane ");
			formService.submitTaskForm(taskId, map);
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();

		}catch(FormFieldValidationException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<Task> taskovi = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		for(Task T: taskovi){
			if(T.getName().equals("CheckingTheme")){
				T.setAssignee(mainEditor);
				taskService.saveTask(T);
			}
		}
		List<Task> tasks2 = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		//return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(flagA, HttpStatus.OK);

	}

	@PostMapping(path = "/fillCA/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity fillCoAuthor(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId) {
		System.out.println("In filling co-author");
		HashMap<String, Object> map = this.mapListToDto(formData);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		String idMagazine="";
		List<FormSubmissionDTO> caInfo = (List<FormSubmissionDTO>)runtimeService.getVariable(processInstanceId,"addCA");

		List<FormSubmissionDTO> magForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processInstanceId, "chosenMagazine");
		for(FormSubmissionDTO item: magForm){
			if(item.getFieldId().equals("magazineList")){
				idMagazine = item.getFieldValue();
				break;
			}
		}
		boolean more = false;
		for(FormSubmissionDTO item: caInfo){
			if(item.getFieldId().equals("more")){
				if(item.getFieldValue().equals("true")){
					more = true;
				}
				break;
			}
		}
		Magazine magazine = magazineService.findMagazineById(Long.parseLong(idMagazine));
		String mainEditor = magazine.getMainEditor().getUsername();
		for(FormSubmissionDTO item: formData){
			String fieldId = item.getFieldId();
			if(item.getFieldValue().equals("")){
				System.out.println("Neko polje je prazno");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		try{
			runtimeService.setVariable(processInstanceId, "coData", formData);
			formService.submitTaskForm(taskId, map);
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		}catch(FormFieldValidationException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		List<Task> taskovi = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		for(Task T: taskovi){
			if(T.getName().equals("CheckingTheme")){
				T.setAssignee(mainEditor);
				taskService.saveTask(T);
			}
		}
		List<Task> tasks2 = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		return new ResponseEntity<>(more, HttpStatus.OK);
	}
    @PostMapping(path = "/saveRev/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity saveReviewers(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId) {
        System.out.println("In save reviewers");
        HashMap<String, Object> map = this.mapListToDto(formData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String idMagazine="";
        List<FormSubmissionDTO> textForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processInstanceId, "textData");

        List<FormSubmissionDTO> magForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processInstanceId, "chosenMagazine");
        for(FormSubmissionDTO item: magForm){
            if(item.getFieldId().equals("magazineList")){
                idMagazine = item.getFieldValue();
                break;
            }
        }

        Magazine magazine = magazineService.findMagazineById(Long.parseLong(idMagazine));
        /*for(FormSubmissionDTO item: formData){
            String fieldId = item.getFieldId();
            if(item.getFieldValue().equals("")){
                System.out.println("Neko polje je prazno");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }*/
        System.out.println("U try je");
        try{
            System.out.println("Submit task");
            formService.submitTaskForm(taskId, map);
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        }catch(FormFieldValidationException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>( HttpStatus.OK);
    }
    @PostMapping(path = "/savePay/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity savePayment(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId) {
        System.out.println("In save payment");
        HashMap<String, Object> map = this.mapListToDto(formData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        Boolean paid = true;
        String usernameLogged = (String)runtimeService.getVariable(processInstanceId, "author");

        for(FormSubmissionDTO fs:formData){
			System.out.println("Id je "+fs.getFieldId());
            if(fs.getFieldId().equals("payId")){
				System.out.println("Poklapaju se ");
				System.out.println("Vrednostt je "+fs.getFieldValue());
                if(!fs.getFieldValue().equals("true")){
                    paid=false;
                    System.out.println("Nije platio");
                }
            }
        }
        User u = userService.findUserByUsername(usernameLogged);
        if(u!=null){
            System.out.println("Menja placanje");
            u.setPayed(paid);
            userService.save(u);
        }
        System.out.println("U try je");
        try{
            System.out.println("Submit task");
            runtimeService.setVariable(processInstanceId, "paid", paid);
            formService.submitTaskForm(taskId, map);
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        }catch(FormFieldValidationException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(paid, HttpStatus.OK);
    }

	@PostMapping(path = "/update/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity update(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId) {
		System.out.println("Update magazina kontroler");
		HashMap<String, Object> map = this.mapListToDto(formData);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		/*for(FormSubmissionDTO item: formData){
			String fieldId = item.getFieldId();
			System.out.println("Id polja je "+fieldId);
			System.out.println("Vrednost polja je "+item.getFieldValue());
			
			if(fieldId.equals("editorsl")){
				if(item.getCategories().size()>2){
					System.out.println("Vise od 2 naucne oblasti");	
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

				}
			}
			if(fieldId.equals("reviewersl")){
				if(item.getCategories().size()!=2){
					System.out.println("Nisu 2 recenzenta");	
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}
		
			
		}*/
		
		try{
			runtimeService.setVariable(processInstanceId, "updateData", formData);
			formService.submitTaskForm(taskId, map);
	     	
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			System.out.println("Taskovi");
			for(Task t : tasks){
				System.out.println(t.getName());
			}
			    
		}catch(FormFieldValidationException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		System.out.println("Zavrsio update magazina");
		return new ResponseEntity<>(HttpStatus.OK);
    }

	
	
	private HashMap<String, Object> mapListToDto(List<FormSubmissionDTO> list)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(FormSubmissionDTO temp : list){
			map.put(temp.getFieldId(), temp.getFieldValue());
		}
		
		return map;
	}
	@GetMapping(path = "/getTextForm/{processId}", produces = "application/json")
	public @ResponseBody FormFieldsDTO getTextTask(@PathVariable String processId) {

		System.out.println("Usao u getTextTask "+ processId);
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).list();
		List<TaskDTO> taskDTOList = new ArrayList<TaskDTO>();
		if(tasks.size()==0){
			System.out.println("Prazna lista");
		}
		Task nextTask = tasks.get(0);
		List<ScientificArea> allAreas = areasService.getAll();

		TaskFormData tfd = formService.getTaskFormData(nextTask.getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties){
			if(fp.getId().equals("areas")){
				EnumFormType enumType = (EnumFormType) fp.getType();
				enumType.getValues().clear();
				for(ScientificArea sa: allAreas){
					enumType.getValues().put(sa.getId().toString(), sa.getName());
				}
				break;
			}
		}

		return new FormFieldsDTO(nextTask.getId(), processId, properties);
	}
	@GetMapping(path = "/loadRev/{processId}", produces = "application/json")
	public @ResponseBody ResponseEntity getRevTask(@PathVariable String processId) {

		List<FormSubmissionDTO> textForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processId, "textData");
		List<FormSubmissionDTO> magForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processId, "chosenMagazine");
		String idMagazine="";
		String titleT="";
		String areaId="";
		for(FormSubmissionDTO item: magForm){
			if(item.getFieldId().equals("magazineList")){
				idMagazine = item.getFieldValue();
				break;
			}
		}
		Magazine magazine = magazineService.findMagazineById(Long.parseLong(idMagazine));

		ArrayList<UserDTO> result = new ArrayList<UserDTO>();
		for(User u: magazine.getReviewerMagazine()){
			result.add(new UserDTO(u));
			System.out.println("Dodaje reviewera "+u.getUsername());
		}
		return new ResponseEntity<>(result, HttpStatus.OK);

	}
	@GetMapping(path = "/loadRevScience/{processId}", produces = "application/json")
	public @ResponseBody ResponseEntity getRevByScence(@PathVariable String processId) {

		List<FormSubmissionDTO> textForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processId, "textData");
		List<FormSubmissionDTO> magForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processId, "chosenMagazine");
		String idMagazine="";
		String titleT="";
		String areaId="";
		for(FormSubmissionDTO item: magForm){
			if(item.getFieldId().equals("magazineList")){
				idMagazine = item.getFieldValue();
				break;
			}
		}
		for(FormSubmissionDTO item: textForm){
			if(item.getFieldId().equals("areas")){
				areaId = item.getFieldValue();
				System.out.println("pronadjen "+areaId);
				break;
			}
		}
		Magazine magazine = magazineService.findMagazineById(Long.parseLong(idMagazine));

		ArrayList<UserDTO> result = new ArrayList<UserDTO>();
		for(User u: magazine.getReviewerMagazine()){
			for(ScientificArea sa: u.getInterestedAreas()){
				String id=sa.getId().toString();
				if(id.equals(areaId)){
					result.add(new UserDTO(u));
					System.out.println("Dodaje reviewera "+u.getUsername());
				}
			}
		}
		return new ResponseEntity<>(result, HttpStatus.OK);

	}
	@GetMapping(path = "/loadRevLocation/{processId}", produces = "application/json")
	public @ResponseBody ResponseEntity getRevByLocation(@PathVariable String processId) throws IOException {
		System.out.println("In get reviewers by location");
		ArrayList<UserDTO>result = locationService.geoFilter(processId);
		for(UserDTO user:result){
			System.out.println(" username je "+user.getUsername());
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	@GetMapping(path = "/loadRevMore/{processId}", produces = "application/json")
	public @ResponseBody ResponseEntity getMoreLikeThis(@PathVariable String processId) throws IOException {
		System.out.println("In get reviewers by text more like this");
		ArrayList<UserDTO>result = mltService.moreLikeThisFilter(processId);
		for(UserDTO user:result){
			System.out.println(" username je "+user.getUsername());
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping(path = "/nextTaskMag/{processId}", produces = "application/json")
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
			nextTask.setAssignee("editor");
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
	@PostMapping(path = "/changeText/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity changeTextData(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId, @Context HttpServletRequest request) {
		System.out.println("Change text kontroler");

		HashMap<String, Object> map = this.mapListToDto(formData);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		String username = Utils.getUsernameFromRequest(request, tokenUtils);
		System.out.println("Autor je "+username);
		runtimeService.setVariable(processInstanceId, "author", username);

		String changedFile="";
		for(FormSubmissionDTO item: formData){
			String fieldId = item.getFieldId();
			String fieldVal = item.getFieldValue();
			System.out.println(fieldId + " "+ item.getFieldValue());
			if(fieldId.equals("newPdf")){
				if(item.getFieldValue().equals("")){
					System.out.println("Neko polje je prazno");
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				changedFile = fieldVal;
			}
		}
		String idMagazine="";
		List<FormSubmissionDTO> magForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processInstanceId, "chosenMagazine");
		System.out.println("Ispred forme");
		for(FormSubmissionDTO item: magForm){
			System.out.println("ispis");
			if(item.getFieldId().equals("magazineList")){
				idMagazine = item.getFieldValue();
				System.out.println(idMagazine);
				break;
			}
		}
		Magazine magazine = magazineService.findMagazineById(Long.parseLong(idMagazine));
		if(magazine==null){
			System.out.println("Null magazin");
		}
		String mainEditor = magazine.getMainEditor().getUsername();
		String magId="";

		System.out.println("Maineditor je "+mainEditor);
		runtimeService.setVariable(processInstanceId, "mainEditor", mainEditor);

		try{
			System.out.println("U try");
			runtimeService.setVariable(processInstanceId, "changedFile", changedFile);
			formService.submitTaskForm(taskId, map);
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			System.out.println("Proveravamo taskove ");
			for(Task t: tasks){
				System.out.println(t.getName());
				if(t.getName().equals("CheckingTheme")){
					t.setAssignee(mainEditor);
					System.out.println("postavlja assigne maineditor");
					taskService.saveTask(t);
					break;
				}
			}
		}catch(FormFieldValidationException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}


	@PostMapping(path = "/approveMagazine/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity approveReviewer(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId) {
		System.out.println("Usao u approveMagazine");
		HashMap<String, Object> map = this.mapListToDto(formData);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		boolean approved = false;
		for(FormSubmissionDTO item: formData){
			String fieldId = item.getFieldId();
			System.out.println("Id polja je "+fieldId);
			System.out.println("Vrednost polja je "+item.getFieldValue());
			if(fieldId.equals("correct")){
				if(item.getFieldValue().equals("true")){
					approved = true;
				}
			}
		}
		
		try{
			runtimeService.setVariable(processInstanceId, "approvedMag", approved);
			
			formService.submitTaskForm(taskId, map);
	     	
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			
			    
		}catch(FormFieldValidationException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
    }

	@PostMapping(path = "/approveTheme/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity approveTheme(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId) {
		System.out.println("Usao u approveTheme");
		HashMap<String, Object> map = this.mapListToDto(formData);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String username = task.getAssignee();
		System.out.println("Assigne je "+username);
		String processInstanceId = task.getProcessInstanceId();
		boolean valid = false;
		for(FormSubmissionDTO item: formData){
			String fieldId = item.getFieldId();
			if(fieldId.equals("result")){
				if(item.getFieldValue().equals("true")){
					valid = true;
				}
			}
		}

		try{
			runtimeService.setVariable(processInstanceId, "validTheme", valid);
			runtimeService.setVariable(processInstanceId, "themeData", formData);
			formService.submitTaskForm(taskId, map);
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			System.out.println("Ispisivanje");
			for(Task t: tasks){
				System.out.println(t.getName());
				if(t.getName().equals("CheckFormat")){
					t.setAssignee(username);
					System.out.println("postavlja assigne");
					taskService.saveTask(t);
					break;
				}
			}
		}catch(FormFieldValidationException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}


	@GetMapping(path = "/loadTaskTheme/{taskId}", produces = "application/json")
	public @ResponseBody FormFieldsDTO loadNextTask(@PathVariable String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		TaskFormData tfd = formService.getTaskFormData(taskId);
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		return new FormFieldsDTO(taskId, task.getProcessInstanceId(), properties);
	}
	@PostMapping(path = "/checkFormat/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity checkFormatPdf(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String taskId) {
		System.out.println("Usao u checkFormat of pdf");
		HashMap<String, Object> map = this.mapListToDto(formData);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		boolean approved = false;
		String assignee = task.getAssignee();
		String deadline="";
		for(FormSubmissionDTO item: formData){
			String fieldId = item.getFieldId();
			System.out.println("Id polja je "+fieldId);
			System.out.println("Vrednost polja je "+item.getFieldValue());
			if(fieldId.equals("validPdf")){
				if(item.getFieldValue().equals("true")){
					approved = true;
				}
			}
			if(fieldId.equals("timePdf")){
				deadline = item.getFieldValue();
				System.out.println("Vreme za ispravku je "+deadline);
			}
		}
		try{
			runtimeService.setVariable(processInstanceId, "formatOk", approved);
			runtimeService.setVariable(processInstanceId, "deadline", deadline);
			formService.submitTaskForm(taskId, map);
			String username = (String) runtimeService.getVariable(processInstanceId, "author");
			System.out.println("Autor je "+username);
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			for(Task t: tasks){
				System.out.println(t.getName());
				if(t.getName().equals("ReenterText")){
					t.setAssignee(username);
					System.out.println("postavlja assigne reenter");
					taskService.saveTask(t);
					break;
				}
				if(t.getName().equals("SelectReviewers")){
                    t.setAssignee(assignee);
                    System.out.println("postavlja assigne add rev");
                    taskService.saveTask(t);
                    break;
                }
			}
		}catch(FormFieldValidationException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(approved, HttpStatus.OK);
	}

	@RequestMapping(value="/addFile/", method= RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON)
	public @ResponseBody ResponseEntity uploadFile(@RequestParam("File") MultipartFile fileReq){
		String ret="";
		try{
			ret=saveUpload(fileReq);
		}catch(IOException e){
			e.printStackTrace();
		}
		//ret-apsolutna putanja fajla
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
	public String saveUpload(MultipartFile file) throws IOException{
		String pack = "files/";
		byte[] bytes = file.getBytes();
		Path path = Paths.get(pack+file.getOriginalFilename());
		System.out.println(path.toAbsolutePath());
		Files.write(path,bytes);
		return path.toAbsolutePath().toString();
	}
	@PostMapping(path = "/search", produces = "application/json")
	public @ResponseBody ResponseEntity search(@RequestBody ArrayList<SearchDTO> searchData) throws IOException {
		for(SearchDTO s : searchData){
			System.out.println(s.getField() + " "+s.getType()+" "+s.getOperator()+" "+s.getText());
		}

		ArrayList<TextDTO> searchResult = searchService.search(searchData);
		for(TextDTO t:searchResult){
			System.out.println("Pronadjen");
			System.out.println(t.getTitle());
		}
		return new ResponseEntity<>(searchResult, HttpStatus.OK);
	}
	@PostMapping(path = "/chooseReviewers/{processId}", produces = "application/json")
	public @ResponseBody ResponseEntity chooseReviewers(@RequestBody List<FormSubmissionDTO> formData, @PathVariable String processId) throws IOException {
		System.out.println("Izbog recenzenata kontroler");
		String title ="";
		String idMagazine="";
		Text reviewedText=new Text();
		List<FormSubmissionDTO> textForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processId, "textData");
		List<FormSubmissionDTO> magForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processId, "chosenMagazine");
		for(FormSubmissionDTO item: magForm){
			if(item.getFieldId().equals("magazineList")){
				idMagazine = item.getFieldValue();
				System.out.println("Ima magazin");
				break;
			}
		}
		Magazine magazine = magazineService.findMagazineById(Long.parseLong(idMagazine));
		for(FormSubmissionDTO item: textForm){
			if(item.getFieldId().equals("title")){
				title = item.getFieldValue();
				System.out.println("Naslov rada je "+title);
				break;
			}
		}
		for(Text t:magazine.getTexts()){
			if(t.getTitle().equals(title)){
				reviewedText = t;
				System.out.println("Pronadjen text");
				break;
			}
		}
		for(FormSubmissionDTO item: formData){
			String fieldId = item.getFieldId();
			System.out.println("Id polja je "+fieldId);
			if(fieldId.equals("lista")){
				for(String username:item.getCategories()){
					System.out.println("username je "+username);
					User u = userService.findUserByUsername(username);
					System.out.println("Ime je "+u.getName()+" "+u.getSurname());
					reviewedText.getReviewersText().add(u);
					u.getReveiwedTexts().add(reviewedText);
					userService.save(u);
				}
			}
		}
		String keywords="";
		for(Keyword val : reviewedText.getKeywords()) {
			System.out.println("Dodavanje keyword sa imenom " + val.getName());
			if (keywords.equals("")) {
				keywords += val.getName();
			} else {
				keywords += ", " + val.getName();
			}
		}
		System.out.println("Indeksiranje rada");
		TextUnit textUnit = new TextUnit();
		textUnit.setId(reviewedText.getId());
		String authors = reviewedText.getAuthor().getName()+" "+reviewedText.getAuthor().getSurname();
		for(User u: reviewedText.getCoauthorText()){
			authors+=", "+u.getName()+" "+u.getSurname();
		}
		textUnit.setAuthors(authors);
		textUnit.setMagazine(reviewedText.getMagazine().getTitle());
		textUnit.setTitle(reviewedText.getTitle());
		textUnit.setScientificArea(reviewedText.getScientificArea().getName());
		textUnit.setKeywords(keywords);
		if(magazine.isOpenaccess()){
		    textUnit.setOpenAccess("yes");
        }else{
		    textUnit.setOpenAccess("no");
        }
		//treba izmeniti

		//D:\UDD_GIT\UDD\demop\files\01-intro.pdf
		String [] pom = reviewedText.getPdf().split("\\\\");
		String fileName = pom[5];
		System.out.println("Naziv fajla je "+fileName);
		File file = new File("files/" +fileName);
		String content = getText(file);
		System.out.println("Sadrzaj je "+content);
		textUnit.setContent(content);

		IndexRequest indexRequest = new IndexRequest("index");
		indexRequest.id(Long.toString(reviewedText.getId()));

		ObjectMapper mapper = new ObjectMapper();

		indexRequest.source(mapper.writeValueAsString(textUnit), XContentType.JSON);

		restClient.index(indexRequest, RequestOptions.DEFAULT);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	public String getText(File file) {
		try {
			PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
			parser.parse();
			PDFTextStripper textStripper = new PDFTextStripper();
			String text = textStripper.getText(parser.getPDDocument());
			return text;
		} catch (IOException e) {
			System.out.println("Error getting text");
			System.out.println(e);
		}
		return null;
	}
}
