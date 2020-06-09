package edu.duke.ece.fantasy;

import java.util.Random;

public class RandomGenerator {
  public static Random random = new Random();

  public static boolean getRandomResult(int possibility) {
    int bound = random.nextInt(100);
    return possibility > bound;
  }

}
