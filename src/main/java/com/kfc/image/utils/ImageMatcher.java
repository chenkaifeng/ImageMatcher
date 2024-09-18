package com.kfc.image.utils;

import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ImageMatcher {

    static {
        // 加载OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        String largeFilePath = "C:\\Users\\ChenKF\\Pictures\\large-small-image\\dtd.jpg";
        String smallFilePath = "C:\\Users\\ChenKF\\Pictures\\large-small-image\\20240918113011.png";
        match(largeFilePath, smallFilePath);


    }

    private static void match(String largeFilePath, String smallFilePath) {
        // 读取大图和小图
        Mat largeImage = Imgcodecs.imread(largeFilePath);
        Mat smallImage = Imgcodecs.imread(smallFilePath);

        // 创建SIFT检测器
        Feature2D detector = SIFT.create();

        //DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);

        // 创建 FLANN 匹配器
        FlannBasedMatcher matcher = FlannBasedMatcher.create();

        // 检测特征点和计算描述符
        MatOfKeyPoint keypointsLarge = new MatOfKeyPoint();
        Mat descriptorsLarge = new Mat();
        detector.detectAndCompute(largeImage, new Mat(), keypointsLarge, descriptorsLarge);

        MatOfKeyPoint keypointsSmall = new MatOfKeyPoint();
        Mat descriptorsSmall = new Mat();
        detector.detectAndCompute(smallImage, new Mat(), keypointsSmall, descriptorsSmall);

        // 匹配描述符
        List<MatOfDMatch> matches = new ArrayList<>();
        matcher.knnMatch(descriptorsSmall, descriptorsLarge, matches, 2);

        // 过滤匹配结果
        float ratioThresh = 0.75f;
        List<DMatch> goodMatchesList = new ArrayList<>();
        for (MatOfDMatch matOfDMatch : matches) {
            DMatch[] dmatches = matOfDMatch.toArray();
            if (dmatches.length > 1 && (dmatches[0].distance < ratioThresh * dmatches[1].distance)) {
                goodMatchesList.add(dmatches[0]);
            }
        }

        // 绘制匹配结果
        Mat outputImg = new Mat();
        Features2d.drawMatches(smallImage, keypointsSmall, largeImage, keypointsLarge,
                new MatOfDMatch(goodMatchesList.toArray(new DMatch[0])), outputImg);

        // 保存输出图像
        Imgcodecs.imwrite("C:\\Users\\ChenKF\\Pictures\\large-small-image\\output_image.jpg", outputImg);

        System.out.println("Matching completed.");
    }
}


