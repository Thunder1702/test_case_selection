package CallGraph;

import com.github.gumtreediff.tree.ITree;
import spoon.reflect.CtModel;

public class CallModel {
    private CtModel ctModel;
    private ITree iTreeOfModel;

    private CallModel(CtModel ctModel, ITree iTree){
        this.ctModel = ctModel;
        this.iTreeOfModel = iTree;
    }

    public static void main(String[] args) {

    }
}
