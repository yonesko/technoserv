package gleb;

import gleb.config.SolrIndexingServiceActivator;
import gleb.data.WordsRepo;
import gleb.data.WordsRepoSolr;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class TechnoservApplication {
    private static final Logger LOG = Logger.getLogger(TechnoservApplication.class);
    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(TechnoservApplication.class, args);
    }

    @Bean
    public WordsRepo wordsRepo() {
        return new WordsRepoSolr(solrClient());
    }

    @Bean
    public HttpSolrClient solrClient() {
        String url = env.getProperty("solr.url");
        LOG.info(String.format("Solr url=%s", url));
        return new HttpSolrClient.Builder(url).build();
    }

    @Bean
    public SolrIndexingServiceActivator solrIndexingServiceActivator() {
        return new SolrIndexingServiceActivator(solrClient());
    }
}
