package SearchChangedMethodInCallGraph;

import ActionChangeAnalyze.ITreeTypes;
import CallGraph.CallGraphResult;
import CallGraph.Invocation;
import com.github.gumtreediff.tree.ITree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GraphMethodSearcher {
    private final Set<ITree> checkForTest;
    private final CallGraphResult callGraphResult;
    private final ITreeTypes iTreeTypes;
    private List<String> testMethodsToRunAgain;

    public GraphMethodSearcher(Set<ITree> checkForTest, CallGraphResult callGraphResult){
        this.checkForTest = checkForTest;
        this.callGraphResult = callGraphResult;
        this.iTreeTypes = new ITreeTypes();
        this.testMethodsToRunAgain = new ArrayList<>();
    }

    public void searchInCallGraph(){
        System.out.println("searching for method...");
        for(ITree iTree: this.checkForTest){
            if(checkMethodSignature(iTree)){

            }
        }
    }
    private boolean checkMethodSignature(ITree iTree){
        for(Invocation i: this.callGraphResult.getAllInvocations()){
            if(checkTypeMethod(iTree.getType()) && i.getMethodSignature().equals(iTree.getLabel())){
                return true;
            }
        }
        return false;
    }
    private boolean checkTypeMethod(int type){
        return type==this.iTreeTypes.getTypeMethod();
    }
}
