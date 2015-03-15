package com.khubla.musicbrainztagger;

/**
 * @author tom
 */
public class TrackInformation {
   private final String artist;
   private final String title;
   private final String release;
   private final String musicbrainzid;
   private final byte[] artwork;
   private final String isrc;

   public TrackInformation(String artist, String title, String release, String musicbrainzid, byte[] artwork, String isrc) {
      this.artist = artist;
      this.title = title;
      this.release = release;
      this.musicbrainzid = musicbrainzid;
      this.artwork = artwork;
      this.isrc = isrc;
   }

   public String getArtist() {
      return artist;
   }

   public byte[] getArtwork() {
      return artwork;
   }

   public String getIsrc() {
      return isrc;
   }

   public String getMusicbrainzid() {
      return musicbrainzid;
   }

   public String getRelease() {
      return release;
   }

   public String getTitle() {
      return title;
   }
}
