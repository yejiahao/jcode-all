package com.yejh.jcode.sboot.restful.service.canal;

import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "middle.elasticsearch")
@Configuration
public class EsClient {

    private String host;
    private Integer port;
    private String username;
    private String password;

//    @Bean
//    public ElasticsearchClient elasticsearchClient() {
//        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
//
//        // Create the low-level client
//        RestClient restClient = RestClient.builder(new HttpHost(host, port))
//                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
//                .setDefaultHeaders(new Header[]{new BasicHeader("Content-type", "application/json")})
//                .build();
//        // Create the transport with a Jackson mapper
//        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
//        // Create the transport with a Jackson mapper
//        return new ElasticsearchClient(transport);
//    }

    @Bean
    public RestHighLevelClient elasticsearchClient_v6() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, HttpHost.DEFAULT_SCHEME_NAME))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
        );
    }
}

