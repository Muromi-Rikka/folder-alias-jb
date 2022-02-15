package com.github.muromirikka.folderaliasjb.tree;

import com.github.muromirikka.folderaliasjb.service.ProjectService;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class FolderAliasTreeStructureProvider implements TreeStructureProvider {
    @NotNull
    @Override
    public Collection<AbstractTreeNode<?>> modify(@NotNull AbstractTreeNode<?> parent,
                                                  @NotNull Collection<AbstractTreeNode<?>> children,
                                                  ViewSettings settings) {
        final Project project = parent.getProject();
        assert project != null;
        ProjectService projectService = project.getService(ProjectService.class);
        ArrayList<AbstractTreeNode<?>> nodes = new ArrayList<>();
        String basePath = project.getBasePath()+"/";
        for (AbstractTreeNode<?> child : children) {

            if(child instanceof PsiFileNode){
                PsiFile psiFile = ((PsiFileNode) child).getValue();
                ViewSettings setting = ((PsiFileNode) child).getSettings();
                PsiFileNode psiFileNode = new PsiFileNode(project,psiFile,setting){
                    @Override
                    protected void updateImpl(@NotNull PresentationData data){
                        super.updateImpl(data);
                        final VirtualFile file = ((PsiFileNode) child).getVirtualFile();
                        if(file!=null){

                            final VirtualFile fileParent =file.getParent();
                            if(fileParent!=null){
                                data.addText(file.getName(),SimpleTextAttributes.REGULAR_ATTRIBUTES);
                                String filePath = file.getPath().replace(basePath,"");
                                Map<String,Object> config = (Map<String, Object>) projectService.getConfig().get(filePath);
                                if(config!=null){
                                    data.addText(" ",SimpleTextAttributes.REGULAR_ATTRIBUTES);
                                    data.addText((String) config.get("description"),SimpleTextAttributes.GRAY_ATTRIBUTES);

                                }

                            }
                        }
                    }
                };
                nodes.add(psiFileNode);
            }else if(child instanceof PsiDirectoryNode){
                PsiDirectory PsiDirectory = ((PsiDirectoryNode) child).getValue();
                ViewSettings setting = ((PsiDirectoryNode) child).getSettings();
                PsiDirectoryNode psiDirectoryNode = new PsiDirectoryNode(project,PsiDirectory,setting){
                    @Override
                    protected void updateImpl(@NotNull PresentationData data){
                        super.updateImpl(data);
                        final VirtualFile file = ((PsiDirectoryNode)child).getVirtualFile();
                        if (file != null) {
                            final VirtualFile fileParent = file.getParent();
                            if (fileParent != null)
                                data.addText(file.getName(),SimpleTextAttributes.REGULAR_ATTRIBUTES);
                                String filePath = file.getPath().replace(basePath,"");
                                Map<String,Object> config = (Map<String, Object>) projectService.getConfig().get(filePath);
                                if(config!=null){
                                    data.addText(" ",SimpleTextAttributes.REGULAR_ATTRIBUTES);
                                    data.addText((String) config.get("description"),SimpleTextAttributes.GRAY_ATTRIBUTES);
                                }

                        }
                        data.setLocationString("");
                    }
                };
                nodes.add(psiDirectoryNode);
            }else{
                nodes.add(child);
            }

        }
        return nodes;
    }

    @Nullable
    @Override
    public Object getData(@NotNull Collection<AbstractTreeNode<?>> selected, @NotNull String dataId) {
        return null;
    }
}
