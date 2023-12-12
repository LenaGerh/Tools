import lenger.math.Fraction;

import java.math.BigDecimal;
import java.util.Arrays;

public class App{
    public static void main(String[] args) {
        Fraction<?,?>[] fracArr = new Fraction[100];

        fillFracArr(fracArr);

        Fraction<?,?> frac1 = new Fraction<>(1,10);
        double d1 = frac1.doubleValue();

        Arrays.sort(fracArr);

        System.out.println(frac1.add(new Fraction<>(20,2)).toFractionString());        
        System.out.println(d1);

        System.out.println(Arrays.toString(fracArr));

        BigDecimal bigd1 = new BigDecimal(frac1.toString());

        System.out.println(bigd1.doubleValue());
    }

    public static void fillFracArr(Fraction<?,?>[] fracArr){
        for(int i = 0; i < fracArr.length; i++){
            double over = Math.random() * 100, under = Math.random() * 200 + 1;
            fracArr[i] = new Fraction<>(over, under);
        }
    }
}
