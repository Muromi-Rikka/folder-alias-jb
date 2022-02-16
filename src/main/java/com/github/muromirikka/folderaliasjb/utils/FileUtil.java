package com.github.muromirikka.folderaliasjb.utils;

import com.github.muromirikka.folderaliasjb.service.ProjectService;
import com.intellij.ide.projectView.PresentationData;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;


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

    public static void setText( PresentationData data,VirtualFile file,Project project){
        if(file!=null){
            Map<String,Object> config = getConfig(file,project);
                if(config!=null){

                    data.setLocationString((String) config.get("description"));
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
