import eu.cosminstefan.NikeScraper;
import eu.cosminstefan.ShoeDao;
import org.junit.jupiter.api.*;



@DisplayName("Test Web Scraper")
public class TestWebScraper {

    @BeforeAll
    static void initAll(){

    }

    @BeforeEach
    void init(){

    }

    @Test
    @DisplayName("Test Scrape")
    public void testScrape(){
        //Create instance of class that we want to test
        NikeScraper Nike = new NikeScraper();

        //Create mock of shoe dao with tests inside
        ShoeDao shoeDao = (ShoeDao) (new MockShoeDao());

        Nike.setShoeDao(shoeDao);
        //WebScraperManager manager = new WebScraperManager();

        //manager.startScraping();
    }

    @AfterEach
    void tearDown() {

    }

    @AfterAll
    static void tearDownAll(){

    }
}
