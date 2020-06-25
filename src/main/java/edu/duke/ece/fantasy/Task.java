package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.MonsterManger;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.json.PositionResultMessage;
import org.hibernate.Session;
import org.hibernate.SharedSessionContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Task {
    private long when; // the time when the task should be executed
    private int repeatedInterval;
    private boolean repeating;

    public Task(long when, int repeatedInterval, boolean repeating) {
        this.when = when;
        this.repeatedInterval = repeatedInterval;
        this.repeating = repeating;
    }

    abstract void doTask();

    public int getRepeatedInterval() { return repeatedInterval; }

    public void setRepeatedInterval(int repeatedInterval) { this.repeatedInterval = repeatedInterval; }

    public long getWhen() { return when; }

    public void setWhen(long when) { this.when = when; }

    public void updateWhen(){ this.when += repeatedInterval; }

    public boolean isRepeating() { return repeating; }

}
