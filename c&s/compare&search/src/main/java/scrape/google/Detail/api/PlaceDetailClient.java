package scrape.google.Detail.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import scrape.google.Detail.Detail;

import java.util.Map;

public interface PlaceDetailClient {
    @Headers({
            "Content-Type:application/json"
    })
    @GET("json")
    Call<Detail> getDetailForGivenPlace(
            @QueryMap Map<String, Object> queries
    );
}
