/**
 * @file FileTreeModel.java
 * @brief Tree model for building file trees
 */

package com.NoSuchCompany.CarPlaylistCreator.ui;

import java.io.File;
import java.util.Arrays;
import javax.swing.tree.TreeModel;

/**
 * Tree model for buliding file trees. Taken from:
 * https://dzone.com/articles/taking-new-swing-tree-table-a-
 */
public class FileTreeModel implements TreeModel {

  /**
   * Custom class to override toString so only names, and not full names,
   * are printed in the tree.
   */
  public class FileNode extends File {
    public FileNode(File file) {
      super(file.getPath());
    }

    public boolean isLeaf() {
      return !isDirectory();
    }
    
    @Override
    public String toString() {
      return getName();
    }
  }

  public FileTreeModel(File root) {
    root_ = new FileNode(root);
  }

  @Override
  public void addTreeModelListener(javax.swing.event.TreeModelListener l) {
    //do nothing
  }

  @Override
  public Object getChild(Object parent, int index) {
    FileNode f = (FileNode) parent;
    return new FileNode(f.listFiles(filter_)[index]);
  }

  @Override
  public int getChildCount(Object parent) {
    FileNode f = (FileNode) parent;
    if (!f.isDirectory()) {
      return 0;
    } else {
      return f.listFiles(filter_).length;
    }
  }

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    FileNode par = (FileNode) parent;
    FileNode ch = (FileNode) child;
    return Arrays.asList(par.listFiles(filter_)).indexOf(ch);
  }

  @Override
  public Object getRoot() {
    return root_;
  }

  @Override
  public boolean isLeaf(Object node) {
    FileNode f = new FileNode((File) node);
    return f.isLeaf();
  }

  @Override
  public void removeTreeModelListener(javax.swing.event.TreeModelListener l) {
    //do nothing
  }

  @Override
  public void valueForPathChanged(javax.swing.tree.TreePath path, 
                                  Object newValue) {
    //do nothing
  }

  /** Root of file tree. */
  private FileNode root_;

  /** File filter to only display directories and music files. */
  final java.io.FileFilter filter_ = (file) -> {
    return file.isDirectory() || file.getName().matches("^.*mp3");
  };
}
