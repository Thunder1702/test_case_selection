package CallGraph;

import com.github.gumtreediff.tree.ITree;

import java.util.ArrayList;
import java.util.List;

//Copy from Fabian Oraze (automized-dp-conflict-resolver-main)
//partly changed

public class CallNode {
    /*
    * Node name = className
    * invocationList = Method that invocation
    * previous = Node from Invocation starts to current Node
    * iTreeNode = ITree Node for this class Node
    */
    private String className;
    private List<Invocation> invocationList;
    private CallNode previous;
    private ITree iTreeNode;

    public CallNode(String className, CallNode previous, ITree iTreeNode) {
        this.className = className;
        this.invocationList = new ArrayList<>();
        this.previous = previous;
        this.iTreeNode = iTreeNode;
    }
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public CallNode getPrevious() {
        return previous;
    }

    public void setPrevious(CallNode previous) {
        this.previous = previous;
    }

    public ITree getITreeNode() {
        return iTreeNode;
    }
    public void setITreeNode(ITree itreeNode){this.iTreeNode=itreeNode;}

    public List<Invocation> getInvocationList(){return this.invocationList;}

    public void addInvocation(Invocation invocation) {
        this.invocationList.add(invocation);
    }

    public boolean isLeafNode() {
        for (Invocation invocation : this.invocationList) {
            if (invocation.getNextNode() != null) return false;
        }
        return true;
    }
}
