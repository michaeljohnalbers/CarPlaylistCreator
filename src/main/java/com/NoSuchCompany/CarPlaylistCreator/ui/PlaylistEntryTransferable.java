/**
 * @file PlaylistEntryTransferable.java
 * @brief Transferable object for carrying PlaylistEntry objects.
 */

package com.NoSuchCompany.CarPlaylistCreator.ui;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.List;

import com.NoSuchCompany.CarPlaylistCreator.playlist.PlaylistEntry;

/**
 * This class is a custom Transferable class used to drag/drop playlist entries.
 */
public class PlaylistEntryTransferable implements Transferable {
  public PlaylistEntryTransferable(List<PlaylistEntry> entries) {
    entries_ = entries;
  }

  @Override
  public DataFlavor[] getTransferDataFlavors() {
    DataFlavor flavors[] = {supportedFlavor_};
    return flavors;
  };

  @Override
  public Object getTransferData(DataFlavor flavor) 
      throws UnsupportedFlavorException {
    if (! isDataFlavorSupported(flavor)) {
      throw new UnsupportedFlavorException(flavor);
    }

    return entries_;
  }

  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor) {
    return (flavor != null && 
            PlaylistEntry.class == flavor.getRepresentationClass());
  }

  /**
   * Returns the supported data flavor.
   * @return supported data flavor.
   */
  public static DataFlavor getSupportedFlavor() {
    return supportedFlavor_;
  }

  /** Only supported flavor supported by this class. */
  private static DataFlavor supportedFlavor_ = 
      new DataFlavor(PlaylistEntry.class, "PlaylistEntry");

  /** Playlist entry/ies to transfer. */
  private List<PlaylistEntry> entries_;
}

