package zju.cst.aces.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class PromptInfo {
    public boolean hasDep;
    public String className;
    public String methodName;
    public String methodSignature;
    public String methodCode;
    public String info;
    public List<Map<String, String>> constructorDeps = new ArrayList<>();
    public List<Map<String, String>> methodDeps = new ArrayList<>();
    public List<String> errorMsg = null;
    public String unitTest = "";

    /**
     * 构造函数
     * @param hasDep
     * @param className
     * @param methodName
     * @param methodSignature
     * @param methodCode
     */
    public PromptInfo(boolean hasDep, String className, String methodName,
                      String methodSignature, String methodCode) {
        this.hasDep = hasDep;
        this.className = className;
        this.methodName = methodName;
        this.methodSignature = methodSignature;
        this.methodCode = methodCode;
    }

    /**
     * 防止加NULL字段
     * @param methodDep
     */
    public void addMethodDeps(Map<String, String> methodDep) {
        if (methodDep == null) {
            return;
        }
        this.methodDeps.add(methodDep);
    }

    /**
     * 防止加NULL字段
     * @param constructorDep
     */
    public void addConstructorDeps(Map<String, String> constructorDep) {
        if (constructorDep == null) {
            return;
        }
        this.constructorDeps.add(constructorDep);
    }
}
