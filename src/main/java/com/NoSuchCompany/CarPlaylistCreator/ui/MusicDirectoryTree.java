/**
 * @file MusicDirectoryTree.java
 * @brief Widgets containing the music directory tree used for building
 * playlists.
 */

package com.NoSuchCompany.CarPlaylistCreator.ui;

import java.io.File;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

/**
 */
class MusicDirectoryTree extends JPanel {

  /**
   * Constructor.
   */
  public MusicDirectoryTree() {
    super();
    JTree directoryTree = new JTree(new FileTreeModel(new File(".")));
    JScrollPane treeView = new JScrollPane(directoryTree);
    setLayout(new GridBagLayout());

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.BOTH;
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 1.0;
    constraints.weighty = 1.0;
    add(treeView, constraints);
  }
}
