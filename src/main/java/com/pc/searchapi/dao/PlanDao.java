package com.pc.searchapi.dao;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pc.searchapi.model.PlanData;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

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

    public List<PlanData> getPlanBySearchCriteria(String criteria)throws IOException, JsonParseException  {
        List<PlanData> results = new ArrayList<PlanData>();
        try{
        JSONParser parser = new JSONParser();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("planName",criteria )
                .fuzziness(Fuzziness.AUTO)
                .maxExpansions(10);
        sourceBuilder.query(matchQueryBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX);
        searchRequest.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest);
        List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
        searchHits.forEach(
                hit ->
                {try{
                    results.add(objectMapper.readValue(hit.getSourceAsString(), PlanData.class));
                }
                catch (IOException e)
                {
                    e.getLocalizedMessage();
                    System.out.println(e.getMessage());
                }

                }) ;
        }
        catch(ElasticsearchException e) {
            e.getDetailedMessage();
            System.out.println(e.getMessage());
        }
        catch (IOException e){
            e.getLocalizedMessage();
            System.out.println(e.getMessage());
        }

        return results;
    }

}

