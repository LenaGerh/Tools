package lenger.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implements Fraction representation for a {@link Number}.
 * @author Lenardt Gerhardts
 * @since 21
 */
@SuppressWarnings("unchecked")
public class Fraction<OverType extends Number, UnderType extends Number> extends Number implements Comparable<Fraction<?,?>>{
    
    private OverType over;
    private UnderType under;

    /**
     * Creates Fraction {@code (0 | 1)}.
     */
    public Fraction(){
        set((OverType)new BigDecimal(0),(UnderType)new BigDecimal(1));
    }

    /**
     * Creates Fraction {@code (over | under)}. Throws {@link IllegalStateException} if {@code under == 0}. 
     * @param over
     * @param under
     */
    public Fraction(OverType over, UnderType under){
        set(over, under);
    }

    /**
     * Creates Fraction {@code (toFrac | 1)}.
     * @param toFrac
     */
    public Fraction(Number toFrac){
        set((OverType)new BigDecimal(toFrac.toString()));
    }

    /**
     * Prints fractions in std->out in format {@code | (f1.over | f1.under)     | (f2.over | f2.under)  | ... | (fn.over | f2.under) |}.
     * @param f
     */
    @SuppressWarnings("rawtypes")
    public static void printFractions(Fraction... f){
        boolean first = true;

        for(Fraction fr : f){
            if(first){
                System.out.print("| %s ".formatted(fr.toString()));
                first = false;
            }
            else
                System.out.print("\t| %s ".formatted(fr.toString()));
        }

        System.out.println("\t|");
    }

    /**
     * Sets this Fraction {@code (toFrac | 1)}.
     * @param toFrac
     */
    public void set(OverType number){
        this.set(number, (UnderType)new BigDecimal(1));
    }

    /**
     * Sets this Fraction {@code (over | under)}. Throws {@link IllegalStateException} if {@code under == 0}. Automatically uses {@link Fraction#simplify()}
     * @param toFrac
     */
    public void set(OverType over, UnderType under){
        if(under.doubleValue() == 0)
            throw new IllegalStateException("Member \"under\" is not allowed to be Zero");

        this.over = over;
        this.under = under;

        simplify();
    }

    /**
     * Determines the greatest common divisor of {@code a} & {@code b}
     * @param a
     * @param b
     * @return long
     */
    public static long gcd(long a, long b) {
        return a == 0 ? b : gcd(a, b % a);
    }

    /**
     * Simplifys this Fraction as low as Possible without using Decimals
     */
    public void simplify(){
        Long gcd = gcd(over.longValue(), under.longValue());

        over = (OverType)new BigDecimal(over.toString()).divide(new BigDecimal(gcd.toString()),  RoundingMode.HALF_UP);
        under = (UnderType)new BigDecimal(under.toString()).divide(new BigDecimal(gcd.toString()), RoundingMode.HALF_UP);
    }

    /**
     * Adds {@code number} to this Fraction by converting it to {@code (number | 1)} and using {@link Fraction#add(Fraction)}.
     * @param <T>
     * @param number
     * @return this
     */
    public <T extends Number> Fraction<OverType, UnderType> add(T number){
        return add(new Fraction<OverType, UnderType>(number));
    }

    /**
     * Adds {@code frac} by using {@code (f1.over * f2.under | f1.under * f2.under) + (f2.over * f1.under | f2.under * f1.under)}. Then uses {@link Fraction#simplify()}
     * @param <T>
     * @param frac
     * @return this
     */
    public Fraction<OverType, UnderType> add(Fraction<?,?> frac){
        BigDecimal overA = new BigDecimal(this.over.toString());
        BigDecimal underA = new BigDecimal(this.under.toString());

        BigDecimal overB = new BigDecimal(frac.over.toString());
        BigDecimal underB = new BigDecimal(frac.under.toString());

        over = (OverType)overA.multiply(underB).add(overB.multiply(underA));
        under = (UnderType)underA.multiply(underB);

        simplify();

        return this;
    }

    /**
     * Subtracts {@code number} to this Fraction by converting it to {@code (number | 1)} and using {@link Fraction#sub(Fraction)}.
     * @param <T>
     * @param number
     * @return this
     */
    public <T extends Number> Fraction<OverType, UnderType> sub(T number){
        return sub(new Fraction<OverType, UnderType>(number));
    }

