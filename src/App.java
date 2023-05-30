import lenger.util.Sound;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Sound sound = new Sound("src/lenger/util/mixkit-arcade-retro-game-over-213.wav");
        sound.play();
        Thread.sleep((long)(sound.getClip().getMicrosecondLength() * 0.001));
        sound.play();
        Thread.sleep((long)(sound.getClip().getMicrosecondLength() * 0.001));
    }
}
