package hierarchy_calc;

import hierachy_calc.Calc_2;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Calc_2_Test {
    private Calc_2 calc_2;

    @Before
    public void setup(){
        calc_2 = new Calc_2();
    }
    @After
    public void finsih(){
        calc_2=null;
    }
    @Test
    public void testMult_5_2(){
        Assert.assertEquals(10,calc_2.mult(5,2));
    }
    @Test
    public void testAddMultSum_2_2(){
        Assert.assertEquals(8,calc_2.add_mult_sum(2,2));
    }
}
