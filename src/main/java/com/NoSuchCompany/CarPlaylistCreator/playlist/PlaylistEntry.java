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
    prettyFormat_ = new File(getTrackLocation()).getName();
    if (commentLine_ != null) {
      // 'Blood, Sweat & Tears' is why there is a limit here.
      String[] commentParts = commentLine.split(",", 2);
      if (commentParts.length == 2) {
        prettyFormat_ = commentParts[1];
      }
    }
    id_ = ++idGenerator;
  }

  @Override
  public boolean equals(Object e) {
    boolean equal = false;
    if (null != e && e instanceof PlaylistEntry) {
      PlaylistEntry entry = (PlaylistEntry) e;
      equal = (entry.getId() == getId());
    }
    return equal;
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
    return prettyFormat_;
  }

  /** Extended playlist directive. */
  private String commentLine_;

  /** Pretty print of the track for display. */
  private String prettyFormat_;

  /** Path to the track.*/
  private String trackLocation_;

  /** Unique id for this entry. For easier searching for entries. */
  private long id_;

  /** Used to create unique ids. */
  private static volatile long idGenerator = 0;
}
