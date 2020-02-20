package com.example.demop.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demop.model.FormSubmissionDTO;

@Service
public class SendingMailService implements JavaDelegate{

	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private MailService mailService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		 List<FormSubmissionDTO> userData = (List<FormSubmissionDTO>)execution.getVariable("registrationData");
		String mail="";
		com.example.demop.model.User newUser = new com.example.demop.model.User();
		 for(FormSubmissionDTO item: userData){
				String fieldId = item.getFieldId();
				if(fieldId.equals("email")){
					newUser.setMail(item.getFieldValue());
				}
				if(fieldId.equals("name")){
					newUser.setName(item.getFieldValue());
				}
				if(fieldId.equals("username")){
					newUser.setUsername(item.getFieldValue());
				}
				
		 }
		System.out.println("Mail je "+mail);
		
		try {
			mailService.sendNotificaitionAsync(newUser,execution.getProcessInstanceId());
		}catch( Exception e ){
			System.out.println("Ima greska");
		}
	}

}
