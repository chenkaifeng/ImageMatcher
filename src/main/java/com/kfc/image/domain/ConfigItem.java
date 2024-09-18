package com.kfc.image.domain;

/**
 * 配置项
 *
 * @author: Chenkf
 * @create: 2024/09/18
 **/
public class ConfigItem {
    private String id;
    private String label;
    private String filename;

    public ConfigItem(String id, String label, String filename) {
        this.id = id;
        this.label = label;
        this.filename = filename;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public String toString() {
        return "ConfigItem{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }
}
