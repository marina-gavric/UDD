package com.example.demop.services;


import com.example.demop.model.FormSubmissionDTO;
import com.example.demop.model.User;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailCorrectionService implements JavaDelegate {

    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("U slanju notifikacije je");
        String deadline =(String)execution.getVariable("deadline");
        String username =(String)execution.getVariable("author");
        User user = userService.findUserByUsername(username);
        try {
            mailService.sendDeclineNotificationFormat(user, deadline);
        }catch( Exception e ){
            System.out.println("Ima greska");
        }
    }
}
