package com.kfc.image.utils;

import com.kfc.image.domain.ConfigItem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
    public static void initDir(List<ConfigItem> configItems){
        //初始化目录
        FileUtil.forceMakeDir(getSceneImageDir());
        FileUtil.forceMakeDir(getMatchImageDir());
        FileUtil.forceMakeDir(getOutputImageDir());

        //初始化场景文件
        for (ConfigItem configItem : configItems) {
            String fileName = configItem.getFilename();
            String classPathFile = "scene" + File.separator + fileName;
            try (InputStream inputStream = CsvConfigUtil.class.getClassLoader().getResourceAsStream(classPathFile)) {
                if (inputStream != null) {
                    // 目标路径，替换为你要拷贝到的目录
                    Path destinationPath = Paths.get(getSceneImageDir() + File.separator + fileName);
                    if(!Files.exists(destinationPath)){
                        System.out.println("拷贝场景文件到scene目录：" + fileName);
                        Files.copy(inputStream, destinationPath);
                    }else {
                        System.out.println("场景文件已存在：" + fileName);
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException("加载配置文件发生异常", e);
            }
        }
    }


}
