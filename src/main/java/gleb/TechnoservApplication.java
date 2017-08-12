package gleb;

import gleb.data.NewsItem;
import gleb.data.WordsRepo;
import gleb.data.WordsRepoSolr;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import java.time.Duration;
import java.time.ZonedDateTime;

@SpringBootApplication
@ImportResource("integration.xml")
public class TechnoservApplication {
    public static void main(String[] args) {
        SpringApplication.run(TechnoservApplication.class, args);
    }

    @Bean
    WordsRepo wordsRepoTest() {
        return new WordsRepoSolr();
    }

    @Bean
    CommandLineRunner initSolr() {
        return (args) -> {
            SolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/newsitem").build();

            solr.deleteByQuery("*:*");

            NewsItem newsItem = new NewsItem("1", "Прохоров продал семь процентов «Русала» по цене ниже рыночной", ZonedDateTime.now().minus(Duration.ofDays(3)));

            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("content_txt_en", newsItem.getContent());
            doc.addField("publication_dt", newsItem.getPublicationDateTime().toInstant().toString());
            doc.addField("id", newsItem.getId());
            solr.add(doc);

            solr.commit();
        };
    }
}
