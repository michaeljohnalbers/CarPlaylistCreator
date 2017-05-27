/**
 * @file Playlists.java
 * @brief Lists playlists and provides editing abilities.
 */

package com.NoSuchCompany.CarPlaylistCreator.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.TransferHandler;

import com.NoSuchCompany.CarPlaylistCreator.playlist.Playlist;
import com.NoSuchCompany.CarPlaylistCreator.playlist.PlaylistEntry;

public class Playlists extends JPanel
    implements DirectoryChangeListener, ActionListener {
  /**
   * Constructor.
   */
  public Playlists() {
    super();
    setLayout(new GridBagLayout());

    // TODO: see http://stackoverflow.com/questions/9370326/default-action-button-icons-in-java for Java icons
    // See also: https://jar-download.com/?detail_search=a%25253A%252522jlfgr%252522&search_type=2&a=jlfgr
    // https://stackoverflow.com/questions/15990258/maven-cant-execute-jar

    // DnD
    // https://docs.oracle.com/javase/tutorial/uiswing/dnd/intro.html
    // https://docs.oracle.com/javase/tutorial/uiswing/examples/dnd/BasicDnDProject/src/dnd/BasicDnD.java

    playlistSelector_.addActionListener(this);
    playlistSelector_.setActionCommand(PLAYLIST_SELECTED);

    playlistEntries_.setTransferHandler(new TransferHandler() {
        @Override
        public boolean canImport(TransferHandler.TransferSupport info) {
          if (!info.isDataFlavorSupported(
                  PlaylistEntryTransferable.getSupportedFlavor())) {
            return false;
          }

          JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();
          if (dl.getIndex() == -1) {
            return false;
          }
          return true;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
          JList list = (JList)c;
          Object[] values = list.getSelectedValues();
          PlaylistEntry[] entries = Arrays.copyOf(values, values.length,
                                                  PlaylistEntry[].class);

          PlaylistEntryTransferable transferable = 
              new PlaylistEntryTransferable(entries);
          return transferable;
        }

        @Override
        public int getSourceActions(JComponent c) {
          return MOVE;
        }

        @Override
        public void exportDone(JComponent c, Transferable t, int action) {
          if (MOVE == action) {
            try {
              Object data = t.getTransferData(
                  PlaylistEntryTransferable.getSupportedFlavor());
              PlaylistEntry entries[] = (PlaylistEntry[]) data;
              
              for (PlaylistEntry entry : entries) {
                playlistEntriesModel_.removeElement(entry);
              }
            }
            catch (UnsupportedFlavorException | IOException e) {
              // TODO: more sophisticated error?
              System.err.println("exportDone: " + e);
            }
          }
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport info) {
          if (!info.isDrop()) {
            return false;
          }

          DataFlavor playlistEntryFlavor = 
              PlaylistEntryTransferable.getSupportedFlavor();

          if (!info.isDataFlavorSupported(playlistEntryFlavor)) {
            return false;
          }

          JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();
          int index = dl.getIndex();
          // In case I change to add copy and forget to update this code.
          if (! dl.isInsert()) {
            return false;
          }

          try {
            // This is actually a
            // java.awt.dnd.DropTargetContext$TransferableProxy not a
            // PlaylistEntryTransferable (though I suspect it contains one).
            Transferable t = info.getTransferable();
            Object data = t.getTransferData(playlistEntryFlavor);
            PlaylistEntry entries[] = (PlaylistEntry[]) data;

            for (PlaylistEntry entry : entries) {
              playlistEntriesModel_.add(index, entry);
            }

            // TODO: update playlist object, mark it as needing save
          } 
          catch (ClassCastException e) {
            // TODO: better error handler?
            System.err.println(e);
          }
          catch (Exception e) {
            return false;
          }

          return true;
        }
      });
    playlistEntries_.setDropMode(DropMode.INSERT);
    playlistEntries_.setDragEnabled(true);

    int column = 0;

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = 0;
    constraints.gridy = column++;
    constraints.weightx = 1.0;
    constraints.weighty = 0.0;
    add(playlistSelector_, constraints);

    // Common constraints for the new/save/etc buttons.
    constraints.fill = GridBagConstraints.NONE;
    constraints.gridy = 0;
    constraints.weightx = 0.0;
    constraints.weighty = 0.0;

    // TODO: change letters to icons

    JButton saveButton = new JButton("S");
    saveButton.setActionCommand(SAVE_PLAYLIST);
    saveButton.addActionListener(this);
    constraints.gridx = column++;
    add(saveButton, constraints);

    JButton saveAllButton = new JButton("Sa");
    saveAllButton.setActionCommand(SAVE_ALL_PLAYLISTS);
    saveAllButton.addActionListener(this);
    constraints.gridx = column++;
    add(saveAllButton, constraints);

    JButton newButton = new JButton("N");
    newButton.setActionCommand(NEW_PLAYLIST);
    newButton.addActionListener(this);
    constraints.gridx = column++;
    add(newButton, constraints);

    JButton deleteButton = new JButton("D");
    deleteButton.setActionCommand(DELETE_PLAYLIST);
    deleteButton.addActionListener(this);
    constraints.gridx = column++;
    add(deleteButton, constraints);

    JScrollPane playlistScroller = new JScrollPane(playlistEntries_);
    constraints.fill = GridBagConstraints.BOTH;
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.weightx = 1.0;
    constraints.weighty = 1.0;
    constraints.gridwidth = column;
    add(playlistScroller, constraints);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case DELETE_PLAYLIST:
        deletePlaylist();
        break;

      case NEW_PLAYLIST:
        newPlaylist();
        break;

      case PLAYLIST_SELECTED:
        selectPlaylist();
        break;

      case SAVE_PLAYLIST:
        savePlaylist();
        break;

      case SAVE_ALL_PLAYLISTS:
        saveAllPlaylists();
        break;

      default:
        System.err.println("unhandled action in Playlists: " +
                           e.getActionCommand());
    }
  }

  /**
   * Deletes the current playlist.
   */
  private void deletePlaylist() {
    System.out.println("deletePlaylist stub");
  }

  @Override
  public void directoryChanged(Path newDirectory) {
    Path playlistPath = Paths.get(newDirectory.toString(), "Playlists");

    if (playlistPath.toFile().exists()) {
      // Clear out old playlists
      playlistSelector_.removeAllItems();
      playlistEntriesModel_.clear();
      File[] playlists = playlistPath.toFile().listFiles((file)->{
          return file.getName().matches("^.*m3u$");
        });
      try {
        for (File playlist : playlists) {
          playlistSelector_.addItem(new Playlist(playlist.toPath()));
        }
      } catch (IOException e) {
        // TODO: error
        System.err.println("Error creating Playlist: " + e);
      }
    }
    else {
      // TODO: error
      System.err.println("<No 'Playlists' directory found. " +
                         "Select another root directory.>");
    }
  }

  /**
   * Creates a new, empty playlist.
   */
  private void newPlaylist() {
    System.out.println("new playlist stub");
  }

  /**
   * Updates all widgets for selecting a new playlist. This clears the current
   * playlist and adds the entries for the new playlist.
   */
  private void selectPlaylist() {
    playlistEntriesModel_.clear();

    Playlist selectedPlaylist = (Playlist)playlistSelector_.getSelectedItem();

    List<PlaylistEntry> playlistEntries = selectedPlaylist.getList();
    for (PlaylistEntry playlistEntry : playlistEntries) {
      playlistEntriesModel_.addElement(playlistEntry);
    }
  }

  /**
   * Saves the current playlist to disk.
   */
  private void savePlaylist() {
    System.out.println("save playlist stub");
  }

  /**
   * Saves all current playlists to disk.
   */
  private void saveAllPlaylists() {
    System.out.println("save all playlists stub");
  }

  /** action command for 'delete' button. */
  private static final String DELETE_PLAYLIST = "delete_playlist";

  /** action command for 'new' button. */
  private static final String NEW_PLAYLIST = "new_playlist";

  /** action command for selecting a playlist */
  private static final String PLAYLIST_SELECTED = "playlist_selected";

  /** action command for saving all playlists */
  private static final String SAVE_ALL_PLAYLISTS = "save_all_playlists";

  /** action command for saving the current playlist */
  private static final String SAVE_PLAYLIST = "save_playlist";

  /** Combo box for selecting playlists. */
  private JComboBox<Playlist> playlistSelector_ = new JComboBox<>();

  /** Model for playlist entries list. */
  private DefaultListModel<PlaylistEntry> playlistEntriesModel_ =
      new DefaultListModel<>();

  /** List of all entries in a playlist. */
  private JList<PlaylistEntry> playlistEntries_ =
      new JList<>(playlistEntriesModel_);
}
