package CallGraph;

import ActionAnalyze.ITreeTypes;
import com.github.gumtreediff.tree.ITree;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtAbstractInvocation;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CallModel {
    /*
     * Maybe ctModel and iTreeOfModel = final???
     */
    private final CtModel ctModelOnlyMainAST;
    private final CtModel ctModelCompleteWithTestAST;
    private final ITree iTreeOfModel;
    private List<CallNode> rootNodes;
    private final ITreeTypes types;
    private List<CallNode> nodesToTraverse;
    private List<CallNode> callNodes;
    private List<Invocation> invocations;

    public CallModel(CtModel ctModelMain,CtModel ctModelCompleteWithTest, ITree iTree){
        this.ctModelOnlyMainAST = ctModelMain;
        this.ctModelCompleteWithTestAST = ctModelCompleteWithTest;
        this.iTreeOfModel = iTree;
        this.rootNodes = new ArrayList<>();
        this.types = new ITreeTypes();
        this.nodesToTraverse = new ArrayList<>();
        this.callNodes =new ArrayList<>();
        this.invocations = new ArrayList<>();
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
    public CallGraphResult analyze(){
        for(CtType completeClazz: this.ctModelCompleteWithTestAST.getAllTypes()){
            if(filterTests(completeClazz)){
                //getSimpleName() --> only class Name without Packages.
                System.out.println("Clazz: "+completeClazz.getSimpleName());
                CallNode root = new CallNode(completeClazz.getSimpleName(),null,findITreeElement(this.iTreeOfModel,completeClazz.getSimpleName(),true,""));
                this.rootNodes.add(root);
                this.callNodes.add(root);
                searchForInvocation(completeClazz,root);
            }
        }
        return leftNodesToTraverse();
    }
    private CallGraphResult leftNodesToTraverse(){
        if(!this.nodesToTraverse.isEmpty()){
            CallNode nodeToTraverse = this.nodesToTraverse.get(0);
            this.nodesToTraverse.remove(nodeToTraverse);
            for(CtType clazz: this.ctModelCompleteWithTestAST.getAllTypes()){
                if(nodeToTraverse.getClassName().equals(clazz.getSimpleName())){
                    searchForInvocation(clazz,nodeToTraverse);
                }
            }
            leftNodesToTraverse();
        }
        return new CallGraphResult(this.callNodes,this.invocations);
    }
    private boolean filterTests(CtType c){
        for(CtType clazz: this.ctModelOnlyMainAST.getAllTypes()){
            if(c.getSimpleName().equals(clazz.getSimpleName())){
                return  false;
            }
        }
        return true;

    }
    /*
     * Maybe split into findITreeElementClass and findITreeElementMethod and findITreeElementConstructor?
     */
    private ITree findITreeElement(ITree iTree,String searchName, boolean isClass, String parentNameClassIfMethod){
        //searching ITree element for a class
        if(isClass){
            for(ITree t: iTree.breadthFirst()){
                if(checkTypeClass(t.getType())&& checkLabel(searchName,t.getLabel())){
                    System.out.println("ITree Element: "+t.toShortString());
                    return t;
                }
            }
        }
        //searching ITree element for a method or Constructor
        else {
            for(ITree t:iTree.breadthFirst()){
                if(checkTypeMethod(t.getType()) &&checkLabel(searchName,t.getLabel()) &&checkTypeClass(t.getParent().getType())&&checkLabel(parentNameClassIfMethod,t.getParent().getLabel())){
                    System.out.println("ITree Element: "+t.toShortString());
                    System.out.println("ITree Element Parent: "+t.getParent().toShortString());
                    return t;
                }else if(t.getType()==types.getTypeConstructor()&&t.getLabel().equals(searchName)&&t.getParent().getType()==types.getTypeClass()&&t.getParent().getLabel().equals(parentNameClassIfMethod)){
                    System.out.println("ITree Element: "+t.toShortString());
                    System.out.println("ITree Element Parent: "+t.getParent().toShortString());
                    return t;
                }
            }
        }
        return null;
    }
    private  boolean checkLabel(String name, String iTreeLabel){
        return iTreeLabel.equals(name);
    }
    private boolean checkTypeMethod(int type){
        return type==types.getTypeMethod();
    }
    private boolean checkTypeClass(int type){
        return type==types.getTypeClass();
    }
    private void searchForInvocation(CtType clazz, CallNode currNode){
        Set<CtMethod> methods = clazz.getMethods();
        for(CtMethod m: methods){
            System.out.println("Method: "+m.getSimpleName());
            List<CtAbstractInvocation> methodCalls = m.getElements(new TypeFilter<>(CtAbstractInvocation.class));
            List<CtConstructorCall> constructorCalls = m.filterChildren(new TypeFilter<>(CtConstructorCall.class)).list();
            // Is Empty if there are no Invocations in this method
            if(!methodCalls.isEmpty()){
                //for every method call and constructor call --> add Invocation to invocationList of currNode
//                System.out.println(methodCalls.size());
                for(CtAbstractInvocation i: methodCalls){
                    //create Invocation Method --> return Invocation
                    if(checkDeclaringType(i)){
                        System.out.println("DeclaringType: "+getMethodDeclaringType(i));
                        System.out.println(getMethodSignature(i));
                        createAndAddInvocation(i,currNode);
                    }
                }
                for(CtConstructorCall c:constructorCalls){
                    System.out.println("Constructor: "+c.getExecutable().getSimpleName());
                }
                //add Invocation
                //create new CallNode (=nextNode from Invocation and previousNode = currNode)
            }
        }
    }
    private boolean checkDeclaringType(CtAbstractInvocation i){
        CtTypeReference fromType = i.getExecutable().getDeclaringType();
        return !isPartOfJDK(fromType.getQualifiedName());

    }
    private void createAndAddInvocation(CtAbstractInvocation i, CallNode currNode){
        System.out.println(currNode.getClassName());

        Invocation invocation = new Invocation(getMethodSignature(i),getMethodDeclaringType(i),currNode,findITreeElement(this.iTreeOfModel,getMethodSignature(i),false, getMethodDeclaringType(i)));
        invocation.setNextNode(createNextNode(getMethodDeclaringType(i),currNode));
        this.invocations.add(invocation);
        currNode.addInvocation(invocation);
    }
    private CallNode createNextNode(String declaringType, CallNode currNode){
        CallNode nextNode = new CallNode(declaringType,currNode,findITreeElement(this.iTreeOfModel,declaringType,true,""));
        if(!checkForDuplicateNodeInList(nextNode)){
            this.nodesToTraverse.add(nextNode);
        }
        if(!checkForDuplicateNodeInOutputList(nextNode)){
            this.callNodes.add(nextNode);
        }
        return nextNode;
    }
    private boolean checkForDuplicateNodeInList(CallNode node){
        for(CallNode clazz: this.nodesToTraverse){
            if(checkClassName(clazz,node) && checkITreeElement(clazz,node) && checkPreviousNode(clazz,node)){
                return true;
            }
        }
        return false;
    }
    private boolean checkForDuplicateNodeInOutputList(CallNode node){
        for(CallNode n: this.callNodes){
            if(checkClassName(n,node) && checkITreeElement(n,node) && checkPreviousNode(n,node)){
                return true;
            }
        }
        return false;
    }
    private boolean checkClassName(CallNode node1, CallNode node2){
        return node1.getClassName().equals(node2.getClassName());
    }
    private boolean checkITreeElement(CallNode node1, CallNode node2){
        return node1.getITreeNode().getLabel().equals(node2.getITreeNode().getLabel()) && node1.getITreeNode().getType()==node2.getITreeNode().getType();
    }
    private boolean checkPreviousNode(CallNode node1, CallNode node2){
        return checkClassName(node1.getPrevious(), node2.getPrevious()) && checkITreeElement(node1.getPrevious(), node2.getPrevious());
    }

    private boolean isPartOfJDK(String qualifiedName){
        return qualifiedName.startsWith("java.") || (qualifiedName.startsWith("javax.xml.parsers.")
                || (qualifiedName.startsWith("com.sun.")) || (qualifiedName.startsWith("sun."))
                || (qualifiedName.startsWith("oracle.")) || (qualifiedName.startsWith("org.xml"))
                || (qualifiedName.startsWith("com.oracle.")) || (qualifiedName.startsWith("jdk."))
                || (qualifiedName.startsWith("javax.xml.stream.")) || (qualifiedName.startsWith("javax.xml.transform."))
                || (qualifiedName.startsWith("org.w3c.dom."))) || (qualifiedName.startsWith("org.junit"));
    }
    private String getMethodSignature(CtAbstractInvocation element){
        return element.getExecutable().getSimpleName();
    }
    private String getMethodDeclaringType(CtAbstractInvocation invocation){
        return invocation.getExecutable().getDeclaringType().getSimpleName();
    }

    public void outputModelInformation(CtModel model, String modelName){
        System.out.println("___________________LOG____________________");
        System.out.println("Model Name: "+modelName);
        for(CtPackage p: model.getAllPackages()){
            System.out.println("Package: "+p.getQualifiedName());
        }
        Set<String> list = new HashSet<>();
        for(CtType c: model.getAllTypes()){
            if(c.isClass()){
                System.out.println("Class: "+c.getSimpleName());
                Set helper =  c.getMethods();
                for(Object m: helper){
                    list.add(m.toString());
                }
            }
        }
        list.forEach(System.out::println);
        System.out.println("___________________LOG END____________________");
    }
}
