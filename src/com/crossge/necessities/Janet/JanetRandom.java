package com.crossge.necessities.Janet;

import java.util.Random;

public class JanetRandom extends Random {
    public int rInt(int m) {//Returns a random int from 0 - m
        return (int) ((System.currentTimeMillis() + nextInt()) % m);
    }
}