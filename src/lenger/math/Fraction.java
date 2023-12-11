package lenger.math;

import java.math.BigDecimal;
/**
 * Implements Fraction representation for {@link Number}.
 * @author Lenardt Gerhardts
 * @since 21
 */
public class Fraction<N extends Number> extends Number{
    
    private N over, under;

    /**
     * Creates Fraction {@code (0 | 1)}.
     */
    @SuppressWarnings("unchecked")
    public Fraction(){
        set((N)new BigDecimal(0),(N)new BigDecimal(1));
    }

    /**
     * Creates Fraction {@code (over | under)}. Throws {@link IllegalStateException} if {@code under == 0}. 
     * @param over
     * @param under
     */
    public Fraction(N over, N under){
        set(over, under);
    }

    /**
     * Creates Fraction {@code (toFrac | 1)}.
     * @param toFrac
     */
    @SuppressWarnings("unchecked")
    public Fraction(Number toFrac){
        set((N)new BigDecimal(toFrac.toString()));
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
    @SuppressWarnings("unchecked")
    public void set(N number){
        this.set(number, (N)new BigDecimal(1));
    }

    /**
     * Sets this Fraction {@code (over | under)}. Throws {@link IllegalStateException} if {@code under == 0}. Automatically uses {@link Fraction#simplify()}
     * @param toFrac
     */
    public void set(N over, N under){
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
        return b == 0 ? a : gcd(b, a % b);
    }

    /**
     * Simplifys this Fraction as low as Possible without using Decimals
     */
    @SuppressWarnings("unchecked")
    public void simplify(){
        Long gcd = gcd(over.longValue(), under.longValue());

        over = (N)new BigDecimal(over.toString()).divide(new BigDecimal(gcd.toString()));
        under = (N)new BigDecimal(under.toString()).divide(new BigDecimal(gcd.toString()));
    }

    /**
     * Adds {@code number} to this Fraction by converting it to {@code (number | 1)} and using {@link Fraction#add(Fraction)}.
     * @param <T>
     * @param number
     * @return this
     */
    public <T extends Number> Fraction<N> add(T number){
        return add(new Fraction<N>(number));
    }

    /**
     * Adds {@code frac} by using {@code (f1.over * f2.under | f1.under * f2.under) + (f2.over * f1.under | f2.under * f1.under)}. Then uses {@link Fraction#simplify()}
     * @param <T>
     * @param frac
     * @return this
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> Fraction<N> add(Fraction<T> frac){
        BigDecimal overA = new BigDecimal(this.over.toString());
        BigDecimal underA = new BigDecimal(this.under.toString());

        BigDecimal overB = new BigDecimal(frac.over.toString());
        BigDecimal underB = new BigDecimal(frac.under.toString());

        over = (N)overA.multiply(underB).add(overB.multiply(underA));
        under = (N)underA.multiply(underB);

        simplify();

        return this;
    }

    /**
     * Subtracts {@code number} to this Fraction by converting it to {@code (number | 1)} and using {@link Fraction#sub(Fraction)}.
     * @param <T>
     * @param number
     * @return this
     */
    public <T extends Number> Fraction<N> sub(T number){
        return sub(new Fraction<N>(number));
    }

    /**
     * Subtracts {@code frac} by using {@code (f1.over * f2.under | f1.under * f2.under) - (f2.over * f1.under | f2.under * f1.under)}. Then uses {@link Fraction#simplify()}
     * @param <T>
     * @param frac
     * @return this
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> Fraction<N> sub(Fraction<T> frac){
        BigDecimal overA = new BigDecimal(this.over.toString());
        BigDecimal underA = new BigDecimal(this.under.toString());

        BigDecimal overB = new BigDecimal(frac.over.toString());
        BigDecimal underB = new BigDecimal(frac.under.toString());

        over = (N)overA.multiply(underB).subtract(overB.multiply(underA));
        under = (N)underA.multiply(underB);

        simplify();

        return this;
    }

    /**
     * Multiplies {@code number} to this Fraction by converting it to {@code (number | 1)} and using {@link Fraction#mul(Fraction)}.
     * @param <T>
     * @param number
     * @return this
     */
    public <T extends Number> Fraction<N> mul(T number){
        return mul(new Fraction<N>(number));
    }

    /**
     * Subtracts {@code frac} by using {@code (f1.over * f2.over | f1.under * f2.under)}. Then uses {@link Fraction#simplify()}
     * @param <T>
     * @param frac
     * @return this
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> Fraction<N> mul(Fraction<T> frac) {
        BigDecimal overA = new BigDecimal(this.over.toString());
        BigDecimal underA = new BigDecimal(this.under.toString());

        BigDecimal overB = new BigDecimal(frac.over.toString());
        BigDecimal underB = new BigDecimal(frac.under.toString());

        over = (N)overA.multiply(overB);
        under = (N)underA.multiply(underB);

        simplify();

        return this;
    }
    
    /**
     * Divides {@code number} to this Fraction by converting it to {@code (number | 1)} and using {@link Fraction#div(Fraction)}.
     * @param <T>
     * @param number
     * @return this
     */
    public <T extends Number> Fraction<N> div(T number){
        return div(new Fraction<T>(number));
    }

    /**
     * Subtracts {@code frac} by using {@code (f1.over * f2.under | f1.under * f2.over)}. Then uses {@link Fraction#simplify()}
     * @param <T>
     * @param frac
     * @return this
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> Fraction<N> div(Fraction<T> frac){
        BigDecimal overA = new BigDecimal(this.over.toString());
        BigDecimal underA = new BigDecimal(this.under.toString());

        BigDecimal overB = new BigDecimal(frac.over.toString());
        BigDecimal underB = new BigDecimal(frac.under.toString());

        over = (N)overA.multiply(underB);
        under = (N)underA.multiply(overB);

        simplify();

        return this;
    }

    /**
     * Converts under so that {@code under == base}, by doing {@code over = over * (base / under)} and {@code under = base}.
     * @param base
     * @return
     */
    @SuppressWarnings("unchecked")
    public Fraction<N> setBase(Integer base){
        over = (N)new BigDecimal(over.toString()).multiply(new BigDecimal(base / under.doubleValue()));
        under = (N)new BigDecimal(base);

        return this;
    }

    @Override
    public String toString(){
        return "{ %s | %s }".formatted(over.doubleValue(), under.doubleValue());
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
}