package zju.cst.aces.runner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Response;
import zju.cst.aces.ProjectTestMojo;
import zju.cst.aces.utils.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractRunner extends ProjectTestMojo {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final String separator = "_";
    public static int testTimeOut = 8000;
    public Path parseOutputPath;
    public Path testOutputPath;
    public Path errorOutputPath;
    public String className;
    public String fullClassName;

    /**
     * 构造函数
     * @param fullClassname
     * @param output
     * @param testPath
     * @throws IOException
     */
    public AbstractRunner(String fullClassname, String output, String testPath) throws IOException {
        fullClassName = fullClassname;
        className = fullClassname.substring(fullClassname.lastIndexOf(".") + 1);
        parseOutputPath = Paths.get(output);
        testOutputPath = Paths.get(testPath);
        errorOutputPath = parseOutputPath.getParent().resolve("error-message");
    }

    /**
     * 如果是第一Round，则生成开始的咒语
     * 否则，生成
     * @param promptInfo
     * @return
     * @throws IOException
     */
    public List<Message> generateMessages(PromptInfo promptInfo) throws IOException {
        List<Message> messages = new ArrayList<>();
        if (promptInfo.errorMsg == null) { // round 1
            messages.add(Message.ofSystem(generateSystemPrompt(promptInfo)));
        }
        messages.add(Message.of(generateUserPrompt(promptInfo)));
        return messages;
    }

    /**
     * 如果promptInfo.errorMsg为空，表示是第一轮生成测试，用户提示包括焦点方法和焦点类的信息。用户提示的格式如下：
     *      The focal method is `{methodSignature}` in the focal class `{className}`, and their information is
     * 如果promptInfo.errorMsg不为空，表示是修复错误的阶段，用户提示会包含错误信息和需要修复的单元测试。用户提示的格式如下：
     *      I need you to fix an error in a unit test, an error occurred while compiling and executing......
     * @param promptInfo
     * @return
     * @throws IOException
     */
    public String generateUserPrompt(PromptInfo promptInfo) throws IOException {
        String user = null;
        if (promptInfo.errorMsg == null) {
            user = String.format("The focal method is `%s` in the focal class `%s`, and their information is\n```%s```",
                    promptInfo.methodSignature, promptInfo.className, promptInfo.info);
            if (promptInfo.hasDep) {
                for (Map<String, String> cDeps : promptInfo.constructorDeps) {
                    for (Map.Entry<String, String> entry : cDeps.entrySet()) {
                        user += String.format("\nThe brief information of dependent class `%s` is\n```%s```", entry.getKey(), entry.getValue());
                    }
                }
                for (Map<String, String> mDeps : promptInfo.methodDeps) {
                    for (Map.Entry<String, String> entry : mDeps.entrySet()) {
                        user += String.format("\nThe brief information of dependent method `%s` is\n```%s```", entry.getKey(), entry.getValue());
                    }
                }
            }
        } else {
//            log.info("报错信息-开始\n");
//            for(String message:promptInfo.errorMsg){
//                log.info(promptInfo.errorMsg.toString());
//            }
//            log.info("报错信息-结束\n");
            int promptTokens = TokenCounter.countToken(promptInfo.unitTest)
                    + TokenCounter.countToken(promptInfo.methodSignature)
                    + TokenCounter.countToken(promptInfo.className)
                    + TokenCounter.countToken(promptInfo.info);
            ErrorProcesser errorProcesser = new ErrorProcesser();
            int allowedTokens = Math.max(Config.maxPromptTokens - promptTokens, Config.minErrorTokens);
            String processedErrorMsg = errorProcesser.processErrorMessage(promptInfo.errorMsg, allowedTokens);
            log.debug("Allowed tokens: " + allowedTokens);
            log.debug("Processed error message: \n" + processedErrorMsg);

            user = String.format("I need you to fix an error in a unit test, an error occurred while compiling and executing\n" +
                            "The unit test is:\n" +
                            "```\n%s```\n" +
                            "The error message is:\n" +
                            "```\n%s```\n" +
                            "The unit test is testing the method %s in the class %s,\n" +
                            "the source code of the method under test and its class is:\n" +
                            "```\n%s```\n" +
                            "Please fix the error and return the whole fixed unit test." +
                            " You can use Junit 5, Mockito 3 and reflection. No explanation is needed.\n",
                    promptInfo.unitTest, processedErrorMsg, promptInfo.methodSignature, promptInfo.className, promptInfo.info);
        }
        return user;
    }

    /**
     * chatGpt的咒语 - 开始咒语
     * @param promptInfo
     * @return
     */
    public String generateSystemPrompt(PromptInfo promptInfo) {
        String system = "Please help me generate a whole JUnit test for a focal method in a focal class.\n" +
                "I will provide the following information of the focal method:\n" +
                "1. Required dependencies to import.\n" +
                "2. The focal class signature.\n" +
                "3. Source code of the focal method.\n" +
                "4. Signatures of other methods and fields in the class.\n";
        if (promptInfo.hasDep) {
            system += "I will provide following brief information if the focal method has dependencies:\n" +
                    "1. Signatures of dependent classes.\n" +
                    "2. Signatures of dependent methods and fields in the dependent classes.\n";
        }
        system += "I need you to create a whole unit test using JUnit 5 and Mockito 3, " +
                "ensuring optimal branch and line coverage. " +
                "The whole test should include necessary imports for JUnit 5 and Mockito 3, " +
                "compile without errors, and use reflection to invoke private methods. " +
                "No additional explanations required.\n";
        return system;
    }

    /**
     * 将列表中的所有字符串使用换行符 "\n" 连接起来
     * @param lines
     * @return
     */
    public String joinLines(List<String> lines) {
        return lines.stream().collect(Collectors.joining("\n"));
    }

    public String filterAndJoinLines(List<String> lines, String filter) {
        return lines.stream()
                .filter(line -> !line.equals(filter))
                .collect(Collectors.joining("\n"));
    }

    /**
     * body.choices[0].message.content
     * @param response
     * @return
     */
    public String parseResponse(Response response) {
        if (response == null) {
            return "";
        }
        Map<String, Object> body = GSON.fromJson(response.body().charStream(), Map.class);
        String content = ((Map<String, String>) ((Map<String, Object>) ((ArrayList<?>) body.get("choices")).get(0)).get("message")).get("content");
        //             测试，打印回答
        log.info("Role: chatGpt的回答\n");
        log.info("Content: "+content+"\n\n");
//        log.debug(content);
        // 记录token使用情况
        int ResponceCostTokens = TokenCounter.countToken(content);
        Config.setHaveCostResponseTokens(Config.haveResponseCostTokens+ResponceCostTokens);

        return extractCode(content);
    }

    /**
     * 导出code到指定的路径
     * @param code
     * @param savePath
     */
    public void exportTest(String code, Path savePath) {
        if (!savePath.toAbsolutePath().getParent().toFile().exists()) {
            savePath.toAbsolutePath().getParent().toFile().mkdirs();
        }
        //set charset utf-8
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(savePath.toFile()), StandardCharsets.UTF_8)) {
            writer.write(code);
        } catch (IOException e) {
            throw new RuntimeException("In AbstractRunner.exportTest: " + e);
        }
    }

    /**
     * 调用CodeExtractor提取代码
     * @param content
     * @return
     */
    public String extractCode(String content) {
        return new CodeExtractor(content).getExtractedCode();
    }

    /**
     * -修复测试用例中缺失的导入语句-
     * @param code
     * @param imports
     * @return
     */
    public String repairImports(String code, List<String> imports) {
        String[] codeParts = code.trim().split("\\n", 2);
        String firstLine = codeParts[0];
        String _code = codeParts[1];
        for (int i = imports.size() - 1; i >= 0; i--) {
            String _import = imports.get(i);
            if (!_code.contains(_import)) {
                _code = _import + "\n" + _code;
            }
        }
        return firstLine + "\n" + _code;
    }

    /**
     * -修复包-
     * 如果code第一行是包的生命则直接return
     * 如果不是则将其添加到包声明添加到顶部
     * @param code
     * @param packageInfo
     * @return
     */
    public String repairPackage(String code, String packageInfo) {
        String[] lines = code.split("\n");
        String firstLine = lines[0];

        if (packageInfo.isEmpty() || firstLine.contains(packageInfo)) {
            return code;
        }

        StringBuilder repairedCode = new StringBuilder();
        repairedCode.append(packageInfo).append("\n");
        repairedCode.append(code);

        return repairedCode.toString();
    }

    /**
     * 对不同的Junit版本添加一个timeout = %d的字段
     * @param testCase
     * @param timeout
     * @return
     */
    public String addTimeout(String testCase, int timeout) {
        // Check JUnit version
        String junit4 = "import org.junit.Test";
        String junit5 = "import org.junit.jupiter.api.Test";
        if (testCase.contains(junit4)) {  // JUnit 4
            if (testCase.contains("@Test(timeout =")) {
                return testCase;
            }
            testCase = testCase.replace("@Test(", String.format("@Test(timeout = %d, ", timeout));
            return testCase.replace("@Test\n", String.format("@Test(timeout = %d)%n", timeout));
        } else if (testCase.contains(junit5)) {  // JUnit 5
            if (testCase.contains("import org.junit.jupiter.api.Timeout;")) {
                return testCase;
            }
            List<String> timeoutImport = new ArrayList<>();
            timeoutImport.add("import org.junit.jupiter.api.Timeout;");
            testCase = repairImports(testCase, timeoutImport);
            return testCase.replace("@Test\n", String.format("@Test%n    @Timeout(%d)%n", timeout));
        } else {
            log.error("Generated with unknown JUnit version, try without adding timeout.");
        }
        return testCase;
    }

    /**
     * 改名字
     * @param code
     * @param className
     * @param newName
     * @return
     */
    public String changeTestName(String code, String className, String newName) {
        String oldName = className + "Test";
        return code.replace(oldName, newName);
    }
}
