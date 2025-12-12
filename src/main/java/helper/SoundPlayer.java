package helper;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {

    private Clip clip;
    private String filePath;

    public SoundPlayer(String filePath) {
        this.filePath = filePath;
        loadSound(); // Panggil method non-static
    }

    // --- DIHAPUS 'static' agar bisa mengakses 'this.filePath' dan 'this.clip' ---
    private void loadSound() {
        try {
            // Menggunakan getClass() di method non-static ini adalah benar
            URL url = getClass().getResource(this.filePath);

            if (url == null) {
                // Gunakan nama file yang dicari di pesan error
                System.err.println("File audio tidak ditemukan di path: " + this.filePath);
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);

            this.clip = AudioSystem.getClip();
            this.clip.open(audioInputStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Gagal memuat file WAV: " + this.filePath);
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop(); // Hentikan pemutaran sebelumnya
            }
            clip.setFramePosition(0); // Kembali ke awal
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            // Pastikan tidak ada pemutaran lain yang aktif
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Atur agar berulang
            // clip.start() akan dipanggil otomatis oleh clip.loop
            // Namun, memanggilnya di sini tidak masalah.
            clip.start();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    public void close() {
        if (clip != null) {
            clip.close();
        }
    }
}