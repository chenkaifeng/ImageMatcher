package com.kfc.image.cache;

import com.kfc.image.domain.OpenCVDetectResult;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.Feature2D;


import java.util.HashMap;
import java.util.Map;

/**
 * openCV检测结果缓存，用于场景图
 *
 * @author: Chenkf
 * @create: 2024/09/18
 **/
public class OpenCVDetectCache {

    /**
     * 缓存
     */
    private static Map<String, OpenCVDetectResult> openCVDetectCache = new HashMap<>();

    /**
     * 获取场景图检测结果，加上缓存
     * @param key   缓存key，建议传图片路径
     * @param detector
     * @param largeImage
     * @return
     */
    public synchronized static OpenCVDetectResult getDetectResult(String key, Feature2D detector, Mat largeImage){

        if(openCVDetectCache.containsKey(key)){
            return openCVDetectCache.get(key);
        }

        MatOfKeyPoint keypointsLarge = new MatOfKeyPoint();
        Mat descriptorsLarge = new Mat();
        detector.detectAndCompute(largeImage, new Mat(), keypointsLarge, descriptorsLarge);

        OpenCVDetectResult openCVDetectResult = new OpenCVDetectResult();
        openCVDetectResult.setMatOfKeyPoint(keypointsLarge);
        openCVDetectResult.setDescriptor(descriptorsLarge);
        openCVDetectCache.put(key,openCVDetectResult);
        return openCVDetectResult;

    }


}
