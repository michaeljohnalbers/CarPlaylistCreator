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

  public FileTreeModel(File root) {
    root_ = root;
  }

  @Override
  public void addTreeModelListener(javax.swing.event.TreeModelListener l) {
    //do nothing
  }

  @Override
  public Object getChild(Object parent, int index) {
    File f = (File) parent;
    return f.listFiles()[index];
  }

  @Override
  public int getChildCount(Object parent) {
    File f = (File) parent;
    if (!f.isDirectory()) {
      return 0;
    } else {
      return f.list().length;
    }
  }

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    File par = (File) parent;
    File ch = (File) child;
    return Arrays.asList(par.listFiles()).indexOf(ch);
  }

  @Override
  public Object getRoot() {
    return root_;
  }

  @Override
  public boolean isLeaf(Object node) {
    File f = (File) node;
    return !f.isDirectory();
  }

  @Override
  public void removeTreeModelListener(javax.swing.event.TreeModelListener l) {
    //do nothing
  }

  @Override
  public void valueForPathChanged(javax.swing.tree.TreePath path, Object newValue) {
    //do nothing
  }

  /** Root of file tree. */
  private File root_;

}
