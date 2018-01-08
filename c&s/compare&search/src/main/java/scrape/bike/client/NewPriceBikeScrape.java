package scrape.bike.client;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scrape.bike.Bike;
import scrape.bike.BikeBrand;
import scrape.bike.BrandRecognition;
import scrape.bike.Scrape;
import util.JSOUPUtils;
import util.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bibek on 12/3/17
 * @project compare&search
 */
public class NewPriceBikeScrape implements Scrape {
    private String base_url = "http://newpricenepal.com/12-motorbikes";

    private List<BikeBrand> prepareListOfBrand() {
        List<BikeBrand> bikeBrands = new ArrayList<>();
        Document doc = JSOUPUtils.provideDocument(base_url);
        if (doc != null) {
            Elements ulElements = doc.select("a.cat_name");
            ulElements.forEach(e -> {
                String brandName = e.text();
                String scrapeUrl = e.attr("href");
                BikeBrand bikeBrand = BrandRecognition.provideBrand(brandName);
                bikeBrand.setScrapeUrl(scrapeUrl);
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
                Element productGridElement = document.selectFirst("div.product_list.product_list_ph.nolist.grid");
                Elements quickViews = productGridElement.select("a.quick-view");
                quickViews.forEach(q -> {
                    Document individualDoc = JSOUPUtils.provideDocument(q.attr("href"));
                    if (individualDoc != null) {
                        Element productNameDiv = individualDoc.selectFirst("div#short_description_content");
                        Element productNaemElem = productNameDiv.selectFirst("h4");
                        Bike bike = new Bike();
                        if (productNaemElem != null) {
                            bike.setName(productNaemElem.text());
                        } else {
                            Element leftElement = individualDoc.selectFirst("div#pb-left-column");
                            if (leftElement != null) {
                                bike.setName(leftElement.text());
                            }
                        }
                        Element rightDiv = individualDoc.selectFirst("div.product_img");
                        Elements imgEles = rightDiv.select("img");
                        imgEles.forEach(i -> {
                            if (i != null) {
                                bike.add(i.attr("src"));
                            }
                        });
                        Element spanEle = individualDoc.selectFirst("span#our_price_display");
                        if (spanEle != null) {
                            bike.setPrice(spanEle.text());
                        }
                        Element specTable = individualDoc.selectFirst("div.sheets.align_justify");
                        Elements rowSpec = specTable.select("table tr");
                        rowSpec.forEach(r -> {
                            Elements tdElem = r.select("td");
                            StringBuilder str = new StringBuilder();
                            tdElem.forEach(td -> {
                                str.append(td.text()).append(" : ");
                            });
                            bike.addSpec(str.toString());
                        });
                        bikeBrand.addBike(bike);
                    }
                });
            }
        });
        try {
            FileUtils.writeStringToFile(new File("/home/bibek/bs-workspace/bike/preprocess/scrape/newpricenepal.json"), JsonUtils.toJsonList(bikeBrands), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void scrape() {
        this.prepareListOfBikeOnBrand();
    }
}
