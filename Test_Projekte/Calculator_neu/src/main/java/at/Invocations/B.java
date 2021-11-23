package at.Invocations;

public class B {
    private A aClass = new A();
    public int add_with_one_number(int a){
        return a+aClass.return_a(a);
    }
}
