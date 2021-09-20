import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.CompositeMatchers;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import gumtree.spoon.builder.SpoonGumTreeBuilder;
import spoon.MavenLauncher;
import spoon.reflect.CtModel;
import spoon.reflect.CtModelImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        String projectOldPath = "D:\\Dokumente\\1_Studium_0-Bachelorarbeit\\___________Working__________\\EasyCalc";
        String projectNewPath = "D:\\Dokumente\\1_Studium_0-Bachelorarbeit\\___________Working__________\\EasyCalc_NEU";

//        String projectOldPath = "D:\\Dokumente\\1_Studium_0-Bachelorarbeit\\___________Working__________\\test_case_selection\\Test_Projekte\\Calculator_alt";
//        String projectNewPath = "D:\\Dokumente\\1_Studium_0-Bachelorarbeit\\___________Working__________\\test_case_selection\\Test_Projekte\\Calculator_neu";

        MavenLauncher launcherOld = new MavenLauncher(projectOldPath, MavenLauncher.SOURCE_TYPE.ALL_SOURCE);
        MavenLauncher launcherNew = new MavenLauncher(projectNewPath, MavenLauncher.SOURCE_TYPE.ALL_SOURCE);

        //Create AST of Project Old
        launcherOld.buildModel();
        CtModel modelOld = launcherOld.getModel();
        //Create AST of Project New
        launcherNew.buildModel();
        CtModel modelNew = launcherNew.getModel();

        modelOld.getElements(ctElement -> ctElement instanceof CtModelImpl.CtRootPackage).forEach(System.out::println);
        modelNew.getElements(ctElement -> ctElement instanceof CtModelImpl.CtRootPackage).forEach(System.out::println);

        //modelOld.getElements(ctElement -> true).forEach(System.out::println);
        //modelNew.getElements(ctElement -> true).forEach(System.out::println);

        final SpoonGumTreeBuilder scanner = new SpoonGumTreeBuilder();


        ITree rootSpoonLeft = scanner.getTree(modelOld.getElements(ctElement -> ctElement instanceof CtModelImpl.CtRootPackage).get(0));
        ITree rootSpoonRight = scanner.getTree(modelNew.getElements(ctElement -> ctElement instanceof CtModelImpl.CtRootPackage).get(0));

        final MappingStore mappingsComp = new MappingStore();

        final Matcher matcher = new CompositeMatchers.ClassicGumtree(rootSpoonLeft, rootSpoonRight, mappingsComp);
        matcher.match();

        final ActionGenerator actionGenerator = new ActionGenerator(rootSpoonLeft, rootSpoonRight, matcher.getMappings());
        List<Action> actions = actionGenerator.generate();

        actions.forEach(System.out::println);
        System.out.println("______________________________________________________________");

//        outputActionInformation(actions);

