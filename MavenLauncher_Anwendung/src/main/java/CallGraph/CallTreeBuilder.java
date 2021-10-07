package CallGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Maybe not needed, because of addNode and addInvocation Method in CallTree
 */

public class CallTreeBuilder {
    private CallTree callTree;
    private List<CallNode> nodes;
    private Set<Invocation> invocations;

    public CallTreeBuilder(){
        this.nodes = new ArrayList<>();
        this.invocations = new HashSet<>();
    }

    public List<CallNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<CallNode> nodes) {
        this.nodes = nodes;
    }
    public void addNode(CallNode nodeToAdd){
        this.nodes.add(nodeToAdd);
    }
    public void addInvocation(Invocation invocationToAdd){
        this.invocations.add(invocationToAdd);
    }
    public void removeNode(CallNode nodeToRemove){
        this.nodes.remove(nodeToRemove);
    }
    public void removeInvocation(Invocation invocationToRemove){
        this.invocations.remove(invocationToRemove);
    }

    public Set<Invocation> getInvocations() {
        return invocations;
    }

    public void setInvocations(Set<Invocation> invocations) {
        this.invocations = invocations;
    }
    public void setCallTree(List<CallNode> nodes, Set<Invocation> invocations){
        this.callTree = new CallTree(nodes,invocations);
    }

    public CallTree getCallTree() {
        return this.callTree;
    }
}
