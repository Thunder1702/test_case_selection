package CallGraph;

import java.util.List;
import java.util.Set;

public class CallTree {

    private List<CallNode> callNodesOfTree;
    private Set<Invocation> invocationsSetOfTree;

    public CallTree(List<CallNode> nodes, Set<Invocation> invocations){
        this.callNodesOfTree = nodes;
        this.invocationsSetOfTree = invocations;
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
}
