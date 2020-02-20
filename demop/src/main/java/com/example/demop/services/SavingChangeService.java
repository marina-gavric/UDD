package com.example.demop.services;

import com.example.demop.model.*;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingChangeService implements JavaDelegate {
    @Autowired
    TextService textService;
    @Autowired
    MagazineService magazineService;
    @Autowired
    UserService userService;
    @Autowired
    ScientificAreaServiceImpl scientificAreaService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("In SavingChange Service");
        List<FormSubmissionDTO> magInfo = (List<FormSubmissionDTO>)execution.getVariable("chosenMagazine");
        List<FormSubmissionDTO> textInfo = (List<FormSubmissionDTO>)execution.getVariable("textData");
        String newFile = (String)execution.getVariable("changedFile");
        execution.setVariable("file", newFile);

        String idMag = "";
        for(FormSubmissionDTO item: magInfo){
            if(item.getFieldId().equals("magazineList")){
                idMag = item.getFieldValue();
                break;
            }
        }
        Long id = Long.parseLong(idMag);
        System.out.println("Id magazina je "+id);
        Text text = new Text();
        String title="";
            for(FormSubmissionDTO item: textInfo){
            System.out.println("VALUE: "+ item.getFieldValue());
            System.out.println("ID "+ item.getFieldId());
            if(item.getFieldId().equals("title")){
              title = item.getFieldValue();
            }
            if(item.getFieldId().equals("file")){
                    System.out.println("Setovao novi fajl");
                    item.setFieldValue(newFile);
            }
        }
        List<Text> allTexts=textService.getAll();
            for(Text t:allTexts){
                String idTMag = t.getMagazine().getId().toString();
                if(idTMag.equals(idMag)){
                    if(t.getTitle().equals(title)){
                        System.out.println("Menjamo informacije");
                        t.setPdf(newFile);
                        textService.save(t);
                        break;
                    }
                }
            }
       }
}
