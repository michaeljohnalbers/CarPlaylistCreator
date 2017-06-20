/**
 * @file DirectoryChangeListener.java
 * @brief Interface for receiving notifications about root directory changes.
 */

package com.NoSuchCompany.CarPlaylistCreator.ui;

import java.nio.file.Path;

/**
 * Interface to allow an object to be notified of a change to the root
 * directory.
 */
public interface DirectoryChangeListener {
  /**
   * Called to notify an object about a change to the root directory.
   *
   * @param newDirectory new root directory
   */
  public void directoryChanged(Path newDirectory);
}
