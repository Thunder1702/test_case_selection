package ActionChangeAnalyze;

import spoon.MavenLauncher;
import spoon.reflect.CtModel;

public class MavenLauncherCtModelsBuild {
    private String projectOldPath;
    private String projectNewPath;
    private CtModel modelOld;
    private CtModel modelNew;
    private CtModel modelNewTest;

    public MavenLauncherCtModelsBuild(String projectOldPath, String projectNewPath){
        this.projectNewPath = projectNewPath;
        this.projectOldPath = projectOldPath;
    }

    public void buildModels(){
        MavenLauncher launcherOld = new MavenLauncher(projectOldPath, MavenLauncher.SOURCE_TYPE.APP_SOURCE);
        MavenLauncher launcherNew = new MavenLauncher(projectNewPath, MavenLauncher.SOURCE_TYPE.APP_SOURCE);
        MavenLauncher launcherNewTest =new MavenLauncher(projectNewPath,MavenLauncher.SOURCE_TYPE.ALL_SOURCE);

        //No dependencies?? maybe fixes the problem
        //launcher.getEnvironment().setNoClasspath(true);

        //Create AST of Project Old (ONLY Main)
        launcherOld.buildModel();
        this.modelOld = launcherOld.getModel();
        //Create AST of Project New (ONLY Main)
        launcherNew.buildModel();
        this.modelNew = launcherNew.getModel();
        //Create AST of project New (ALL Sources Test+Main)
        launcherNewTest.buildModel();
        this.modelNewTest = launcherNewTest.getModel();
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
}
