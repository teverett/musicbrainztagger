package com.khubla.musicbrainztagger.musicbrainz;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author tom
 */
public class MusicBrainzResult {
   @SerializedName("video")
   String video;
   @SerializedName("artist-credit")
   List<ArtistCredit> artistcredit = new ArrayList<ArtistCredit>();
   @SerializedName("disambiguation")
   String disambiguation;
   @SerializedName("length")
   String length;

   public List<ArtistCredit> getArtistcredit() {
      return artistcredit;
   }

   public String getDisambiguation() {
      return disambiguation;
   }

   public String getLength() {
      return length;
   }

   public String getVideo() {
      return video;
   }

   public void setArtistcredit(List<ArtistCredit> artistcredit) {
      this.artistcredit = artistcredit;
   }

   public void setDisambiguation(String disambiguation) {
      this.disambiguation = disambiguation;
   }

   public void setLength(String length) {
      this.length = length;
   }

   public void setVideo(String video) {
      this.video = video;
   }
}