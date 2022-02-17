package com.github.muromirikka.folderaliasjb.action;

import com.github.muromirikka.folderaliasjb.service.ProjectService;
import com.github.muromirikka.folderaliasjb.utils.FileUtil;
import com.intellij.icons.AllIcons;
import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;

public class ActionDescription extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String basePath = FileUtil.getBasePath(e);
        ProjectService projectService = e.getProject().getService(ProjectService.class);
        Map<String, Object> config = projectService.getConfig().get(basePath);

        String defaultInput = "";
        if(config!=null&&config.get("description")!=null){
            defaultInput = (String) config.get("description");
        }else{
            VirtualFile file = VIRTUAL_FILE.getData(e.getDataContext());
            defaultInput = file.getName();
        }
        String txt = Messages.showInputDialog(e.getProject(), "Add alias","Input Your Alias", AllIcons.Actions.MenuPaste,defaultInput,null);
        Map<String,Object> descriptionMap = new HashMap<>();
        descriptionMap.put("description",txt);
        try {
            projectService.setOnceConfig(basePath,descriptionMap);
            ProjectView.getInstance(e.getProject()).refresh();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }
}
