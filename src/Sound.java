import javax.sound.sampled.*;

public class Sound {
    public static Clip load(String path) {
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(Sound.class.getResource(path));
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void play(Clip clip) {
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public static void loop(Clip clip) {
        if (clip == null) return;
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
