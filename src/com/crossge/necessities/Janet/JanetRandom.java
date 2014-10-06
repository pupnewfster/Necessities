package com.crossge.necessities.Janet;

import java.util.Random;

public class JanetRandom {
	Random n = new Random();
	
	public int rInt(int m) {//Returns a random int from 0 - m
		return (int) ((System.currentTimeMillis() + n.nextInt()) % m);
	}
}