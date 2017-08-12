package gleb.integration;

import com.rometools.rome.feed.synd.SyndEntry;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SolrIndexingServiceActivator {
    private static SolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/newsitem").build();

    public void process(SyndEntry syndEntry) throws IOException, SolrServerException {
        String content = String.format("%s\n%s", syndEntry.getTitle(), syndEntry.getDescription().getValue());
        ZonedDateTime publishedDate = ZonedDateTime.ofInstant(syndEntry.getPublishedDate().toInstant(),
                ZoneId.systemDefault());
        String id = syndEntry.getUri();

        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("content_txt_en", content);
        doc.addField("publication_dt", publishedDate.toInstant().toString());
        doc.addField("id", id);
        solr.add(doc);
        solr.commit(false,false);
    }
}
