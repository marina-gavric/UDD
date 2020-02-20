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
        List<FormSubmissionDTO> caInfo = (List<FormSubmissionDTO>)execution.getVariable("addCA");
        List<FormSubmissionDTO> textInfo = (List<FormSubmissionDTO>)execution.getVariable("textData");
        List<FormSubmissionDTO> magInfo = (List<FormSubmissionDTO>)execution.getVariable("chosenMagazine");

        String mail="";
        String title="";
        String magId="";
        for(FormSubmissionDTO item: caInfo){
            if(item.getFieldId().equals("mailC")){
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
        User user = userService.findUserByMail(mail);

        for(Text t: allT){
            if(t.getTitle().equals(title) && t.getMagazine().getId() == Long.parseLong(magId)){
                System.out.println("Unutar if-a");
                if(user != null){
                    user.getCoauthoredTexts().add(t);
                    userService.save(user);
                    System.out.println("Sacuvan koautor");
                }
            }
        }


    }
}
