package com.example.demop.services;
import com.example.demop.model.*;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Access;
import java.util.List;

@Service
public class SavingTextService implements JavaDelegate {

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
        System.out.println("In SavingText Service");
        List<FormSubmissionDTO> magInfo = (List<FormSubmissionDTO>)execution.getVariable("chosenMagazine");
        List<FormSubmissionDTO> textInfo = (List<FormSubmissionDTO>)execution.getVariable("textData");
        String username =(String)execution.getVariable("author");
        User author = userService.findUserByUsername(username);

        Magazine newMagazine = new Magazine();
        String idMag = "";
        for(FormSubmissionDTO item: magInfo){
            if(item.getFieldId().equals("magazineList")){
                idMag = item.getFieldValue();
                break;
            }
        }
        Long id = Long.parseLong(idMag);
        System.out.println(id);
         newMagazine = magazineService.findMagazineById(id);
        if(newMagazine == null){
            System.out.println("Magazin je null");
        }
        Text text = new Text();
        for(FormSubmissionDTO item: textInfo){
            System.out.println("VALUE: "+ item.getFieldValue());
            System.out.println("ID "+ item.getFieldId());
            if(item.getFieldId().equals("title")){
                text.setTitle(item.getFieldValue());
            }
            if(item.getFieldId().equals("summary")){
                text.setSummary(item.getFieldValue());
            }
            if(item.getFieldId().equals("file")){
                text.setPdf(item.getFieldValue());
            }
            if(item.getFieldId().equals("keywords")){
                String[] arrayKW = item.getFieldValue().split(",");
                for(String val : arrayKW){
                    System.out.println("Dodavanje keyword sa imenom "+val.trim());
                    val = val.trim();
                    Keyword kw = new Keyword();
                    kw.setName(val);
                    kw.setText(text);
                    text.getKeywords().add(kw);
                }
            }
            if(item.getFieldId().equals("summary")){
                text.setSummary(item.getFieldValue());
            }
            if(item.getFieldId().equals("areas")){
                String idArea = item.getFieldValue();
                ScientificArea area = scientificAreaService.findOneById(Long.parseLong(idArea));
                text.setScientificArea(area);
            }

        }
        text.setMagazine(newMagazine);
        text.setAuthor(author);
        //newMagazine.getTexts().add(text);
        System.out.println("Ispred save");
        textService.save(text);
    }
    }

