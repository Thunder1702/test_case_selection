package CallGraph;

//Copy from Fabian Oraze (automized-dp-conflict-resolver-main)

import java.util.*;

public class CallTree {
    private List<CallNode> rootNodes;
    private Set<Invocation> leafInvocations;

    public CallTree() {
        this.rootNodes = new ArrayList<>();
        this.leafInvocations = new HashSet<>();
    }

    public List<CallNode> getRootNodes() {
        return this.rootNodes;
    }

    public void addNodes(Collection<CallNode> nodes) {
        this.rootNodes.addAll(nodes);
    }

    public Set<Invocation> getCurrLeaves() {
        return this.leafInvocations;
    }

    public void addLeaves(Collection<Invocation> leaves) {
        this.leafInvocations.addAll(leaves);
    }

    public void removeLeaves(Collection<Invocation> leaves) {
        this.leafInvocations.removeAll(leaves);
    }

    /**
     * helper function to compute the current leaf elements of the whole call tree
     * new leaf elements are appended via the next() method from callNodes class to invocation objects
     * old leaves are then removed
     */
    public void computeLeafElements() {
        List<Invocation> toBeRemoved = new ArrayList<>();
        List<Invocation> toBeAdded = new ArrayList<>();
        for (Invocation invocation : getCurrLeaves()) {
            if (!invocation.isLeafInvocation()) {
                toBeAdded.addAll(invocation.getNextNode().getInvocations());
                toBeRemoved.add(invocation);
            }
        }
        addLeaves(toBeAdded);
        removeLeaves(toBeRemoved);
    }
}
