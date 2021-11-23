package Invocations;

public class C {
    private B bClass =new B();

    public int mutl_fun(int a){
        return a*bClass.add_with_one_number(a);
    }
}
