package com.example.demop.services;

import com.example.demop.model.FormSubmissionDTO;
import com.example.demop.model.User;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendingMailEditorService implements JavaDelegate {


    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

    System.out.println("U sending maileditor je");
   /* String username =(String)execution.getVariable("editor");
    System.out.println(username);
    User user = userService.findUserByUsername(username);
    System.out.println("Pronadjen user sa "+user.getName());
    try {
        mailService.sendNotificationScience(user);
    }catch( Exception e ){
        System.out.println("Ima greska");
    }
    */}

}
