package scrape.util;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author bibek on 11/27/17
 * @project compare&search
 */
public class RetrofitUtils {
    public static Retrofit callForGoogleClient(String baseUrl, String apikey) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(
                chain -> {
                    Request original = chain.request();
                    HttpUrl httpUrl = original.url();
                    HttpUrl newHttpUrl = httpUrl.newBuilder()
                            .addQueryParameter("key", apikey)
                            .build();
                    Request.Builder reqBuilder = original.newBuilder().url(newHttpUrl);
                    Request request = reqBuilder.build();
                    return chain.proceed(request);
                }).build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build();
    }
}
