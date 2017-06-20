/**
 * @file MusicDirectoryTree.java
 * @brief Widgets containing the music directory tree used for building
 * playlists.
 */

package com.NoSuchCompany.CarPlaylistCreator.ui;

import java.awt.datatransfer.Transferable;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.NoSuchCompany.CarPlaylistCreator.playlist.PlaylistEntry;

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
      directoryTree.setTransferHandler(new TransferHandler() {
          @Override 
          public int getSourceActions(JComponent c) {
            return COPY;
          }

          @Override
          public Transferable createTransferable(JComponent c) {
            JTree tree = (JTree) c;
            TreePath[] selections = tree.getSelectionPaths();
            List<PlaylistEntry> entries = new Vector<>();
            for (TreePath selection : selections) {
              FileTreeModel.FileNode treeNode = 
                  (FileTreeModel.FileNode) selection.getLastPathComponent();

              if (treeNode.isLeaf()) {
                int numComponents = selection.getPathCount();
                String[] pathComponents = new String[numComponents];
                for (int ii = 0; ii < numComponents; ++ii) {
                  pathComponents[ii] = 
                      selection.getPathComponent(ii).toString();
                }
                Path fullPath = Paths.get("..", pathComponents);
                entries.add(new PlaylistEntry("", fullPath.toString()));
              }
              else {
                // If a directory is selected at all, abort the drag. Prevents
                // possible confusion about allowing a directory to be dragged
                // when it actually isn't.
                return null;
              }
            }
            return new PlaylistEntryTransferable(entries);
          }
        });
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
