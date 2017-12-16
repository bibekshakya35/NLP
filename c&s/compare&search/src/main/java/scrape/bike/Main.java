package scrape.bike;

import scrape.bike.client.AutoLifeScrape;
import scrape.bike.client.NewPriceBikeScrape;

/**
 * @author bibek on 12/3/17
 * @project compare&search
 */
public class Main {
    public static void main(String[] args) {
        Scrape scrapeFirst = new NewPriceBikeScrape();
        Scrape scrapeSecond = new AutoLifeScrape();
        scrapeFirst.scrape();
        scrapeSecond.scrape();
    }
}
