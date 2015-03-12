package com.khubla.musicbrainztagger;

/**
 * @author tom
 */
public class TrackInformation {
   private final String artist;
   private final String title;
   private final String release;

   public TrackInformation(String artist, String title, String release) {
      this.artist = artist;
      this.title = title;
      this.release = release;
   }

   public String getArtist() {
      return artist;
   }

   public String getRelease() {
      return release;
   }

   public String getTitle() {
      return title;
   }
}
