package com.example.demop.services;

import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demop.model.FormSubmissionDTO;
import com.example.demop.model.Magazine;
import com.example.demop.model.ScientificArea;
import com.example.demop.model.User;

@Service
public class SavingUpdateService implements JavaDelegate{
	@Autowired
	IdentityService identityService;
	
	@Autowired
	UserServiceImpl userService;
	
	@Autowired
	ScientificAreaServiceImpl scienceService;
	@Autowired
	MagazineService magazineService;
	@Autowired
	TaskService taskService;
	
	@Autowired
	FormService formService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Saving Update Service");
		 List<FormSubmissionDTO> magInfo = (List<FormSubmissionDTO>)execution.getVariable("magazineData");
		 List<FormSubmissionDTO> updateInfo = (List<FormSubmissionDTO>)execution.getVariable("updateData");
		 String issn="";
		 for(FormSubmissionDTO item: magInfo){
			 if(item.getFieldId().equals("issn")){
				 issn = item.getFieldValue();
				 break;
			 }
		 }
		 
		 Magazine magazine = new Magazine();
		  magazine = magazineService.findMagazineByIssn(issn);
		  for(FormSubmissionDTO item: updateInfo){
			  String fieldId=item.getFieldId();
			  
			 if(fieldId.equals("editorsl")){
				  System.out.println("Editori su su");
				  System.out.println(item.getCategories().size());
				  List<User> allUsers = userService.getAll();
				  for(User u : allUsers){
					  for(String selectedEd:item.getCategories()){
	
						  System.out.println("Editor sa id je "+selectedEd);
						  String idS=u.getId().toString();
						  if(idS.equals(selectedEd)){
							  System.out.println("Pronadjen editor");
							  System.out.println(u.getName());
							  magazine.getEditorMagazine().add(u);
							  
						  }
					  }
				  }
			 }
			 if(fieldId.equals("reviewersl")){
				  System.out.println("Recenzenti su su");
				  System.out.println(item.getCategories().size());
				  List<User> allUsers = userService.getAll();
				  for(User u : allUsers){
					  for(String selectedEd:item.getCategories()){
	
						  System.out.println("Recenzent sa id je "+selectedEd);
						  String idS=u.getId().toString();
						  if(idS.equals(selectedEd)){
							  System.out.println("Pronadjen recenzent");
							  System.out.println(u.getName());
							  magazine.getReviewerMagazine().add(u);
							  
						  }
					  }
				  }
			 }
			}
		 magazineService.save(magazine);
	}
}
