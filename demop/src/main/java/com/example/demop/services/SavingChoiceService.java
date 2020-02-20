package com.example.demop.services;

import com.example.demop.model.FormSubmissionDTO;
import com.example.demop.model.Magazine;
import com.example.demop.model.ScientificArea;
import com.example.demop.model.Text;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingChoiceService implements JavaDelegate {
    @Autowired
    IdentityService identityService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    ScientificAreaServiceImpl scienceService;

    @Autowired
    MagazineServiceImpl magazineService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    TextService textService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("In SavingChoice Service");
        List<FormSubmissionDTO> userInfo = (List<FormSubmissionDTO>)execution.getVariable("chosenMagazine");

        Magazine newMagazine = new Magazine();
       String idMag = "";
        for(FormSubmissionDTO item: userInfo){
            System.out.println("***************");
            String fieldId = item.getFieldId();
            String fieldValue = item.getFieldValue();
            System.out.println("vrednost je " + fieldValue);
            if(fieldId.equals("magazineList")){
                idMag = fieldValue;
            }
        }
        Long id = Long.parseLong(idMag);
        System.out.println(id);
        if(magazineService==null){
            System.out.println("Null je servis");
        }
        newMagazine = magazineService.findMagazineById(id);
        if(newMagazine == null){
            System.out.println("Magazin je null");
        }
        Text text = new Text();
        text.setMagazine(newMagazine);
        newMagazine.getTexts().add(text);
        System.out.println("Ispred save");
        magazineService.save(newMagazine);
    }
}
