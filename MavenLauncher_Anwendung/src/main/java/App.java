import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.CompositeMatchers;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.tree.ITree;
import gumtree.spoon.builder.SpoonGumTreeBuilder;
import spoon.MavenLauncher;
import spoon.reflect.CtModel;
import spoon.reflect.CtModelImpl;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        String projectOldPath = "D:\\Dokumente\\1_Studium_0-Bachelorarbeit\\___________Working__________\\EasyCalc";
        String projectNewPath = "D:\\Dokumente\\1_Studium_0-Bachelorarbeit\\___________Working__________\\EasyCalc_NEU";

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
        //System.out.println(rootSpoonLeft.toTreeString());
        System.out.println("____________Information of actions_________");
        for (Action a:actions) {
            System.out.println("Hash: "+a.getNode().getHash());
            System.out.println("Type: "+a.getNode().getType());
            System.out.println("Label: "+a.getNode().getLabel());
        }
        System.out.println("___________Information End____________");

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

        //get Mappings from MappingStore
        Set<Mapping> maps = matcher.getMappings().asSet();
        //Check Mapping NodeLabel with Action->Node->NodeLabel
        for (Action a: actions){
            //Extract the NodeLabel from every action
            int type = a.getNode().getType();
            String label = a.getNode().getLabel();
            String nodeLabel = type+"@@"+label;
            System.out.println("NodeLabel: "+nodeLabel);

            //Get Mappings from mappingstore as Object
//            System.out.println(maps.size());
            Object[] mappings = maps.toArray();
            for(Object o:mappings){
//                System.out.println(o.toString());
                String object = o.toString();

                //Check if mapping includes NodeLabel from actions

                boolean check = object.contains(nodeLabel);
//                System.out.println("Check for NodeLabel: "+object.contains(nodeLabel));
                if(check){
                    //if true, output the mapped NodeLable from other Node
                    System.out.println(o);
                    System.out.println("Check for NodeLabel: "+object.contains(nodeLabel));
                    String nodeFound =object.split(",")[1];
                    nodeFound = nodeFound.replace(")","");
                    System.out.println("-----------NodeFound: "+nodeFound);
                    //Outputs information --> depth is important
                    System.out.println("__________rootSpoonRight Information____________");
                    System.out.println("Size: "+rootSpoonRight.getSize());
                    System.out.println("Depth: "+rootSpoonRight.getDepth());
                    System.out.println("Height: "+rootSpoonRight.getHeight());
                    System.out.println("Lenght: "+rootSpoonRight.getLength());
                    System.out.println("__________rootSpoonRight Information End____________");
                    //initialize for usage
                }
            }
        }



    }
}
