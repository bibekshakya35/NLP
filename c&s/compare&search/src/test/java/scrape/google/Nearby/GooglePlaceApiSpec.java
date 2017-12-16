package scrape.google.Nearby;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import scrape.google.Detail.Detail;
import scrape.google.GooglePlaceApi;
import scrape.request.NearByRequest;

import java.util.Optional;

public class GooglePlaceApiSpec {
    private NearByRequest nearByRequest;
    GooglePlaceApi googlePlaceApi = new GooglePlaceApi();

    @Before
    public void init() {
        nearByRequest = NearByRequest.forKathmandu("Hero Honda");
    }

    @Test
    public void shouldSearchForNearby() {
        Optional<NearbySearch> nearbySearch = googlePlaceApi.callRetroClient(nearByRequest);
        Assert.assertNotNull(nearbySearch);
        Assert.assertEquals(nearbySearch.get().getStatus(), "OK");
        Assert.assertTrue(nearbySearch.get().getResults().size() > 0);
    }

    @Test
    public void shouldProvidePlaceDetail() {
        Optional<NearbySearch> nearbySearch = googlePlaceApi.callRetroClient(nearByRequest);
        nearbySearch.get().getResults().forEach(n -> {

            Optional<Detail> detail = googlePlaceApi.getPlaceDetail(n.getPlaceId());
            Assert.assertNotNull(detail);
            System.out.println(detail.get().getResult().getName());

        });
    }
    @Test
    public void shouldSearchForShowRoom(){
        String testBrand = "Hero Honda";

    }
}
