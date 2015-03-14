package com.khubla.musicbrainztagger;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.khubla.musicbrainztagger.acoustid.AcoustID;
import com.khubla.musicbrainztagger.acoustid.ChromaPrint;
import com.khubla.musicbrainztagger.id3.ID3;
import com.khubla.musicbrainztagger.id3.ID3Data;
import com.khubla.musicbrainztagger.musicbrainz.MusicBrainz;
import com.khubla.musicbrainztagger.strategy.DefaultNamingStrategy;
import com.khubla.musicbrainztagger.strategy.DefaultTaggingStrategy;
import com.khubla.musicbrainztagger.strategy.DefaultTrackinformationStrategy;

/**
 * @author tom
 */
public class Tagger {
   /**
    * number of seconds to wait b/t musicbrainz queries
    */
   static final int queryrate = 5;
   /**
    * input mp3's
    */
   final String inputDir;
   /**
    * output mp3's
    */
   final String outputDir;
   /**
    * fpcalc path
    */
   final String fpcalc;
   /**
    * random strategy
    */
   final NamingStrategy namingStrategy = new DefaultNamingStrategy();
   /**
    * track information
    */
   final TrackInformationStrategy trackInformationStrategy = new DefaultTrackinformationStrategy();
   /**
    * tagging strategy
    */
   final TaggingStrategy taggingStrategy = new DefaultTaggingStrategy();

   /**
    * ctor
    */
   public Tagger(String inputDir, String outputDir, String fpcalc) {
      this.inputDir = inputDir;
      this.outputDir = outputDir;
      this.fpcalc = fpcalc;
   }

   /**
    * copy the mp3 from the input dir to the outputDir
    */
   private void copyFile(File mp3File, File outputFile) throws IOException {
      /*
       * create dirs
       */
      FileUtils.forceMkdir(outputFile.getParentFile());
      /*
       * copy file
       */
      FileUtils.copyFile(mp3File, outputFile);
   }

   /**
    * process file
    */
   private void processMP3(File mp3File, String fpcalc, File outputDirectory) throws Exception {
      final ID3Data id3Data = ID3.readTag(mp3File);
      final ChromaPrint chromaprint = AcoustID.chromaprint(mp3File, fpcalc);
      final String musicbrainzId = AcoustID.lookup(chromaprint);
      if (null != musicbrainzId) {
         /*
          * get the track info from MusicBrainz
          */
         final TrackInformation trackInformation = MusicBrainz.lookup(musicbrainzId);
         if (null != trackInformation) {
            /*
             * figure out the final Track information
             */
            final TrackInformation finalTrackInformation = trackInformationStrategy.merge(id3Data, trackInformation);
            /*
             * generate a name
             */
            final String outputFileName = namingStrategy.name(outputDirectory, finalTrackInformation);
            /*
             * copy
             */
            File outputFile = new File(outputFileName);
            copyFile(mp3File, outputFile);
            /*
             * tag
             */
            final ID3Data finalID3Data = taggingStrategy.tag(finalTrackInformation);
            outputFile = new File(outputFileName);
            ID3.writeTag(outputFile, finalID3Data);
            /*
             * dump to console
             */
            System.out.println("\"" + mp3File.getName() + "\" --> \"" + outputFile.getName() + "\"");
         }
      } else {
         /*
          * we have no idea what this file is. Drop it in the right place, in UNCATEGORIZED
          */
         System.out.println("Unmatched: \"" + mp3File.getName() + "\"");
      }
   }

   /**
    * tag the mp3's and sort them
    */
   public void tag() throws Exception {
      if (null != inputDir) {
         /*
          * walk the dir
          */
         final File inputDirectory = new File(inputDir);
         final File outputDirectory = new File(outputDir);
         if (inputDirectory.exists() && (inputDirectory.isDirectory())) {
            walkDirectory(inputDirectory, fpcalc, outputDirectory);
         }
      }
   }

   /**
    * recursively walk input directory
    */
   private void walkDirectory(File inputDirectory, String fpcalc, File outputDirectory) throws Exception {
      final File[] files = inputDirectory.listFiles();
      for (final File file : files) {
         if (false == file.isHidden()) {
            if (file.isDirectory()) {
               walkDirectory(file, fpcalc, outputDirectory);
            } else if (file.isFile()) {
               if (file.getName().toLowerCase().endsWith(".mp3")) {
                  processMP3(file, fpcalc, outputDirectory);
                  /*
                   * brief sleep so we don't overload musicbrainz
                   */
                  // https://musicbrainz.org/doc/XML_Web_Service/Rate_Limiting
                  Thread.sleep(queryrate * 1000);
               }
            }
         }
      }
   }
}
