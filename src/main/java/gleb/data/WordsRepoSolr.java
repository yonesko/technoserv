package gleb.data;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class WordsRepoSolr implements WordsRepo {
    public static final int WORDS_NUM = 1_000;
    public static final String CONTENT_TXT_EN = "content_txt_en";
    public static final String TERM_FREQ = "TERM_FREQ";
    public static final String ID = "id";
    public static final String PUBLICATION_DT = "publication_dt";

    private final SolrClient solr;

    public WordsRepoSolr(SolrClient solr) {
        this.solr = solr;
    }

    @Override
    public Map<String, Long> wordStat() {
        SolrQuery query = new SolrQuery("*:*");
        query.setRows(1);
        try {
            List<TermsResponse.Term> termList = findAllTermsInIndex();
            termList.stream()
                    .filter(term -> !term.getTerm().contains("'"))
                    .forEach(term -> query.addField(String.format("prefix%d:ttf(%s,'%s')", term.getTerm().hashCode(), CONTENT_TXT_EN, term.getTerm())));

            QueryRequest request = new QueryRequest(query, SolrRequest.METHOD.POST);
            QueryResponse response = request.process(solr);

            SolrDocument document = response.getResults().get(0);

            return termList.stream()
                    .collect(
                            Collectors.toMap(
                                    TermsResponse.Term::getTerm,
                                    term -> (Long) document.get(String.format("prefix%d", term.getTerm().hashCode()))));
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyMap();
    }

    private List<TermsResponse.Term> findAllTermsInIndex() throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();

        query.setTerms(true);
        query.setRequestHandler("/terms");
        query.setTermsLimit(WORDS_NUM);
        query.addTermsField(CONTENT_TXT_EN);

        return new QueryRequest(query).process(solr).getTermsResponse().getTerms(CONTENT_TXT_EN);
    }

    @Override
    public WordDetail findByWord(String word) {
        WordDetail ret = new WordDetail();

        SolrQuery query = new SolrQuery();
        query.set("q", String.format("%s:%s", CONTENT_TXT_EN, ClientUtils.escapeQueryChars(word)));
        query.setRows(1000);

        query.addField(ID);
        query.addField(PUBLICATION_DT);
        query.addField(String.format("%s:termfreq(%s, '%s')", TERM_FREQ, CONTENT_TXT_EN, ClientUtils.escapeQueryChars(word)));

        query.addHighlightField(CONTENT_TXT_EN);
        query.setHighlightSimplePre("<span style=\"color: red\">");
        query.setHighlightSimplePost("</span>");
        query.setHighlightFragsize(0);

        try {
            QueryResponse response = solr.query(query);

            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

            SolrDocumentList results = response.getResults();
            results.forEach(doc -> {
                String id = (String) doc.get(ID);
                String content = highlighting.get(id).get(CONTENT_TXT_EN).get(0);
                String day = ((Date) doc.get(PUBLICATION_DT)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
                Integer tf = (Integer) doc.get(TERM_FREQ);

                ret.getContentList().add(content);
                ret.getDayToTTF().compute(day, (key, val) -> val == null ? tf : val + tf);
            });

            return ret;
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

        return new WordDetail();
    }
}
