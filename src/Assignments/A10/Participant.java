package Assignments.A10;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Participant implements Runnable {
    private final int sleepTime;
    private final int speed;
    private final int id;
    private int x;
    private int sleepsRemaining;
    private int sleepsUsed;

    private final ExecutorService executorService;
    public Participant(int sleepTime, int speed, int id) {
        this.sleepTime = sleepTime;
        this.speed = speed;
        this.id = id;
        x = 0;
        sleepsRemaining = 0;
        sleepsUsed = 0;
        executorService = Executors.newSingleThreadExecutor();
    }

    public int getX() {
        return x;
    }

    public void addSleep() {
        if (sleepsUsed + sleepsRemaining >= 2) return; // prevent sleeping more than twice per run
        sleepsRemaining++;
    }

    public int getSleeps() {
        return sleepsUsed + sleepsRemaining;
    }

    @Override
    public void run() {
        try{
            Thread.sleep(speed);
            if (sleepsRemaining > 0){
                sleepsUsed++;
                sleepsRemaining--;
                Thread.sleep(sleepTime);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        if (x >= 15 * 64){
            OptionPanel.onConclusion(id);
            executorService.shutdownNow();
        }
        else{
            x+=2;
            executorService.submit(this);
        }
    }
}
