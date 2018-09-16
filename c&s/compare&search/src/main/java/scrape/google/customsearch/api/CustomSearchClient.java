package scrape.google.customsearch.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import scrape.google.customsearch.Result;

import java.util.Map;

/**
 * @author bibek on 11/27/17
 * @project compare&search
 */
public interface CustomSearchClient {
    @Headers({
            "Content-Type:application/json"
    })
    @GET("json")
    Call<Result> getGoogleSearch(
            @QueryMap Map<String,Object> queries
    );

}
