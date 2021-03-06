package Manager;

import java.util.HashMap;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public class SoundManager {
	
    private float MASTER_VOLUME = 0.1f;
    
    private static SoundManager instance;
    private static HashMap<Sound, Clip> list;
    
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    public SoundManager() {
        list = new HashMap<>();
    }
    
    private void loadSound(Sound sound) {
        if (!list.containsKey(sound)) {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream(sound.getPath()));
                AudioFormat af = audioInputStream.getFormat();
                int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
                byte[] audio = new byte[size];
                DataLine.Info info = new DataLine.Info(Clip.class, af, size);
                audioInputStream.read(audio, 0, size);
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(af, audio, 0, size);
                FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                control.setValue(20f * (float) Math.log10(MASTER_VOLUME));
                list.put(sound, clip);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void setVolume(float volume) {
        this.MASTER_VOLUME = volume;
    }
    
    public void playSound(Sound sound) {
        try {
            loadSound(sound);
            Clip clip = list.get(sound);
            clip.setMicrosecondPosition(0);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public enum Sound {

        TEST("/Sounds/glassbell.wav"),
        TEST2("/Sounds/chipquest.wav");

        String path;

        Sound(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
