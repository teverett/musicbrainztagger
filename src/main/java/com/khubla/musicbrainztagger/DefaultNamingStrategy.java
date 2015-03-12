package com.khubla.musicbrainztagger;

/**
 * @author tom
 */
public class DefaultNamingStrategy implements NamingStrategy {
   @Override
   public String name(TrackInformation trackInformation) {
      /**
       * the default strategy is <artist>-<release>-<title>
       */
      String ret = "";
      if (null != trackInformation.getArtist()) {
         ret = trackInformation.getArtist();
      }
      if (null != trackInformation.getRelease()) {
         ret += "-" + trackInformation.getRelease();
      }
      if (null != trackInformation.getTitle()) {
         ret += "-" + trackInformation.getTitle();
      }
      return ret;
   }
}
