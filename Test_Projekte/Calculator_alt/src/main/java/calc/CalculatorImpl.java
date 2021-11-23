package calc;

public class CalculatorImpl implements Calculator {
    int never_used = 0;

    public CalculatorImpl(){

    }
    //subtraction of two numbers
    public int add(int a, int b){
        int x = a;
        return a+b;
    }
    //addition of two numbers
    public int sub(int a, int b){
        return a-b;
    }
    //division of two numbers
    public int div(int a, int b){
        return a/b;
    }
    //multiplication of two numbers are lost

    public String printMsg(String msg){
        //not useful comment
        System.out.println("The message was: "+msg);
        return "The message was: "+msg;
    }
}
