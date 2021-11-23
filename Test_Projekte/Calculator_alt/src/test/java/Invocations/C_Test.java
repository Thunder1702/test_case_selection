package Invocations;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class C_Test {
    private C cClass;

    @Before
    public void setup(){
        cClass = new C();
    }
    @After
    public void finish(){
        cClass = null;
    }
    @Test
    public void testMult_wiht_oneNum(){
        Assert.assertEquals(8,cClass.mutl_fun(2));
    }
}
