package ActionChangeAnalyze;

import com.github.gumtreediff.tree.ITree;
import gumtree.spoon.builder.SpoonGumTreeBuilder;
import spoon.reflect.CtModel;
import spoon.reflect.CtModelImpl;

public class ITreeBuilder {
    final SpoonGumTreeBuilder scanner = new SpoonGumTreeBuilder();
    final private ITree rootSpoonLeft;
    final private ITree rootSpoonRight;
    final private ITree completeModelNewITree;

    public ITreeBuilder(CtModel modelOld, CtModel modelNew, CtModel modelNewTest){
        this.rootSpoonLeft = scanner.getTree(modelOld.getElements(ctElement -> ctElement instanceof CtModelImpl.CtRootPackage).get(0));
        this.rootSpoonRight = scanner.getTree(modelNew.getElements(ctElement -> ctElement instanceof CtModelImpl.CtRootPackage).get(0));
        this.completeModelNewITree = scanner.getTree(modelNewTest.getElements(ctElement -> ctElement instanceof CtModelImpl.CtRootPackage).get(0));
    }

    public ITree getRootSpoonLeft(){
        return rootSpoonLeft;
    }

    public ITree getRootSpoonRight() {
        return rootSpoonRight;
    }

    public ITree getCompleteModelNewITree() {
        return completeModelNewITree;
    }
}
