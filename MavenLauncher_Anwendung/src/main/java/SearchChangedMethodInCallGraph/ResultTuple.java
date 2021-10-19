package SearchChangedMethodInCallGraph;

public class ResultTuple {
    private String className;
    private String methodName;

    public ResultTuple(String clazz, String method){
        this.className = clazz;
        this.methodName = method;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }
}
