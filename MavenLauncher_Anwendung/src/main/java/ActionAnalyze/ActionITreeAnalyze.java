package ActionAnalyze;

import com.github.gumtreediff.actions.model.Action;
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

    public void extractsTypesOfActions(List<Action> actions){
        //maybe use this.actions not a parameter
        actions.forEach(action -> {
            if(action.toString().startsWith("INS")){
                this.inserts.add(action);
            }else if(action.toString().startsWith("MOV")){
                this.moves.add(action);
            }else if(action.toString().startsWith("DEL")){
                this.deletes.add(action);
            }else if(action.toString().startsWith("UPD")){
                this.updates.add(action);
            }
        });
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
    /*
     * Maybe should be deleted
     */
    public List<ITree> checkRoot(ITree rootTree, int checkType, String checkLabel){
        if(rootTree.getType()==checkType && rootTree.getLabel().equals(checkLabel)){
            System.out.println("Found Node in Root.");
            return null;
        }else {
            return rootTree.getChildren();
        }
    }
    /*
     * Maybe should be deleted
     */
    public void traverseChildren(List<ITree> childrenList,int checkType, String checkLabel){
        for (ITree iTree : childrenList) {
            if (iTree.getType() == checkType && iTree.getLabel().equals(checkLabel) && iTree.getParent().getType() == 65190232) {
                System.out.println("________Found:");
                System.out.println("Type: " + iTree.getType());
                System.out.println("Label: " + iTree.getLabel());
                System.out.println(iTree.toTreeString());
                System.out.println(iTree.toShortString());
                System.out.println("Parent: " + iTree.getParent().toShortString());
                System.out.println("ID of Children: " + iTree.getId());
            } else {
                if (iTree.getChildren().size() != 0) {
                    traverseChildren(iTree.getChildren(), checkType, checkLabel);
                }
            }
        }
    }
}
