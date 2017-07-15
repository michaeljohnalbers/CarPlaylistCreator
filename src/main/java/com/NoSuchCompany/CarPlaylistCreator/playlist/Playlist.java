/**
 * @file Playlist.java
 * @brief Reads and writes a single playlist.
 */

package com.NoSuchCompany.CarPlaylistCreator.playlist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

/**
 * Manages a single M3U playlist.
 */
public class Playlist implements Comparable<Playlist> {

  /**
   * Constructor.
   *
   * @param playlistFile location of playlist file
   * @throws IOException on error reading the playlist file
   */
  public Playlist(Path playlistFile) throws IOException{
    playlistFile_ = playlistFile;
    readPlaylist();
  }

  /**
   * Adds the given playlist entry at the given position. Essentially a wrapper
   * around {@link java.util.List#add(int, T)}.
   *
   * @param index index at which to insert entry
   * @param entry playlist entry to add
   * @throws see {@link java.util.List#add(int, T)}
   */
  public void add(int index, PlaylistEntry entry) {
    entries_.add(index, entry);
  }

  @Override
  public int compareTo(Playlist other) {
    int compare = 0;
    if (other != null && other instanceof Playlist) {
      Playlist otherPlaylist = (Playlist) other;
      compare = playlistFile_.toString().compareToIgnoreCase(
          otherPlaylist.playlistFile_.toString());
    }
    return compare;
  }

  /**
   * Deletes the playlist from disk. Calling this if the playlist does not
   * exist does nothing.
   */
  public void delete() {
    playlistFile_.toFile().delete();
  }

  /**
   * Returns the playlist file.
   *
   * @return playlist file
   */
  public Path getFile() {
    return playlistFile_;
  }

  /**
   * Returns all playlist entries, in order.
   * @return playlist entries
   */
  public List<PlaylistEntry> getList() {
    return entries_;
  }

  /**
   * Reads in the playlist file.
   * @throws IOException on any error reading the file
   */
  private void readPlaylist() throws IOException{
    if (playlistFile_.toFile().exists()) {
      Scanner playlistReader = new Scanner(playlistFile_);

      String trackDirective = null;

      while (playlistReader.hasNextLine()){
        String line = playlistReader.nextLine();
        // TODO: add classes that will store empty lines and comments.
        if (! line.matches("^[ 	]*$")) {
          // This will ignore multiple header directives.
          if (line.matches("^[ 	]*#EXTM3U.*$")) {
            // Header directive. Ignore it.
          }
          else if (line.matches("^[ 	]*#EXTINF.*$")) {
            trackDirective = line;
          }
          else if (line.matches("^[ 	]*[^#].*$")) {
            // It's OK not to have a track directive.
            if (null == trackDirective) {
              trackDirective = "";
            }
            entries_.add(new PlaylistEntry(trackDirective, line));
            trackDirective = null;
          }
          else {
            // Line must start with a #, just a comment. Ignore it for the time
            // being.
          }
        }
      }
    }
    else {
      // Playlist file doesn't exist, must be creating a new playlist.
    }
  }

  /**
   * See {@link java.util.List#Remove(T)}.
   */
  public void remove(PlaylistEntry entry) {
    entries_.remove(entry);
  }

  /**
   * Returns the number of entries in the playlist.
   *
   * @return the number of entries in the playlist.
   */
  public int size() {
    return entries_.size();
  }

  /**
   * Returns the filename of the playlist. Implemented this way for use in
   * the playlist combo box in ui.Playlists.java.
   * @return playlist file name
   */
  @Override
  public String toString() {
    return playlistFile_.toFile().getName();
  }

  /**
   * Writes the playlist back to the original file.
   * @throws IOException on any error writing the file.
   */
  public void writePlaylist() throws IOException {
    List<String> playlistLines = new Vector<>();
    playlistLines.add("#EXTM3U");
    entries_.forEach((e) -> {
        playlistLines.add(e.getCommentLine());
        playlistLines.add(e.getTrackLocation());
            });
    Files.write(playlistFile_, playlistLines);
  }

  /** Location of the playlist file. */
  private Path playlistFile_;

  /** All of the entries in the playlist, in order. */
  private List<PlaylistEntry> entries_ = new Vector<>();
}
