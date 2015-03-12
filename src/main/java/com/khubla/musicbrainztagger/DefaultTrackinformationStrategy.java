package com.khubla.musicbrainztagger;

import com.khubla.musicbrainztagger.id3.ID3Data;

/**
 * @author tom
 */
public class DefaultTrackinformationStrategy implements TrackInformationStrategy {
   @Override
   public TrackInformation merge(ID3Data id3Data, TrackInformation trackInformation) {
      String artist = null;
      String release = null;
      String title = null;
      /**
       * artist
       */
      if ((id3Data.artist != null) && (id3Data.artist.length() > 0)) {
         artist = id3Data.artist;
      } else {
         artist = trackInformation.getArtist();
      }
      /**
       * release
       */
      if ((id3Data.album != null) && (id3Data.album.length() > 0)) {
         release = id3Data.album;
      } else {
         release = trackInformation.getRelease();
      }
      /**
       * title
       */
      if ((id3Data.title != null) && (id3Data.title.length() > 0)) {
         title = id3Data.title;
      } else {
         title = trackInformation.getTitle();
      }
      return new TrackInformation(artist, title, release);
   }
}
