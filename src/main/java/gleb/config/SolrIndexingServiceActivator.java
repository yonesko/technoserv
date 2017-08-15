package gleb.config;

import com.rometools.rome.feed.synd.SyndEntry;
import gleb.data.WordsRepoSolr;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public class SolrIndexingServiceActivator {
    private static final Logger LOG = Logger.getLogger(SolrIndexingServiceActivator.class);
    private final SolrClient solr;

    public SolrIndexingServiceActivator(SolrClient solr) {
        this.solr = solr;
    }

    public void process(SyndEntry syndEntry) {
        String content = String.format("%s\n%s", syndEntry.getTitle(), syndEntry.getDescription().getValue());
        ZonedDateTime publishedDate = ZonedDateTime.ofInstant(syndEntry.getPublishedDate().toInstant(),
                ZoneId.systemDefault());
        String id = syndEntry.getUri();

        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(WordsRepoSolr.CONTENT_TXT_EN, content);
        doc.addField(WordsRepoSolr.PUBLICATION_DT, publishedDate.toInstant().toString());
        doc.addField("id", id);
        
        try {
            solr.add(doc);
            solr.commit(false, false);
        } catch (SolrServerException | IOException e) {
            LOG.warn(e);
        }
    }
}
