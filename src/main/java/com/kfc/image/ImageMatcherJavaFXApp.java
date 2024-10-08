package com.kfc.image;

import com.kfc.image.domain.ConfigItem;
import com.kfc.image.domain.ResultItem;
import com.kfc.image.utils.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Chenkf
 * @create: 2024/09/18
 **/
public class ImageMatcherJavaFXApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle("-fx-padding: 20; -fx-background-color: #f0f0f0;");

        FileChooser fileChooser = new FileChooser();
        Button chooseButton = new Button("选择图片");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        Label instructions = new Label("请选择你想查的场景:");
        instructions.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");


        Label imagePath = new Label();
        imagePath.setStyle("-fx-font-size: 10px;");



        //初始化
        List<ConfigItem> configItems = CsvConfigUtil.getConfigItems();
        SaveDirUtils.initDir(configItems);



        CheckBox[] checkBoxes = new CheckBox[configItems.size()];
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i] = new CheckBox(configItems.get(i).getLabel());
            grid.add(checkBoxes[i], i % 3, (i / 3) + 2); // 更新位置，留出空间给说明
        }

        // 全选/全不选复选框
        CheckBox selectAllCheckBox = new CheckBox("全选/全不选");
        selectAllCheckBox.setOnAction(event -> {
            boolean isSelected = selectAllCheckBox.isSelected();
            for (CheckBox checkBox : checkBoxes) {
                checkBox.setSelected(isSelected);
            }
        });


        Button confirmButton = new Button("开始匹配");


        chooseButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);

                imagePath.setText(file.getAbsolutePath());
            }
        });

        confirmButton.setOnAction(e -> {
            try {
                List<String> chooseScene = new ArrayList<>();
                for (CheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        chooseScene.add(checkBox.getText());
                    }
                }

                if (chooseScene.size() == 0) {
                    alert("至少选择一个场景！");
                    return;
                }
                List<ConfigItem> itemList = CsvConfigUtil.getConfigItemsByName(chooseScene);
                List<ResultItem> resultList = ImageMatcher.matchForEach(itemList, imagePath.getText());
                showImages(resultList);
            } catch (Exception ex){
                ex.printStackTrace();
                alert("匹配中出现异常:" + ex.getLocalizedMessage());
            }

        });

        grid.add(chooseButton, 0, 0);
        grid.add(instructions, 0, 1, 3, 1); // 添加说明
        grid.add(selectAllCheckBox, 0, 8, 3, 1); // 全选框
        grid.add(imageView, 0, 9, 3, 1); // 图片显示
        grid.add(imagePath, 0, 10, 3, 1);
        grid.add(confirmButton, 0, 11, 3, 1); // 确定按钮

        Scene scene = new Scene(grid, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("挖了个宝");
        primaryStage.show();
    }

    private void alert(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }

    private void showImages(List<ResultItem> resultItems) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("最佳匹配：" + resultItems.get(0).getLabel() + "\n");
        stringBuilder.append("完整匹配率如下：\n");
        for (ResultItem resultItem : resultItems) {
            stringBuilder.append(resultItem.getLabel() + "：" + StringUtil.percentFormat(resultItem.getMatchRate()) + "\n");
        }
        alert(stringBuilder.toString());

        FileUtil.openImage(resultItems.get(0).getOutputFilePath());
    }


    public static void main(String[] args) {
        launch(args);
    }
}