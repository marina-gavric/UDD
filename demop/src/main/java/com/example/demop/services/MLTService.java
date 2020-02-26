package com.example.demop.services;

import com.example.demop.model.*;
import com.example.demop.repository.TextRepository;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.text.PDFTextStripper;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class MLTService {

    @Autowired
    TextRepository textRepository;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    MagazineServiceImpl magazineService;

    @Qualifier("restHighLevelCient")
    @Autowired
    RestHighLevelClient restClient;

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    public ArrayList<UserDTO> moreLikeThisFilter(String processId) throws IOException {
      ArrayList<UserDTO> resultList = new ArrayList<UserDTO>();
        List<FormSubmissionDTO> magInfo = (List<FormSubmissionDTO>)runtimeService.getVariable(processId, "chosenMagazine");
        List<FormSubmissionDTO> textInfo = (List<FormSubmissionDTO>)runtimeService.getVariable(processId, "textData");
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
        String keywords="";
        for(FormSubmissionDTO item: textInfo){
            System.out.println("VALUE: "+ item.getFieldValue());
            System.out.println("ID "+ item.getFieldId());
            if(item.getFieldId().equals("file")){
                text.setPdf(item.getFieldValue());
            }
        }

        //D:\UDD_GIT\UDD\demop\files\bajka.pdf
        String [] pom = text.getPdf().split("\\\\");
        String fileName = pom[5];
        System.out.println("Naziv fajla je "+fileName);
        File file = new File("files/" +fileName);
        String content = getContent(file);
        System.out.println("Sadrzaj je "+content);

        String[] arrayOfFields = new String[] {"content"};
        String[] contentArray = new String[] {content};

        MoreLikeThisQueryBuilder mltqb = new MoreLikeThisQueryBuilder(arrayOfFields, contentArray , null);
        mltqb.analyzer("serbian").minDocFreq(1).maxQueryTerms(50).minimumShouldMatch("60%");
        System.out.println("tu");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(mltqb);
        System.out.println("ovde");
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
        Set<User> reviewers = new HashSet<User>();
        System.out.println("Ispod hasha");
        if(searchResponse.getHits()==null){
            System.out.println("Nema pogodaka");
        }
        for(SearchHit hit : searchResponse.getHits()) {
            System.out.println("Usao u for searchHit");
            Map<String, Object> resultMap = hit.getSourceAsMap();
            //dobija username od recenzenta
            String foundTextId = resultMap.get("id").toString();
            Text foundText = textRepository.findOneById(Long.parseLong(foundTextId));
            System.out.println("Naslov teksta je "+foundText.getTitle());
            if(foundText.getReviewersText().size()!=0){
                System.out.println("Recenziran je text");
                for(User reviewer:foundText.getReviewersText()) {
                    if(!reviewers.contains(reviewer)) {
                        reviewers.add(reviewer);
                    }else{
                        System.out.println("Vec ga sadrzi");
                    }
                }
            }else{
                System.out.println("Nije recenziran");
            }

        }
        for(User u : reviewers){
            resultList.add(new UserDTO(u));
            System.out.println("Dodat recenzent "+u.getName()+" "+u.getUsername());
        }
        return  resultList;
    }
    public String getContent(File file) {
        try {
            PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
            parser.parse();
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(parser.getPDDocument());
            return text;
        } catch (IOException e) {
            System.out.println("Error getting text");
            System.out.println(e);
        }
        return null;
    }

}
