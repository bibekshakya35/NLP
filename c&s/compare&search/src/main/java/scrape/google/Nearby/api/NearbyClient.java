package scrape.google.Nearby.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import scrape.google.Nearby.NearbySearch;

import java.util.Map;

public interface NearbyClient {
    @Headers({
            "Content-Type:application/json"
    })
    @GET("json")
    Call<NearbySearch> searchForNearPlace(
            @QueryMap Map<String, Object> queries
    );
}
