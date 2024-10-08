package com.kfc.image.utils;

import com.kfc.image.cache.OpenCVDetectCache;
import com.kfc.image.domain.ConfigItem;
import com.kfc.image.domain.OpenCVDetectResult;
import com.kfc.image.domain.ResultItem;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ImageMatcher {

    static {
        // 加载OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // 设置OpenCV的并行线程数为 4
        //Core.setNumThreads(4);

        // 查看当前 OpenCV 使用的线程数
        int numThreads = Core.getNumThreads();
        System.out.println("当前使用的线程数: " + numThreads);
    }


    /**
     * 遍历匹配，多个大图，一个小图
     *
     * @param largeFileItems
     * @param smallFilePath
     * @return
     * @throws IOException
     */
    public static List<ResultItem> matchForEach(List<ConfigItem> largeFileItems, String smallFilePath) throws IOException {

        long beginTime = System.currentTimeMillis();

        //由于路径中带中文的话opencv会报错，重命名图片
        smallFilePath = FileUtil.copyAndRenameSmallImage(smallFilePath);

        // 创建SIFT检测器，自定义参数，更加精确
        Feature2D detector = SIFT.create(0, 4, 0.03, 10, 1.6);

        //读取小图
        Mat smallImage = Imgcodecs.imread(smallFilePath);

        // 检测小图特征点和计算描述符
        long time = System.currentTimeMillis();
        MatOfKeyPoint keypointsSmall = new MatOfKeyPoint();
        Mat descriptorsSmall = new Mat();
        detector.detectAndCompute(smallImage, new Mat(), keypointsSmall, descriptorsSmall);
        System.out.println("小图detectAndCompute耗时" + (System.currentTimeMillis() - time) + "ms");

        //定义最佳匹配
        Mat bestLargeImage = new Mat();
        MatOfKeyPoint bestKeypointsLarge = new MatOfKeyPoint();
        List<DMatch> bestGoodMatchesList = new ArrayList<>();
        double bestmatchRate = 0;

        //遍历每张场景大图进行匹配
        int i = 0;
        String baseLargeFilePath = SaveDirUtils.getSceneImageDir();
        List<ResultItem> resultList = new ArrayList<>();
        for (ConfigItem configItem : largeFileItems) {
            System.out.println(">>>匹配开始，当前场景: " + configItem.getLabel());
            long curtime = System.currentTimeMillis();
            String largeFilePath = baseLargeFilePath + File.separator + configItem.getFilename();
            String outputFilePath = SaveDirUtils.getOutputImageDir() + File.separator + "result_" + configItem.getFilename();

            System.out.println("largeFilePath: " + largeFilePath);
            System.out.println("smallFilePath: " + smallFilePath);
            System.out.println("outputFilePath: " + outputFilePath);

            // 读取大图
            time = System.currentTimeMillis();
            Mat largeImage = Imgcodecs.imread(largeFilePath);
            double largeImageSize = FileUtil.calculateMatSizeInMB(largeImage);
            System.out.println("大图read耗时" + (System.currentTimeMillis() - time) + "ms，大图MAT大小=" + largeImageSize + "（MB）");

            // 创建 FLANN 匹配器
            FlannBasedMatcher matcher = FlannBasedMatcher.create();

            // 检测大图特征点和计算描述符
            time = System.currentTimeMillis();
            OpenCVDetectResult openCVDetectResult = OpenCVDetectCache.getDetectResult(largeFilePath, detector, largeImage);
            MatOfKeyPoint keypointsLarge = openCVDetectResult.getMatOfKeyPoint();
            Mat descriptorsLarge = openCVDetectResult.getDescriptor();
            double largeDescriptorsSize = FileUtil.calculateMatSizeInMB(descriptorsLarge);
            System.out.println("大图detectAndCompute耗时" + (System.currentTimeMillis() - time) + "ms，大图特征点MAT大小=" + largeDescriptorsSize + "（MB）");


            // 匹配描述符
            time = System.currentTimeMillis();
            List<MatOfDMatch> matches = new ArrayList<>();
            matcher.knnMatch(descriptorsSmall, descriptorsLarge, matches, 2);
            System.out.println("knnMatch耗时" + (System.currentTimeMillis() - time) + "ms");

            // 过滤匹配结果
            float ratioThresh = 0.75f;
            List<DMatch> goodMatchesList = new ArrayList<>();
            for (MatOfDMatch matOfDMatch : matches) {
                DMatch[] dmatches = matOfDMatch.toArray();
                if (dmatches.length > 1 && (dmatches[0].distance < ratioThresh * dmatches[1].distance)) {
                    goodMatchesList.add(dmatches[0]);
                }
            }

            //计算匹配率
            double matchRate = (double) goodMatchesList.size() / keypointsSmall.toArray().length;
            System.out.println("Matching completed.匹配率=" + matchRate);

            //判断是否最佳匹配
            if(i++ == 0 || matchRate >= bestmatchRate){
                bestGoodMatchesList = goodMatchesList;
                bestKeypointsLarge = keypointsLarge;
                bestLargeImage = largeImage;
                bestmatchRate = matchRate;
            } else {
                largeImage.release();
            }


            ResultItem resultItem = new ResultItem();
            resultItem.setOutputFilePath(outputFilePath);
            resultItem.setLabel(configItem.getLabel());
            resultItem.setMatchRate(matchRate);
            resultList.add(resultItem);
            System.out.println("<<<场景结束: " + configItem.getLabel() + "，耗时" + (System.currentTimeMillis() - curtime) + "毫秒");
        }

        // 按匹配率降序排序
        List<ResultItem> sortedResult = resultList.stream()
                .sorted(Comparator.comparing(ResultItem::getMatchRate).reversed())
                .collect(Collectors.toList());
        ResultItem bestItem = sortedResult.get(0);


        // 绘制最佳匹配结果
        time = System.currentTimeMillis();
        Mat outputImg = new Mat();
        Features2d.drawMatches(smallImage, keypointsSmall, bestLargeImage, bestKeypointsLarge,
                new MatOfDMatch(bestGoodMatchesList.toArray(new DMatch[0])), outputImg);
        System.out.println("drawMatches耗时" + (System.currentTimeMillis() - time) + "ms");

        // 保存输出最佳匹配图像
        time = System.currentTimeMillis();
        Imgcodecs.imwrite(bestItem.getOutputFilePath(), outputImg);
        System.out.println("保存输出图像耗时" + (System.currentTimeMillis() - time) + "ms");


        // 可选，添加以下代码以释放内存
        smallImage.release();
        keypointsSmall.release();
        descriptorsSmall.release();
        outputImg.release();
        FileUtil.delete(smallFilePath);

        System.out.println("总共耗时：" + (System.currentTimeMillis() - beginTime) + "ms");
        return sortedResult;
    }
}


