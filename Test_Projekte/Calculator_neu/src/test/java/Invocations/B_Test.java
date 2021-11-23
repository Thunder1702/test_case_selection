package Invocations;

import at.Invocations.B;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class B_Test {
    private B bClass;

    @Before
    public void setup(){
        bClass = new B();
    }
    @After
    public void finish(){
        bClass = null;
    }
    @Test
    public void testAdd_with_one_num(){
        Assert.assertEquals(4,bClass.add_with_one_number(2));
    }
}
