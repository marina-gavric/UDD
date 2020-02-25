package com.example.demop.services;

import com.example.demop.model.FormSubmissionDTO;
import com.example.demop.model.Text;
import com.example.demop.model.User;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingCoAuthorService implements JavaDelegate {
    @Autowired
    private UserService userService;
    @Autowired
    MagazineService magazineService;
    @Autowired
    TaskService taskService;
    @Autowired
    TextService textService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Usao u savingCoauthor service");
        List<FormSubmissionDTO> caInfo = (List<FormSubmissionDTO>)execution.getVariable("addCA");
        List<FormSubmissionDTO> textInfo = (List<FormSubmissionDTO>)execution.getVariable("textData");
        List<FormSubmissionDTO> magInfo = (List<FormSubmissionDTO>)execution.getVariable("chosenMagazine");

        String mail="";
        String title="";
        String magId="";
        for(FormSubmissionDTO item: caInfo){
            if(item.getFieldId().equals("mail_c")){
                mail = item.getFieldValue();
                System.out.println("mail");
                break;
            }
        }
        for(FormSubmissionDTO item: textInfo){
            if(item.getFieldId().equals("title")){
                title = item.getFieldValue();
                System.out.println("title");
                break;
            }
        }
        for(FormSubmissionDTO item: magInfo){
            if(item.getFieldId().equals("magazineList")){
                magId = item.getFieldValue();
                System.out.println("magazine");
                break;
            }
        }
        List<Text> allT = textService.getAll();
        System.out.println("Dobijen mail je "+mail);
        User user = null;
        for(User u : userService.getAll()){
            String mailU=u.getMail();
            System.out.println("Mail je "+mailU);
            if(mailU.equals(mail)){
                System.out.println("Pronadjen user ");
                user = u;
                break;
            }
        }

        for(Text t: allT){
            System.out.println("Prolazimo kroz tekstove");
            if(t.getTitle().equals(title) && t.getMagazine().getId() == Long.parseLong(magId)){
                System.out.println("Unutar if-a");
                if(user != null){
                    System.out.println("Ima usera");
                    t.getCoauthorText().add(user);
                    user.getCoauthoredTexts().add(t);
                    userService.save(user);
                    System.out.println("Sacuvan koautor");
                }
            }
        }


    }
}
