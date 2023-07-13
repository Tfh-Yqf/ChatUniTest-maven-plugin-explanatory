package com.example.spring.Impl;

import com.example.spring.Controller.ZipUploadController;
import com.example.spring.Mapper.ProjectMapper;
import com.example.spring.Mapper.UserMapper;
import com.example.spring.Service.ProjectService;
import com.example.spring.pojo.Entity.ProjectEntity;
import com.example.spring.pojo.Entity.ReturnCode;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    public ReturnCode getStatus(ProjectEntity projectEntity) throws IOException{
        ReturnCode res = new ReturnCode();
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        ProjectMapper projectMapper = sqlSession.getMapper(ProjectMapper.class);
        List<ProjectEntity> list = projectMapper.selectByEmail(projectEntity.email);

        Collections.sort(list, new Comparator<ProjectEntity>() {
            @Override
            public int compare(ProjectEntity o1, ProjectEntity o2) {
                return o2.createtime.compareTo(o1.createtime);
            }
        });

        res.excuteSuccess(list);
        return res;
    }

    /**
     * -执行ChatUniTest:project-
     * @param projectEntity
     * @return
     */
    public ReturnCode start(ProjectEntity projectEntity) throws IOException {
        ReturnCode res = new ReturnCode();

        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        ProjectMapper projectMapper = sqlSession.getMapper(ProjectMapper.class);

        // 检查目录
        Path uploadPath = Paths.get(ZipUploadController.UPLOAD_DIR).toAbsolutePath().normalize();
        Path dirPath = uploadPath.resolve(projectEntity.dirName.substring(0,projectEntity.dirName.indexOf(".")));
        if(!Files.isDirectory(dirPath)){
            res.excuteFail(12,"文件未上传成功");
            return res;
        }

        // 找到pom.xml路径
        String pomPath = findPomXml(new File(dirPath.toString()));
        if(pomPath==null){
            res.excuteFail(13,"不是Maven项目");
            return res;
        }
        // 修改状态
        projectEntity.result = "处理中";
        projectMapper.insert(projectEntity);

        String pom = readFileAsString(Paths.get(pomPath));
        // 添加依赖
        pom = addDependency(pom);
        // 添加插件
        pom = addPlugin(pom);
        // 重新写pom.xml
        writeToFile(pom,pomPath);
        // 写bat
        String projectPath = Paths.get(pomPath).getParent().toString();
        String content = "cmd /k \"cd "+projectPath+" &&  mvn chatunitestexplanatory:project\"";
        writeToFile(content,projectPath+File.separator+"project.bat");
        // 执行命令行 mvn chatunitestexplanatory:project
        String commadn1 = "java -version";
        String command2 = "";
        List<String> commands = new ArrayList<>();
        commands.add(commadn1);
        commands.add(command2);
        executeCommands(commands,projectPath);

        projectEntity.status = "finished";
        projectEntity.result = "执行完成";
        projectMapper.updateStatus(projectEntity);
        return res;
    }

    public static void executeCommands(List<String> commands,String projectPath) {
        try {
            String command =  String.join(" && ", commands);
            System.out.println(command);
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("cmd", "-c", command);

            processBuilder.redirectErrorStream(true);
            System.out.println("进程开始");
            processBuilder.directory(new File(projectPath));
            Process process = processBuilder.start();

            // 等待命令执行完成
            int exitCode = process.waitFor();
            System.out.println("Waiting over.");
            // 读取命令执行结果
            InputStream inputStream = process.getInputStream();
            saveInputStreamToFile(inputStream,projectPath+File.separator+"log.txt");

            System.out.println("命令执行完毕，退出码：" + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void saveInputStreamToFile(InputStream inputStream, String filePath) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }


    public static void writeToFile(String content, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    private static String findPomXml(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String pomXmlPath = findPomXml(file);
                    if (pomXmlPath != null) {
                        return pomXmlPath;
                    }
                } else if (file.isFile() && file.getName().equals("pom.xml")) {
                    return file.getAbsolutePath();
                }
            }
        }
        return null;
    }

    private static String readFileAsString(Path filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(filePath);
        return new String(bytes);
    }

    public static String addDependency(String pomXml) {
        // 检查 pom.xml 是否为空
        if (pomXml == null || pomXml.isEmpty()) {
            return pomXml;
        }

        // 构建新的 dependency 字符串
        String newDependency = String.format(
                "    <dependency>\n" +
                "        <groupId>org.junit.jupiter</groupId>\n" +
                "        <artifactId>junit-jupiter-api</artifactId>\n" +
                "        <version>5.8.2</version>\n" +
                "        <scope>test</scope>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "        <groupId>org.mockito</groupId>\n" +
                "        <artifactId>mockito-core</artifactId>\n" +
                "        <version>3.12.4</version>\n" +
                "        <scope>test</scope>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "        <groupId>org.mockito</groupId>\n" +
                "        <artifactId>mockito-junit-jupiter</artifactId>\n" +
                "        <version>3.12.4</version>\n" +
                "        <scope>test</scope>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "        <groupId>org.mockito</groupId>\n" +
                "        <artifactId>mockito-inline</artifactId>\n" +
                "        <version>3.12.4</version>\n" +
                "        <scope>test</scope>\n" +
                "    </dependency>\n"
        );



        // 查找最后一个 </dependencies> 标签的位置
        int lastIndex = pomXml.lastIndexOf("</dependencies>");
        if (lastIndex == -1) {
            // 如果不存在 </dependencies> 标签，则不进行修改
            return pomXml;
        }



        // 在最后一个 </dependencies> 标签前插入新的 dependency
        StringBuilder modifiedPomXml = new StringBuilder(pomXml);
        modifiedPomXml.insert(lastIndex, newDependency);

        return modifiedPomXml.toString();
    }

    public static String addPlugin(String pomXml) {
        // 检查 pom.xml 是否为空
        if (pomXml == null || pomXml.isEmpty()) {
            return pomXml;
        }


        // 查找最后一个 </plugins> 标签的位置
        int lastIndex = pomXml.lastIndexOf("</plugins>");
        if (lastIndex == -1) {
            // 如果不存在 </plugins> 标签，则不进行修改
            return pomXml;
        }

        // 构建新的 plugin 字符串
        String newPlugin = String.format(
                        "            <plugin>\n" +
                        "                <groupId>org.apache.maven.plugins</groupId>\n" +
                        "                <artifactId>maven-surefire-plugin</artifactId>\n" +
                        "                <version>3.1.0</version>\n" +
                        "            </plugin>\n" +
                        "            <plugin>\n" +
                        "                <groupId>io.github.TFH</groupId>\n" +
                        "                <artifactId>chatunitest-maven-plugin-explanatory</artifactId>\n" +
                        "                <version>1.0.0</version>\n" +
                        "                <configuration>\n" +
                        "                    <!-- Required: You must specify your OpenAI API keys. -->\n" +
                        "                    <apiKeys>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</apiKeys>\n" +
                        "                    <model>gpt-3.5-turbo</model>\n" +
                        "                    <testNumber>1</testNumber>\n" +
                        "                    <maxRounds>1</maxRounds>\n" +
                        "                    <minErrorTokens>500</minErrorTokens>\n" +
                        "                    <temperature>0.5</temperature>\n" +
                        "                    <topP>1</topP>\n" +
                        "                    <frequencyPenalty>0</frequencyPenalty>\n" +
                        "                    <presencePenalty>0</presencePenalty>\n" +
                        "                    <proxy>127.0.0.1:7890</proxy>\n" +
                        "                    <thread>false</thread>\n" +
                        "                    <maxUseTokens>10000</maxUseTokens>\n" +
                        "                </configuration>\n" +
                        "            </plugin>"
        );

        // 在最后一个 </plugins> 标签前插入新的 plugin
        StringBuilder modifiedPomXml = new StringBuilder(pomXml);
        modifiedPomXml.insert(lastIndex, newPlugin);

        return modifiedPomXml.toString();
    }
}
