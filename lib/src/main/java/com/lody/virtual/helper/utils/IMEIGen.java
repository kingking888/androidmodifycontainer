package com.lody.virtual.helper.utils;

public class IMEIGen {
    public static  IMEIGen INSTANCE = null;

    static {
        IMEIGen iMEIGen = new IMEIGen();
    }

    private IMEIGen() {
        INSTANCE = this;
    }


    public final String genCode( String code) {

        int sum1 = 0;
        int sum2 = 0;
        char[] chs = code.toCharArray();
        int length = chs.length;
        for (int i = 0; i < length; i++) {
            int num = chs[i] - 48;
            if (i % 2 == 0) {
                sum1 += num;
            } else {
                int temp = num * 2;
                if (temp < 10) {
                    sum2 += temp;
                } else {
                    sum2 = ((sum2 + temp) + 1) - 10;
                }
            }
        }
        int total = sum1 + sum2;
        if (total % 10 == 0) {
            return "0";
        }
        return String.valueOf(10 - (total % 10)) + "";
    }
}
