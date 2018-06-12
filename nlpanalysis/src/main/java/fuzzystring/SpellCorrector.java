package fuzzystring;

import org.apache.lucene.search.spell.NGramDistance;
import org.apache.lucene.search.spell.StringDistance;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.Iterator;

/**
 * to get possible corrections from solr and rank them
 */
public class SpellCorrector {
    private SolrClient solrClient;
    private SolrQuery solrQuery;
    private StringDistance sd;
    private float threshold;

    public SpellCorrector(StringDistance sd, float threshold) {
        this.solrClient =
                new HttpSolrClient.Builder("http://localhost:8983/solr/factivadna").build();
        this.solrQuery = new SolrQuery();
        this.solrQuery.setFields("words");
        solrQuery.setRows(50);
        this.sd = sd;
        this.threshold = threshold;
    }

    public String topSuggestion(String spelling) throws IOException, SolrServerException {
        solrQuery.setQuery("title_prefix_typeahead:" + spelling);
        QueryResponse response = solrClient.query(solrQuery);
        SolrDocumentList solrDocuments = response.getResults();
        Iterator<SolrDocument> solrDocumentIterator = solrDocuments.iterator();
        float maxDistance = 0;
        String suggestion = null;
        while (solrDocumentIterator.hasNext()) {
            SolrDocument document = solrDocumentIterator.next();
            String title = (String) document.getFieldValue("words");
            float distance = this.sd.getDistance(title, spelling);
            if (distance > maxDistance) {
                maxDistance = distance;
                suggestion = title;
            }
        }
        if (maxDistance > threshold) {
            return suggestion;
        }
        return null;

    }
    public static void main(String[] args) throws IOException, SolrServerException {
        SpellCorrector spellCorrector = new SpellCorrector(new NGramDistance(1),0.05f);
        String data = spellCorrector.topSuggestion("Medica");
        System.out.println(data);
    }
}
