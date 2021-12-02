package ActionChangeAnalyze;

import spoon.MavenLauncher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.util.ArrayList;

public class MavenLauncherCtModelsBuild {
    private final String projectOldPath;
    private final String projectNewPath;
    private CtModel modelOld;
    private CtModel modelNew;
    private CtModel modelNewTest;
    private CtModel onlyTest;
    private ArrayList onlyTestMethods;

    public MavenLauncherCtModelsBuild(String projectOldPath, String projectNewPath){
        this.projectNewPath = projectNewPath;
        this.projectOldPath = projectOldPath;
        this.onlyTestMethods = new ArrayList();
    }

    public void buildModels(){
        MavenLauncher launcherOld = new MavenLauncher(projectOldPath, MavenLauncher.SOURCE_TYPE.APP_SOURCE);
        MavenLauncher launcherNew = new MavenLauncher(projectNewPath, MavenLauncher.SOURCE_TYPE.APP_SOURCE);
        MavenLauncher launcherNewTest =new MavenLauncher(projectNewPath,MavenLauncher.SOURCE_TYPE.ALL_SOURCE);
        MavenLauncher onlytest = new MavenLauncher(projectNewPath,MavenLauncher.SOURCE_TYPE.TEST_SOURCE);

        //Ignore dependencies
        //launcher.getEnvironment().setNoClasspath(true);

        //Create AST of Project Old (ONLY Main)
        launcherOld.getEnvironment().setNoClasspath(true);
        launcherOld.buildModel();
        this.modelOld = launcherOld.getModel();
        //Create AST of Project New (ONLY Main)
        launcherNew.getEnvironment().setNoClasspath(true);
        launcherNew.buildModel();
        this.modelNew = launcherNew.getModel();
        //Create AST of project New (ALL Sources Test+Main)
        launcherNewTest.getEnvironment().setNoClasspath(true);
        launcherNewTest.buildModel();
        this.modelNewTest = launcherNewTest.getModel();
        //Create AST of Project New (ONLY Test)
        onlytest.getEnvironment().setNoClasspath(true);
        onlytest.buildModel();
        this.onlyTest = onlyTest;
        generateTestMethodList();
    }

    public CtModel getModelOld(){
        return this.modelOld;
    }
    public CtModel getModelNew(){
        return modelNew;
    }
    public  CtModel getModelNewTest(){
        return modelNewTest;
    }
    public CtModel getModelOnlyTest() {
        return onlyTest;
    }
    public void generateTestMethodList(){
        for(CtType clazz: this.onlyTest.getAllTypes()){
            for(Object o: clazz.getMethods()){
                CtMethod method = (CtMethod) o;
                this.onlyTestMethods.add(method);
            }
        }
    }
    public ArrayList getOnlyTestMethods() {
        return onlyTestMethods;
    }
}
