package zju.cst.aces.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassInfo {
    public String className;
    public String packageDeclaration;
    public String classSignature;
    public List<String> imports;
    // 字段
    public List<String> fields;
    public List<String> superClasses;
    public Map<String, String> methodSignatures;
    public List<String> briefMethods;
    public boolean hasConstructor;
    public List<String> constructors;
    // Getter 和 Setter 方法
    public List<String> getterSetters;

    // 构造函数依赖
    public Map<String, Set<String>> constructorDeps;

    public ClassInfo(String className, String packageDeclaration, String classSignature, List<String> imports,
                     List<String> fields, List<String> superClasses, Map<String, String> methodSignatures,
                     List<String> briefMethods, boolean hasConstructor, List<String> constructors,
                     List<String> getterSetters, Map<String, Set<String>> constructorDeps) {
        this.className = className;
        this.packageDeclaration = packageDeclaration;
        this.classSignature = classSignature;
        this.imports = imports;
        this.fields = fields;
        this.superClasses = superClasses;
        this.methodSignatures = methodSignatures;
        this.briefMethods = briefMethods;
        this.hasConstructor = hasConstructor;
        this.constructors = constructors;
        this.getterSetters = getterSetters;
        this.constructorDeps = constructorDeps;
    }
}
