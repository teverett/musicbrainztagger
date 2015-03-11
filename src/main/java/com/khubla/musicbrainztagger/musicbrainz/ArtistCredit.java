package com.khubla.musicbrainztagger.musicbrainz;

import com.google.gson.annotations.SerializedName;

/**
 * @author tom
 */
public class ArtistCredit {
   @SerializedName("artist")
   private Artist artist;
   @SerializedName("name")
   private String name;
   @SerializedName("joinphrase")
   private String joinphrase;

   public Artist getArtist() {
      return artist;
   }

   public String getJoinphrase() {
      return joinphrase;
   }

   public String getName() {
      return name;
   }

   public void setArtist(Artist artist) {
      this.artist = artist;
   }

   public void setJoinphrase(String joinphrase) {
      this.joinphrase = joinphrase;
   }

   public void setName(String name) {
      this.name = name;
   }
}
