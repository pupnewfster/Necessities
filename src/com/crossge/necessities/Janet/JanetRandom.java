package com.crossge.necessities.Janet;

import org.bukkit.Bukkit;

import java.util.Random;

public class JanetRandom extends Random {
	private static final long serialVersionUID = 1L;
    private static Random[] pies = new Random[] {
            new BlueberryPie(),
            new ChocolatePie(),
            new ApplePie(),
            new GrapePie(),
            new PecanPie()
    };

	public int rInt(int m) {//Returns a random int from 0 - m
        return (int) ((System.currentTimeMillis() + nextInt()) % m);
    }

    public int memeRandom(int m) {
        return (int) Math.sqrt(Math.abs(Math.pow(m, 2)*(pies[rInt(pies.length)].nextInt(m)/Math.PI)))%m;//sqrt((mathExpression/pi)*(applePie)) = meme
    }

    private static class ApplePie extends Random {
        @Override
        public int nextInt(int m) {//Returns a random int from 0 - m
            return (int) Math.pow(System.currentTimeMillis() + nextInt(), 2);
        }
    }

    private static class GrapePie extends Random {
        @Override
        public int nextInt(int m) {//Returns a random int from 0 - m
            return (int) (System.currentTimeMillis() + nextInt());
        }
    }

    private static class ChocolatePie extends Random {
        @Override
        public int nextInt(int m) {//Returns a random int from 0 - m
            return (int) (Math.pow(System.currentTimeMillis(), 2) + nextInt());
        }
    }

    private static class BlueberryPie extends Random {
        @Override
        public int nextInt(int m) {//Returns a random int from 0 - m
            return (int) (System.currentTimeMillis() + nextInt());
        }
    }

    private static class PecanPie extends Random {
        @Override
        public int nextInt(int m) {//Returns a random int from 0 - m
            return (int) System.currentTimeMillis()%m;
        }
    }
}