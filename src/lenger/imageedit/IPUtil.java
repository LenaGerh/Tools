package lenger.imageedit;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Utility Class for Image Processing purposes
 * 
 * @author Lenardt Gerhardts
 * @since 17.0.1
 */
public class IPUtil {

    /**
     * simple Function that turns a rgb {@code Integer} into a
     * {@link java.awt.Color} object
     * 
     * @param rgb
     * @return {@link java.awt.Color}
     * @author Lenardt Gerhardts
     * @since 17.0.1
     */
    public static Color rgbToColor(int rgb) {
        return new Color(rgb);
    }

    /**
     * Returns a {@code Float} between {@code 0 - 255} based on the difference
     * between the surounding Pixels
     * 
     * @author Lenardt Gerhardts
     * @since 17.0.1
     * @param img
     * @param x
     * @param y
     * @return float
     */
    public static float pixelDifference(BufferedImage img, int x, int y) {
        float returnf = 0;
        int avgDivCount = 0;

        if (x > 0) {
            returnf += ColorDiff(rgbToColor(img.getRGB(x, y)), rgbToColor(img.getRGB(x - 1, y)));
            avgDivCount++;
        }
        if (x < img.getWidth() - 1) {
            returnf += ColorDiff(rgbToColor(img.getRGB(x, y)), rgbToColor(img.getRGB(x + 1, y)));
            avgDivCount++;
        }
        if (y > 0) {
            returnf += ColorDiff(rgbToColor(img.getRGB(x, y)), rgbToColor(img.getRGB(x, y - 1)));
            avgDivCount++;
        }
        if (y < img.getHeight() - 1) {
            returnf += ColorDiff(rgbToColor(img.getRGB(x, y)), rgbToColor(img.getRGB(x, y + 1)));
            avgDivCount++;
        }

        return returnf / avgDivCount;
    }

    /**
     * Returns a {@code Float} between {@code 0 - 255} based on the difference
     * between the two colors {@code col1} and {@code col2}
     * 
     * @author Lenardt Gerhardts
     * @since 17.0.1
     * @param col1
     * @param col2
     * @return float
     */
    public static float ColorDiff(Color col1, Color col2) {
        return (difference(col1.getRed(), col2.getRed()) +
                difference(col1.getBlue(), col2.getBlue()) +
                difference(col1.getGreen(), col2.getGreen()) +
                difference(col1.getAlpha(), col2.getAlpha())) / 4;
    }

    /**
     * Returns the differnce between the two floats {@code a} and {@code b}
     * 
     * <p>
     * will always return a Positive number
     * </p>
     * 
     * @author Lenardt Gerhardts
     * @since 17.0.1
     * @param a
     * @param b
     * @return float
     */
    public static float difference(float a, float b) {
        return Math.max(a, b) - Math.min(a, b);
    }

    public static float increaseIntensity(float in) {
        float f = (float) Math.exp(-in) + 1;
        return f;
    }

    public static BufferedImage imgAvg(BufferedImage img1, BufferedImage img2) {
        BufferedImage copyImg = new BufferedImage(Math.max(img1.getWidth(), img2.getWidth()),
                Math.max(img1.getHeight(), img2.getHeight()), BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < copyImg.getWidth(); x++) {
            for (int y = 0; y < copyImg.getHeight(); y++) {
                if (coordinateInbound(img1, x, y) && coordinateInbound(img2, x, y)) {
                    copyImg.setRGB(x, y,
                            getColorAverage(false, rgbToColor(img1.getRGB(x, y)), rgbToColor(img2.getRGB(x, y)))
                                    .getRGB());
                } else if (coordinateInbound(img1, x, y)) {
                    copyImg.setRGB(x, y, img1.getRGB(x, y));
                } else if (coordinateInbound(img2, x, y)) {
                    copyImg.setRGB(x, y, img2.getRGB(x, y));
                }
            }
        }

        return copyImg;
    }

    /**
     * returns Height to Width ratio from Image {@code in}
     * @author Lenardt Gerhardts
     * @since 17.0.1
     * @param in
     * @return float
     */
    public static float getRatioXtoY(BufferedImage in) {
        return (float) in.getHeight() / (float) in.getWidth();
    }

    /**
     * returns Width to Height ration from Image {@code in}
     * @author Lenardt Gerhardts
     * @since 17.0.1
     * @param in
     * @return float
     */
    public static float getRatioYtoX(BufferedImage in) {
        return (float) in.getWidth() / (float) in.getHeight();
    }

    /**
     * returns {@code true} when {@code x} and {@code y} are inboud of {@code img}
     * @param img
     * @param x
     * @param y
     * @return boolean
     * @author Lenardt Gerhardts
     * @since 17.0.1
     */
    public static boolean coordinateInbound(BufferedImage img, int x, int y) {
        if (x < 0 || y < 0 || img.getWidth() <= x || img.getHeight() <= y)
            return false;

        return true;
    }

    /**
     * Returns all the average between Colors given to {@code arg}
     * 
     * <p>when {@code changeAlpha} is {@code false} the alpha value will always be 255</p>
     * <p>when {@code changeAlpha} is {@code true} the alpha value will be a average between the colors, like the rgb values</p>
     * @param changeAlpha
     * @param arg
     * @return {@link java.awt.Color}
     * @author Lenardt Gerhardts
     * @since 17.0.1
     */
    public static Color getColorAverage(boolean changeAlpha, Color... arg) {
        float red = 0, green = 0, blue = 0, alpha = 0;

        for (Color c : arg) {
            red += c.getRed();
            green += c.getGreen();
            blue += c.getBlue();
            alpha += c.getAlpha();
        }
        red /= arg.length;
        green /= arg.length;
        blue /= arg.length;
        alpha /= arg.length;

        if (!changeAlpha)
            alpha = 255;

        return new Color((int) red, (int) green, (int) blue, (int) alpha);
    }

    /**
     * returns a inversed {@link java.awt.Color}
     * @author Lenardt Gerhardts
     * @since 17.0.1
     * @param color
     * @return {@link java.awt.Color}
     */
    public static Color inverseColor(Color color) {
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
    }

    /**
     * returns same Color as {@code originColor} with the given {@code alpha} value
     * @author Lenardt Gerhardts
     * @since 17.0.1
     * @param originColor
     * @param alpha
     * @return {@link java.awt.Color}
     */
    public static Color changeColorAlpha(Color originColor, int alpha) {
        return new Color(originColor.getRed(), originColor.getGreen(), originColor.getBlue(), alpha);
    }
}
