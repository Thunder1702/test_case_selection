package CallGraph;

import java.util.ArrayList;
import java.util.List;

public class CallGraphResult {
    private List<CallNode> allNodes;
    private List<Invocation> allInvocations;

    public CallGraphResult(){
        this.allNodes = new ArrayList<>();
        this.allInvocations = new ArrayList<>();
    }
    public CallGraphResult(List<CallNode> nodeList, List<Invocation> invocationList){
        this.allNodes = nodeList;
        this.allInvocations = invocationList;
    }

    public void addNode(CallNode node){
        this.allNodes.add(node);
    }
    public void removeNode(CallNode node){
        this.allNodes.remove(node);
    }
    public void addInvocation(Invocation invocation){
        this.allInvocations.add(invocation);
    }
    public void removeInvocation(Invocation invocation){
        this.allInvocations.remove(invocation);
    }
    public List<CallNode> getAllNodes(){
        return this.allNodes;
    }
    public List<Invocation> getAllInvocations(){
        return this.allInvocations;
    }
}
