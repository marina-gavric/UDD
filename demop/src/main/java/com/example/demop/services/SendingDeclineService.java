package com.example.demop.services;

import com.example.demop.model.FormSubmissionDTO;
import com.example.demop.model.Text;
import com.example.demop.model.User;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendingDeclineService implements JavaDelegate {
    @Autowired
    private IdentityService identityService;

    @Autowired
    private MailService mailService;

    @Autowired
    private TextService textService;
    @Autowired
    private MagazineService magazineService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> magData = (List<FormSubmissionDTO>)execution.getVariable("chosenMagazine");
        List<FormSubmissionDTO> textData = (List<FormSubmissionDTO>)execution.getVariable("textData");

        String mail="";
        String idMag="";
        com.example.demop.model.User newUser = new com.example.demop.model.User();
        for(FormSubmissionDTO item: magData){
            String fieldId = item.getFieldId();
            if(fieldId.equals("magazineList")){
                idMag = item.getFieldValue();
                System.out.println("Magazin je id " + idMag);
            }
        }
        String title="";
        for(FormSubmissionDTO item: textData){
            String fieldId = item.getFieldId();
            if(fieldId.equals("title")){
                title = item.getFieldValue();
                System.out.println("Naslov je "+title);
            }
        }
        List<Text> allTexts = textService.getAll();
        User user = new User();
        for(Text t:allTexts){
            if(t.getTitle().equals(title)){
                System.out.println("Pronadjen naslov");
                if(t.getMagazine().getId() == Long.parseLong(idMag)){
                    System.out.println("Pronadjen autor "+t.getAuthor().getUsername());
                    user = t.getAuthor();
                }
            }
        }

        try {
            mailService.sendDeclineNotificationAuthor(user, title);
        }catch( Exception e ){
            System.out.println("Ima greska");
        }
    }
}
