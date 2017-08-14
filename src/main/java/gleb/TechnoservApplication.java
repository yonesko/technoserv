package gleb;

import gleb.data.WordsRepo;
import gleb.data.WordsRepoSolr;
import gleb.config.SolrIndexingServiceActivator;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TechnoservApplication {
    public static void main(String[] args) {
        SpringApplication.run(TechnoservApplication.class, args);
    }

    @Bean
    public WordsRepo wordsRepo() {
        return new WordsRepoSolr(solrClient());
    }

    @Bean
    public HttpSolrClient solrClient() {
        return new HttpSolrClient.Builder("http://localhost:8983/solr/newsitem").build();
    }

    @Bean
    public SolrIndexingServiceActivator solrIndexingServiceActivator() {
        return new SolrIndexingServiceActivator(solrClient());
    }
}
