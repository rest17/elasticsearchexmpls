package com.pc.searchapi.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pc.searchapi.model.PlanData;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

/**
 * Created by chitra_chitralekha on 10/28/19.
 */

@Repository
public class PlanDao {

    private final String INDEX = "plandata";
    private final String TYPE = "plan";

    private RestHighLevelClient restHighLevelClient;

    private ObjectMapper objectMapper;

    public PlanDao( ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
        this.objectMapper = objectMapper;
        this.restHighLevelClient = restHighLevelClient;
    }

    public PlanData insertData(PlanData data){
        data.setAckID(UUID.randomUUID().toString());
        Map<String, Object> dataMap = objectMapper.convertValue(data, Map.class);
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, data.getAckID())
                .source(dataMap);
        try {
            IndexResponse response = restHighLevelClient.index(indexRequest);
        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }
        return data;
    }

    public Map<String, Object> getPlanBySearchCriteria(String id){
        GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
        GetResponse getResponse = null;
        try {
            getResponse = restHighLevelClient.get(getRequest);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        return sourceAsMap;
    }

}

