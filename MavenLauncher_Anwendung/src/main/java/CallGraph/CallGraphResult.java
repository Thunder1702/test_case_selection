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

    public void printNodes(){
        System.out.println("Nodes...");
        for(CallNode node: allNodes){
            System.out.println("Node Classname: "+node.getClassName());
            if(node.getPrevious() != null){
                System.out.println("Previous: "+node.getPrevious().getClassName());
            }else {
                System.out.println("Previous: "+node.getPrevious());
            }
        }
    }
    public  void printInvocations(){
        System.out.println("\nInvocations...");
        for(Invocation i: allInvocations){
            System.out.println("Method Signature: "+i.getMethodSignature());
            System.out.println("ParentMethodSignature: "+i.getParentMethodSignature());
            System.out.println("NextNode name: "+i.getNextNode().getClassName());
            System.out.println("Parent Node: "+i.getParentNode().getClassName());
            System.out.println("\n");
        }
    }
}
