package com.lody.virtual.helper.utils;

import java.util.Random;

public class RandomUitls {
    private static final Random RANDOM = new Random();




    public static int nextInt(int startInclusive, int endExclusive) {

        return RANDOM.nextInt(endExclusive);
    }

    public static int nextInt() {
        return nextInt(0, Integer.MAX_VALUE);
    }


}
