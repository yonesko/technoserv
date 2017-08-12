package gleb.data;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;

public class WordsRepoSolr implements WordsRepo {
    private static SolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/newsitem").build();

    @Override
    public Map<String, Integer> wordStat() {
        LukeRequest request = new LukeRequest();
        request.addField("content_txt_en");

        try {
            LukeResponse response = request.process(solr);
            return response.getFieldInfo().get("content_txt_en").getTopTerms().asShallowMap();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Set<NewsItem> findByWord(String word) {
        Set<NewsItem> ret = new HashSet<>();

        SolrQuery query = new SolrQuery();
        query.set("q", String.format("content_txt_en:%s", word));
        query.addHighlightField("content_txt_en");
        query.setHighlightSimplePost("</b>");
        query.setHighlightSimplePre("<b>");

        try {
            QueryResponse response = solr.query(query);

            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

            SolrDocumentList results = response.getResults();
            results.forEach(doc -> {

                String id = (String) doc.get("id");
                ret.add(new NewsItem(id, highlighting.get(id).get("content_txt_en").get(0), ((Date) doc.get("publication_dt")).toInstant().atZone((ZoneId.systemDefault()))));
            });

            return ret;

        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
