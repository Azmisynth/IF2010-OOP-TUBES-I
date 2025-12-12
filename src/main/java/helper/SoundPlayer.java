package helper;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {

    private Clip clip;
    private String filePath;

    public SoundPlayer(String filePath) {
        this.filePath = filePath;
        loadSound();
    }

    private void loadSound() {
        try {
            URL url = getClass().getResource(filePath);
            if (url == null) {
                System.err.println("File audio tidak ditemukan di path: " + filePath);
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Gagal memuat file WAV: " + filePath);
            e.printStackTrace();
        }
    }

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