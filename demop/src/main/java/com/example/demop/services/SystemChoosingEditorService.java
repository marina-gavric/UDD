package com.example.demop.services;

import com.example.demop.model.FormSubmissionDTO;
import com.example.demop.model.User;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SystemChoosingEditorService implements JavaDelegate {
    @Autowired
    TextService textService;
    @Autowired
    MagazineService magazineService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ScientificAreaServiceImpl scientificAreaService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("In systemchoosing service");
        List<FormSubmissionDTO> textInfo = (List<FormSubmissionDTO>)execution.getVariable("textData");
        List<FormSubmissionDTO> magInfo = (List<FormSubmissionDTO>)execution.getVariable("chosenMagazine");

        String idMag = "";
        for(FormSubmissionDTO item: magInfo){
            if(item.getFieldId().equals("magazineList")){
                idMag = item.getFieldValue();
                break;
            }
        }
        String science="";
        for(FormSubmissionDTO fs : textInfo){
            System.out.println("id: "+fs.getFieldId()+", value: "+fs.getFieldValue());
            if(fs.getFieldId().equals("areas")){
                science = fs.getFieldValue();
                break;
            }
        }
        List<User> editors = userService.getEditorByScience(science, idMag);

        String chosenEditor = "";
        if(editors.size()!=0){
            System.out.println("Ima editora");
            chosenEditor = editors.get(0).getUsername();
        }else{
            chosenEditor = (String)execution.getVariable("mainEditor");
        }
        System.out.println("Zavrseno cuvanje");
        execution.setVariable("scienceEditor", chosenEditor);
    }
}
