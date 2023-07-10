import eu.cosminstefan.Comparison;
import eu.cosminstefan.Shoe;
import eu.cosminstefan.ShoeDao;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MockShoeDao extends ShoeDao {
    public Comparison simpleSave(Comparison comparison) {return comparison;}

    public void saveAndMerge(Comparison comparison) throws Exception{
        //assertions about comparisons
        assertTrue(comparison.getUrl().length()>0);
    }

    public Shoe simpleSave(Shoe shoe){return shoe;}


    public void init() {

    }
}
