package cspersistence.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * @author bibek on 12/18/17
 * @project elasticsearch
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "cspersistence.repository")
public class ElasticSearchConfiguration {

    @Value("${elasticsearch.host}")
    private String EsHost;

    @Value("${elasticsearch.port}")
    private int EsPort;

    @Value("${elasticsearch.clustername}")
    private String EsClusterName;

    @Bean
    public Client client(){
        try {
            return new PreBuiltTransportClient(Settings.builder()
                    .put("cluster.name",EsClusterName)
                    .put("node.name","node1")
                    .build())
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(EsHost), EsPort));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }
}
