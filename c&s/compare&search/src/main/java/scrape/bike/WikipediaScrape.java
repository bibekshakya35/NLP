package scrape.bike;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scrape.bike.repository.BikeBrandRepository;
import scrape.util.JSOUPUtils;
import scrape.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author bibek on 12/1/17
 * @project compare&search
 */
public class WikipediaScrape {
    private BikeBrandRepository bikeBrandRepository;
    private final String URL = "https://en.wikipedia.org/wiki/List_of_motorcycle_manufacturers";
    private final String BASE_URL = "https://en.wikipedia.org";
    Properties prop = ResourceUtils.getProp();
    public WikipediaScrape(BikeBrandRepository bikeBrandRepository) {
        this.bikeBrandRepository = bikeBrandRepository;
    }

    public void doScrape() {

        Document doc = JSOUPUtils.provideDocument(URL);
        if (doc != null) {
            Element body = doc.selectFirst("div.mw-parser-output");
            Elements ulElem = body.select("h3");
            List<WikiBike> wikiBikes = new ArrayList<>();
            ulElem.forEach(e -> {
                Optional<WikiBike> wikiBikeOpt = wikiBikes.stream().filter(wikiBike -> wikiBike.getCountryName().equals(e.selectFirst("span").text())).findFirst();
                if (!wikiBikeOpt.isPresent()) {
                    WikiBike wikiBike = new WikiBike(e.selectFirst("span").text());
                    Element next = e.nextElementSibling();
                    Elements nextLi = next.select("li");
                    nextLi.forEach(nl -> {
                        Element aElem = nl.selectFirst("a");
                        wikiBike.add(new BikeBrandWiki(aElem.text(), BASE_URL + aElem.attr("href")));
                    });
                    wikiBikes.add(wikiBike);
                }
            });
            this.doIndividualScrape(wikiBikes);
            try {
                FileUtils.writeStringToFile(new File(prop.getProperty("bike.wiki")), new Gson().toJson(wikiBikes), StandardCharsets.UTF_8);
            } catch (IOException io) {

            }
        }

    }

    /**
     * Mutate {@code WikiBike}
     *
     * @param wikiBikes
     */
    public void doIndividualScrape(List<WikiBike> wikiBikes) {
        if (!wikiBikes.isEmpty()) {
            wikiBikes.forEach(wikiBike -> {
                if (!wikiBike.getBikeBrandWikis().isEmpty()) {
                    wikiBike.getBikeBrandWikis().forEach(bikeBrandWiki -> {
                        Document document = JSOUPUtils.provideDocument(bikeBrandWiki.getWikiUrl());
                        if (document != null) {
                            Optional<Element> tableElem = Optional.ofNullable(document.selectFirst("table.infobox.vcard"));
                            if (tableElem.isPresent()) {
                                Elements trElements = tableElem.get().select("tr");
                                trElements.forEach(tr -> {
                                    Element thElem = tr.selectFirst("th");
                                    Element tdElem = tr.selectFirst("td");
                                    boolean isThereLogo = thElem == null && tdElem != null && tdElem.hasClass("logo");
                                    boolean isThereThAndTd = thElem != null && tdElem != null;
                                    if (isThereLogo) {
                                        String aLogo = tdElem.selectFirst("a").attr("href");
                                        bikeBrandWiki.put("logo", BASE_URL + aLogo);
                                    }
                                    if (isThereThAndTd) {
                                        String header = thElem.text();
                                        if (header.toLowerCase().equals("website")) {
                                            bikeBrandWiki.put("website", tdElem.selectFirst("a").attr("href"));
                                        } else {
                                            bikeBrandWiki.put(header, tdElem.text());
                                        }
                                    }
                                });
                            }
                        }

                    });
                }
            });
        }
    }
}

