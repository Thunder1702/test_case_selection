package SearchChangedMethodInCallGraph;

public class ResultTuple {
    private final String className;
    private final String methodName;

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
