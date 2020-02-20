package com.example.demop.services;

import com.example.demop.model.FormSubmissionDTO;
import com.example.demop.model.Magazine;
import com.example.demop.model.User;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendingNotificationService  implements JavaDelegate {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("U slanju notifikacije je");
        String username =(String)execution.getVariable("author");
        User user = userService.findUserByUsername(username);

        String editor = "";
        List<FormSubmissionDTO> magInfo = (List<FormSubmissionDTO>)execution.getVariable("chosenMagazine");
        String idMag = "";
        for(FormSubmissionDTO item: magInfo) {
            String fieldId = item.getFieldId();
            String fieldValue = item.getFieldValue();
            if (fieldId.equals("magazineList")) {
                idMag = fieldValue;
            }
        }
        try {
            mailService.sendNotificationAuthor(user);
        }catch( Exception e ){
            System.out.println("Ima greska");
        }
        try {
            mailService.sendNotificationEditor(idMag);
        }catch( Exception e ){
            System.out.println("Ima greska");
        }
    }

}
