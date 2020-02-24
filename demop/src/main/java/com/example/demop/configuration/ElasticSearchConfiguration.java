package com.example.demop.configuration;
import java.io.IOException;

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

@Configuration
public class ElasticSearchConfiguration {

    @Qualifier("restHighLevelCient")
    @Autowired
    RestHighLevelClient restClient;

    @PostConstruct
    public void postConstruct() throws IOException {

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
        GetIndexRequest indexrequestRev = new GetIndexRequest("reviewers");

        if(!restClient.indices().exists(indexrequestRev, RequestOptions.DEFAULT)) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("reviewers");
            restClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

            PutMappingRequest mappingRequest = new PutMappingRequest("reviewers");

            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.startObject("properties");
                {

                    builder.startObject("username");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();

                    builder.startObject("location");
                    {
                        builder.field("type", "geo_point");
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
            mappingRequest.source(builder);
            restClient.indices().putMapping(mappingRequest, RequestOptions.DEFAULT);

            addUser(new UserUnit("rev1", (float)45.267136, (float)19.833549));
            addUser(new UserUnit("rev2", (float)40.7128, (float)-74.0060));
            addUser(new UserUnit("rev3", (float)44.7866, (float)20.4489));
            addUser(new UserUnit("rev4", (float)48.2082, (float)16.3738));

        }

    }
    private void addUser(UserUnit user) {
        IndexRequest indexRequest = new IndexRequest("reviewers");
        indexRequest.id(user.getUsername());

        ObjectMapper mapper = new ObjectMapper();

        try {
            indexRequest.source(mapper.writeValueAsString(user), XContentType.JSON);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            restClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
