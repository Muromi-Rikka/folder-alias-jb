package com.github.muromirikka.folderaliasjb.tree;

import com.github.muromirikka.folderaliasjb.utils.FileUtil;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.ExternalLibrariesNode;
import com.intellij.ide.projectView.impl.nodes.NamedLibraryElementNode;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;


public class FolderAliasTreeStructureProvider implements TreeStructureProvider {
    @NotNull
    @Override
    public Collection<AbstractTreeNode<?>> modify(@NotNull AbstractTreeNode<?> parent,
                                                  @NotNull Collection<AbstractTreeNode<?>> children,
                                                  ViewSettings settings) {
        final Project project = parent.getProject();
        ArrayList<AbstractTreeNode<?>> nodes = new ArrayList<>();
        for (AbstractTreeNode<?> child : children) {
            if(child instanceof PsiFileNode){
                nodes.add(FileUtil.PsiFileSet((PsiFileNode) child,project));
            }else if(child instanceof PsiDirectoryNode){
                nodes.add(FileUtil.PsiDirectorySet((PsiDirectoryNode) child,project));
            }else if(child instanceof NamedLibraryElementNode){
                nodes.add(child);
            }else if(child instanceof ExternalLibrariesNode){
                nodes.add(child);
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
