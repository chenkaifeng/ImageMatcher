package com.kfc.image.utils;

import com.kfc.image.domain.ConfigItem;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageMatcher {

    static {
        // 加载OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        String largeFilePath = "C:\\Users\\kfc\\Pictures\\large-small-image\\scene\\hc2.jpg";
        String smallFilePath = "C:\\Users\\kfc\\Pictures\\large-small-image\\20240918232208.png";

        String outputFilePath = "C:\\Users\\kfc\\Pictures\\large-small-image\\output_image.jpg";
        match(largeFilePath, smallFilePath, outputFilePath);


    }

    public static List<String> match(List<ConfigItem> itemList, String smallFilePath){
        List<String> resultList = new ArrayList<>();
        String baseLargeFilePath = "C:\\Users\\kfc\\Pictures\\large-small-image\\scene";
        for (ConfigItem configItem : itemList) {
            System.out.println("当前场景: " + configItem.getLabel());
            String largeFilePath = baseLargeFilePath + File.separator + configItem.getFilename();
            String outputFilePath = baseLargeFilePath + File.separator + "result" + File.separator + "result_" + configItem.getFilename();

            match(largeFilePath, smallFilePath, outputFilePath);
            resultList.add(outputFilePath);
        }
        return resultList;

    }
    public static void match(String largeFilePath, String smallFilePath, String outputFilePath) {

        System.out.println("largeFilePath: " + largeFilePath);
        System.out.println("smallFilePath: " + smallFilePath);
        System.out.println("outputFilePath: " + outputFilePath);

        // 读取大图和小图
        Mat largeImage = Imgcodecs.imread(largeFilePath);
        Mat smallImage = Imgcodecs.imread(smallFilePath);

        // 创建SIFT检测器
        Feature2D detector = SIFT.create();
        // 创建 ORB 对象
        //ORB orb = ORB.create();
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


        // 将描述符转换为 CV_32F 类型
        //descriptorsLarge.convertTo(descriptorsLarge, CvType.CV_32F);
        //descriptorsSmall.convertTo(descriptorsSmall, CvType.CV_32F);
        //descriptorsLarge.convertTo(descriptorsLarge, CvType.CV_32F);
        //descriptorsSmall.convertTo(descriptorsSmall, CvType.CV_32F);

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
        Imgcodecs.imwrite(outputFilePath, outputImg);

        System.out.println("Matching completed.");
    }
}


