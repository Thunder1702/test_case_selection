import ActionAnalyze.ActionITreeAnalyze;
import ActionAnalyze.ITreeTypes;
import CallGraph.CallModel;
import CallGraph.CallNode;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {
    public static void main(String[] args) {
//        String projectOldPath = "D:\\Dokumente\\1_Studium_0-Bachelorarbeit\\___________Working__________\\EasyCalc";
//        String projectNewPath = "D:\\Dokumente\\1_Studium_0-Bachelorarbeit\\___________Working__________\\EasyCalc_NEU";

        String projectOldPath = "D:\\Dokumente\\1_Studium_0-Bachelorarbeit\\___________Working__________\\test_case_selection\\Test_Projekte\\Calculator_alt";
        String projectNewPath = "D:\\Dokumente\\1_Studium_0-Bachelorarbeit\\___________Working__________\\test_case_selection\\Test_Projekte\\Calculator_neu";

        MavenLauncher launcherOld = new MavenLauncher(projectOldPath, MavenLauncher.SOURCE_TYPE.APP_SOURCE);
        MavenLauncher launcherNew = new MavenLauncher(projectNewPath, MavenLauncher.SOURCE_TYPE.APP_SOURCE);
        MavenLauncher launcherNewTest =new MavenLauncher(projectNewPath,MavenLauncher.SOURCE_TYPE.ALL_SOURCE);

        //Create AST of Project Old (ONLY Main)
        launcherOld.buildModel();
        CtModel modelOld = launcherOld.getModel();
        //Create AST of Project New (ONLY Main)
        launcherNew.buildModel();
        CtModel modelNew = launcherNew.getModel();
        //Create AST of project New (ALL Sources Test+Main)
        launcherNewTest.buildModel();
        CtModel modelNewTest = launcherNewTest.getModel();


        final SpoonGumTreeBuilder scanner = new SpoonGumTreeBuilder();
        ITree rootSpoonLeft = scanner.getTree(modelOld.getElements(ctElement -> ctElement instanceof CtModelImpl.CtRootPackage).get(0));
        ITree rootSpoonRight = scanner.getTree(modelNew.getElements(ctElement -> ctElement instanceof CtModelImpl.CtRootPackage).get(0));
        ITree completeModelNewITree = scanner.getTree(modelNewTest.getElements(ctElement -> ctElement instanceof CtModelImpl.CtRootPackage).get(0));

        CallModel callModel = new CallModel(modelNew,modelNewTest,completeModelNewITree);
//        callModel.outputModelInformation(modelNewTest, "modelNewTest");
//        callModel.outputModelInformation(modelNew,"modelNew");
//        callModel.outputModelInformation(modelOld,"modelOld");

        List<CallNode> list = callModel.analyze();
        for(CallNode node: list){
            System.out.println("Node Classname: "+node.getClassName());
            if(node.getPrevious() != null){
                System.out.println("Previous: "+node.getPrevious().getClassName());
            }else {
                System.out.println("Previous: "+node.getPrevious());
            }
        }
        System.out.println("________________________________________________________________________________");

        final MappingStore mappingsComp = new MappingStore();
        final Matcher matcher = new CompositeMatchers.ClassicGumtree(rootSpoonLeft, rootSpoonRight, mappingsComp);
        matcher.match();

        final ActionGenerator actionGenerator = new ActionGenerator(rootSpoonLeft, rootSpoonRight, matcher.getMappings());
        List<Action> actions = actionGenerator.generate();

        actions.forEach(System.out::println);
        System.out.println("______________________________________________________________");

        ActionITreeAnalyze actionITreeAnalyze  = new ActionITreeAnalyze(rootSpoonRight,rootSpoonLeft, actions, matcher);
        actionITreeAnalyze.analyzeActions();
        actionITreeAnalyze.printCheckForTestList();
        Set<ITree> testCheckList = actionITreeAnalyze.getCheckForTestList();
    }
}