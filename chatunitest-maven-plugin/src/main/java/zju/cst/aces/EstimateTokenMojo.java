package zju.cst.aces;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import zju.cst.aces.parser.ProjectParser;
import zju.cst.aces.runner.ClassRunner;
import zju.cst.aces.utils.Config;
import zju.cst.aces.utils.MethodTokenCostInfo;
import zju.cst.aces.utils.TestCompiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Mojo(name = "EstimateToken")
public class EstimateTokenMojo extends ProjectTestMojo {


    @Override
    public void execute() throws MojoExecutionException{
        init();
        log.info("\n==========================\n[ChatTester] EstimateToken for project " + project.getBasedir().getName() + " ...");
        log.warn("[ChatTester] It dont consume  token!");

        Path srcMainJavaPath = Paths.get(project.getBasedir().getAbsolutePath(), "src", "main", "java");
        if (!srcMainJavaPath.toFile().exists()) {
            log.error("\n==========================\n[ChatTester] No compile source found in " + project);
            return;
        }
        ProjectParser parser = new ProjectParser(srcMainJavaPath.toString(), parseOutput);
        if (! (new File(parseOutput).exists())) {
            log.info("\n==========================\n[ChatTester] Parsing class info ...");
            parser.parse();
            log.info("\n==========================\n[ChatTester] Parse finished");
        }

        List<String> classPaths = new ArrayList<>();
        parser.scanSourceDirectory(srcMainJavaPath.toFile(), classPaths);

        TestCompiler.backupTestFolder();

        List<List<MethodTokenCostInfo>> methodTokenCostInfos = new ArrayList<>();
        if (Config.enableMultithreading == true) {
            ExecutorService executor = Executors.newFixedThreadPool(classThreads);
            List<Future<String>> futures = new ArrayList<>();
            for (String classPath : classPaths) {
                Callable<String> callable = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        String className = classPath.substring(classPath.lastIndexOf(File.separator) + 1, classPath.lastIndexOf("."));
                        try {
                            className = getFullClassName(className);
                            log.info("\n==========================\n[ChatTester] Generating tests for class < " + className + " > ...");
                            methodTokenCostInfos.addAll( new ClassRunner(className, parseOutput, testOutput).StartEstimateCostTokenClass());
                        } catch (IOException e) {
                            log.error("[ChatTester] Generate tests for class " + className + " failed: " + e);
                        }
                        return "Processed " + classPath;
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
            for (String classPath : classPaths) {
                String className = classPath.substring(classPath.lastIndexOf(File.separator) + 1, classPath.lastIndexOf("."));
                try {
                    className = getFullClassName(className);
                    log.info("\n==========================\n[ChatTester] Generating tests for class < " + className + " > ...");
                    methodTokenCostInfos.addAll( new ClassRunner(className, parseOutput, testOutput).StartEstimateCostTokenClass());
                } catch (IOException e) {
                    log.error("[ChatTester] Generate tests for class " + className + " failed: " + e);
                }
            }
        }

        int minResult = 0;
        int maxResult = 0;
        double minMoney = 0;
        double maxMoney = 0;
        Map<String,Object> methodTokenCostInfoMap = new HashMap<>();
        for(List<MethodTokenCostInfo> methodTokenCostInfoList:methodTokenCostInfos){
            String classname = methodTokenCostInfoList.get(0).className;
            for(MethodTokenCostInfo methodTokenCostInfo:methodTokenCostInfoList){
                minResult += methodTokenCostInfo.FirstAskCost + methodTokenCostInfo.FirstResponseCost;
                maxResult += methodTokenCostInfo.FirstAskCost
                        + methodTokenCostInfo.FirstResponseCost
                        + (methodTokenCostInfo.FixedResponseCost+methodTokenCostInfo.FixedAskCost)
                        * (Config.testNumber*Config.maxRounds);
                minMoney += TokenTransfer2Money(methodTokenCostInfo.FirstAskCost,methodTokenCostInfo.FirstResponseCost);
                maxMoney += TokenTransfer2Money(methodTokenCostInfo.FirstAskCost,methodTokenCostInfo.FirstResponseCost)
                        + TokenTransfer2Money(methodTokenCostInfo.FixedAskCost,methodTokenCostInfo.FixedResponseCost)
                        * (Config.testNumber*Config.maxRounds);
            }
            methodTokenCostInfoMap.put(classname,methodTokenCostInfoList);
        }
        methodTokenCostInfoMap.put("maxRound",Config.maxRounds);
        methodTokenCostInfoMap.put("testNumber",Config.testNumber);
        String savePath = project.getBasedir().getAbsolutePath()+File.separator + testOutput+File.separator+"EstimateCostTokens.json";
        SaveMap(methodTokenCostInfoMap,savePath);

        log.info("\n=========================="
                + "\n==========================\n"
                + "[ChatTester] Eventually this project will cost "+minResult+" ~ "+maxResult+" tokens\n"
                + "[ChatTester] Eventually this project will cost "+minMoney+"$ ~ "+maxMoney+"$"
                + "\n=========================="
                + "\n==========================\n");
    }


    /**
     * 将Map保存为JSON文件
     * @param input
     * @param savePath
     */
    private void SaveMap(Map<String,Object> input,String savePath) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String json = objectMapper.writeValueAsString(input);
            File file = new File(savePath);
            file.getParentFile().mkdirs();
            objectMapper.writeValue(new File(savePath), input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double TokenTransfer2Money(int inputTokens,int outputTokens){
        double price = 0 ;
        switch (Config.model){
            case "gpt-4":
                price = inputTokens * 0.03 + outputTokens*0.06;
                break;
            case "gpt-4-32k":
                price = inputTokens * 0.06 + outputTokens*0.12;
                break;
            case "gpt-3.5-turbo":
                price = inputTokens * 0.0015 + outputTokens*0.002;
                break;
            case "gpt-3.5-turbo-16k":
                price = inputTokens * 0.003  + outputTokens*0.004;
                break;
        }
        return price/1000;
    }
}
