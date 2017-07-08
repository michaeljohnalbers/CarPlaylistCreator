/**
 * @file Metadata.java
 * @brief Contains metadata for a music file.
 */

package com.NoSuchCompany.CarPlaylistCreator.file;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class Metadata {

  static {
    // Otherwise a bunch of garbage is dumped to console.
    Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
  }

  /**
   * Constructor.
   *
   * @param file audio file to read
   * @throws IOException on any error reading data from the file.
   */
  public Metadata(File file) throws IOException{
    try {
      AudioFile audioFile = AudioFileIO.read(file);
      Tag tag = audioFile.getTag();
      artist_ = tag.getFirst(FieldKey.ARTIST);
      title_ = tag.getFirst(FieldKey.TITLE);
      durationInSeconds_ = audioFile.getAudioHeader().getTrackLength();
    }
    catch (Exception e) {
      // The jAudioTagger code can throw so many exceptions that it's easier
      // to just catch the generic exception.
      String error = "Error reading metadata from '" + file + "': " + e;
      IOException ioe = new IOException(error, e);
    }
  }

  /**
   * Returns the song's artist.
   *
   * @return artist
   */
  public String getArtist() {
    return artist_;
  }

  /**
   * Returns the duration of the song, in seconds.
   *
   * @return duration
   */
  public int getDurationInSeconds() {
    return durationInSeconds_;
  }

  /**
   * Returns the title of the song.
   *
   * @return title
   */
  public String getTitle() {
    return title_;
  }

  /** Song artist. */
  private String artist_;

  /** Duration of the song. */
  private int durationInSeconds_;

  /** Song title. */
  private String title_;
}
