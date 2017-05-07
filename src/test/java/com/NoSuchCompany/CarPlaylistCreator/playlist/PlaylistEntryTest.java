/**
 * @file PlaylistEntryTest.java
 * @brief Tests for PlaylistEntry class.
 */

package com.NoSuchCompany.CarPlaylistCreator.playlist;

import java.util.Set;
import java.util.HashSet;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit test for PlaylistEntry.
 */
public class PlaylistEntryTest
{
  /**
   * Tests the class fully.
   */
  @Test
  public void fullTest()
  {
    String comment = "#EXT";
    String trackLocation = "../Dir/File.mp3";
    PlaylistEntry entry = new PlaylistEntry(comment, trackLocation);
    assertTrue(entry.getCommentLine(), comment.equals(entry.getCommentLine()));
    assertTrue(entry.getTrackLocation(), 
               trackLocation.equals(entry.getTrackLocation()));
    assertTrue(new Long(entry.getId()).toString(), entry.getId() > 0);

    // Verify id's aren't reused.
    Set<Long> ids = new HashSet<>();
    for (int ii = 0; ii < 100; ++ii)
    {
      entry = new PlaylistEntry("", "");
      long id = entry.getId();
      assertFalse(new Long(id).toString(), ids.contains(id));
      ids.add(id);
    }
  }
}
