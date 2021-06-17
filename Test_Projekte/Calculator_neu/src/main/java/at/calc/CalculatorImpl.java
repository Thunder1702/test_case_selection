package at.calc;

public class CalculatorImpl implements Calculator {

    public CalculatorImpl(){

    }
    //addition of two numbers
    public int add(int a, int b){
        int x = a;
        return a+b;
    }
    //subtraction of two numbers
    public int sub(int a, int b){
        return a-b;
    }
    //division of two numbers
    public int div(int a, int b){
        return a/b;
    }
    //multiplication of two numbers
    public int mult(int a, int b){
        return a*b;
    }

    public void printMsg(String msg){
        System.out.println("The message was: "+msg);
    }

}
