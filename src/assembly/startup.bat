@echo off
setlocal

:: 设置 Java 运行时环境
set JAVA_HOME=bellsoft-jdk11.0.24+9-windows-amd64\jdk-11.0.24
set PATH=%JAVA_HOME%\bin;%PATH%

:: 初始化类路径
set CLASSPATH=

:: 遍历 lib 目录下的所有 JAR 文件并添加到 CLASSPATH
for %%i in (*.jar) do (
     echo %%i
    set "CLASSPATH=%%i;%CLASSPATH%"
)

:: 添加主 JAR 文件
set "CLASSPATH=lib\image-matcher-1.0-SNAPSHOT.jar;%CLASSPATH%"

:: 输出当前的类路径（可选）
echo CLASSPATH: %CLASSPATH%

:: 执行 Java 应用程序
java  -Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 --module-path "javafx-sdk-17.0.12\lib" --add-modules javafx.controls,javafx.fxml -cp "%CLASSPATH%" com.kfc.image.ImageMatcherJavaFXLauncher

:: 按任意键继续
:: pause
