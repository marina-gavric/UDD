package com.example.demop.services;

import com.example.demop.model.FormSubmissionDTO;
import com.example.demop.model.Magazine;
import com.example.demop.model.User;
import com.example.demop.model.UserDTO;
import com.example.demop.repository.TextRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class LocationService {

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

    public ArrayList<UserDTO> geoFilter(String processId) throws IOException {

        String username = (String) runtimeService.getVariable(processId, "author");
        User user = userService.findUserByUsername(username);
        System.out.println("Autor je "+username);
        System.out.println("Lokacije su: longitude:"+user.getLongitude() +" latitude: "+user.getLatitude());
        String title="";
        List<FormSubmissionDTO> textForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processId, "textData");
        for(FormSubmissionDTO item: textForm){
            if(item.getFieldId().equals("title")){
                title = item.getFieldValue();
                System.out.println("pronadjen "+title);
                break;
            }
        }
        List<FormSubmissionDTO> magForm = (List<FormSubmissionDTO>)runtimeService.getVariable(processId, "chosenMagazine");
        String idMagazine="";
        for(FormSubmissionDTO item: magForm){
            if(item.getFieldId().equals("magazineList")){
                idMagazine = item.getFieldValue();
                break;
            }
        }
        com.example.demop.model.Text text = new com.example.demop.model.Text();
        Magazine magazine = magazineService.findMagazineById(Long.parseLong(idMagazine));
        for(com.example.demop.model.Text t: magazine.getTexts()){
            if(t.getTitle().equals(title)){
                text=t;
                break;
            }
        }
        Set<User> coauthors = text.getCoauthorText();

        ArrayList<UserDTO> results = distanceQuery(user.getLatitude(),user.getLongitude());
        for(User u : coauthors) {
            results.retainAll(distanceQuery(u.getLatitude(),u.getLongitude()));
        }

        return  results;
    }

    private ArrayList<UserDTO> distanceQuery(float latitude, float longitude) throws IOException{
        BoolQueryBuilder qb = new BoolQueryBuilder();
        System.out.println("In distance "+longitude+"sirina "+latitude);

        qb.must(QueryBuilders.matchAllQuery());
        qb.mustNot(QueryBuilders.geoDistanceQuery("location").distance("100km").point(latitude,longitude));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(qb);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);

        Set<String> searchResults = new HashSet<String>();

        for(SearchHit hit : searchResponse.getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //dobija username od recenzenta
            searchResults.add(sourceAsMap.get("username").toString());
            System.out.println("Ubacen user sa "+sourceAsMap.get("username").toString());
        }
        ArrayList<UserDTO> resultUsers = new ArrayList<UserDTO>();
        for(String s: (HashSet<String>)searchResults){
            User u = userService.findUserByUsername(s);
            System.out.println("pronadjen "+u.getName());
            resultUsers.add(new UserDTO(u));
        }
        return resultUsers;

    }

}
