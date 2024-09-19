package com.kfc.image.domain;

/**
 * 配置结果
 *
 * @author: Chenkf
 * @create: 2024/09/18
 **/
public class ResultItem {
    /**
     * 输出结果图
     */
    private String outputFilePath;

    /**
     * 所属场景名称
     */
    private String label;

    /**
     * 匹配率
     */
    private double matchRate;

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getMatchRate() {
        return matchRate;
    }

    public void setMatchRate(double matchRate) {
        this.matchRate = matchRate;
    }

    @Override
    public String toString() {
        return "ResultItem{" +
                "outputFilePath='" + outputFilePath + '\'' +
                ", label='" + label + '\'' +
                ", matchRate=" + matchRate +
                '}';
    }
}
