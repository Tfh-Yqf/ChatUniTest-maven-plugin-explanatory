package zju.cst.aces.runner;

import okhttp3.Response;
import zju.cst.aces.utils.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class MethodRunner extends ClassRunner {

    public MethodInfo methodInfo;

    /**
     * 构造函数，super会到AbstractRunner中
     * @param fullClassName
     * @param parsePath
     * @param testOutputPath
     * @param methodInfo
     * @throws IOException
     */
    public MethodRunner(String fullClassName, String parsePath, String testOutputPath, MethodInfo methodInfo) throws IOException {
        super(fullClassName, parsePath, testOutputPath);
        this.methodInfo = methodInfo;
    }

    /**
     * 多线程：
     *      创建一个线性池-大小为testNumber
     *      futures存储每个线程的结果
     *      每一个测试执行-startRounds
     *      存储线程结果
     *      打印线程结果
     *      线程池的关闭
     *  单线程：
     *      同上，只是少了多线程池的操作
     * @throws IOException
     */
    @Override
    public void start() throws IOException {
        if (Config.stopWhenSuccess == false && Config.enableMultithreading == true) {
            ExecutorService executor = Executors.newFixedThreadPool(Config.testNumber);
            List<Future<String>> futures = new ArrayList<>();
            for (int num = 1; num <= Config.testNumber; num++) {
                int finalNum = num;
                Callable<String> callable = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        startRounds(finalNum);
                        return "";
                    }
                };
                Future<String> future = executor.submit(callable);
                futures.add(future);
            }
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    executor.shutdownNow();
                }
            });

            for (Future<String> future : futures) {
                try {
                    String result = future.get();
                    System.out.println(result);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            executor.shutdown();
        } else {
            for (int num = 1; num <= Config.testNumber; num++) {
                if (startRounds(num)) {
                    break;
                }
            }
        }
    }

    /**
     * 预测消耗token数量
     * @return
     * @throws IOException
     */
    public List<MethodTokenCostInfo> StartEstimateCostTokenMethod() throws IOException {
        List<MethodTokenCostInfo> methodTokenCostInfos = new ArrayList<>();
        if (Config.stopWhenSuccess == false && Config.enableMultithreading == true) {
            ExecutorService executor = Executors.newFixedThreadPool(Config.testNumber);
            List<Future<String>> futures = new ArrayList<>();
            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    methodTokenCostInfos.add(EstimateCostTokens());
                    return "";
                }
            };
            Future<String> future = executor.submit(callable);
            futures.add(future);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    executor.shutdownNow();
                }
            });

            for (Future<String> futureItem : futures) {
                try {
                    String result = futureItem.get();
                    System.out.println(result);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            executor.shutdown();
        } else {
            methodTokenCostInfos.add(EstimateCostTokens());

        }
        return methodTokenCostInfos;
    }



    /**
     * 测试名称：由类名、方法名、方法签名、轮数和后缀组成
     * 执行MaxRounds轮
     * 每一轮：
     *      如果是新的测试：
     *          如果存在依赖方法：
     *              generatePromptInfoWithDep()-带有依赖的promptInfo
     *          如果不存在依赖方法：
     *              generatePromptInfoWithoutDep()-不带依赖的promptInfo
     *      生成chatgpt的咒语
     *      根据咒语去问chatGpt
     *      解析回答-提取里面的代码-没代码则失败
     *      修改新的类名
     *      对包名进行修复
     *      添加timeout字段
     *      设置promptInfo的UnitTest的字段
     *      修复code中缺失的导入语句，然后将code导出
     *      调用 TestCompiler 编译测试用例，并将编译错误信息保存到文件中。如果编译成功，则表示测试用例生成成功，否则返回false失败并删除刚刚保存的文件
     * @param num
     * @return
     * @throws IOException
     */
    public boolean startRounds(final int num) throws IOException {
        PromptInfo promptInfo = null;
        String testName = className + separator + methodInfo.methodName + separator
                + classInfo.methodSignatures.get(methodInfo.methodSignature) + separator + num + separator + "Test";
        log.info("\n==========================\n[ChatTester] Generating test for method < "
                + methodInfo.methodName + " > number " + num + "...\n");
        for (int rounds = 1; rounds <= Config.maxRounds; rounds++) {
            if (promptInfo == null) {
                log.info("Generating test for method < " + methodInfo.methodName + " > round " + rounds + " ...");
                if (methodInfo.dependentMethods.size() > 0) {
                    promptInfo = generatePromptInfoWithDep(classInfo, methodInfo);
                } else {
                    promptInfo = generatePromptInfoWithoutDep(classInfo, methodInfo);
                }
            } else {
                log.info("Fixing test for method < " + methodInfo.methodName + " > round " + rounds + " ...");
            }
            List<Message> prompt = generateMessages(promptInfo);
            log.debug("[Prompt]:\n" + prompt.toString());

            AskGPT askGPT = new AskGPT();
            Response response = askGPT.askChatGPT(prompt);
            Path savePath = testOutputPath.resolve(classInfo.packageDeclaration
                            .replace(".", File.separator)
                            .replace("package ", "")
                            .replace(";", ""))
                    .resolve(testName + ".java");

            // 测试，打印prompt
//            log.info("-----------------------------打印promptInfo------------------------------------"+num);
//            log.info(classInfo.imports.toString());
//            log.info(classInfo.packageDeclaration.toString());
//            log.info(classInfo.classSignature.toString());
//            log.info(classInfo.constructors.toString());
//            log.info(classInfo.fields.toString());
//            log.info("-----------------------------打印prompt------------------------------------"+num);
//            for(int i=0;i<prompt.size();i++){
//                log.info("Role: "+prompt.get(i).getRole()+" Name: "+prompt.get(i).getName());
//                log.info("Content: "+prompt.get(i).getContent()+"\n\n");
//            }
//            log.info("结束\n");

            String code = parseResponse(response);
            if (code.isEmpty()) {
                log.info("Test for method < " + methodInfo.methodName + " > extract code failed");
                continue;
            }
            code = changeTestName(code, className, testName);
            code = repairPackage(code, classInfo.packageDeclaration);
            code = addTimeout(code, testTimeOut);

            promptInfo.setUnitTest(code);

            code = repairImports(code, classInfo.imports);
            exportTest(code, savePath);

            TestCompiler compiler = new TestCompiler();
            if (compiler.compileAndExport(savePath.toFile(),
                    errorOutputPath.resolve(testName + "CompilationError_" + rounds + ".txt"), promptInfo)) {
                log.info("Test for method < " + methodInfo.methodName + " > generated successfully");
                return true;
            } else {
                removeTestFile(savePath.toFile());
                log.info("Test for method < " + methodInfo.methodName + " > generated failed");
            }
        }
        return false;
    }

    /**
     * 估计每一个method的具体消耗情况
     * @return
     * @throws IOException
     */
    public MethodTokenCostInfo EstimateCostTokens() throws IOException {
        MethodTokenCostInfo methodTokenCostInfo = new MethodTokenCostInfo();
        PromptInfo promptInfo = null;
        String testName = className + separator + methodInfo.methodName + separator
                + classInfo.methodSignatures.get(methodInfo.methodSignature) + separator + "Test";


        if (methodInfo.dependentMethods.size() > 0) {
            promptInfo = generatePromptInfoWithDep(classInfo, methodInfo);
        } else {
            promptInfo = generatePromptInfoWithoutDep(classInfo, methodInfo);
        }
        List<Message> prompt = generateMessages(promptInfo);
        int codePredictCount = TokenCounter.countToken(classInfo.packageDeclaration)
                            + TokenCounter.countToken(classInfo.imports.toString())
                            + 200
                            + TokenCounter.countToken(classInfo.classSignature)
                            + TokenCounter.countToken(methodInfo.sourceCode) *2;
        log.debug("[Prompt]:\n" + prompt.toString());
        methodTokenCostInfo.FirstAskCost = TokenCounter.countToken(prompt);
        methodTokenCostInfo.FirstResponseCost = 54 + codePredictCount;

        // 增加错误信息
        methodTokenCostInfo.FixedAskCost = Config.maxPromptTokens;
        methodTokenCostInfo.FixedResponseCost = 39 +codePredictCount;

        methodTokenCostInfo.className = classInfo.className;
        methodTokenCostInfo.methodName = methodInfo.methodName;
        return methodTokenCostInfo;
    }

    /**
     * Remove the failed test file
     * 删除传进来的文件
     */
    public static void removeTestFile(File testFile) {
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }
}