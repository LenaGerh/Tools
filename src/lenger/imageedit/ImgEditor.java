package lenger.imageedit;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;

/**
 * the Class ImgEditor gives Multithreading Functionality for the Algorithms found in {@link IEAlg}
 * @author Lenardt Gerhardts
 * @since 1.17.1
 */
public class ImgEditor {

    private static ImgEditor instance = null;

    @FunctionalInterface
    public interface MaskFunction{
        public BufferedImage masker(BufferedImage img, IEAInfo maInf);
    };
    private MaskFunction fn = IEAlg::contrastMaskColor;
    private List<MaskFunction> functions = new ArrayList<>();
    private int functionpointer = 0;

    private static class IEThread extends Thread{

        private MaskFunction fn;
        private BufferedImage in;
        private BufferedImage workingRect;
        private int posX, posY, width, height;
        private IEAInfo maInf;

        public IEThread(MaskFunction fn, BufferedImage in, int posX, int posY, int width, int height, IEAInfo maInf){
            this.fn = fn;
            this.in = in;
            this.posX = posX;
            this.posY = posY;
            this.width = width;
            this.height = height;
            this.maInf = maInf;
        }

        public BufferedImage getResult(){
            if(this.isAlive())
                return null;

            return workingRect;
        }


        @Override
        public void run(){
            super.run();
            workingRect = fn.masker(in.getSubimage(0, posY, width, height), maInf);
        }

        public void drawSelf(BufferedImage img){
            Graphics2D g2 = (Graphics2D)img.getGraphics();
            while(!g2.drawImage(getResult(), posX, posY, null));
            g2.dispose();
        }

        @Override
        public String toString(){
            String out = super.toString();
            return  out + ", posX: %d | posY: %d | width: %d | height: %d,".formatted(posX,posY,width,height) + "img:: width: %d | height: %d".formatted(in.getWidth(),in.getHeight());
        }
    }
    
    private static final int MAX_THREADS = 2048;

    private static ImgEditor get(){
        if(instance == null)
            instance = new ImgEditor();

        return instance;
    }

    private ImgEditor(){
    }
    
    /**
     * uses the Algorithm set by {@link #setMaskFunction() setMaskFunction}
     * @param img
     * @param maInf
     * @return {@link java.awt.image.BufferedImage}
     * @author Lenardt Gerhardts
     * @since 1.17.1
     */
    public static BufferedImage maskBufferedImage(BufferedImage img, IEAInfo maInf){
        int threadCount = img.getHeight() / 1;
        threadCount = threadCount > MAX_THREADS ? threadCount = MAX_THREADS : threadCount;
        threadCount = threadCount <= 0 ? threadCount = 1 : threadCount; 
        IEThread[] mtArr = new IEThread[threadCount];

        initMaskerThreads(img, maInf, mtArr);

        while(threadsRunning(mtArr));

        return stitchImg(mtArr);
    }

    /**
     * {@code fn} sets the currently used masking Algorithm used by {@link #maskBufferedImage(BufferedImage, IEAInfo) maskBufferedImage}
     * @param fn
     * @author Lenardt Gerhardts
     * @since 1.17.1
     */
    public static synchronized void setMaskFunction(MaskFunction fn){
        get().fn = fn;
    }

    /**
     * adds {@code fn} to a list of masking Algorithms which can be cycled through with {@link #nextFunction() nextFunction}
     * @param fn
     * @author Lenardt Gerhardts
     * @since 1.17.1
     */
    public static synchronized void addMaskFunction(MaskFunction fn){
        while(!get().functions.add(fn));
    }

    /**
     * cycles through the masking Algortihms added by {@link #addMaskFunction(MaskFunction) addMaskFunction}
     * @author Lenardt Gerhardts
     * @since 1.17.1
     */
    public static void nextFunction(){
        get().functionpointer++;

        if(get().functionpointer >= get().functions.size() || get().functionpointer < 0)
            get().functionpointer = 0;

        setMaskFunction(get().functions.get(get().functionpointer));
    }
    
    private static void initMaskerThreads(BufferedImage img, IEAInfo maInf, IEThread[] mtArr){
        int posY = 0;
        int tHeight = img.getHeight() / mtArr.length; 

        for(int i = 0; i < mtArr.length; i++){
            mtArr[i] = new IEThread(get().fn, img, 0, posY, img.getWidth(), tHeight, maInf);
            mtArr[i].start();
            posY += tHeight;
        }
    }

    private static boolean threadsRunning(Thread[] tArr){
        boolean running = false;

        for(int i = 0; i < tArr.length; i++){
            running = tArr[i].isAlive() || running;
        }

        return running;
    }

    private static BufferedImage stitchImg(IEThread[] mtArr){
        IEThread lastThread = mtArr[mtArr.length - 1];
        BufferedImage finalImg = new BufferedImage(lastThread.posX + lastThread.width, lastThread.posY + lastThread.height, BufferedImage.TYPE_INT_ARGB);
        for(IEThread t : mtArr){
            t.drawSelf(finalImg);
            //System.err.println(t.toString());
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return finalImg;
    }
}
