package Invocations;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class A_Test {
    private A aClass;

    @Before
    public void setup(){
        aClass = new A();
    }
    @After
    public void finish(){
        aClass = null;
    }
    @Test
    public void testReturnA(){
        Assert.assertEquals(2, aClass.return_a(2));
    }
}
