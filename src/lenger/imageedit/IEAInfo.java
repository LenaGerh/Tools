package lenger.imageedit;

import java.util.ArrayList;
import java.util.List;

import java.awt.Color;

/**
 * this class is a variable Carriage class
 * @author Lenardt Gerhardts
 * @since 1.17.1
 */

public class IEAInfo {
    // actual function values
    public float threshold = 0;
    public float threshold_min = 0;
    public float threshold_max = 255;

    public List<Color> colors = new ArrayList<>();

    // boolean wich tells your gui of choice if the function currently used, needs a value
    public boolean b_threshold = true;
    public boolean debug = false;

    public void addColors(Color... colors){
        for(Color c : colors){
            this.colors.add(c);
        }
    }

    public void falseAll(){
        b_threshold = false;
    }

    public void trueAll(){
        b_threshold = true;
    }
}
