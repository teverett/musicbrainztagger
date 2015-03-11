package com.khubla.musicbrainztagger.musicbrainz;

import com.google.gson.annotations.SerializedName;

/**
 * @author tom
 */
public class Artist {
   @SerializedName("disambiguation")
   private String disambiguation;
   @SerializedName("sort-name")
   private String sortname;
   @SerializedName("name")
   private String name;
   @SerializedName("id")
   private String id;

   public String getDisambiguation() {
      return disambiguation;
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getSortname() {
      return sortname;
   }

   public void setDisambiguation(String disambiguation) {
      this.disambiguation = disambiguation;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setSortname(String sortname) {
      this.sortname = sortname;
   }
}
