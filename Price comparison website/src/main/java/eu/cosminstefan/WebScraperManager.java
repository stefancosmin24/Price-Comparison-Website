package eu.cosminstefan;

import java.util.ArrayList;

public class WebScraperManager {
    ArrayList<WebScraper> webScraperArrayList = new ArrayList<>();

    //adds each scraper to an array list
    public WebScraperManager(){
        webScraperArrayList.add(new KFScraper());
        webScraperArrayList.add(new SnsScraper());
        webScraperArrayList.add(new NikeScraper());
    }

    public void startScraping(){
    for (WebScraper scraper : webScraperArrayList){
        scraper.start();
    }
    }

    public void stopScraping(){
        for (WebScraper scraper : webScraperArrayList){
            scraper.stopThread();
        }
        for (WebScraper scraper : webScraperArrayList){
            try{
            scraper.join();
            } catch (InterruptedException ex) {
                System.err.println("Error joining " + ex.getMessage());
            }
        }
        System.out.println("Threads finished");
    }
}
