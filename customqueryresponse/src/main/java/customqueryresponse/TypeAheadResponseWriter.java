package customqueryresponse;

import org.apache.lucene.document.Document;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.QueryResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.search.DocIterator;
import org.apache.solr.search.DocList;
import org.apache.solr.search.SolrIndexSearcher;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

public class TypeAheadResponseWriter implements QueryResponseWriter {
    private Set<String> fields;

    @Override
    public void write(Writer writer, SolrQueryRequest solrQueryRequest, SolrQueryResponse solrQueryResponse) throws IOException {
        SolrIndexSearcher searcher = solrQueryRequest.getSearcher();
        NamedList namedList = solrQueryResponse.getValues();
        int size = namedList.size();
        for (int i = 0; i < size; i++) {
            Object val = namedList.getVal(i);
            if (val instanceof DocList) {
                DocList docList = (DocList) val;
                DocIterator docIterator = docList.iterator();
                writer.append("<ul>\n");
                while (docIterator.hasNext()) {
                    int id = docIterator.nextDoc();
                    Document doc = searcher.doc(id, fields);
                    String name = doc.get("title");
                    writer.append("<li>" + name + "</li>");
                }
                writer.append("</ul>\n");
            }
        }
    }

    @Override
    public String getContentType(SolrQueryRequest solrQueryRequest, SolrQueryResponse solrQueryResponse) {
        return "text/html;charset=UTF-8";
    }

    @Override
    public void init(NamedList namedList) {
        fields = new HashSet<String>();
        fields.add("title");
    }
}
