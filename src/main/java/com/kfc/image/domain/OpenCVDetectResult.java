package com.kfc.image.domain;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

/**
 * openCV检测结果
 *
 * @author: Chenkf
 * @create: 2024/09/18
 **/
public class OpenCVDetectResult {

    /**
     * 检测特征点
     */
    private MatOfKeyPoint matOfKeyPoint;

    /**
     * 计算描述符
     */
    private Mat descriptor;

    public MatOfKeyPoint getMatOfKeyPoint() {
        return matOfKeyPoint;
    }

    public void setMatOfKeyPoint(MatOfKeyPoint matOfKeyPoint) {
        this.matOfKeyPoint = matOfKeyPoint;
    }

    public Mat getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Mat descriptor) {
        this.descriptor = descriptor;
    }
}
