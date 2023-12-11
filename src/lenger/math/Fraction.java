package lenger.math;

import java.math.BigDecimal;
/**
 * Implements Fraction representation for {@link Number}
 * @author Lenardt Gerhardts
 * @since 21
 */
public class Fraction<N extends Number> extends Number{
    
    private N over, under;

    /**
     * Creates Fraction {@code (0 | 1)}
     */
    @SuppressWarnings("unchecked")
    public Fraction(){
        set((N)new BigDecimal(0),(N)new BigDecimal(1));
    }

    /**
     * Creates Fraction {@code (over | under)}. Throws {@link IllegalStateException} if {@code under == 0} 
     * @param over
     * @param under
     */
    public Fraction(N over, N under){
        set(over, under);
    }

    /**
     * 
     * @param toFrac
     */
    @SuppressWarnings("unchecked")
    public Fraction(Number toFrac){
        set((N)new BigDecimal(toFrac.toString()));
    }

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

    @SuppressWarnings("unchecked")
    public void set(N number){
        this.set(number, (N)new BigDecimal(1));
    }

    public void set(N over, N under){
        if(under.doubleValue() == 0)
            throw new IllegalStateException("Member \"under\" is not allowed to be Zero");

        this.over = over;
        this.under = under;

        simplify();
    }

    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    @SuppressWarnings("unchecked")
    public void simplify(){
        Long gcd = gcd(over.longValue(), under.longValue());

        over = (N)new BigDecimal(over.toString()).divide(new BigDecimal(gcd.toString()));
        under = (N)new BigDecimal(under.toString()).divide(new BigDecimal(gcd.toString()));
    }

    public <T extends Number> Fraction<N> add(T number){
        return add(new Fraction<N>(number));
    }

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

    public <T extends Number> Fraction<N> sub(T number){
        return sub(new Fraction<N>(number));
    }

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

    
    public <T extends Number> Fraction<N> mul(T number){
        return mul(new Fraction<N>(number));
    }

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
    
    
    public <T extends Number> Fraction<N> div(T number){
        return div(new Fraction<T>(number));
    }

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

    /**
     * returns Decimal representation of {@link Fraction}
     * @return double
     * @since 21
     */
    @Override
    public double doubleValue() {
        return over.doubleValue() / under.doubleValue();
    }
}