/**
 * @file MusicDirectoryTree.java
 * @brief Widgets containing the music directory tree used for building
 * playlists.
 */

package com.NoSuchCompany.CarPlaylistCreator.ui;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

/**
 * This class displays the music directory as a file tree.
 */
class MusicDirectoryTree extends JPanel implements DirectoryChangeListener {

  /**
   * Constructor.
   */
  public MusicDirectoryTree() {
    super();
    setLayout(new GridBagLayout());
  }

  @Override
  public void directoryChanged(Path newDirectory) {
    Path musicPath = Paths.get(newDirectory.toString(), "Music");

    if (musicPath.toFile().exists()) {
      // Clear out old tree.
      removeAll();

      // TODO: may need to make into a SwingWorker
      JTree directoryTree = new JTree(new FileTreeModel(musicPath.toFile()));
      directoryTree.setDragEnabled(true);
      JScrollPane treeView = new JScrollPane(directoryTree);

      GridBagConstraints constraints = new GridBagConstraints();
      constraints.fill = GridBagConstraints.BOTH;
      constraints.gridx = 0;
      constraints.gridy = 0;
      constraints.weightx = 1.0;
      constraints.weighty = 1.0;
      add(treeView, constraints);
      validate();
    }
    else {
      // TODO: pop up error dialog
    }
  }
}
