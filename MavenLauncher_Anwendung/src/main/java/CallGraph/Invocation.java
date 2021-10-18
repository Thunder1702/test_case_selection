package CallGraph;

import com.github.gumtreediff.tree.ITree;

//Copy from Fabian Oraze (automized-dp-conflict-resolver-main)
//partly changed

public class Invocation {
    /*
    * methodSignature = Name of the Method that calls Invocation
    * declaringType = Name of the Class/Interface etc where Method exists
    * parentNode = Node in which starts Invocation
    * nextNode = Node which will be called from Invocation (Node in which method exists)
    * iTreeNodeMethod = ITree Element of the Method
    */
    private String methodSignature;
    private String declaringType;
    private CallNode parentNode;
    private CallNode nextNode;
    private ITree iTreeNodeMethod;

    public Invocation(String methodSignature, String declaringType, CallNode parentNode, ITree iTreeNodeMethod) {
        this.setMethodSignature(methodSignature);
        this.setDeclaringType(declaringType);
        this.setParentNode(parentNode);
        this.iTreeNodeMethod = iTreeNodeMethod;
        this.nextNode = null;
    }

    public String getDeclaringType() {
        return declaringType;
    }

    public void setDeclaringType(String declaringType) {
        this.declaringType = declaringType.replace("[]", "");
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public CallNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(CallNode parentNode) {
        this.parentNode = parentNode;
    }

    public CallNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(CallNode nextNode) {
        this.nextNode = nextNode;
    }

    public void setITreeNodeMethod(ITree node){
        this.iTreeNodeMethod = node;
    }

    public ITree getITreeNodeMethod(){
        return this.iTreeNodeMethod;
    }
}
