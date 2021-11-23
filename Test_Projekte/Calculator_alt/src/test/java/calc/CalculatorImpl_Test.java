package calc;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculatorImpl_Test {
    private CalculatorImpl calculator;
    @Before
    public void setup(){
        this.calculator = new CalculatorImpl();
    }
    @After
    public void finsih(){
        this.calculator = null;
    }
    @Test
    public void testAdd_3_2(){
        Assert.assertEquals(5,calculator.add(3,2));
    }
    @Test
    public void testAdd_2_2(){
        Assert.assertEquals(4,calculator.add(2,2));
    }
    @Test
    public void testSub(){
        Assert.assertEquals(5,calculator.sub(10,5));
    }
    @Test
    public void testDiv(){
        Assert.assertEquals(10,calculator.div(100,10));
    }
    @Test
    public void testprintMsg(){
        Assert.assertEquals("The message was: Hello",calculator.printMsg("Hello"));
    }
}
