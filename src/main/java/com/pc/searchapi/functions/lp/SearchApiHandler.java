package com.pc.searchapi.functions.lp;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.pc.searchapi.dao.PlanDao;
import com.pc.searchapi.exceptions.SearchApiClientException;
import com.pc.searchapi.exceptions.SearchApiInternalExcpetion;
import com.pc.searchapi.model.PlanData;
import com.pc.searchapi.util.SearchApiMsg;
import com.pc.searchapi.util.SearchApiUtil;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by chitra_chitralekha on 10/28/19.
 */
public class SearchApiHandler implements RequestStreamHandler {


        private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
        static final Logger log = Logger.getLogger(SearchApiHandler.class);
        PlanDao planDao;

        static {
            try {
                handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Function.class);
                handler.activateSpringProfiles("lambda");
            } catch (ContainerInitializationException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not initialize Spring application", e);
            }
        }

        @Override
        public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
            handler.proxyStream(inputStream, outputStream, context);

            JSONObject responseJson = new JSONObject();

            JSONObject parsedInput = null;
            JSONObject logItems = new JSONObject();
            try{
                logItems.put("handler",this.getClass().getSimpleName());

                parsedInput = SearchApiUtil.parseLambdaInputStream(inputStream);

                if ("GET".equalsIgnoreCase((String) parsedInput.get("httpMethod"))) {
                    responseJson = handleGet(inputStream, outputStream, context, parsedInput, logItems);

                }

                logItems.put("method", parsedInput.get("httpMethod"));
                logItems.put("awsReqId", (String) ((JSONObject) parsedInput.get("requestContext")).get("requestId"));
                logItems.put("awsCfId", (String) ((JSONObject) parsedInput.get("requestContext")).get("X-Amz-Cf-Id"));


            }
            catch (Exception e){

            }

            // just in case it wasn't closed by the mapper
            outputStream.close();
        }


    @SuppressWarnings("unchecked")
    public JSONObject handleGet(InputStream inputStream, OutputStream outputStream, Context context,
                                JSONObject parsedInput, JSONObject logItems) throws SearchApiClientException,SearchApiInternalExcpetion {
        JSONObject responseJson = new JSONObject();
        JSONObject headers = new JSONObject();


        try {

            if (parsedInput.get("pathParameters") == null) {
                throw new SearchApiClientException(HttpStatus.SC_BAD_REQUEST, String.format(SearchApiMsg.MISSING_INVALID_FIELD_ERR, "pathParameters"));
            }
            String planName = (String) ((JSONObject) parsedInput.get("pathParameters")).get("planname");
            logItems.put("planName", planName);
            List<PlanData> result=planDao.getPlanBySearchCriteria(planName);



            if (result != null) {
                responseJson.put("body", result);
                responseJson.put("statusCode", HttpStatus.SC_OK);
                responseJson.put("headers", headers);

            } else {
                throw new SearchApiClientException(HttpStatus.SC_NOT_FOUND, "No Search Data Availbale");
            }

        }
        catch (IOException e){
            throw new SearchApiInternalExcpetion(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.toString(), e);
        }
        catch (IllegalArgumentException e) {
            throw new SearchApiClientException(HttpStatus.SC_BAD_REQUEST, e.toString(), e);
        }
        return responseJson;
    }


}
