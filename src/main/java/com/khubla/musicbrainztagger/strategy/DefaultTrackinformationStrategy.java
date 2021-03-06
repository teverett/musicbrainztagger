package com.khubla.musicbrainztagger.strategy;

import com.khubla.musicbrainztagger.TrackInformation;
import com.khubla.musicbrainztagger.TrackInformationStrategy;
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
      byte[] coverart = null;
      String isrc = null;
      if (null != id3Data) {
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
         /**
          * art
          */
         if (null != id3Data.coverart) {
            coverart = id3Data.coverart;
         }
         /**
          * isrc
          */
         if ((id3Data.isrc != null) && (id3Data.isrc.length() > 0)) {
            isrc = id3Data.isrc;
         } else {
            isrc = trackInformation.getIsrc();
         }
      } else {
         /*
          * no id3, just copy the tags
          */
         artist = trackInformation.getArtist();
         release = trackInformation.getRelease();
         title = trackInformation.getTitle();
         coverart = null;
         isrc = trackInformation.getIsrc();
      }
      /*
       * done
       */
      return new TrackInformation(artist, title, release, trackInformation.getMusicbrainzid(), coverart, isrc);
   }
}
