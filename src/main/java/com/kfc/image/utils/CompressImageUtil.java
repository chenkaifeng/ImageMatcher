package com.kfc.image.utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CompressImageUtil {

    static {
        // 加载OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        String filePath = SaveDirUtils.getSceneImageDir() + File.separator + "hys.jpg";

        //缩放比例
        double scale = 1.1;


        String outputFilePath = filePath.replace(".jpg", "") + StringUtil.formatDouble(scale) + ".jpg";


        Mat originalImage = Imgcodecs.imread(filePath);
        Mat resizedImage = new Mat();
        Imgproc.resize(originalImage, resizedImage, new Size(originalImage.width() / scale, originalImage.height() / scale));


        Imgcodecs.imwrite(outputFilePath, resizedImage);
        System.out.println("压缩完成：" + outputFilePath);

    }
}
