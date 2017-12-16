package scrape.bike.client;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scrape.bike.*;
import scrape.util.JSOUPUtils;
import scrape.util.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bibek on 12/5/17
 * @project compare&search
 */
public class ZigWheelScrape implements Scrape {
    private String base_url = "https://www.zigwheels.com/newbikes";

    private List<BikeBrand> prepareListOfBrand() {
        List<BikeBrand> bikeBrands = new ArrayList<>();
        Document doc = JSOUPUtils.provideDocument(base_url);
        if (doc != null) {
            Element ulElement = doc.selectFirst("div#zwn-brandslider");
            Elements liElements = ulElement.select("li");
            liElements.forEach(e -> {
                Element aElem = e.selectFirst("a");
                String brandName = aElem.text();
                String scrapeUrl = aElem.attr("href");
                BikeBrand bikeBrand = new BikeBrand();
                bikeBrand.setScrapeUrl(scrapeUrl);
                bikeBrand.setFullName(brandName);
                bikeBrands.add(bikeBrand);
            });
        } else {
            this.prepareListOfBrand();
        }
        return bikeBrands;
    }

    private void prepareListOfBikeOnBrand() {
        List<BikeBrand> bikeBrands = this.prepareListOfBrand();
        bikeBrands.forEach(bikeBrand -> {
            Document document = JSOUPUtils.provideDocument(bikeBrand.getScrapeUrl());
            if (document != null) {
                Element productGridElement = document.selectFirst("ul.mob-m-lr-0.srp_main_div");
                Elements aElements = productGridElement.select("a");
                aElements.forEach(a -> {
                    String url = null;
                    if(a.attr("href").contains("specifications")){
                        url = a.attr("href");
                    }
                    Document individualDoc = null;
                    if (url.contains("http")) {
                        individualDoc = JSOUPUtils.provideDocument(url);
                    }
                    if (individualDoc != null) {

                    }
                });
            }
        });
        try {
            FileUtils.writeStringToFile(new File("/home/bibek/bs-workspace/bike/autolife.json"), JsonUtils.toJsonList(bikeBrands), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void scrape() {
        this.prepareListOfBikeOnBrand();
    }
}
