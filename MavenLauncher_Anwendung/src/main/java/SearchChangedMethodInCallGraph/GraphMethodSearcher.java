package SearchChangedMethodInCallGraph;

import CallGraph.CallGraphResult;
import com.github.gumtreediff.tree.ITree;

import java.util.Set;

public class GraphMethodSearcher {
    private final Set<ITree> checkForTest;
    private final CallGraphResult callGraphResult;

    public GraphMethodSearcher(Set<ITree> checkForTest, CallGraphResult callGraphResult){
        this.checkForTest = checkForTest;
        this.callGraphResult = callGraphResult;
    }

    public void searchInCallGraph(){

    }
}
