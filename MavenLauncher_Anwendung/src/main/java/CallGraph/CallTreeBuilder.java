package CallGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Set<Invocation> getInvocations() {
        return invocations;
    }

    public void setInvocations(Set<Invocation> invocations) {
        this.invocations = invocations;
    }
}
