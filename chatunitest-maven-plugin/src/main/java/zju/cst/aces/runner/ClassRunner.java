package zju.cst.aces.runner;

import zju.cst.aces.parser.ClassParser;
import zju.cst.aces.utils.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

public class ClassRunner extends AbstractRunner {
    public ClassInfo classInfo;
    public File infoDir;

    /**
     * 构造函数，首先调用了父类的构造函数
     * 其次初始化自己的两个参数-classInfo和infoDir
     * @param fullClassName
     * @param parsePath
     * @param testPath
     * @throws IOException
     */
    public ClassRunner(String fullClassName, String parsePath, String testPath) throws IOException {
        super(fullClassName, parsePath, testPath);
        infoDir = new File(parseOutputPath + File.separator + fullClassName.replace(".", File.separator));
        if (!infoDir.isDirectory()) {
            log.error("Error: " + fullClassName + " no parsed info found");
        }
        File classInfoFile = new File(infoDir + File.separator + "class.json");
        classInfo = GSON.fromJson(Files.readString(classInfoFile.toPath(), StandardCharsets.UTF_8), ClassInfo.class);
    }

    /**
     * 多线程：
     *      methodJob()
     * 单线程：
     *      methodJob()的执行方法
     * @throws IOException
     */
    public void start() throws IOException {
        if (Config.enableMultithreading == true) {
            methodJob();
        } else {
            for (String mSig : classInfo.methodSignatures.keySet()) {
                MethodInfo methodInfo = getMethodInfo(classInfo, mSig);
                if (methodInfo == null) {
                    continue;
                }
                new MethodRunner(fullClassName, parseOutputPath.toString(), testOutputPath.toString(), methodInfo).start();
            }
        }
    }