//Extract types of actions
        ArrayList<Action> inserts = new ArrayList<>();
        ArrayList<Action> deletes = new ArrayList<>();
        ArrayList<Action> updates = new ArrayList<>();
        ArrayList<Action> moves = new ArrayList<>();

        actions.forEach(action -> {
            if(action.toString().startsWith("INS")){
                inserts.add(action);
            }else if(action.toString().startsWith("MOV")){
                moves.add(action);
            }else if(action.toString().startsWith("DEL")){
                deletes.add(action);
            }else if(action.toString().startsWith("UPD")){
                updates.add(action);
            }
        });

        int typeClass = 65190232;
        int typeInterface = -1788375783;
        int typePackage = 857590822;
        int typeMethod = -1993687807;
        int typeComment = -1679915457;
        int typeParameter = -33653874;
        int typeDatatype = 188328733;
        int typeVariable = 67875034;
        int typeValue = 1847113871;
        int typeReturn = -1850529456;
        int typeReturnDatatype = 69274153;

        Set<ITree> checkForTestsList = new HashSet<>();
        int num = 0;

        for (Action a:actions) {
            System.out.println(num+") Node: "+a.getNode().toShortString());
            System.out.println(num+") ParentNode: "+a.getNode().getParent().toShortString());

            if(a.toString().startsWith("INS")){
                //Exclude packages --> only Method changes
                if(!(a.getNode().getType() == typePackage || a.getNode().getParent().getType() == typePackage)){
                    //If Node is Method
                    if(a.getNode().getType()==typeMethod && a.getNode().getParent().getType()==typeClass){
                        checkForTestsList.add(traverseTree(rootSpoonRight,a.getNode()));
                        //If Node is not a Method
                    }else if(a.getNode().getType()!=typeMethod){
                        //search for parent(übergeordnete) method or class (if no parent method exists)
                        ITree parentForSearch = searchParentMethodOrClass(a.getNode());
                        if(parentForSearch != null){
                            System.out.println("Test search Parent: "+ parentForSearch.toShortString());
                            checkForTestsList.add(traverseTree(rootSpoonRight,parentForSearch));
                        }
                    }
                }

            }else if(a.toString().startsWith("MOV")){
                //Exclude packages --> only Method changes
                if(!(a.getNode().getType() == typePackage || a.getNode().getParent().getType() == typePackage)){
                    ITree nodeForSearchInTree = null;
                    //If Node is Method
                    if(a.getNode().getType()==typeMethod && a.getNode().getParent().getType()==typeClass){
                        for (Mapping m:matcher.getMappings()) {
                            if(m.getFirst().toShortString().equals(a.getNode().toShortString())){
                                nodeForSearchInTree = m.getSecond();
                            }
                        }
                        ITree parentForSearch = searchParentMethodOrClass(nodeForSearchInTree);
                        System.out.println("Test search Parent: "+ parentForSearch.toShortString());
                        checkForTestsList.add(traverseTree(rootSpoonRight,parentForSearch));
                        //If Node is not a Method
                    }else if(a.getNode().getType()!=typeMethod){
                        for(Mapping m:matcher.getMappings()){
                            if(m.getFirst().toShortString().equals(a.getNode().toShortString())){
                                nodeForSearchInTree = m.getSecond();
                            }
                        }
                        if(nodeForSearchInTree.getType()!=typeClass && nodeForSearchInTree.getType()!=typeInterface){
                            ITree parent = searchParentMethodOrClass(nodeForSearchInTree);
                            System.out.println("Test search Parent: "+ parent.toShortString());
                            checkForTestsList.add(traverseTree(rootSpoonRight,parent));
                        }
                    }
                }

            }else if(a.toString().startsWith("UPD")){
                //Exclude packages --> only Method changes
                if(!(a.getNode().getType() == typePackage || a.getNode().getParent().getType() == typePackage)){
                    ITree mappingNode = null;
                    //If Node is Method
                    if(a.getNode().getType()==typeMethod && a.getNode().getParent().getType()==typeClass){
                        for(Mapping m:matcher.getMappings()){
                            if(m.getFirst().toShortString().equals(a.getNode().toShortString())){
                                mappingNode = m.getSecond();
                            }
                        }
                        checkForTestsList.add(traverseTree(rootSpoonRight,mappingNode));
                        //If Node is not a Method
                    }else if(a.getNode().getType()!=typeMethod){
                        for(Mapping m:matcher.getMappings()){
                            if(m.getFirst().toShortString().equals(a.getNode().toShortString())){
                                mappingNode = m.getSecond();
                            }
                        }
                        ITree parent = searchParentMethodOrClass(mappingNode);
                        if(parent!= null){
                            System.out.println("Test search Parent: "+ parent.toShortString());
                            checkForTestsList.add(traverseTree(rootSpoonRight,parent));
                        }
                    }
                }

            }else if(a.toString().startsWith("DEL")){
                //Exclude packages --> only Method changes
                if(!(a.getNode().getType() == typePackage || a.getNode().getParent().getType() == typePackage)){
                    //If Node is Method --> ignored --> done by the compiler
                    if(a.getNode().getType()==typeMethod && a.getNode().getParent().getType()==typeClass){
                        //If Node is not a Method
                    }else if(a.getNode().getType()!=typeMethod){
                        ITree parent = searchParentMethodOrClass(a.getNode());
                        if(parent != null && parent.getType() != typeClass){
                            System.out.println(parent.toShortString());
                            ITree mappingNode = null;
                            for(Mapping m: matcher.getMappings()){
                                if(m.getFirst().toShortString().equals(parent.toShortString()) && m.getFirst().getParent().getType() == typeClass){
                                    mappingNode = m.getSecond();
                                }
                            }
                            checkForTestsList.add(traverseTree(rootSpoonRight,mappingNode));
                        }
                    }
                }

            }
            num++;
        }

        System.out.println("______________________________________________________________________________________");
        checkForTestsList.remove(null);
        System.out.println(checkForTestsList.size());
        for (ITree t:checkForTestsList) {
            System.out.println(t.toShortString());
            System.out.println(t.getParent().toShortString());
        }



    }
    public static List<ITree> checkRoot(ITree rootTree, int checkType, String checkLabel){
        if(rootTree.getType()==checkType && rootTree.getLabel().equals(checkLabel)){
            System.out.println("Found Node in Root.");
            return null;
        }else {
            return rootTree.getChildren();
        }
    }
    public static void traverseChildren(List<ITree> childrenList,int checkType, String checkLabel){
        int listSize = childrenList.size();

        for(int i = 0; i<listSize;i++){
            if(childrenList.get(i).getType()==checkType && childrenList.get(i).getLabel().equals(checkLabel) && childrenList.get(i).getParent().getType()==65190232){
                System.out.println("________Found:");
                System.out.println("Type: "+childrenList.get(i).getType());
                System.out.println("Label: "+childrenList.get(i).getLabel());
                System.out.println(childrenList.get(i).toTreeString());
                System.out.println(childrenList.get(i).toShortString());
                System.out.println("Parent: "+childrenList.get(i).getParent().toShortString());
                System.out.println("ID of Children: "+childrenList.get(i).getId());
            }else {
                if(childrenList.get(i).getChildren().size() != 0){
                      traverseChildren(childrenList.get(i).getChildren(),checkType,checkLabel);
                }
            }
        }
    }
    public static ITree searchParentMethodOrClass(ITree node){
     if(node.getParent().getType()==-1993687807 && node.getParent().getParent().getType()==65190232){
         return node.getParent();
     }else if(node.getParent().getType() ==65190232){
         return node.getParent();
     }else if(node.getParent().getType()== -1){
         return null;
     }
     return searchParentMethodOrClass(node.getParent());
    }
    public static void outputActionInformation (List<Action> actions){
        for (Action a:actions) {
            System.out.println("__________Action Information__________");
            System.out.println("Hash: "+a.getNode().getHash());
            System.out.println("Type: "+a.getNode().getType());
            System.out.println("Label: "+a.getNode().getLabel());
            System.out.println("__________Action Information End__________");
        }
    }
    public static ITree traverseTree(ITree tree,ITree searchNode){
        for (ITree t:tree.breadthFirst()) {
            if(t.getType()==searchNode.getType() && t.getLabel().equals(searchNode.getLabel()) && t.getParent().getType()==65190232){
                System.out.println("______________________Found__________________________");
                System.out.println("Node Found in AST Tree: "+t.toShortString());
                System.out.println("_______________________________________________________________");
                return t;
            }
        }
        return null;
    }
}
