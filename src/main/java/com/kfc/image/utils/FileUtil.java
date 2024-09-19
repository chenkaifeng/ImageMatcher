package com.kfc.image.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 文件工具类
 *
 * @author: Chenkf
 * @create: 2024/09/19
 **/
public class FileUtil {

    /**
     * 强制创建目录
     * @param dirPath
     */
    public static void forceMakeDir(String dirPath){
        File directory = new File(dirPath);

        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("目录创建成功: " + dirPath);
            } else {
                System.out.println("目录创建失败: " + dirPath);
            }
        } else {
            System.out.println("目录已存在: " + dirPath);
        }
    }

    /**
     * 打开图片
     * @param imagePath
     */
    public static void openImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (Desktop.isDesktopSupported() && imageFile.exists()) {
                Desktop.getDesktop().open(imageFile);
            } else {
                System.out.println("桌面不支持或文件不存在。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
