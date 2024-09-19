package com.kfc.image.utils;

import java.text.NumberFormat;

/**
 * 字符串工具类
 *
 * @author: Chenkf
 * @create: 2024/09/19
 **/
public class StringUtil {

    /**
     * 转成百分比
     * @param value
     */
    public static String percentFormat(double value){
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMinimumFractionDigits(3);
        percentFormat.setMaximumFractionDigits(3);
        return percentFormat.format(value);
    }

    /**
     * 数值为整数时不保留小数点
     * @param value
     * @return
     */
    public static String formatDouble(double value) {
        if (value % 1 == 0) { // 检查是否为整数
            return String.valueOf((int) value); // 转换为整数类型
        } else {
            return String.valueOf(value); // 保留小数部分
        }
    }
}
