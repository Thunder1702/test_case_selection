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
    private List<ResultTuple> testMethodsToRunAgain;

    public GraphMethodSearcher(Set<ITree> checkForTest, CallGraphResult callGraphResult){
        this.checkForTest = checkForTest;
        this.callGraphResult = callGraphResult;
        this.iTreeTypes = new ITreeTypes();
        this.testMethodsToRunAgain = new ArrayList<>();
    }

    public List<ResultTuple> searchInCallGraph(){
        List<ResultTuple> resultList = new ArrayList<>();
        System.out.println("searching for Test-Methods to run again...");
        for(ITree iTree: this.checkForTest){
            resultList = checkMethodSignature(iTree);
            if(resultList!=null){
                for(ResultTuple tuple : resultList){
                    if(!checkForNull(tuple,iTree.getLabel())){
                        this.testMethodsToRunAgain.add(tuple);
                    }
                }
            }
        }
        removeNulls();
        return this.testMethodsToRunAgain;
    }
    private List<ResultTuple> checkMethodSignature(ITree iTree){
        List<ResultTuple> temp = new ArrayList<>();
        for(Invocation i: this.callGraphResult.getAllInvocations()){
            if(checkTypeMethod(iTree.getType()) && i.getMethodSignature().equals(iTree.getLabel()) &&i.getDeclaringType().equals(iTree.getParent().getLabel())){
                temp.add(new ResultTuple(i.getParentNode().getClassName(),i.getParentMethodSignature()));
//                return new ResultTuple(i.getParentNode().getClassName(),i.getParentMethodSignature());
            }
        }
        if(temp.isEmpty()){
            return null;
        }else {
            return temp;
        }

    }
    private boolean checkTypeMethod(int type){
        return type==this.iTreeTypes.getTypeMethod();
    }
    private boolean checkForNull(ResultTuple resultTuple, String methodToTest){
        if(resultTuple==null){
            System.out.println("Method "+ methodToTest+" has not been tested");
            return true;
        }
        return false;
    }
    private void removeNulls(){
        this.testMethodsToRunAgain.remove(null);
    }
}
