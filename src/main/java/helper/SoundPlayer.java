package helper;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {

    private Clip clip;
    private String filePath;
    private FloatControl gainControl;

    public SoundPlayer(String filePath) {
        this.filePath = filePath;
        loadSound();
    }

    private void loadSound() {
        try {
            URL url = getClass().getResource(this.filePath);

            if (url == null) {
                System.err.println("File audio tidak ditemukan di path: " + this.filePath);
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);

            this.clip = AudioSystem.getClip();
            this.clip.open(audioInputStream);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                this.gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } else {
                System.err.println("Kontrol Volume tidak didukung untuk file: " + this.filePath);
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Gagal memuat file WAV: " + this.filePath);
            e.printStackTrace();
        }
    }

    public void setVolume(double volume) {
        if (gainControl != null) {
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();

            double vol = Math.min(1.0, Math.max(0.0, volume));

            if (vol == 0.0) {
                gainControl.setValue(min);
            } else {
                float gain = (float) (Math.log10(vol) * 20.0);
                gainControl.setValue(Math.min(max, gain));
            }
        }
    }

    public double getVolume() { return gainControl.getValue(); }

    public void play() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}