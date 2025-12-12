package helper;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {

    private Clip clip;
    private String filePath;
    private FloatControl gainControl; // Sudah dideklarasikan

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

            // --- TAMBAHAN: Dapatkan Kontrol Volume ---
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                this.gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } else {
                System.err.println("Kontrol Volume tidak didukung untuk file: " + this.filePath);
            }
            // --- END TAMBAHAN ---

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Gagal memuat file WAV: " + this.filePath);
            e.printStackTrace();
        }
    }

    // --- TAMBAHAN: Method setVolume ---
    /**
     * Mengatur volume suara.
     * @param volume Tingkat volume (0.0 = Mute, 1.0 = Volume Maks).
     */
    public void setVolume(double volume) {
        if (gainControl != null) {
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();

            // Batasi input volume (0.0 - 1.0)
            double vol = Math.min(1.0, Math.max(0.0, volume));

            // Gunakan konversi logaritmik untuk volume yang dipersepsikan lebih akurat
            if (vol == 0.0) {
                // Set ke minimum dB (Mute)
                gainControl.setValue(min);
            } else {
                // Konversi skala 0-1 ke desibel (dB)
                float gain = (float) (Math.log10(vol) * 20.0);
                // Batasi nilai yang dimasukkan
                gainControl.setValue(Math.min(max, gain));
            }
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