package com.pc.searchapi.functions.lp;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by chitra_chitralekha on 10/28/19.
 */
public class SearchApiHandler implements RequestStreamHandler {


        private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

        static {
            try {
                handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Function.class);
                handler.activateSpringProfiles("lambda");
            } catch (ContainerInitializationException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not initialize Spring Boot application", e);
            }
        }

        @Override
        public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
            handler.proxyStream(inputStream, outputStream, context);

            // just in case it wasn't closed by the mapper
            outputStream.close();
        }


}
