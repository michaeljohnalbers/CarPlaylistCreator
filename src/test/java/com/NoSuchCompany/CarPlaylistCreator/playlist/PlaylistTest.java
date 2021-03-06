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
import static org.hamcrest.Matchers.*;
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
    assertEquals(playlist.getFile(), file);

    List<PlaylistEntry> entries = playlist.getList();
    assertEquals(2, entries.size());
    assertEquals(2, playlist.size());
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

    assertTrue(playlist.toString(), 
               playlist.toString().equals(file.toFile().getName()));

    // Test adding an entry
    PlaylistEntry newEntry = new PlaylistEntry("Comment line", "File name");
    playlist.add(2, newEntry);
    assertEquals(3, playlist.size());
    assertEquals(newEntry, entries.get(2));

    // Test removing an entry
    playlist.remove(newEntry);
    assertEquals(2, playlist.size());
    for (int ii = 0; ii < entries.size(); ++ii) {
      assertThat("entry " + ii, newEntry, is(not(entries.get(ii))));
    }

    // Test that a non-existant playlist can be used (creating a new playlist).
    // No assert*, just letting JUnit handle exception (which is a failure)
    Playlist newPlaylist = new Playlist(Paths.get("/no/Such/playList.m3u"));
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
