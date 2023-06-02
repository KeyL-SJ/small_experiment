package main.common;

import java.awt.Color;
import java.util.Random;

public class RandomColor {
	/** 获取指定长度的16进制字符串. */
    public static String randomHexStr(int len) {
        try {
            StringBuffer result = new StringBuffer();
            for(int i=0;i<len;i++) {
                //随机生成0-15的数值并转换成16进制
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            return result.toString().toUpperCase();
        } catch (Exception e) {
            System.out.println("获取16进制字符串异常，返回默认...");
            return "00CCCC";
        }
    }
 
    public static Color getRandomColor() {
        int color = Integer.valueOf(randomHexStr(6), 16);
        return new Color(color);
    }
}
