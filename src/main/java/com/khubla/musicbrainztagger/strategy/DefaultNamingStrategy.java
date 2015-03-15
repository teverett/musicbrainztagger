package com.khubla.musicbrainztagger.strategy;

import java.io.File;

import com.khubla.musicbrainztagger.NamingStrategy;
import com.khubla.musicbrainztagger.TrackInformation;

/**
 * @author tom
 */
public class DefaultNamingStrategy implements NamingStrategy {
   /**
    * the default strategy is <artist>/<release>/<title>.mp3
    */
   @Override
   public String name(File rootDirectory, TrackInformation trackInformation) {
      String filename = rootDirectory.getAbsolutePath().toString().trim();
      filename += File.separator;
      if (null != trackInformation.getArtist()) {
         filename += trackInformation.getArtist().trim() + File.separator;
      }
      if (null != trackInformation.getRelease()) {
         filename += trackInformation.getRelease().trim() + File.separator;
      }
      if (null != trackInformation.getTitle()) {
         filename += trackInformation.getTitle().trim() + ".mp3";
      }
      return filename.trim();
   }
}
