package com.khubla.musicbrainztagger;

import java.io.File;

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
    * process file
    */
   private static void processMP3(File mp3File, String fpcalc, File outputDirectory) throws Exception {
      final NamingStrategy namingStrategy = new DefaultNamingStrategy();
      final TrackInformationStrategy trackInformationStrategy = new DefaultTrackinformationStrategy();
      final TaggingStrategy taggingStrategy = new DefaultTaggingStrategy();
      final ID3Data id3Data = ID3.readTag(mp3File);
      if (null != id3Data) {
         // System.out.println("ID3 Artist " + id3Data.artist + " Title " +
         // id3Data.title + " Release " + id3Data.title);
      }
      final ChromaPrint chromaprint = AcoustID.chromaprint(mp3File, fpcalc);
      System.out.println("\"" + mp3File.getName() + "\"");
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
             * create dirs
             */
            File outputFile = new File(outputFileName);
            FileUtils.forceMkdir(outputFile.getParentFile());
            /*
             * copy file
             */
            FileUtils.copyFile(mp3File, outputFile);
            /*
             * tag
             */
            final ID3Data finalID3Data = taggingStrategy.tag(finalTrackInformation);
            outputFile = new File(outputFileName);
            ID3.writeTag(outputFile, finalID3Data);
         }
      }
   }

   /**
    * recursively walk dirs
    */
   private static void walkDirectory(File dir, String fpcalc, File outputDirectory) throws Exception {
      final File[] files = dir.listFiles();
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
                  Thread.sleep(1000);
               }
            }
         }
      }
   }

   private final String inputDir;
   private final String outputDir;
   private final String fpcalc;

   public Tagger(String inputDir, String outputDir, String fpcalc) {
      this.inputDir = inputDir;
      this.outputDir = outputDir;
      this.fpcalc = fpcalc;
   }

   public void tag() throws Exception {
      if (null != inputDir) {
         /*
          * walk the dir
          */
         final File rootDir = new File(inputDir);
         final File outputDirectory = new File(outputDir);
         if (rootDir.exists() && (rootDir.isDirectory())) {
            walkDirectory(rootDir, fpcalc, outputDirectory);
         }
      }
   }
}
