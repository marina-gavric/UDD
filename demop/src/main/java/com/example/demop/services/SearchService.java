package com.example.demop.services;
import com.example.demop.model.*;
import com.example.demop.repository.TextRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import java.io.IOException;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class SearchService {

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

    public ArrayList<TextDTO> search(ArrayList<SearchDTO> searchFields) throws IOException {
        System.out.println("Usao u searchService");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightContent = new HighlightBuilder.Field("content");
        //The unified highlighter uses the Lucene Unified Highlighter.
        //This highlighter breaks the text into sentences
        highlightContent.highlighterType("unified");
        highlightBuilder.field(highlightContent);

        sourceBuilder.highlighter(highlightBuilder);

        sourceBuilder.query(getQueryBuilder(searchFields));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);

        ArrayList<TextDTO> retVal = new ArrayList<>();

        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println("Hit");
            long textId = Long.parseLong(hit.getId());
            com.example.demop.model.Text text = textRepository.findOneById(textId);
            TextDTO textDTO = new TextDTO();
            textDTO.setId(text.getId());
            textDTO.setTitle(text.getTitle());
            textDTO.setMagazine(text.getMagazine().getTitle());
            String keywords="";
            for(Keyword k : text.getKeywords()){
                if(keywords.equals("")){
                    keywords+=k.getName();
                }else{
                    keywords+=", " + k.getName();
                }
            }
            textDTO.setKeywords(keywords);
            String coauthors="";
            for(User u : text.getCoauthorText()){
                if(coauthors.equals("")){
                    coauthors+=u.getName() + " "+u.getSurname();
                }else{
                    coauthors+=", " + u.getName() + " "+u.getSurname();
                }
            }
            textDTO.setAuthors(text.getAuthor().getName()+", "+text.getAuthor().getSurname());
            textDTO.setKeywords(keywords);

            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            textDTO.setScientificArea((String)sourceAsMap.get("scientificArea"));
            textDTO.setOpenAccess(((String) sourceAsMap.get("openAccess")));
            String highlightStirng = "";

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlight = highlightFields.get("content");
            if(highlight == null) {
                String documentContent = (String) sourceAsMap.get("content");
                highlightStirng = documentContent.substring(0, 200) + "...";
            }else {
                Text[] fragments = highlight.fragments();
                for(Text textFragment : fragments) {
                    highlightStirng += textFragment.toString() + "... ";
                }
            }

            textDTO.setHiglight(highlightStirng);
            retVal.add(textDTO);
        }
        return retVal;
    }

    private BoolQueryBuilder getQueryBuilder(ArrayList<SearchDTO> searchFields) {
        BoolQueryBuilder qb = new BoolQueryBuilder();
        for(SearchDTO searchField : searchFields) {
            if(searchField.getOperator().equals("must")) {
                if(searchField.getType().equals("regular")) {
                    qb.must(QueryBuilders.matchQuery(searchField.getField(), searchField.getText()));
                }else {
                    qb.must(QueryBuilders.matchPhraseQuery(searchField.getField(), searchField.getText()));
                }
            }else if(searchField.getOperator().equals("mustnot")){
                if(searchField.getType().equals("regular")) {
                    qb.mustNot(QueryBuilders.matchQuery(searchField.getField(), searchField.getText()));
                }else {
                    qb.mustNot(QueryBuilders.matchPhraseQuery(searchField.getField(), searchField.getText()));
                }
            }else {
                if(searchField.getType().equals("regular")) {
                    qb.should(QueryBuilders.matchQuery(searchField.getField(), searchField.getText()));
                }else {
                    qb.should(QueryBuilders.matchPhraseQuery(searchField.getField(), searchField.getText()));
                }
            }
        }
        return qb;
    }



}
