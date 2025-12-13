package model.kitchen;

public interface GameStatusListener {
    void onStageCleared(int finalScore);
    void onGameOver(int finalScore);
    void onTimesUp();
}