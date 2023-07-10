package eu.cosminstefan;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.util.List;


public class SnsScraper extends WebScraper{
    ShoeDao dao;

    /** Constructor*/
    SnsScraper(){
        try{
            dao= new ShoeDao();
            dao.init();
            scrapeSns();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /** Demonstrates use of ChromeDriver with Selenium */
    public void scrapeSns(){
        //We need an options class to run headless - not needed if we want default options
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        //Create instance of web driver - this must be on the path.
       // WebDriver driver = new ChromeDriver(options);


        for(int j=1;j<=4;j++) {
            WebDriver driver = new ChromeDriver(options);
            //Navigate Chrome to page 1, 2 and 3
            if(j==1)
                driver.get("https://sportshowroom.co.uk/shoes?search=nike%20air%20force&order=price-desc%2Cprice_group-desc");
            if(j==2)
                driver.get("https://sportshowroom.co.uk/shoes?search=nike%20air%20force&order=price-desc%2Cprice_group-desc&page=2#");
            if(j==3)
                driver.get("https://sportshowroom.co.uk/shoes?search=nike%20air%20force&order=price-desc%2Cprice_group-desc&page=3");
            if(j==4)
                driver.get("https://sportshowroom.co.uk/shoes?search=nike%20air%20force&order=price-desc%2Cprice_group-desc&page=4");

            //Wait for page to load
            try {
                Thread.sleep(3000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                driver.manage().window().maximize();

                String handle = driver.getWindowHandle();

                Actions actions = new Actions(driver);
                //scrolls down the page
                actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
                Thread.sleep(3000);
                actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();


            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //Output the HTML of the page - should contain products
            //System.out.println(driver.getPageSource());

            //Output details for individual products
            List<WebElement> shoeList = driver.findElements(By.className("details"));
            List<WebElement> linkList = driver.findElements (By.className("product"));
            int x=0;
            for (WebElement shoe : shoeList) {
                //saves the useful scrapped data
                String[] strArray = shoe.getText().split("\\r?\\n|\\r");
                String price = strArray[3].substring(1);
                String name = strArray[1];
                name = name.replace("'", "");
                name = name.replace("x", "");
                name = name.replaceAll("07", "");
                //System.out.println(name);
                String link = (linkList.get(x).getAttribute("href"));x++;

                //creates the object
                Shoe shoe1 = new Shoe();
                shoe1.setBrand("Nike");
                shoe1.setStyleCode(strArray[2]);
                shoe1.setModel(name);

                ShoeInstance shoeinstance1 = new ShoeInstance();
                shoeinstance1.setShoe(shoe1);
                 shoeinstance1.setSize(9);

                Comparison comparison1 = new Comparison();
                comparison1.setShoeInstance(shoeinstance1);
                comparison1.setUrl(link);
                comparison1.setPrice(Double.parseDouble(price));
                comparison1.setName("Nike " + name);

                try{

                    dao.saveAndMerge(comparison1);
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }



            }

            //Exit driver and close Chrome
            driver.quit();
        }
    }






    public void run(){
        runThread = true;
        System.out.println("Sns scraper thread started");
        while(runThread) {
            System.out.println("Sns scraper thread running");

            try{
                sleep(crawlDelay);
            }
            catch(InterruptedException ex){
                runThread = false;
            }
        }

        System.out.println("Sns scraper thread stopped");
    }
}
