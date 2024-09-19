package com.kfc.image.utils;

import java.io.File;

/**
 * 文件保存目录
 * @author: Chenkf
 * @create: 2024/09/19
 **/
public class SaveDirUtils {

    /**
     * 获取保存根目录，如：windows为 C:\Users\当前用户
     * @return
     */
    public static String getRootSaveDir() {
        //windows系统(user.dir可能需要管理员权限)，使用user.home
        return System.getProperty("user.home").concat(File.separator).concat("image-matcher");
    }

    /**
     * 场景图保存目录
     * @return
     */
    public static String getSceneImageDir() {
        //windows系统(user.dir可能需要管理员权限)，使用user.home
        return getRootSaveDir().concat(File.separator).concat("scene");
    }

    /**
     * 匹配结果图保存目录
     * @return
     */
    public static String getOutputImageDir() {
        //windows系统(user.dir可能需要管理员权限)，使用user.home
        return getRootSaveDir().concat(File.separator).concat("result");
    }

    /**
     * 待匹配图保存目录
     * @return
     */
    public static String getMatchImageDir() {
        //windows系统(user.dir可能需要管理员权限)，使用user.home
        return getRootSaveDir().concat(File.separator).concat("match");
    }

    /**
     * 初始化目录
     */
    public static void initDir(){
        FileUtil.forceMakeDir(getSceneImageDir());
        FileUtil.forceMakeDir(getMatchImageDir());
        FileUtil.forceMakeDir(getOutputImageDir());
    }


}
