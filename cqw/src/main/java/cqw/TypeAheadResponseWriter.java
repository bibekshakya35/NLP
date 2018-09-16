package cqw;

import org.apache.lucene.document.Document;
import org.apache.solr.common.StringUtils;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.BasicResultContext;
import org.apache.solr.response.QueryResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.search.DocIterator;
import org.apache.solr.search.DocList;
import org.apache.solr.search.SolrIndexSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

public class TypeAheadResponseWriter implements QueryResponseWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TypeAheadResponseWriter.class);
    private Set<String> fields;


    public void write(Writer writer, SolrQueryRequest solrQueryRequest, SolrQueryResponse solrQueryResponse) throws IOException {
        LOGGER.info("Here we are....");
        SolrIndexSearcher searcher = solrQueryRequest.getSearcher();
        NamedList namedList = solrQueryResponse.getValues();
        LOGGER.info("Named List " + namedList.size());
        int size = namedList.size();
        for (int i = 0; i < size; i++) {
            Object val = namedList.getVal(i);
            LOGGER.info(val.toString());
            if (val instanceof BasicResultContext) {
                BasicResultContext basicResultContext = (BasicResultContext) val;
                DocList docList = basicResultContext.getDocList();
                LOGGER.info("docList List " + docList.size());
                DocIterator docIterator = docList.iterator();
                writer.append("<ul>\n");
                while (docIterator.hasNext()) {
                    int id = docIterator.nextDoc();
                    LOGGER.info("id id " + id);
                    Document doc = searcher.doc(id, fields);
                    for (String field : fields) {
                        String value = doc.get(field);
                        LOGGER.info(value);
                        if (!StringUtils.isEmpty(value)) {
                            writer.append("<li>" + value + "</li>");
                        }
                    }
                }
                writer.append("</ul>\n");
            }
        }
    }


    public String getContentType(SolrQueryRequest solrQueryRequest, SolrQueryResponse solrQueryResponse) {
        return "text/html;charset=UTF-8";
    }


    public void init(NamedList namedList) {
        LOGGER.info("Here we are....");
        fields = new HashSet<String>();
        fields.add("titleKeywords");
        fields.add("bodyKeywords");
        fields.add("snippetKeywords");
        fields.add("byLines");
    }
}