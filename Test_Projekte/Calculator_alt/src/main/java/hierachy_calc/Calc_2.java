package hierachy_calc;

public class Calc_2 extends Calc_1{

    public int mult(int a, int b){
        return a*b;
    }

    public int add_mult_sum(int a, int b){
        return super.add(a,b)+mult(a,b);
    }
}
