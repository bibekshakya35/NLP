package util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author bibek on 12/1/17
 * @project compare&search
 */
public class JSOUPUtils {
    private final static Logger LOG = Logger.getLogger(JSOUPUtils.class.getName());

    public static Document provideDocument(String link) {
        try {
            Connection conn = Jsoup.connect(link)
                    .userAgent(
                            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                    .timeout(30000).ignoreContentType(true).ignoreHttpErrors(true);
            Connection.Response response = conn.execute();
            switch (response.statusCode()) {
                case 200:
                    return conn.get();
                case 404:
                    LOG.info("Url that failed " + link);
                    return null;
                default:
                    return conn.get();
            }
        } catch (IOException io) {
            LOG.info("Problem while connecting provided URL " + link);
            LOG.info("Reason is " + io.getMessage().toLowerCase());
            return null;
        }
    }
}
