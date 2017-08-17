package gleb;

import gleb.config.SolrIndexingServiceActivator;
import gleb.data.WordsRepo;
import gleb.data.WordsRepoSolr;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.time.Instant;

@SpringBootApplication
public class TechnoservApplication {
    private static final Logger LOG = Logger.getLogger(TechnoservApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TechnoservApplication.class, args);
    }

//    @Bean
    CommandLineRunner commandLineRunner(SolrClient solrClient) throws IOException, SolrServerException {
        return args -> {
            System.out.println(CoreAdminRequest.getCoreStatus("0fba93de8476", solrClient));


            System.exit(1);
        };
    }

    @Bean
    public WordsRepo wordsRepo(SolrClient solrClient) {
        return new WordsRepoSolr(solrClient);
    }

    @Bean
    public HttpSolrClient solrClient(@Value("${solr.url}") String url) {
        LOG.info(String.format("Solr url=%s", url));
        return new HttpSolrClient.Builder(url).build();
    }

    @Bean
    public SolrIndexingServiceActivator solrIndexingServiceActivator(SolrClient solrClient) {
        return new SolrIndexingServiceActivator(solrClient);
    }
}
