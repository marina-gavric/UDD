package com.example.demop.services;

import java.util.ArrayList;
import java.util.List;

import com.example.demop.model.*;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class SavingMagazineService implements JavaDelegate{
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
		System.out.println("Cuvanje magazina");
		 List<FormSubmissionDTO> userInfo = (List<FormSubmissionDTO>)execution.getVariable("magazineData");
		 String usernameEditor = (String)execution.getVariable("mainEditor");
		 User mainEditor = userService.findUserByUsername(usernameEditor);

		 System.out.println(userInfo);
		 System.out.println(userInfo.toString()); 
		 Magazine newMagazine = new Magazine();
		 for(FormSubmissionDTO item: userInfo){
			  System.out.println("***************");
			  String fieldId = item.getFieldId();
			  String fieldValue = item.getFieldValue();
			  if(fieldId.equals("title")){
				  newMagazine.setTitle(fieldValue);
			  }
			  if(fieldId.equals("issn")){
				  newMagazine.setIssn(fieldValue);
			  }
			  if(fieldId.equals("paying")){
				  newMagazine.setOpenaccess(false);
				  if(fieldValue.equals("true")){
					  newMagazine.setOpenaccess(true);
				  }
			  }
			  if(fieldId.equals("areas")){
				  List<ScientificArea> allAreas = scienceService.getAll();
				  for(ScientificArea area : allAreas){
					  for(String selectedArea:item.getCategories()){

						  System.out.println("Area sa id je "+selectedArea);
						  String idS=area.getId().toString();
						  if(idS.equals(selectedArea)){
							  System.out.println("Pronadjena oblast");
							  System.out.println(area.getName());
							  newMagazine.getMagazineAreas().add(area);
							  
						  }
					  }
				  }

			  }

		 }
		 newMagazine.setMainEditor(mainEditor);
		 magazineService.save(newMagazine);
	}
	}
