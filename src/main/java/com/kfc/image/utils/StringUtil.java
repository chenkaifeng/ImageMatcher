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

}
