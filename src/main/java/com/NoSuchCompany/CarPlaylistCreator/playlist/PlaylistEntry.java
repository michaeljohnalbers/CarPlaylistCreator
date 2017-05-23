/**
 * @file PlaylistEntry.java
 * @brief Stores a single playlist entry
 */

package com.NoSuchCompany.CarPlaylistCreator.playlist;

import java.io.File;

/**
 * Manages a single M3U playlist entry. Based off the wikipedia page:
 * https://en.wikipedia.org/wiki/M3U.
 */
public class PlaylistEntry {

  /**
   * Constructor.
   *
   * @param commentLine comment line for playlist entry
   * @param trackLocation path to the track
   */
  public PlaylistEntry(String commentLine, String trackLocation) {
    commentLine_ = commentLine;
    trackLocation_ = trackLocation;
    id_ = ++idGenerator;
  }

  /**
   * Returns the comment line.
   * @return comment line
   */
  public String getCommentLine() {
    return commentLine_;
  }

  /**
   * Returns the id of this entry.
   * @return id
   */
  public long getId() {
    return id_;
  }

  /**
   * Returns the location of the track.
   * @return track location
   */
  public String getTrackLocation() {
    return trackLocation_;
  }

  /**
   * Returns the file name of the playlist item. Implemented this way primarily
   * for ui.Playlists.java.
   * @return file name of track
   */
  @Override
  public String toString() {
    return new File(getTrackLocation()).getName();
  }

  /** Extended playlist directive. */
  private String commentLine_;
  /** Path to the track.*/
  private String trackLocation_;

  /** Unique id for this entry. For easier searching for entries. */
  private long id_;

  /** Used to create unique ids. */
  private static volatile long idGenerator = 0;
}
