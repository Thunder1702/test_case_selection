package TestsForCalculator;

import at.calc.CalculatorImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MainTests {
    private CalculatorImpl calculator;
    @Before
    public void setup(){
        calculator = new CalculatorImpl();
    }
    @After
    public void finsih(){
        calculator = null;
    }
    @Test
    public void testAdd(){
        Assert.assertEquals(5,calculator.add(3,2));
    }
}
