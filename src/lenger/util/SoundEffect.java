package lenger.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * small utility Class for playing short SoundEffects
 * @author Lenardt Gerhardts
 * @since 17.0.1
 */
public class SoundEffect {
    private Clip audioClip;
    private File audioFile;

    private final double MICRO_TO_MILLIS = 0.001;

    public SoundEffect(String filepath){
        audioFile = new File(filepath);
        createClip();
    }

    private Clip createClip(){
        try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            return audioClip;
        } catch (UnsupportedAudioFileException e){
            System.err.println("filepath: \"%s\" does not have a valid format".formatted(audioFile.getAbsolutePath()));
            e.printStackTrace();
        } catch(LineUnavailableException | IOException e){
            e.printStackTrace();
        }

        return null;
    }

    private Clip getClip(){
        return this.audioClip;
    }

    /**
     * plays the the Sound File defined in the Constructor
     * @author Lenardt Gerhardts
     * @since 1.17.1
     */
    public void play(){
        createClip();
        if(getClip() != null){
            getClip().setFramePosition(0);
            getClip().start();
        }
    }

    /**
     * stops the soundeffect from being played further
     * @author Lenardt Gerhardts
     * @since 1.17.1
     */
    public void stop(){
        if(getClip() != null)
            getClip().stop();
    }

    /**
     * returns Soundeffect Length in Milliseconds
     * @return long
     * @author Lenardt Gerhardts
     * @since 1.17.1
     */
    public long getMilliseconds(){
        if(this.getClip() != null)
            return (long)(this.getClip().getMicrosecondLength() * MICRO_TO_MILLIS);

        return 0;
    }
}
