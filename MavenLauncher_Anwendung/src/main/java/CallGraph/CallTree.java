package CallGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CallTree {

    private List<CallNode> callNodesOfTree;
    private Set<Invocation> invocationsSetOfTree;

    public CallTree(List<CallNode> nodes, Set<Invocation> invocations){
        this.callNodesOfTree = nodes;
        this.invocationsSetOfTree = invocations;
    }
    public CallTree(){
        this.callNodesOfTree = new ArrayList<>();
        this.invocationsSetOfTree = new HashSet<>();
    }

    public List<CallNode> getCallNodesOfTree() {
        return callNodesOfTree;
    }

    public void setCallNodesOfTree(List<CallNode> callNodesOfTree) {
        this.callNodesOfTree = callNodesOfTree;
    }

    public Set<Invocation> getInvocationsSetOfTree() {
        return invocationsSetOfTree;
    }

    public void setInvocationsSetOfTree(Set<Invocation> invocationsSetOfTree) {
        this.invocationsSetOfTree = invocationsSetOfTree;
    }

    public void addNode(CallNode nodeToAdd){
        this.callNodesOfTree.add(nodeToAdd);
    }
    public void addInvocation(Invocation invocationToAdd){
        this.invocationsSetOfTree.add(invocationToAdd);
    }

    public void removeNode(CallNode nodeToRemove){
        this.callNodesOfTree.remove(nodeToRemove);
    }
    public void removeInvocation(Invocation invocationToRemove){
        this.invocationsSetOfTree.remove(invocationToRemove);
    }
}
