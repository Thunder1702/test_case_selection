package CallGraph;

import com.github.gumtreediff.tree.ITree;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.List;

public class CallModel {
    /*
     * Maybe ctModel and iTreeOfModel = final???
     */
    private CtModel ctModelCompleteAST;
    private CtModel ctModelOnlyTestAST;
    private ITree iTreeOfModel;
    private List<CallNode> rootNodes;

    private CallModel(CtModel ctModelComplete,CtModel ctModeTest, ITree iTree){
        this.ctModelCompleteAST = ctModelComplete;
        this.ctModelOnlyTestAST = ctModeTest;
        this.iTreeOfModel = iTree;
        this.rootNodes = new ArrayList<>();
    }
    /*
     * Every single test class from package "test" is a root in a CallTree.
     * Every single test class from package "test" is the start(root) of a tree.
     * 1) Iterate over all classes in "test" and add it to callTreeRootList
     * 2) Add the right ITree Element for every class
     * 3) build a new tree for every Node in this list (add to CallNodeList from tree)
     * 4) Iterate over callTreeRootList, for every rootNode build Tree
     * Build Tree:
     * 1) check for Invocations, if yes, add Invocation to Node (when Invocation is complete = add to Tree)
     * After that the Tree has one root Node and a number of Invocations without a EndNode on it
     * 2) Find End Node (nextNode) and set it in Invocation
     * 3) Add the right ITree element from Method which exists nextNode class
     * Now Invocation is complete (has parent and nextNode and ITree element) --> add in List of Tree
     *
     * Now update this:
     * Case: One Method calls another method
     *
     */
    public void analyze(CtModel testModel, ITree iTree){
        for(CtType c: testModel.getAllTypes()){
            //Name überprüfen, ob passt, wenn nicht --> auslagern eigene Methode Name extrahieren.
            //richtiges ITree Element finden --> eigene Methode (auslagern)
            System.out.println(c.getQualifiedName());
            CallNode root = new CallNode(c.getQualifiedName(),null,iTree);
            this.rootNodes.add(root);
        }
    }


}
