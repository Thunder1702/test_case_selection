import  org.junit.*;

public class Fun_with_Code_1_Test {
    private Fun_with_Code_1 fun_with_code_1;

    @Before
    public void setup(){
        fun_with_code_1 = new Fun_with_Code_1();
    }
    @After
    public void finish(){
        fun_with_code_1=null;
    }

    @Test
    public void testPrintHello(){
        Assert.assertEquals("Hello",fun_with_code_1.printHello());
    }
}
