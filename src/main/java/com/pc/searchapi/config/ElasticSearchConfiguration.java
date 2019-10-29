package com.pc.searchapi.config;

/**
 * Created by chitra_chitralekha on 10/28/19.
 */

import com.pc.searchapi.util.FunctionsProperties;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.AWSRequestSigningApacheInterceptor;



@Configuration
public class ElasticSearchConfiguration extends AbstractFactoryBean {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchConfiguration.class);
    private RestHighLevelClient restHighLevelClient;
    private String esURL;
    private int esPort;
    private static String serviceName = "es";
    private static String region;
    static final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

    @Override
    public void destroy() {
        try {
            if (restHighLevelClient != null) {
                restHighLevelClient.close();
            }
        } catch (final Exception e) {
            LOG.error("Error closing ElasticSearch client: ", e);
        }
    }

    @Override
    public Class<RestHighLevelClient> getObjectType() {
        return RestHighLevelClient.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public RestHighLevelClient createInstance() {
        return buildClient();
    }

    private RestHighLevelClient buildClient() {
        try {
            this.esURL = FunctionsProperties.getProperty("esURL");
            this.esPort = Integer.parseInt(FunctionsProperties.getProperty("esPort"));
            this.region= FunctionsProperties.getProperty("region");
            AWS4Signer signer = new AWS4Signer();
            signer.setServiceName(serviceName);
            signer.setRegionName(region);
            HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer, credentialsProvider);
            return new RestHighLevelClient(RestClient.builder(HttpHost.create(aesEndpoint)).setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)));
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return restHighLevelClient;
    }
}
