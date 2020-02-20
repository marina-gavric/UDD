package com.example.demop.configuration;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfiguration {

    @Qualifier("elasticsearchRestHighLevelClient")
    @Autowired
    RestHighLevelClient restClient;

    @PostConstruct
    public void postConstruct() throws IOException {
        GetIndexRequest getIndexrequest = new GetIndexRequest("text_index");
        boolean existIndex = restClient.indices().exists(getIndexrequest, RequestOptions.DEFAULT);
        if(existIndex == false) {
            //prvi put ulazi u if, i kreiramo index
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("text_index");
            restClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

            PutMappingRequest mappingRequest = new PutMappingRequest("text_index");

            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.startObject("properties");
                {

                    builder.startObject("id");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();

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

                    builder.startObject("coauthors");
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

                    builder.startObject("author");
                    {
                        builder.field("store", "true");
                        builder.field("type", "text");
                        builder.field("analyzer", "serbian");
                    }
                    builder.endObject();

                }
                builder.endObject();
            }
            builder.endObject();
            mappingRequest.source(builder);
            restClient.indices().putMapping(mappingRequest, RequestOptions.DEFAULT);
        }
    }

}
