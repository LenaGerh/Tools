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

public class Sound {
    private Clip audioClip;
    private File audioFile;

    public Sound(String filepath){
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

    public Clip getClip(){
        return this.audioClip;
    }

    public void play(){
        createClip();
        if(getClip() != null){
            getClip().setFramePosition(0);
            getClip().start();
        }
    }

    public void stop(){
        if(getClip() != null)
            getClip().stop();
    }
}
