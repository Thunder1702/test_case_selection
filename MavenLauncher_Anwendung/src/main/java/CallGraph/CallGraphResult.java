package CallGraph;

import java.util.List;

public class CallGraphResult {
    private final List<CallNode> allNodes;
    private final List<Invocation> allInvocations;

    public CallGraphResult(List<CallNode> nodeList, List<Invocation> invocationList){
        this.allNodes = nodeList;
        this.allInvocations = invocationList;
    }

    public List<CallNode> getAllNodes(){
        return this.allNodes;
    }
    public List<Invocation> getAllInvocations(){
        return this.allInvocations;
    }
}
