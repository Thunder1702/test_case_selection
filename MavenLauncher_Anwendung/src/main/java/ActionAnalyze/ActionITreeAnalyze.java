package ActionAnalyze;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.tree.ITree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActionITreeAnalyze {
    private ITree iTreeModelNew;
    private ITree iTreeModelOld;
    private List<Action>  actions;
    private Set<ITree> checkForTestList;
    private Matcher matcher;
    private ITreeTypes types;
    private List<Action> inserts;
    private List<Action> updates;
    private List<Action> moves;
    private List<Action> deletes;

    public  ActionITreeAnalyze(ITree modelNew, ITree modelOld, List<Action> actions, Matcher matcher){
        this.iTreeModelNew = modelNew;
        this.iTreeModelOld = modelOld;
        this.actions = actions;
        this.matcher = matcher;
        this.checkForTestList = new HashSet<>();
        this.types = new ITreeTypes();
        this.inserts = new ArrayList<>();
        this.updates = new ArrayList<>();
        this.moves = new ArrayList<>();
        this.deletes = new ArrayList<>();
    }

    public void extractsTypesOfActions(){
        //maybe use this.actions not a parameter
        int num = 0;
        for(Action a: this.actions){
            System.out.println(num+") Node: "+a.getNode().toShortString());
            System.out.println(num+") ParentNode: "+a.getNode().getParent().toShortString());
            if(a.toString().startsWith("INS")){
                this.inserts.add(a);
            }else if(a.toString().startsWith("MOV")){
                this.moves.add(a);
            }else if(a.toString().startsWith("DEL")){
                this.deletes.add(a);
            }else if(a.toString().startsWith("UPD")){
                this.updates.add(a);
            }
            num++;
        }
        checkInserts();
        checkMoves();
        checkDeletes();
        checkUpdates();
    }
    private void checkInserts(){
        if(!this.inserts.isEmpty()){
            for(Action a: this.inserts){
                //Changes in Packages are not important
                if(excludePackages(a)){
                    //if true it is a method
                    //If Node is Method
                    if(checkForMethod(a)){
                        this.checkForTestList.add(traverseTree(this.iTreeModelNew,a.getNode()));
                    }
                    //if false it is an inner element of a methode or something else
                    //If Node is not a Method
                    else {
                        //search for parent(übergeordnete) method or class (if no parent method exists)
                        ITree parentForSearch = searchParentMethodOrClass(a.getNode());
                        if(parentForSearch != null){
                            System.out.println("Test search Parent: "+ parentForSearch.toShortString());
                            this.checkForTestList.add(traverseTree(this.iTreeModelNew,parentForSearch));
                        }
                    }
                }
            }
        }
    }
    private void checkMoves(){
        if(!this.moves.isEmpty()){
            for(Action a: this.moves){
                if(excludePackages(a)){
                    ITree nodeForSearchInTree = null;
                    if(checkForMethod(a)){
                        for (Mapping m:this.matcher.getMappings()) {
                            if(m.getFirst().toShortString().equals(a.getNode().toShortString())){
                                nodeForSearchInTree = m.getSecond();
                            }
                        }
                        assert nodeForSearchInTree != null;
                        ITree parentForSearch = searchParentMethodOrClass(nodeForSearchInTree);
                        assert parentForSearch != null;
                        System.out.println("Test search Parent: "+ parentForSearch.toShortString());
                        this.checkForTestList.add(traverseTree(this.iTreeModelNew,parentForSearch));

                    }else {
                        for(Mapping m:matcher.getMappings()){
                            if(m.getFirst().toShortString().equals(a.getNode().toShortString())){
                                nodeForSearchInTree = m.getSecond();
                            }
                        }
                        assert nodeForSearchInTree != null;
                        if(nodeForSearchInTree.getType()!=types.getTypeClass() && nodeForSearchInTree.getType()!=types.getTypeInterface()){
                            ITree parent = searchParentMethodOrClass(nodeForSearchInTree);
                            assert parent != null;
                            System.out.println("Test search Parent: "+ parent.toShortString());
                            this.checkForTestList.add(traverseTree(this.iTreeModelNew,parent));
                        }
                    }
                }

            }
        }

    }
    private void checkDeletes(){
        if(!this.deletes.isEmpty()){
            for(Action a: this.deletes){
                if(excludePackages(a)){
                    if(checkForMethod(a)){
                        System.out.println("Method "+a.getNode()+" has been deleted.");
                    }else {
                        ITree parent = searchParentMethodOrClass(a.getNode());
                        if(parent != null && parent.getType() !=types.getTypeClass()){
                            System.out.println(parent.toShortString());
                            ITree mappingNode = null;
                            for(Mapping m: matcher.getMappings()){
                                if(m.getFirst().toShortString().equals(parent.toShortString()) && m.getFirst().getParent().getType() ==types.getTypeClass()){
                                    mappingNode = m.getSecond();
                                }
                            }
                            this.checkForTestList.add(traverseTree(this.iTreeModelNew,mappingNode));
                        }
                    }
                }
            }
        }

    }
    private void checkUpdates(){
        if(!this.updates.isEmpty()){
            for(Action a: this.updates){
                if(excludePackages(a)){
                    if(checkForMethod(a)){

                    }else {

                    }
                }
            }
        }

    }
    public void outputActionInformation (List<Action> actions){
        //maybe use this.actions not a parameter
        for (Action a:actions) {
            System.out.println("__________Action Information__________");
            System.out.println("Hash: "+a.getNode().getHash());
            System.out.println("Type: "+a.getNode().getType());
            System.out.println("Label: "+a.getNode().getLabel());
            System.out.println("__________Action Information End__________");
        }
    }
    private ITree traverseTree(ITree tree,ITree searchNode){
        for (ITree t:tree.breadthFirst()) {
            if(t.getType()==searchNode.getType() && t.getLabel().equals(searchNode.getLabel()) && t.getParent().getType()==65190232){
                System.out.println("______________________Found__________________________");
                System.out.println("Node Found in ITree from ModelNew: "+t.toShortString());
                System.out.println("_______________________________________________________________");
                return t;
            }
        }
        return null;
    }
    private ITree searchParentMethodOrClass(ITree node){
        if(node.getParent().getType()==-1993687807 && node.getParent().getParent().getType()==65190232){
            return node.getParent();
        }else if(node.getParent().getType() ==65190232){
            return node.getParent();
        }else if(node.getParent().getType()== -1){
            return null;
        }
        return searchParentMethodOrClass(node.getParent());
    }
    //Exclude packages --> only Method changes
    private boolean excludePackages(Action a){
        return !(a.getNode().getType()==this.types.getTypePackage() || a.getNode().getParent().getType()==this.types.getTypePackage());
    }
    //If Node is Method
    private boolean checkForMethod(Action a){
        return a.getNode().getType()==this.types.getTypeMethod() && a.getNode().getParent().getType()==this.types.getTypeClass();
    }
}
