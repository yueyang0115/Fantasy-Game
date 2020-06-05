package edu.duke.ece.fantasy;

import java.util.Random;

public class RandomGenerator {
    Random random = new Random();

    public boolean getRandomResult(int possibility) {
        int bound = random.nextInt(100);
        return possibility > bound;
    }
}
