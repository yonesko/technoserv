package gleb.data;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.TermsParams;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class WordsRepoSolr implements WordsRepo {
    public static final int WORDS_NUM = 10_000;
    public static final String CONTENT_TXT_EN = "content_txt_en";
    public static final String TERM_FREQ = "TERM_FREQ";
    public static final String ID = "id";
    public static final String PUBLICATION_DT = "publication_dt";
    private static SolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/newsitem").build();

    @Override
    public Map<String, Integer> wordStat() {
        SolrQuery query = new SolrQuery();
        query.setRequestHandler("/terms");
        query.setTerms(true);
        query.setParam(TermsParams.TERMS_TTF, true);
        query.setTermsLimit(WORDS_NUM);
        query.addTermsField(CONTENT_TXT_EN);


        try {
            query.setParam(TermsParams.TERMS_LIST, findWords().stream().map(TermsResponse.Term::getTerm).collect(Collectors.joining(",")));
            QueryRequest request = new QueryRequest(query, SolrRequest.METHOD.POST);
            List<TermsResponse.Term> terms = request.process(solr).getTermsResponse().getTerms(CONTENT_TXT_EN);

            Map<String, Integer> ret = new HashMap<>();


            for (TermsResponse.Term term : terms) ret.put(term.getTerm(), (int) term.getTotalTermFreq());

            return ret;
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<TermsResponse.Term> findWords() throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setTerms(true);
        query.setTermsLimit(WORDS_NUM);
        query.addTermsField(CONTENT_TXT_EN);
        QueryRequest request = new QueryRequest(query);
        return request.process(solr).getTermsResponse().getTerms(CONTENT_TXT_EN);
    }

    @Override
    public WordDetailDTO findByWord(String word) {
        WordDetailDTO ret = new WordDetailDTO();

        SolrQuery query = new SolrQuery();
        query.set("q", String.format("%s:%s", CONTENT_TXT_EN, ClientUtils.escapeQueryChars(word)));
        query.addField(ID);
        query.addField(PUBLICATION_DT);
        query.addField(String.format("%s:termfreq(%s, '%s')", TERM_FREQ, CONTENT_TXT_EN, ClientUtils.escapeQueryChars(word)));
        query.addHighlightField(CONTENT_TXT_EN);
        query.setHighlightSimplePre("<span style=\"color: red\">");
        query.setHighlightSimplePost("</span>");
        query.setHighlightFragsize(0);
        query.setRows(1000);

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

        return null;
    }
}
