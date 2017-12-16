package scrape.google;

import retrofit2.Call;
import retrofit2.Retrofit;
import scrape.google.Detail.Detail;
import scrape.google.Detail.api.PlaceDetailClient;
import scrape.google.Nearby.NearbySearch;
import scrape.google.Nearby.api.NearbyClient;
import scrape.request.NearByRequest;
import scrape.util.RetrofitUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class GooglePlaceApi {
    final static String apiKey = "AIzaSyAwH-HNs_We3gqJI0N491XjYMuOI3vtvpA";
    private final static Logger LOG = Logger.getLogger(GooglePlaceApi.class.getName());
    private final static String PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";


    public static Optional<NearbySearch> callRetroClient(NearByRequest near) {
        try {
            Retrofit retrofit = RetrofitUtils.callForGoogleClient("https://maps.googleapis.com/maps/api/place/nearbysearch/",apiKey);
            //get client
            NearbyClient nearbyClient = retrofit.create(NearbyClient.class);
            //query map
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("location", near.getLocation());
            queryMap.put("radius", near.getRadius());
            queryMap.put("keyword", near.getKeyword());
            Call<NearbySearch> call = nearbyClient.searchForNearPlace(queryMap);
            return Optional.of(call.execute().body());
        } catch (IOException io) {
            LOG.info("Exception occurs while searching for nearBy Location");
            return Optional.empty();
        }
    }

    public static Optional<Detail> getPlaceDetail(String placeId) {
        try {
            Retrofit retrofit = RetrofitUtils.callForGoogleClient("https://maps.googleapis.com/maps/api/place/details/",apiKey);
            //getClient
            PlaceDetailClient client = retrofit.create(PlaceDetailClient.class);
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("placeid", placeId);
            Call<Detail> call = client.getDetailForGivenPlace(queryMap);
            return Optional.of(call.execute().body());
        } catch (IOException IO) {
            LOG.info("Error to get place detail ");
            return Optional.empty();
        }
    }
    public static String prepareGooglePlacePhotoApi(String key){
        StringBuilder str = new StringBuilder(PHOTO_URL)
                .append(key)
                .append("&key=")
                .append(apiKey);
        return str.toString();
    }
}
