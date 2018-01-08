package scrape.bike;

import preprocess.BikePreProcessing;
import preprocess.BikePreprocessingImpl;
import preprocess.BikeProduct;
import scrape.bike.client.AutoLifeScrape;
import scrape.bike.client.NewPriceBikeScrape;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author bibek on 12/3/17
 * @project compare&search
 */
public class Main {
    private static final Logger LOG  = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        LOG.log(Level.INFO,"Starting");
        Scrape scrape = new NewPriceBikeScrape();
        scrape.scrape();
        scrape = new AutoLifeScrape();
        scrape.scrape();
        BikePreProcessing<BikeProduct> bikeProductBikePreProcessing = new BikePreprocessingImpl();
        bikeProductBikePreProcessing.download();
        LOG.log(Level.INFO,"Ending");
    }
}
