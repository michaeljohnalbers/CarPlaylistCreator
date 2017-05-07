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
public class Playlist {

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

  /**
   * Writes the playlist back to the original file.
   * @throws IOException on any error writing the file.
   */
  public void writePlaylist() throws IOException{
    List<String> playlistLines = new Vector<>();
    playlistLines.add("#EXTM3U");
    entries_.forEach((e) -> {
        playlistLines.add(e.getCommentLine());
        playlistLines.add(e.getTrackLocation());
            });
    Files.write(playlistFile_, playlistLines);

  };

  /** Location of the playlist file. */
  private Path playlistFile_;

  /** All of the entries in the playlist, in order. */
  private List<PlaylistEntry> entries_ = new Vector<>();
}
