import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.CompositeMatchers;
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

public class App {
    public static void main(String[] args) {
        String projectOldPath = "D:\\Dokumente\\1_Studium_0-Bachelorarbeit\\MavenLauncher_Aenderungen_feststellen\\Test_Projekte\\Calculator_alt";
        String projectNewPath = "D:\\Dokumente\\1_Studium_0-Bachelorarbeit\\MavenLauncher_Aenderungen_feststellen\\Test_Projekte\\Calculator_neu";

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



    }
}
