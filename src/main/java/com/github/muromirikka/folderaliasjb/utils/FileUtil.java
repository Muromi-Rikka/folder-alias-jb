package com.github.muromirikka.folderaliasjb.utils;

import com.github.muromirikka.folderaliasjb.service.ProjectService;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;

public class FileUtil {

    public static String getBasePath(AnActionEvent anActionEvent){
        final Project project = anActionEvent.getProject();
        VirtualFile file = VIRTUAL_FILE.getData(anActionEvent.getDataContext());
        assert file != null;
        assert project != null;
        return getRelativizePath(file,project);
    }

    public static PsiFileNode PsiFileSet(PsiFileNode child,Project project){
        PsiFile psiFile = child.getValue();

        return new PsiFileNode(project,psiFile,child.getSettings()){

            @Override
            protected void updateImpl(@NotNull PresentationData data){
                super.updateImpl(data);
                final VirtualFile file =  child.getVirtualFile();
                setText(data,file,project);
            }
        };
    }


    public static PsiDirectoryNode PsiDirectorySet(PsiDirectoryNode child, Project project){
        PsiDirectory PsiDirectory = child.getValue();
        return new PsiDirectoryNode(project,PsiDirectory,child.getSettings()){
            @Override
            protected void updateImpl(@NotNull PresentationData data){
                super.updateImpl(data);
                final VirtualFile file = child.getVirtualFile();
                setText(data,file,project);
            }
        };
    }

    private static void setText( PresentationData data,VirtualFile file,Project project){
        if(file!=null){
            Map<String,Object> config = getConfig(file,project);
                if(config!=null){
                    data.addText(file.getName(),SimpleTextAttributes.REGULAR_ATTRIBUTES);
                    data.addText(" ",SimpleTextAttributes.REGULAR_ATTRIBUTES);
                    data.addText((String) config.get("description"),SimpleTextAttributes.GRAY_ATTRIBUTES);
                }
        }
    }

    private static Map<String,Object> getConfig(VirtualFile file, Project project){
        ProjectService projectService = project.getService(ProjectService.class);
        return projectService.getConfig().get(getRelativizePath(file,project));
    }

    private static String getRelativizePath(VirtualFile file, Project project){
        Path basePath = Paths.get(Objects.requireNonNull(project.getBasePath()));
        Path nowFile = Paths.get(file.getPath());
        return basePath.relativize(nowFile).toString().replaceAll("\\\\","/");
    }

}
