package com.example.demop.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demop.model.FormSubmissionDTO;

public class CheckingRegistrationService implements JavaDelegate{

	@Autowired
	IdentityService identityService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		 
	      List<FormSubmissionDTO> userData = (List<FormSubmissionDTO>)execution.getVariable("registrationData");
	      System.out.println("U Checkingregistration servisu je");
	      System.out.println(userData);
	      boolean valid = true;
	      for(FormSubmissionDTO item: userData){
				String fieldId = item.getFieldId();
				System.out.println("Id polja je "+fieldId);
				System.out.println("Vrednost polja je "+item.getFieldValue());
				System.out.println("Vrednost kategorija je "+item.getCategories());

				if(fieldId.equals("branches")){
					System.out.println("Naucne oblasti su ");
					ArrayList<String> selectedBranches = item.getCategories();
					for(String nam : selectedBranches){
							System.out.println("bRANCH JE "+nam);
					}
					if(selectedBranches.size()==0){
						System.out.println("Prazno je ");
						valid = false;
					}
				}
				if(!fieldId.equals("title") &&  !fieldId.equals("reviserFlag") && !fieldId.equals("branches")){
					if(item.getFieldValue()==null || item.getFieldValue().equals("") || item.getFieldValue().isEmpty()==true){
						System.out.println("Ima greske");
						valid = false;
					}
				}
			}
	      
	     execution.setVariable("validInput", valid);
	      
	}

}
