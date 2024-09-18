package com.kfc.image.utils;

import com.kfc.image.domain.ConfigItem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 配置文件解析
 *
 * @author: Chenkf
 * @create: 2024/09/18
 **/
public class CsvConfigUtil {

    /**
     * 配置项
     */
    private static List<ConfigItem> configItems = new ArrayList<>();


    private static final String CONFIG_FILE_NAME = "config.csv";

    private static synchronized void init(){
        String fileName = CONFIG_FILE_NAME;
        System.out.println(">>>>>>开始读取配置");
        try (InputStream inputStream = CsvConfigUtil.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if( i++ == 0){
                    continue;
                }
                String[] values = line.split(",");
                if (values.length == 3) {
                    String id = values[0].trim();
                    String label = values[1].trim();
                    String filename = values[2].trim();
                    ConfigItem item = new ConfigItem(id, label, filename);
                    System.out.println(item);
                    configItems.add(item);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("加载配置文件发生异常", e);
        }
        System.out.println("<<<<<<读取完成");
    }


    /**
     * 获取配置项
     * @return
     * @throws IOException
     */
    public static synchronized List<ConfigItem> getConfigItems() {
        if (configItems.size() == 0) {
            init();
        }
        return configItems;
    }

    public static List<ConfigItem> getConfigItemsByName(List<String> names) {
        List<ConfigItem> list = getConfigItems();

        List<ConfigItem> resultList = list.stream()
                .filter(a -> names.contains(a.getLabel()))
                .collect(Collectors.toList());

        return resultList;
    }
}