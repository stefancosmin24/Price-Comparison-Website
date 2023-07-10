package eu.cosminstefan;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){
    System.out.println("Starting web scraping");
    WebScraperManager manager = new WebScraperManager();

    manager.startScraping();
        //creates instances of scrapers
        new SnsScraper();
        new NikeScraper();
        new KFScraper();

    Scanner scanner = new Scanner(System.in);
    String userInput = scanner.nextLine();
    while(!userInput.equals("stop")){
        userInput = scanner.nextLine();
    }
    manager.stopScraping();

    }
}
