/**
 * @file PlaylistTest.java
 * @brief Tests for Playlist class.
 */

package com.NoSuchCompany.CarPlaylistCreator.playlist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for Playlist.
 */
public class PlaylistTest
{
  /**
   * Tests a very simple playlist
   */
  @Test
  public void readWriteBasicPlaylist() throws IOException {
    Path file = Paths.get("/tmp/Playlist1.m3u");
    playlists.add(file);
    List<String> lines = Arrays.asList(
        "#EXTM3U",
        "#EXTINF:0, Artist - Title",
        "../File.mp3",
        "#EXTINF:10, Artist - Title2",
        "../File2.mp3");
    Files.write(file, lines);

    Playlist playlist = new Playlist(file);
    List<PlaylistEntry> entries = playlist.getList();
    assertEquals(2, entries.size());
    assertTrue(lines.get(1), 
               lines.get(1).equals(entries.get(0).getCommentLine()));
    assertTrue(lines.get(2), 
               lines.get(2).equals(entries.get(0).getTrackLocation()));
    assertTrue(lines.get(3), 
               lines.get(3).equals(entries.get(1).getCommentLine()));
    assertTrue(lines.get(4), 
               lines.get(4).equals(entries.get(1).getTrackLocation()));

    Files.delete(file);
    playlist.writePlaylist();

    List<String> readLines = Files.readAllLines(file);
    assertEquals(lines.size(), readLines.size());
    for (int ii = 0; ii < lines.size(); ++ii)
    {
      String error = "Line " + ii + ": " + lines.get(ii);
      assertTrue(error, lines.get(ii).equals(readLines.get(ii)));
    }
  }

  @AfterClass
  public static void cleanUpLists() throws IOException{
    for (Path file : playlists)
    {
      Files.deleteIfExists(file);
    }
  }

  /** Names of playlists to write/cleanup. */
  private static List<Path> playlists = new Vector<>();
}
