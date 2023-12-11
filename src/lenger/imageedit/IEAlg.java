package lenger.imageedit;

import java.awt.image.BufferedImage;
import java.awt.Color;

/**
 * A collection of predefined Algorhithms
 * @author Lenardt Gerhardts
 * @since 17.0.1
 */
public class IEAlg {
    public static BufferedImage contrastMask(BufferedImage img, IEAInfo maInf){
        BufferedImage copyImg = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
        maInf.b_threshold = false;

        for(int x = 0; x < img.getWidth(); x++){
            for(int y = 0; y < img.getHeight(); y++){
                int pd = (int)(IPUtil.pixelDifference(img, x, y));
                pd *= (int)IPUtil.increaseIntensity(pd);
                //System.out.println(pd);
                copyImg.setRGB(x, y, new Color(pd,pd,pd,pd).getRGB());
            }
        }

        return copyImg;
    }

    public static BufferedImage pixelStrengthAlg(BufferedImage img, IEAInfo maInf){
        maInf.b_threshold = true;
        BufferedImage copyImg = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
        boolean[][] color = new boolean[img.getWidth()][img.getHeight()];
        Color temp = Color.BLACK;

        for(int x = 0; x < img.getWidth(); x++){
            for(int y = 0; y < img.getHeight(); y++){
                temp = IPUtil.rgbToColor(img.getRGB(x, y));
                color[x][y] = false;

                if((temp.getRed() + temp.getBlue() + temp.getGreen()) / 3 > maInf.threshold)
                    color[x][y] = true;

                copyImg.setRGB(x, y, color[x][y] ? new Color(255,255,255,0).getRGB() : new Color(0,0,0).getRGB());
            } 
        }      

        return copyImg;
    }

    /**
     * work in progress
     * @param img
     * @param maInf
     * @return
     */
    public static BufferedImage contrastMaskColor(BufferedImage img, IEAInfo maInf){
        maInf.b_threshold = true;
        return IPUtil.imgAvg(pixelStrengthAlg(img, maInf), img);
    }

    public static BufferedImage redColorMask(BufferedImage img, IEAInfo maInf){
        maInf.b_threshold = false;
        BufferedImage copyImg = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Color temp = Color.BLACK;
        for(int x = 0; x < img.getWidth(); x++){
            for(int y = 0; y < img.getHeight(); y++){
                temp = IPUtil.rgbToColor(img.getRGB(x,y));
                copyImg.setRGB(x, y, new Color(temp.getRed(),0,0,temp.getAlpha()).getRGB());
            } 
        }      

        return copyImg;
    }

    public static BufferedImage blueColorMask(BufferedImage img, IEAInfo maInf){
        maInf.b_threshold = false;
        BufferedImage copyImg = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Color temp = Color.BLACK;
        for(int x = 0; x < img.getWidth(); x++){
            for(int y = 0; y < img.getHeight(); y++){
                temp = IPUtil.rgbToColor(img.getRGB(x,y));
                copyImg.setRGB(x, y, new Color(0,0,temp.getBlue(),temp.getAlpha()).getRGB());
            } 
        }      

        return copyImg;
    }

    public static BufferedImage greenColorMask(BufferedImage img, IEAInfo maInf){
        maInf.b_threshold = false;
        BufferedImage copyImg = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Color temp = Color.BLACK;
        for(int x = 0; x < img.getWidth(); x++){
            for(int y = 0; y < img.getHeight(); y++){
                temp = IPUtil.rgbToColor(img.getRGB(x,y));
                copyImg.setRGB(x, y, new Color(0,temp.getGreen(),0,temp.getAlpha()).getRGB());
            } 
        }      

        return copyImg;
    }

    public static BufferedImage rgColorMask(BufferedImage img, IEAInfo maInf){
        return IPUtil.imgAvg(greenColorMask(img, maInf),redColorMask(img, maInf));
    }

    public static BufferedImage gbColorMask(BufferedImage img, IEAInfo maInf){
        return IPUtil.imgAvg(greenColorMask(img, maInf),blueColorMask(img, maInf));
    }

    public static BufferedImage rbColorMask(BufferedImage img, IEAInfo maInf){
        return IPUtil.imgAvg(blueColorMask(img, maInf),redColorMask(img, maInf));
    }

    public static BufferedImage colorstrengthChangeColor(BufferedImage pieceImg, IEAInfo info){
        info.b_threshold = true;
        BufferedImage copyImg = new BufferedImage(pieceImg.getWidth(),pieceImg.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Color[][] color = new Color[pieceImg.getWidth()][pieceImg.getHeight()];

        for(int x = 0; x < copyImg.getWidth(); x++){
            for(int y = 0; y < copyImg.getHeight(); y++){
                if(info.debug)
                    copyImg.setRGB(x,y,Color.PINK.getRGB());
                else{
                    color[x][y] = new Color(pieceImg.getRGB(x, y),true);
                    int alpha = color[x][y].getAlpha();
    
                    if((color[x][y].getRed() + color[x][y].getBlue() + color[x][y].getGreen()) / 3 > info.threshold)
                        color[x][y] = info.colors.get(0);
                    else
                        color[x][y] = info.colors.get(1);
    
                    copyImg.setRGB(x,y,IPUtil.changeColorAlpha(color[x][y], alpha).getRGB());
                }
            } 
        }      

        return copyImg;
    }
}