    /**
     * token消耗预测
     * @return
     * @throws IOException
     */
    public List<List<MethodTokenCostInfo>> StartEstimateCostTokenClass() throws IOException {
        List<List<MethodTokenCostInfo>> methodTokenCostInfos = new ArrayList<>();
        if (Config.enableMultithreading == true) {
            ExecutorService executor = Executors.newFixedThreadPool(methodThreads);
            List<Future<String>> futures = new ArrayList<>();
            for (String mSig : classInfo.methodSignatures.keySet()) {
                Callable<String> callable = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        MethodInfo methodInfo = getMethodInfo(classInfo, mSig);
                        if (methodInfo == null) {
                            return "No parsed info found for " + mSig + " in " + fullClassName;
                        }
                        methodTokenCostInfos.add( new MethodRunner(fullClassName, parseOutputPath.toString(), testOutputPath.toString(), methodInfo).StartEstimateCostTokenMethod());
                        return "Processed " + mSig;
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
            for (String mSig : classInfo.methodSignatures.keySet()) {
                MethodInfo methodInfo = getMethodInfo(classInfo, mSig);
                if (methodInfo == null) {
                    continue;
                }
                methodTokenCostInfos.add( new MethodRunner(fullClassName, parseOutputPath.toString(), testOutputPath.toString(), methodInfo).StartEstimateCostTokenMethod());
            }
        }
        return methodTokenCostInfos;
    }

    /**
     * 首先创建一个线程池-大小为methodThreads
     * futures-用于存储每个线程的结果
     * 对于每一个方法签名定义了一个执行方式并提交给线性池并将结果放到futures
     * 注册一个在程序关闭时执行的钩子函数，用于关闭线程池
     * 将结果打印出来
     * 关闭线性池
     */
    public void methodJob() {
        ExecutorService executor = Executors.newFixedThreadPool(methodThreads);
        List<Future<String>> futures = new ArrayList<>();
        for (String mSig : classInfo.methodSignatures.keySet()) {
            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    MethodInfo methodInfo = getMethodInfo(classInfo, mSig);
                    if (methodInfo == null) {
                        return "No parsed info found for " + mSig + " in " + fullClassName;
                    }
                    new MethodRunner(fullClassName, parseOutputPath.toString(), testOutputPath.toString(), methodInfo).start();
                    return "Processed " + mSig;
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
    }

    /**
     * -生成具有依赖关系的测试提示信息的方法-
     * 首先new一个PromptInfo类
     * 设置了一个information字段
     * @param classInfo
     * @param methodInfo
     * @return
     */
    public PromptInfo generatePromptInfoWithoutDep(ClassInfo classInfo, MethodInfo methodInfo) {
        PromptInfo promptInfo = new PromptInfo(
                false,
                classInfo.className,
                methodInfo.methodName,
                methodInfo.methodSignature,
                methodInfo.sourceCode);
        String fields = joinLines(classInfo.fields);
        String methods = filterAndJoinLines(classInfo.briefMethods, methodInfo.brief);
        String imports = joinLines(classInfo.imports);

        String information = classInfo.packageDeclaration
                + "\n" + imports
                + "\n" + classInfo.classSignature
                + " {"
                + "\n" + fields
                + "\n" + methods
                + "\n" + methodInfo.sourceCode
                + "\n}";

        promptInfo.setInfo(information);

        return promptInfo;
    }

    /**
     * -生成具有依赖关系的测试提示信息的方法-
     * 首先new一个PromptInfo类
     * 遍历这个methodInfo的所有依赖方法：
     *      如果依赖的类名与当前类名相同，表示依赖的方法也属于当前类的方法，因此需要将这些方法的简要信息添加到otherBriefMethods列表中
     *      如果依赖的类名与当前类名不相同，表示依赖的方法来自其他类，需要调用getDepInfo()方法获取依赖的信息，并将其添加到promptInfo的方法依赖列表中
     * 遍历classInfo中的构造方法依赖关系，将未在方法依赖中出现的构造方法依赖添加到promptInfo的构造方法依赖列表中
     * 设置了一个information字段
     * @param classInfo
     * @param methodInfo
     * @return
     * @throws IOException
     */
    public PromptInfo generatePromptInfoWithDep(ClassInfo classInfo, MethodInfo methodInfo) throws IOException {
        PromptInfo promptInfo = new PromptInfo(
                true,
                classInfo.className,
                methodInfo.methodName,
                methodInfo.methodSignature,
                methodInfo.sourceCode);
        List<String> otherBriefMethods = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : methodInfo.dependentMethods.entrySet()) {
            String depClassName = entry.getKey();
            if (depClassName.equals(className)) {
                Set<String> otherSig = methodInfo.dependentMethods.get(depClassName);
                for (String otherMethod : otherSig) {
                    MethodInfo otherMethodInfo = getMethodInfo(classInfo, otherMethod);
                    if (otherMethodInfo == null) {
                        continue;
                    }
                    otherBriefMethods.add(otherMethodInfo.brief);
                }
                continue;
            }
            Set<String> depMethods = entry.getValue();
            promptInfo.addMethodDeps(getDepInfo(promptInfo, depClassName, depMethods));
        }
        for (Map.Entry<String, Set<String>> entry : classInfo.constructorDeps.entrySet()) {
            String depClassName = entry.getKey();
            Set<String> depMethods = entry.getValue();
            if (methodInfo.dependentMethods.containsKey(depClassName)) {
                continue;
            }
            promptInfo.addConstructorDeps(getDepInfo(promptInfo, depClassName, depMethods));
        }

        String fields = joinLines(classInfo.fields);
        String imports = joinLines(classInfo.imports);

        String information = classInfo.packageDeclaration
                + "\n" + imports
                + "\n" + classInfo.classSignature
                + " {\n";
        //TODO: handle used fields instead of all fields
        if (methodInfo.useField) {
            information += fields + "\n" + joinLines(classInfo.getterSetters) + "\n";
        }
        if (classInfo.hasConstructor) {
            information += joinLines(classInfo.constructors) + "\n";
        }
        information += joinLines(otherBriefMethods) + "\n";
        information += methodInfo.sourceCode + "\n}";

        promptInfo.setInfo(information);
        return promptInfo;
    }

    /**
     * 根据ClassInfo中的一个方法签名返回MethodInfo
     * @param info
     * @param mSig
     * @return
     * @throws IOException
     */
    public MethodInfo getMethodInfo(ClassInfo info, String mSig) throws IOException {
        String packagePath = info.packageDeclaration
                .replace("package ", "")
                .replace(".", File.separator)
                .replace(";", "");
        Path depMethodInfoPath = parseOutputPath
                .resolve(packagePath)
                .resolve(info.className)
                .resolve(ClassParser.getFilePathBySig(mSig, info));
        if (!depMethodInfoPath.toFile().exists()) {
            return null;
        }
        return GSON.fromJson(Files.readString(depMethodInfoPath, StandardCharsets.UTF_8), MethodInfo.class);
    }

    public Map<String, String> getDepInfo(PromptInfo promptInfo, String depClassName, Set<String> depMethods) throws IOException {
        Path depClassInfoPath = parseOutputPath.resolve(depClassName).resolve("class.json");
        if (!depClassInfoPath.toFile().exists()) {
            return null;
        }
        ClassInfo depClassInfo = GSON.fromJson(Files.readString(depClassInfoPath, StandardCharsets.UTF_8), ClassInfo.class);

        String classSig = depClassInfo.classSignature;
        String fields = joinLines(depClassInfo.fields);
        String constructors = joinLines(depClassInfo.constructors);
        Map<String, String> methodDeps = new HashMap<>();

        String basicInfo = classSig + " {\n" + fields + "\n";
        if (depClassInfo.hasConstructor) {
            basicInfo += constructors + "\n";
        }

        String briefDepMethods = "";
        for (String sig : depMethods) {
            //TODO: identify used fields in dependent class
            MethodInfo depMethodInfo = getMethodInfo(depClassInfo, sig);
            if (depMethodInfo == null) {
                continue;
            }
            briefDepMethods += depMethodInfo.brief + "\n";
        }
        String getterSetter = joinLines(depClassInfo.getterSetters) + "\n";
        methodDeps.put(depClassName, basicInfo + getterSetter + briefDepMethods + "}");
        return methodDeps;
    }
}
