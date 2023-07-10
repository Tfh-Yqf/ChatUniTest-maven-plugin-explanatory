package zju.cst.aces.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import zju.cst.aces.utils.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectParser {

    /**
     * 要解析的项目路径
     */
    private String srcFolderPath;
    /**
     * 解析结果输出路径
     */
    private String outputPath;

    /**
     * 存放的是类名和对应的完整包名List
     */
    public Map<String, List<String>> classMap = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    /**
     * 设置类的两个属性
     * @param src 项目main目录
     * @param output
     */
    public ProjectParser(String src, String output) {
        setSrcFolderPath(src);
        setOutputPath(output);
    }

    /**
     * Parse the project.
     * 首先扫描出所有java文件
     * addClassMap - 将每一个java文件的包名映射加入到classMap
     * 建立一个输出路径:output+包路径
     * 对每一个类进行extractClass
     * 最后exportClassMap()将classMap输出为JSON文件
     */
    public void parse() {
        List<String> classPaths = new ArrayList<>();
        scanSourceDirectory(new File(srcFolderPath), classPaths);
        if (classPaths.isEmpty()) {
            throw new RuntimeException("No java file found in " + srcFolderPath);
        }
        for (String classPath : classPaths) {
            try {
                addClassMap(classPath);
                String packagePath = classPath.substring(srcFolderPath.length() + 1);
                Path output = Paths.get(outputPath, packagePath).getParent();
                ClassParser classParser = new ClassParser(output);
                classParser.extractClass(classPath);
            } catch (Exception e) {
                throw new RuntimeException("In ProjectParser.parse: " + e);
            }
        }
        exportClassMap();
    }

    /**
     * 首先从路径中提取出包名，并将路径名转换为相对路径去掉.java，/->.
     *
     * @param classPath
     */
    public void addClassMap(String classPath) {
        String fullClassName = classPath.substring(srcFolderPath.length() + 1)
                .replace(".java", "")
                .replace(File.separator, ".");

        String className = Paths.get(classPath).getFileName().toString().replace(".java", "");
        if (classMap.containsKey(className)) {
            classMap.get(className).add(fullClassName);
        } else {
            List<String> fullClassNames = new ArrayList<>();
            fullClassNames.add(fullClassName);
            classMap.put(className, fullClassNames);
        }
    }

    /**
     * classMapPath 是类映射的一个路径
     * 将classMap转化为json文件存放在classMapPath路径中
     */
    public void exportClassMap() {
        Path classMapPath = Config.classMapPath;
        if (!Files.exists(classMapPath.getParent())) {
            try {
                Files.createDirectories(classMapPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(classMapPath.toFile()), StandardCharsets.UTF_8)){
            writer.write(GSON.toJson(classMap));
        } catch (Exception e) {
            throw new RuntimeException("In ProjectParser.exportNameMap: " + e);
        }
    }

    /**
     * 递归查找所有.java文件
     * @param directory
     * @param classPaths
     */
    public static void scanSourceDirectory(File directory, List<String> classPaths) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        scanSourceDirectory(file, classPaths);
                    } else if (file.getName().endsWith(".java")) {
                        String classPath = file.getPath();
                        classPaths.add(classPath);
                    }
                }
            }
        }
    }

    public void setSrcFolderPath(String src) {
        this.srcFolderPath = src;
    }

    public void setOutputPath(String output) {
        this.outputPath = output;
    }
}
