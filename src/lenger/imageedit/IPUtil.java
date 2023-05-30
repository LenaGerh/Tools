package lenger.imageedit;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class IPUtil {
    
    public static Color rgbToColor(int rgb){
        return new Color(rgb);
    }

    public static float pixelDifference(BufferedImage img, int x, int y){
        float returnf = 0;
        int avgDiv = 0;

        if(x > 0){
            returnf += ColorDiff(rgbToColor(img.getRGB(x,y)),rgbToColor(img.getRGB(x-1,y)));
            avgDiv++;
        }
        if(x < img.getWidth() -1){
            returnf += ColorDiff(rgbToColor(img.getRGB(x,y)),rgbToColor(img.getRGB(x+1,y)));
            avgDiv++;
        }
        if(y > 0){
            returnf += ColorDiff(rgbToColor(img.getRGB(x,y)),rgbToColor(img.getRGB(x,y-1)));
            avgDiv++;
        }
        if(y < img.getHeight() -1){
            returnf += ColorDiff(rgbToColor(img.getRGB(x,y)),rgbToColor(img.getRGB(x,y+1)));
            avgDiv++;
        }

        return returnf/avgDiv;
    }

    public static float ColorDiff(Color col1, Color col2){
        return (difference(col1.getRed(), col2.getRed()) + difference(col1.getBlue(), col2.getBlue()) + difference(col1.getGreen(), col2.getGreen()) + difference(col1.getAlpha(), col2.getAlpha())) / 4;
    }

    public static float difference(float a, float b){
        return Math.max(a, b) - Math.min(a, b);
    }

    public static float increaseIntensity(float in){
        float f = (float)Math.exp(-in) + 1;
        return f;
    }

    public static BufferedImage imgAvg(BufferedImage img1, BufferedImage img2){
        BufferedImage copyImg = new BufferedImage(Math.max(img1.getWidth(),img2.getWidth()), Math.max(img1.getHeight(),img2.getHeight()), BufferedImage.TYPE_INT_ARGB);
        
        for(int x = 0; x < copyImg.getWidth(); x++){
            for(int y = 0; y < copyImg.getHeight(); y++){
                if(coordinateInbound(img1, x, y) && coordinateInbound(img2, x, y)){
                    copyImg.setRGB(x, y, getColorAverage(false,rgbToColor(img1.getRGB(x, y)),rgbToColor(img2.getRGB(x, y))).getRGB());
                }
                else if(coordinateInbound(img1, x, y)){
                    copyImg.setRGB(x, y, img1.getRGB(x, y));
                }
                else if(coordinateInbound(img2, x, y)){
                    copyImg.setRGB(x, y, img2.getRGB(x, y));
                }
            }
        }

        return copyImg;
    }

    public static float getRatioXtoY(BufferedImage in){
        return (float)in.getHeight() / (float)in.getWidth();
    }

    public static float getRatioYtoX(BufferedImage in){
        return (float)in.getWidth() / (float)in.getHeight();
    }

    public static boolean coordinateInbound(BufferedImage img, int x, int y){
        if(x < 0 || y < 0 || img.getWidth() <= x || img.getHeight() <= y)
            return false;
        
        return true;
    }

    public static Color getColorAverage(boolean changeAlpha, Color... arg){
        float red = 0, green = 0, blue = 0, alpha = 0;

        for(Color c : arg){
            red+=c.getRed();
            green+=c.getGreen();
            blue+=c.getBlue();
            alpha+=c.getAlpha();
        }
        red/=arg.length;
        green/=arg.length;
        blue/=arg.length;
        alpha/=arg.length;

        if(!changeAlpha)
            alpha = 255;
        
        return new Color((int)red,(int)green,(int)blue,(int) alpha);
    }

    public static Color inverseColor(Color color) {
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
    }

    public static Color changeColorAlpha(Color originColor, int alpha){
        return new Color(originColor.getRed(), originColor.getGreen(), originColor.getBlue(), alpha);
    }
}
