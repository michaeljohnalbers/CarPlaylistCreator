/**
 * @file Playlists.java
 * @brief Lists playlists and provides editing abilities.
 */

package com.NoSuchCompany.CarPlaylistCreator.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
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
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
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

    // DnD
    // https://docs.oracle.com/javase/tutorial/uiswing/dnd/intro.html
    // https://docs.oracle.com/javase/tutorial/uiswing/examples/dnd/BasicDnDProject/src/dnd/BasicDnD.java

    playlistSelector_.setMaximumRowCount(25); // Arbitrary number
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
        @SuppressWarnings("unchecked")
        protected Transferable createTransferable(JComponent c) {
          JList<PlaylistEntry> list = (JList<PlaylistEntry>)c;
          List<PlaylistEntry> entries = list.getSelectedValuesList();

          PlaylistEntryTransferable transferable = 
              new PlaylistEntryTransferable(entries);
          return transferable;
        }

        @Override
        public int getSourceActions(JComponent c) {
          return MOVE;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void exportDone(JComponent c, Transferable t, int action) {
          if (MOVE == action) {
            try {
              Object data = t.getTransferData(
                  PlaylistEntryTransferable.getSupportedFlavor());
              List<PlaylistEntry> entries = (List<PlaylistEntry>) data;
              
              Playlist currentPlaylist = 
                  (Playlist)playlistSelector_.getSelectedItem();

              for (PlaylistEntry entry : entries) {
                playlistEntriesModel_.removeElement(entry);
                currentPlaylist.remove(entry);
              }
            }
            catch (UnsupportedFlavorException | IOException e) {
              // TODO: more sophisticated error?
              System.err.println("exportDone: " + e);
            }
          }
        }

        @Override
        @SuppressWarnings("unchecked")
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
            List<PlaylistEntry> entries = (List<PlaylistEntry>) data;
            
            // Without this offset, multiple items in a single drag are added in
            // reverse order
            int indexOffset = 0;
            for (PlaylistEntry entry : entries) {
              // A new entry is created so that it gets a new ID. The primary
              // purpose for this is to allow the exportDone function to work
              // properly. If a new entry wasn't used, an entry couldn't be
              // moved up the list as it would be the first one found by
              // removeElement.
              int adjustedIndex = index + indexOffset;

              // Comment line is populated in MusicDirectoryTree.java.
              PlaylistEntry newEntry = new PlaylistEntry(
                  entry.getCommentLine(), entry.getTrackLocation());

              playlistEntriesModel_.add(adjustedIndex, newEntry);
              
              Playlist currentPlaylist = 
                  (Playlist)playlistSelector_.getSelectedItem();
              currentPlaylist.add(adjustedIndex, newEntry);

             indexOffset++;
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

    final String deleteAction = "deleteEntry";
    playlistEntries_.getInputMap().put(
        KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
        deleteAction);
    playlistEntries_.getInputMap().put(
        KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
        deleteAction);
    playlistEntries_.getActionMap().put(deleteAction, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          List<PlaylistEntry> selectedEntries = 
              playlistEntries_.getSelectedValuesList();
          Playlist currentPlaylist = 
              (Playlist)playlistSelector_.getSelectedItem();
          for (PlaylistEntry playlistEntry : selectedEntries) {
            // TODO: mark playlist as dirty
            currentPlaylist.remove(playlistEntry);
            playlistEntriesModel_.removeElement(playlistEntry);
          }
        }
      });

    // TODO: add context menu to delete entries

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
    saveButton.setToolTipText("Save current playlist");
    saveButton.setActionCommand(SAVE_PLAYLIST);
    saveButton.addActionListener(this);
    constraints.gridx = column++;
    add(saveButton, constraints);

    JButton saveAllButton = new JButton("Sa");
    saveAllButton.setToolTipText("Save all playlists");
    saveAllButton.setActionCommand(SAVE_ALL_PLAYLISTS);
    saveAllButton.addActionListener(this);
    constraints.gridx = column++;
    add(saveAllButton, constraints);

    JButton newButton = new JButton("N");
    newButton.setToolTipText("Create a new playlist");
    newButton.setActionCommand(NEW_PLAYLIST);
    newButton.addActionListener(this);
    constraints.gridx = column++;
    add(newButton, constraints);

    JButton deleteButton = new JButton("D");
    newButton.setToolTipText("Delete the selected playlist");
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
   * Adds a new playlist to the playlist selection widget. Keeps the playlists
   * in sorted order.
   *
   * @param newPlaylist playlist to add
   */
  private void addPlaylist(Playlist newPlaylist) {
    playlistSelector_.addItem(newPlaylist);
    playlistSelector_.setSelectedItem(newPlaylist);
  }

  /**
   * Deletes the current playlist.
   */
  private void deletePlaylist() {
    int userSelection = JOptionPane.showConfirmDialog(
        this, "Are you sure you want to delete the selected playlist?", 
        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
    if (JOptionPane.YES_OPTION == userSelection) {
      Playlist playlist = (Playlist) playlistSelector_.getSelectedItem();
      playlist.delete();
      playlistSelector_.removeItem(playlist);
    }
  }

  @Override
  public void directoryChanged(Path newDirectory) {
    playlistDirectory = Paths.get(newDirectory.toString(), "Playlists");

    if (playlistDirectory.toFile().exists()) {
      // Clear out old playlists
      playlistSelector_.removeAllItems();
      playlistEntriesModel_.clear();
      File[] playlists = playlistDirectory.toFile().listFiles((file)->{
          return file.getName().matches("^.*m3u$");
        });
      try {
        for (File playlist : playlists) {
          addPlaylist(new Playlist(playlist.toPath()));
        }
        if (playlists.length > 0) {
          playlistSelector_.setSelectedIndex(0);
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
    String playlistName = JOptionPane.showInputDialog(
        this, "Enter playlist name (with or without .m3u extension):",
        "New Playlist", JOptionPane.PLAIN_MESSAGE);
    if (null != playlistName && playlistName.length() > 0 && 
        playlistName.indexOf('/') == -1) {

      if (! playlistName.matches("^.*m3u$")) {
        playlistName += ".m3u";
      }

      Path playlistPath = Paths.get(playlistDirectory.toString(), playlistName);

      int numberPlaylists = playlistSelector_.getItemCount();
      Playlist foundPlaylist = null;
      for (int ii = 0; ii < numberPlaylists; ++ii) {
        Playlist playlist = playlistSelector_.getItemAt(ii);
        if (playlist.getFile().equals(playlistPath)) {
          foundPlaylist = playlist;
          break;
        }
      }

      if (null != foundPlaylist) {
        playlistSelector_.setSelectedItem(foundPlaylist);
      }
      else {
        try {
          addPlaylist(new Playlist(playlistPath));
        }
        catch (IOException e) {
          // TODO: error
          System.err.println("Error creating new playlist '" + playlistPath + 
                             "': " + e);
        }
      }
    }
    else {
      // TODO: error
      System.err.println("Error creating new playlist '" + playlistName + 
                         "'. Must be non-empty and cannot contain a '/'.");
    }
  }

  /**
   * Updates all widgets for selecting a new playlist. This clears the current
   * playlist and adds the entries for the new playlist.
   */
  private void selectPlaylist() {
    playlistEntriesModel_.clear();

    Playlist selectedPlaylist = (Playlist)playlistSelector_.getSelectedItem();

    // This will be null when the directory is changed. When removeAllItems it
    // causes the PLAYLIST_SELECTED event to be fired.
    if (null != selectedPlaylist) {
      List<PlaylistEntry> playlistEntries = selectedPlaylist.getList();
      for (PlaylistEntry playlistEntry : playlistEntries) {
        playlistEntriesModel_.addElement(playlistEntry);
      }
    }
  }

  /**
   * Saves the current playlist to disk.
   */
  private void savePlaylist() {
    Playlist selectedPlaylist = (Playlist) playlistSelector_.getSelectedItem();
    writePlaylist(selectedPlaylist);
  }

  /**
   * Saves all current playlists to disk.
   */
  private void saveAllPlaylists() {
    int numberPlaylists = playlistSelector_.getItemCount();
    for (int ii = 0; ii < numberPlaylists; ++ii) {
      Playlist playlist = (Playlist) playlistSelector_.getItemAt(ii);
      writePlaylist(playlist);
    }
  }

  /**
   * Writes the given playlist data to disk.
   *
   * @param playlist playlist to write
   */
  private void writePlaylist(Playlist playlist) {
    // TODO: add dirty flag to Playlist, only save if dirty
    try {
      playlist.writePlaylist();
    } 
    catch (IOException e) {
      // TODO: error
      System.err.println("Failed to save playlist '" + playlist + "': " + e);
    }
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

  /** Directory where playlists are located. */
  private Path playlistDirectory;

  /** Combo box for selecting playlists. */
  private JComboBox<Playlist> playlistSelector_ = new JComboBox<>(
      new SortedComboBoxModel<>());

  /** Model for playlist entries list. */
  private DefaultListModel<PlaylistEntry> playlistEntriesModel_ =
      new DefaultListModel<>();

  /** List of all entries in a playlist. */
  private JList<PlaylistEntry> playlistEntries_ =
      new JList<>(playlistEntriesModel_);
}
