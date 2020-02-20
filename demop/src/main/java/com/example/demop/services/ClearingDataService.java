package com.example.demop.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demop.model.FormSubmissionDTO;

@Service
public class ClearingDataService implements JavaDelegate{

	@Autowired
	private IdentityService identityService;

	@Autowired
	private UserService userService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Usao u clearingData");
		 List<FormSubmissionDTO> userData = (List<FormSubmissionDTO>)execution.getVariable("registrationData");
		 String username="";
		
		 for(FormSubmissionDTO item: userData){
			  String fieldId = item.getFieldId();
			  String fieldValue = item.getFieldValue();
			  
			  if(fieldId.equals("username")){
				  System.out.println("Username--> "+fieldValue);
				  username=fieldValue;
			  }
		  }
		 System.out.println("Username je " +username );
		 com.example.demop.model.User inactiveUser= userService.findUserByUsername(username);
		 if(inactiveUser != null) {
				System.out.println("Deleting user");
				userService.deleteUser(inactiveUser);
				identityService.deleteUser(username);			
		 }
		 
		 	
	}

}