    /**
     * Subtracts {@code frac} by using {@code (f1.over * f2.under | f1.under * f2.under) - (f2.over * f1.under | f2.under * f1.under)}. Then uses {@link Fraction#simplify()}
     * @param <T>
     * @param frac
     * @return this
     */
    public Fraction<OverType, UnderType> sub(Fraction<?,?> frac){
        BigDecimal overA = new BigDecimal(this.over.toString());
        BigDecimal underA = new BigDecimal(this.under.toString());

        BigDecimal overB = new BigDecimal(frac.over.toString());
        BigDecimal underB = new BigDecimal(frac.under.toString());

        over = (OverType)overA.multiply(underB).subtract(overB.multiply(underA));
        under = (UnderType)underA.multiply(underB);

        simplify();

        return this;
    }

    /**
     * Multiplies {@code number} to this Fraction by converting it to {@code (number | 1)} and using {@link Fraction#mul(Fraction)}.
     * @param <T>
     * @param number
     * @return this
     */
    public <T extends Number> Fraction<OverType, UnderType> mul(T number){
        return mul(new Fraction<OverType, UnderType>(number));
    }

    /**
     * Subtracts {@code frac} by using {@code (f1.over * f2.over | f1.under * f2.under)}. Then uses {@link Fraction#simplify()}
     * @param <T>
     * @param frac
     * @return this
     */
    public Fraction<OverType, UnderType> mul(Fraction<?,?> frac) {
        BigDecimal overA = new BigDecimal(this.over.toString());
        BigDecimal underA = new BigDecimal(this.under.toString());

        BigDecimal overB = new BigDecimal(frac.over.toString());
        BigDecimal underB = new BigDecimal(frac.under.toString());

        over = (OverType)overA.multiply(overB);
        under = (UnderType)underA.multiply(underB);

        simplify();

        return this;
    }
    
    /**
     * Divides {@code number} to this Fraction by converting it to {@code (number | 1)} and using {@link Fraction#div(Fraction)}.
     * @param <T>
     * @param number
     * @return this
     */
    public <T1 extends Number> Fraction<OverType, UnderType> div(T1 number){
        return div(new Fraction<T1, UnderType>(number));
    }

    /**
     * Subtracts {@code frac} by using {@code (f1.over * f2.under | f1.under * f2.over)}. Then uses {@link Fraction#simplify()}
     * @param <T>
     * @param frac
     * @return this
     */
    public Fraction<OverType, UnderType> div(Fraction<?,?> frac){
        BigDecimal overA = new BigDecimal(this.over.toString());
        BigDecimal underA = new BigDecimal(this.under.toString());

        BigDecimal overB = new BigDecimal(frac.over.toString());
        BigDecimal underB = new BigDecimal(frac.under.toString());

        over = (OverType)overA.multiply(underB);
        under = (UnderType)underA.multiply(overB);

        simplify();

        return this;
    }

    /**
     * Converts under so that {@code under == base}, by doing {@code over = over * (base / under)} and {@code under = base}.
     * @param base
     * @return
     */
    public Fraction<OverType, UnderType> setBase(Integer base){
        over = (OverType)new BigDecimal(over.toString()).multiply(new BigDecimal(base / under.doubleValue()));
        under = (UnderType)new BigDecimal(base);

        return this;
    }

    /**
     * Converts Fraction to {@link String} in the Format {@code ( over / under)}.
     * @return String
     */
    public String toFractionString(){
        return "( %s / %s )".formatted(over.toString(), under.toString());
    }

    @Override
    public String toString(){
        return "" + (over.doubleValue() / under.doubleValue());
    }

    @Override
    public int intValue() {
        return over.intValue() / under.intValue();
    }

    @Override
    public long longValue() {
        return over.longValue() / under.longValue();
    }

    @Override
    public float floatValue() {
        return over.floatValue() / under.floatValue();
    }

    @Override
    public double doubleValue() {
        return over.doubleValue() / under.doubleValue();
    }

    @Override
    public int compareTo(Fraction<?, ?> frac) {
        if(frac == null)
            return Integer.MIN_VALUE;

        return (int)((this.over.doubleValue() * frac.under.doubleValue()) - (frac.over.doubleValue() * this.under.doubleValue()));
    }

    @Override
    public Object clone(){
        return new Fraction<OverType, UnderType>(this.over, this.under);
    }
}