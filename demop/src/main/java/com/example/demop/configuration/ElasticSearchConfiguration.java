package com.example.demop.configuration;
import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import com.example.demop.model.UserUnit;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.RequestContextUtils;

@Configuration
public class ElasticSearchConfiguration {

    @Qualifier("restHighLevelCient")
    @Autowired
    RestHighLevelClient restClient;

    @PostConstruct
    public void postConstruct() throws IOException {

        //DeleteIndexRequest request1 = new DeleteIndexRequest("index");
        //AcknowledgedResponse deleteIndexResponse1 = restClient.indices().delete(request1, RequestOptions.DEFAULT);

        GetIndexRequest getIndexrequest = new GetIndexRequest("index");
        boolean existIndex = restClient.indices().exists(getIndexrequest, RequestOptions.DEFAULT);
        if(existIndex == false) {
            //prvi put ulazi u if, i kreiramo index
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("index");
            restClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

            PutMappingRequest mappingRequest = new PutMappingRequest("index");

            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.startObject("properties");
                {

                    builder.startObject("magazine");
                    {
                        builder.field("store", "true");
                        builder.field("type", "text");
                        builder.field("analyzer", "serbian");
                    }
                    builder.endObject();

                    builder.startObject("title");
                    {
                        builder.field("store", "true");
                        builder.field("type", "text");
                        builder.field("analyzer", "serbian");
                    }
                    builder.endObject();

                    builder.startObject("keywords");
                    {
                        builder.field("store", "true");
                        builder.field("type", "text");
                        builder.field("analyzer", "serbian");
                    }
                    builder.endObject();


                    builder.startObject("scientificArea");
                    {
                        builder.field("store", "true");
                        builder.field("type", "text");
                        builder.field("analyzer", "serbian");
                    }
                    builder.endObject();

                    builder.startObject("content");
                    {
                        builder.field("store", "true");
                        builder.field("type", "text");
                        builder.field("analyzer", "serbian");
                    }
                    builder.endObject();

                    builder.startObject("authors");
                    {
                        builder.field("store", "true");
                        builder.field("type", "text");
                        builder.field("analyzer", "serbian");
                    }
                    builder.endObject();

                    builder.startObject("id");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();

                }
                builder.endObject();
            }
            builder.endObject();
            mappingRequest.source(builder);
            restClient.indices().putMapping(mappingRequest, RequestOptions.DEFAULT);
        }
       // DeleteIndexRequest request = new DeleteIndexRequest("reviewers");
        //AcknowledgedResponse deleteIndexResponse = restClient.indices().delete(request, RequestOptions.DEFAULT);

        GetIndexRequest getRevrequest = new GetIndexRequest("reviewers");


        if(!restClient.indices().exists(getRevrequest, RequestOptions.DEFAULT)) {
            System.out.println("Creating index of reviewers");
            CreateIndexRequest createIndex = new CreateIndexRequest("reviewers");
            restClient.indices().create(createIndex, RequestOptions.DEFAULT);
            PutMappingRequest mappingReq = new PutMappingRequest("reviewers");
            XContentBuilder builder2 = XContentFactory.jsonBuilder();

            builder2.startObject();
            {
                builder2.startObject("properties");
                {

                    builder2.startObject("location");
                    {
                        builder2.field("type", "geo_point");
                    }
                    builder2.endObject();

                    builder2.startObject("username");
                    {
                        builder2.field("type", "keyword");
                    }
                    builder2.endObject();

                }
                builder2.endObject();
            }
            builder2.endObject();
            mappingReq.source(builder2);
            restClient.indices().putMapping(mappingReq, RequestOptions.DEFAULT);
            //indeksiranje recenzenata

            ArrayList<UserUnit> recenzenti = new ArrayList<UserUnit>();
            recenzenti.add(new UserUnit("rev1",(float)44.7866, (float)20.4489));
            recenzenti.add(new UserUnit("rev2",(float)40.7128, (float)-74.0060));
            recenzenti.add(new UserUnit("rev3",(float)44.7866, (float)20.4489));
            recenzenti.add(new UserUnit("rev4",(float)48.2082, (float)16.3738));

            for(UserUnit rec : recenzenti) {
                System.out.println("indeksiranje recenzenta"+rec.getUsername());
                IndexRequest request2 = new IndexRequest("reviewers");
                request2.id(rec.getUsername());
                ObjectMapper mapper = new ObjectMapper();
                request2.source(mapper.writeValueAsString(rec), XContentType.JSON);
                restClient.index(request2, RequestOptions.DEFAULT);
            }

        }
    }

}
