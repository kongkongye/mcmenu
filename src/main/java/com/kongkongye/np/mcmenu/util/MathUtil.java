package com.kongkongye.np.mcmenu.util;

import com.google.common.base.Preconditions;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtil {
    private static final DecimalFormat FORMAT_2 = new DecimalFormat("#.00");

    private static Random RANDOM = new Random();

    /**
     * 改变精度
     * @param num 需要改变的实数
     * @param accuracy 精确度,表示小数点后保留的位数,>=0,小于0会被当作0
     * @return 改变精度后的实数(返回的小数部分长度可能比精确度小)
     */
    public static double getDouble(double num,int accuracy) {
        if (accuracy < 0) {
            accuracy = 0;
        }

        DecimalFormat format;
        if (accuracy == 2) {
            format = FORMAT_2;
        } else {
            String f = "#.";
            for (int index=0;index<accuracy;index++) {
                f += "0";
            }
            format = new DecimalFormat(f);
        }
        return Double.parseDouble(format.format(num));
    }

    /**
     * 产生随机数
     * max>=min
     * @param min 最小值(可以是整数的最小值)
     * @param max 最大值(可以是整数的最大值-1,如果是整数的最大值则按最大值-1来看待)
     * @return [min, max]
     */
    public static int nextInt(int min, int max) {
        Preconditions.checkArgument(max >= min);

        if (min == max) {
            return min;
        }

        //防止溢出错误
        if (max == Integer.MAX_VALUE) {
            max--;
        }

        return ThreadLocalRandom.current().nextInt(min, max+1);
    }

    /**
     * 产生随机数
     * max>=min
     * @param min 最小值(可以是长整数的最小值)
     * @param max 最大值(可以是长整数的最大值-1,如果是长整数的最大值则按最大值-1来看待)
     * @return [min, max]
     */
    public static long nextLong(long min, long max) {
        Preconditions.checkArgument(max >= min);

        if (min == max) {
            return min;
        }

        //防止溢出错误
        if (max == Long.MAX_VALUE) {
            max--;
        }

        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    /**
     * 获取随机Int值
     */
    public static long randomInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    /**
     * 获取随机Long值
     */
    public static long randomLong() {
        return ThreadLocalRandom.current().nextLong();
    }
}
