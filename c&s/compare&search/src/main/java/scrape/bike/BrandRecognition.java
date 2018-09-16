package scrape.bike;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import scrape.bike.domain.ShowRoom;
import scrape.google.Detail.Detail;
import scrape.google.GooglePlaceApi;
import scrape.google.Nearby.NearbySearch;
import scrape.request.NearByRequest;
import util.JsonUtils;
import util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BrandRecognition {
    private static BikeBrand bikeBrand;
    private final List<String> possibleQueryForOfficial = new ArrayList<>();
    private final List<String> possibleQueryForShowRoom = new ArrayList<>();
    private List<WikiBike> wikiBikes;


    private BrandRecognition(String brandName) {
        try {
            wikiBikes = JsonUtils.fromJsonToList(FileUtils.readFileToString(new File(ResourceUtils
                            .getProp()
                            .getProperty("bike.wiki")), StandardCharsets.UTF_8),
                    new TypeToken<List<WikiBike>>() {
                    }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        bikeBrand = new BikeBrand(brandName);
        possibleQueryForOfficial.add("bike Official Website");
        possibleQueryForOfficial.add("motorcycle Official Website");
        possibleQueryForShowRoom.add("showroom in nepal");
        possibleQueryForShowRoom.add("workshop in nepal");

    }

    public static BikeBrand provideBrand(String brandName) {
        BrandRecognition brandRecognition = new BrandRecognition(brandName);
        brandRecognition.searchForBrandUrl(brandName);
        brandRecognition.searchForShowRoom(brandName);
        return bikeBrand;
    }

    private void searchForBrandUrl(String brandName) {
        wikiBikes.stream().forEach(wikiBike -> {
            List<BikeBrandWiki> bikeBrandWikis = wikiBike.getBikeBrandWikis();
            Optional<BikeBrandWiki> bikeBrandWiki = bikeBrandWikis.stream().filter(biBrand -> {
                boolean is = this.isContainOrEqual(brandName, biBrand.getBrandName().toLowerCase());
                if (is) {
                    return is;
                }
                String[] tempBrand = null;
                if (brandName.contains(" ")) {
                    tempBrand = brandName.split(" ");
                }
                if (tempBrand != null) {
                    for (String temp : tempBrand) {
                        is = this.isContainOrEqual(brandName, temp.toLowerCase());
                    }
                }
                return is;
            }).findFirst();
            if (bikeBrandWiki.isPresent()) {
                if (!bikeBrandWiki.get().getInformations().isEmpty()) {
                    bikeBrand.setOfficialUrl(
                            bikeBrandWiki
                                    .get()
                                    .getInformations()
                                    .get("website"));
                }
            }
        });
    }

    private boolean isContainOrEqual(String brandName, String biBrand) {
        if (brandName.toLowerCase().equals(biBrand)) {
            return true;
        }
        if (brandName.toLowerCase().contains(biBrand)) {
            return true;
        }
        return false;
    }

    public void searchForShowRoom(String brandName) {
        possibleQueryForShowRoom.forEach(p -> {
            String query = brandName + " " + p;
            NearByRequest nearByRequest = NearByRequest.forKathmandu(query);
            Optional<NearbySearch> nearbySearch = GooglePlaceApi.callRetroClient(nearByRequest);
            if (nearbySearch.isPresent()) {
                nearbySearch.get().getResults().forEach(r -> {
                    Optional<Detail> detail = GooglePlaceApi.getPlaceDetail(r.getPlaceId());
                    if (detail.isPresent()) {
                        Detail tempDetail = detail.get();
                        if (tempDetail.getResult() != null) {
                            ShowRoom showRoom = new ShowRoom();
                            showRoom.setName(tempDetail.getResult().getName());
                            showRoom.setMapUrl(tempDetail.getResult().getUrl());
                            showRoom.setAddress(tempDetail.getResult().getFormattedAddress());
                            showRoom.setPhoneNumber(tempDetail.getResult().getFormattedPhoneNumber());
                            showRoom.setWebsite(tempDetail.getResult().getWebsite());
                            List<String> photos = new ArrayList<>();
                            if (tempDetail.getResult().getPhotos() != null) {
                                tempDetail.getResult().getPhotos().forEach(
                                        pic -> photos.add(GooglePlaceApi.prepareGooglePlacePhotoApi(pic.getPhotoReference())));
                            }
                            showRoom.setPhotos(photos);
                            bikeBrand.add(showRoom);
                        }
                    }
                });
            }
        });
    }
}
