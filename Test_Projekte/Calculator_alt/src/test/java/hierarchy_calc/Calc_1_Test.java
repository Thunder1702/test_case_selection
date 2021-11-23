package hierarchy_calc;

import hierachy_calc.Calc_1;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Calc_1_Test {
    private Calc_1 calc_1;

    @Before
    public void setup(){
        calc_1=new Calc_1();
    }
    @After
    public void finish(){
        calc_1=null;
    }
    @Test
    public void testAdd(){
        Assert.assertEquals(100,calc_1.add(50,50));
    }
}
