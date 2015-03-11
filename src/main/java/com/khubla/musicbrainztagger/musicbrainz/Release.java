package com.khubla.musicbrainztagger.musicbrainz;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author tom
 */
public class Release {
   private String country;
   private String status;
   private String date;
   private String barcode;
   private String title;
   @SerializedName("artist-credit")
   List<ArtistCredit> artistcredit = new ArrayList<ArtistCredit>();
   @SerializedName("text-representation")
   private TextRepresentation textRepresentation;
   private String id;

   public List<ArtistCredit> getArtistcredit() {
      return artistcredit;
   }

   public String getBarcode() {
      return barcode;
   }

   public String getCountry() {
      return country;
   }

   public String getDate() {
      return date;
   }

   public String getId() {
      return id;
   }

   public String getStatus() {
      return status;
   }

   public String getTitle() {
      return title;
   }

   public void setArtistcredit(List<ArtistCredit> artistcredit) {
      this.artistcredit = artistcredit;
   }

   public void setBarcode(String barcode) {
      this.barcode = barcode;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public void setTitle(String title) {
      this.title = title;
   }
}
