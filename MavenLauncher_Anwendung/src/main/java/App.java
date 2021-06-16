import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.CompositeMatchers;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.tree.ITree;
import gumtree.spoon.builder.SpoonGumTreeBuilder;
import spoon.MavenLauncher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtElement;

import java.util.List;

public class App {
    public static void main(String[] args) {
        //how decleare the path? how to input other projects?
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

        final SpoonGumTreeBuilder scanner = new SpoonGumTreeBuilder();

        //Difference between CtModel and CtElement?
        ITree rootSpoonLeft = scanner.getTree((CtElement) modelOld);
        ITree rootSpoonRight = scanner.getTree((CtElement) modelNew);

        final MappingStore mappingsComp = new MappingStore();

        final Matcher matcher = new CompositeMatchers.ClassicGumtree(rootSpoonLeft, rootSpoonRight, mappingsComp);
        matcher.match();

        final ActionGenerator actionGenerator = new ActionGenerator(rootSpoonLeft, rootSpoonRight, matcher.getMappings());
        List<Action> actions = actionGenerator.generate();

        actions.forEach(System.out::println);

    }
}
