package SearchChangedMethodInCallGraph;

import ActionChangeAnalyze.ITreeTypes;
import CallGraph.CallGraphResult;
import CallGraph.Invocation;
import com.github.gumtreediff.tree.ITree;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphMethodSearcher {
    private final Set<ITree> checkForTest;
    private final CallGraphResult callGraphResult;
    private final ITreeTypes iTreeTypes;
    private Set<ResultTuple> testMethodsToRunAgain;
    private final ArrayList<CtMethod> testMethods;

    public GraphMethodSearcher(Set<ITree> checkForTest, CallGraphResult callGraphResult, ArrayList<CtMethod> testMethods){
        this.checkForTest = checkForTest;
        this.callGraphResult = callGraphResult;
        this.iTreeTypes = new ITreeTypes();
        this.testMethodsToRunAgain = new HashSet<>();
        this.testMethods = testMethods;
    }

    public Set<ResultTuple> searchInCallGraph(){
        List<ResultTuple> resultList;
        System.out.println("searching for Test-Methods to run again...");
        for(ITree iTree: this.checkForTest){
            resultList = checkMethodSignature(iTree);
            if(resultList!=null){
                for(ResultTuple tuple : resultList){
                    if(!checkForNull(tuple,iTree.getLabel()) && !checkForDuplicateTuple(tuple)){
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
            if(checkTypeMethod(iTree.getType()) && i.getMethodSignature().equals(iTree.getLabel()) &&i.getDeclaringType().equals(iTree.getParent().getLabel()) && checkIfTestMethod(i.getParentMethodSignature())){
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
    private boolean checkForDuplicateTuple(ResultTuple tuple){
        for(ResultTuple resultTuple: this.testMethodsToRunAgain){
            if(resultTuple.getMethodName().equals(tuple.getMethodName()) && resultTuple.getClassName().equals(tuple.getClassName())){
                return true;
            }
        }
        return false;
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
    private boolean checkIfTestMethod(String methodName){
        for(Object o: this.testMethods){
            CtMethod method = (CtMethod) o;
            if(method.getSimpleName().equals(methodName)){
                return true;
            }
        }
        return false;
    }
}
