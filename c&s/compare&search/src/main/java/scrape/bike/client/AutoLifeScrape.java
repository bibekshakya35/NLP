package scrape.bike.client;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scrape.bike.*;
import scrape.bike.domain.ShowRoom;
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
public class AutoLifeScrape implements Scrape {
    private String base_url = "http://autolife.com.np/auto_bike/";

    private List<BikeBrand> prepareListOfBrand() {
        List<BikeBrand> bikeBrands = new ArrayList<>();
        Document doc = JSOUPUtils.provideDocument(base_url);
        if (doc != null) {
            Elements blockElement = doc.select("div.block");
            blockElement.forEach(e -> {
                Element h3Elem = e.selectFirst("h3");
                Element aElem = h3Elem.selectFirst("a");
                String brandName = aElem.text();
                String scrapeUrl = aElem.attr("href");
                Element brandLogoAhref = e.selectFirst("a");
                Element brandLogoImg = brandLogoAhref.selectFirst("img");
                String brandLogo = brandLogoImg.attr("src");
                String fullName = brandLogoImg.attr("alt");
                BikeBrand bikeBrand = BrandRecognition.provideBrand(brandName);
                bikeBrand.setScrapeUrl(scrapeUrl);
                bikeBrand.setFullName(fullName);
                bikeBrand.setLogo(brandLogo);
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
                Element productGridElement = document.selectFirst("div.block-section.latest-cat");
                Elements aElements = productGridElement.select("a");
                aElements.forEach(a -> {
                    String url = a.attr("href");
                    Document individualDoc = null;
                    if (url.contains("http")) {
                        individualDoc = JSOUPUtils.provideDocument(url);
                    }
                    if (individualDoc != null) {
                        Element productNameH4 = individualDoc.selectFirst("h4.title.alignleft");
                        Element proName = productNameH4.selectFirst("a");
                        Bike bike = new Bike();
                        bike.setName(proName.text());
                        Element rightDiv = individualDoc.selectFirst("div.detail-full.span12");
                        Elements imgEles = rightDiv.select("img");
                        imgEles.forEach(i -> {
                            if (i != null) {
                                bike.add(i.attr("src"));
                            }
                        });
                        Element liElement = individualDoc.selectFirst("li.price");
                        Element priceAhreEle = liElement.selectFirst("a");
                        bike.setPrice(priceAhreEle.text());
                        Element specTable = individualDoc.selectFirst("ul.part-detail");
                        if (specTable != null) {
                            Elements liSpec = specTable.select("li");
                            liSpec.forEach(li -> {
                                Element aElement = li.selectFirst("a");
                                Element divElement = li.selectFirst("div.accordion-content");
                                Elements pElements = divElement.select("p");
                                List<String> specs = new ArrayList<>();
                                pElements.forEach(p -> {
                                    specs.add(p.text());
                                });
                                bike.addSpecWithCategory(aElement.text(), specs);
                            });
                        }

                        Element versionElement = individualDoc.selectFirst("div.version");
                        if (versionElement != null) {
                            String version = versionElement.selectFirst("h3").text();
                            Elements versionDetails = versionElement.select("p");
                            List<String> specs = new ArrayList<>();
                            versionDetails.forEach(v -> {
                                specs.add(v.text());
                            });
                            bike.addSpecWithCategory(version, specs);
                        }
                        bikeBrand.addBike(bike);
                        Element locationElem = individualDoc.selectFirst("a.location-detail");
                        if (locationElem != null) {
                            String urlHref = locationElem.attr("href");
                            Document locationDetailDoc = JSOUPUtils.provideDocument(urlHref);
                            if (locationDetailDoc != null) {
                                ShowRoom showRoom = new ShowRoom();
                                Element divElem = locationElem.selectFirst("div.primary");
                                if(divElem!=null){
                                    Element imgEleme = divElem.selectFirst("img");
                                    if (imgEleme != null) {
                                        List<String> lists = new ArrayList<>();
                                        lists.add(imgEleme.attr("src"));
                                        showRoom.setPhotos(lists);
                                    }
                                    Element showRoomNameEle = divElem.selectFirst("h4");
                                    if (showRoomNameEle != null) {
                                        showRoom.setName(showRoomNameEle.text());
                                    }
                                    Element ulElement = divElem.selectFirst("ul");
                                    Elements showRoomLiElements = ulElement.select("li");
                                    showRoomLiElements.forEach(s -> {
                                        Element addressEle = s.selectFirst("li.add");
                                        if (addressEle != null) {
                                            showRoom.setAddress(addressEle.text());
                                        }
                                        Element openingHourELement = s.selectFirst("li.time");
                                        if (openingHourELement != null) {
                                            showRoom.setOpeningHour(openingHourELement.text());
                                        }
                                        Element phoneElement = s.selectFirst("li.phone");
                                        if (phoneElement != null) {
                                            showRoom.setPhoneNumber(phoneElement.text());
                                        }
                                        Element urlElem = s.selectFirst("li.site-url");
                                        if (showRoom != null) {
                                            showRoom.setWebsite(urlElem.text());
                                        }
                                    });
                                    bikeBrand.add(showRoom);
                                }

                            }
                        }
                    }
                });
            }
        });
        try {
            FileUtils.writeStringToFile(new File("/home/bibek/bs-workspace/bike/preprocess/scrape/autolife.json"), JsonUtils.toJsonList(bikeBrands), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void scrape() {
        this.prepareListOfBikeOnBrand();
    }
}
