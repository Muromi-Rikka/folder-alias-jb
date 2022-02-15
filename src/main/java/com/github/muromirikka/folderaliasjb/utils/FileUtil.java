package com.github.muromirikka.folderaliasjb.utils;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;

public class FileUtil {

    public static String getBasePath(AnActionEvent anActionEvent){
        final Project project = anActionEvent.getProject();
        if(null == project){
            return "";
        }

        VirtualFile file = VIRTUAL_FILE.getData(anActionEvent.getDataContext());
        if(null!= file){
            return file.getPath().replace(project.getBasePath()+"/","");
        }
        return "";
    }

}
