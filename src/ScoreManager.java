public class ScoreManager {
    private int currentScore;
    private int failedOrdersStreak;
    private int maxFailedStreak;

    public ScoreManager(int maxFailedStreak) {
        this.currentScore = 0;
        this.failedOrdersStreak = 0;
        this.maxFailedStreak = maxFailedStreak;
    }

    public void addScore(int points) {
        this.currentScore += points;
        this.failedOrdersStreak = 0;
        System.out.println(">> SKOR BERTAMBAH +" + points + "! Total: " + currentScore);
    }

    public void applyPenalty(int penalty) {
        this.currentScore -= penalty;
        this.failedOrdersStreak++;
        System.out.println(">> PENALTI -" + penalty + "! (Total: " + currentScore + ")");
        System.out.println(">> Warning: Gagal berturut-turut " + failedOrdersStreak + "/" + maxFailedStreak);
    }

    public boolean isTooManyFailed() {
        return failedOrdersStreak >= maxFailedStreak;
    }

    public int getCurrentScore() {
        return currentScore;
    }
}
