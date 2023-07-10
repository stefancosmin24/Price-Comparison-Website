package eu.cosminstefan;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class KFScraper extends WebScraper{
    ShoeDao dao;
    /** Constructor*/
    KFScraper(){
        try{
            dao= new ShoeDao();
            dao.init();
            scrapeKF();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /** Demonstrates use of ChromeDriver with Selenium */
    public void scrapeKF(){
        //We need an options class to run headless - not needed if we want default options
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        //Create instance of web driver - this must be on the path.
        WebDriver driver = new ChromeDriver(options);

        //Navigate Chrome to page.
        driver.get("https://kicksfinder.com/air-force-1");

        //Wait for page to load
        try {
            Thread.sleep(3000);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        try{
            driver.manage().window().maximize();
            //accepts cookies
            //driver.findElement(By.className("load-more")).click();
            Actions actions = new Actions(driver);
            //scrolls down the age in order to get more items
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
            Thread.sleep(3000);
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
            Thread.sleep(3000);
            driver.findElement(By.className("load-more")).click();
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
            Thread.sleep(3000);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        //Output the HTML of the page - should contain products
        //System.out.println(driver.getPageSource());

        //Output details for individual products
        List<WebElement> shoeList = driver.findElements(By.className("prod-name"));
        List<WebElement> priceList = driver.findElements(By.className("prod-price"));
        List<WebElement> linkList = driver.findElements (By.className("prod-detail-cnt"));
        for (int i = 0; i<= shoeList.size()-1; i++) {


            //saves useful scrapped data
            String name = (shoeList.get(i).getText());
            String price = (priceList.get(i).getText()).substring(1);
            String[] strArray = linkList.get(i).getText().split("\\r?\\n|\\r");
            String SC = strArray[1];
            String link = "https://kicksfinder.com/";
            link+=SC.toLowerCase();

            name = name.replace("'", "");
            name = name.replace("x", "");
            name = name.replaceAll("07", "");
            price = price.replaceAll("NA", "120");
            System.out.println(link);


            //creates the object
            Shoe shoe1 = new Shoe();
            shoe1.setBrand("Nike");
            shoe1.setStyleCode(SC);
            shoe1.setModel(name);

            ShoeInstance shoeinstance1 = new ShoeInstance();
            shoeinstance1.setShoe(shoe1);
            shoeinstance1.setSize(9);

            Comparison comparison1 = new Comparison();
            comparison1.setShoeInstance(shoeinstance1);
            comparison1.setUrl(link);
            comparison1.setPrice(Double.parseDouble(price));
            comparison1.setName( name);

            try{

                dao.saveAndMerge(comparison1);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }

        //Exit driver and close Chrome
        driver.quit();
        driver.quit();
    }






    public void run(){
        runThread = true;
        System.out.println("KF scraper thread started");
        while(runThread) {
            System.out.println("KF scraper thread running");

            try{
                sleep(crawlDelay);
            }
            catch(InterruptedException ex){
                runThread = false;
            }
        }

        System.out.println("KF scraper thread stopped");
    }
}
