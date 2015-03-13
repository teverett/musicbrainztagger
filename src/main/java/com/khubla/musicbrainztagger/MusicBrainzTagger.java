package com.khubla.musicbrainztagger;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import com.khubla.musicbrainztagger.acoustid.AcoustID;
import com.khubla.musicbrainztagger.acoustid.ChromaPrint;
import com.khubla.musicbrainztagger.id3.ID3;
import com.khubla.musicbrainztagger.id3.ID3Data;
import com.khubla.musicbrainztagger.musicbrainz.MusicBrainz;
import com.khubla.musicbrainztagger.strategy.DefaultNamingStrategy;
import com.khubla.musicbrainztagger.strategy.DefaultTrackinformationStrategy;

/**
 * @author tom
 */
public class MusicBrainzTagger {
   public static void main(String[] args) {
      try {
         System.out.println("khubla.com MusicBrainz Tagger");
         final Options options = new Options();
         /*
          * INPUTDIR option
          */
         OptionBuilder.withArgName(INPUTDIR);
         OptionBuilder.isRequired(false);
         OptionBuilder.withType(String.class);
         OptionBuilder.hasArg();
         OptionBuilder.withDescription("input directory");
         final Option o1 = OptionBuilder.create(INPUTDIR);
         options.addOption(o1);
         /*
          * OUTPUTDIR option
          */
         OptionBuilder.withArgName(OUTPUTDIR);
         OptionBuilder.isRequired(false);
         OptionBuilder.withType(String.class);
         OptionBuilder.hasArg();
         OptionBuilder.withDescription("output directory");
         final Option o2 = OptionBuilder.create(OUTPUTDIR);
         options.addOption(o2);
         /*
          * FPCALC option
          */
         OptionBuilder.withArgName(FPCALC);
         OptionBuilder.isRequired(false);
         OptionBuilder.withType(String.class);
         OptionBuilder.hasArg();
         OptionBuilder.withDescription("fpcalc executable");
         final Option o3 = OptionBuilder.create(FPCALC);
         options.addOption(o3);
         /*
          * parse
          */
         final CommandLineParser parser = new PosixParser();
         CommandLine cmd = null;
         try {
            cmd = parser.parse(options, args);
         } catch (final Exception e) {
            e.printStackTrace();
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("posix", options);
            System.exit(0);
         }
         /*
          * fpcalc
          */
         final String fpcalc = cmd.getOptionValue(FPCALC);
         /*
          * get the dirs
          */
         final String inputDir = cmd.getOptionValue(INPUTDIR);
         final String outputDir = cmd.getOptionValue(OUTPUTDIR);
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
      } catch (final Exception e) {
         e.printStackTrace();
      }
   }

   /**
    * process file
    */
   private static void processMP3(File mp3File, String fpcalc, File outputDirectory) throws Exception {
      final NamingStrategy namingStrategy = new DefaultNamingStrategy();
      final TrackInformationStrategy trackInformationStrategy = new DefaultTrackinformationStrategy();
      final ID3Data id3Data = ID3.readTag(mp3File);
      if (null != id3Data) {
         // System.out.println("ID3 Artist " + id3Data.artist + " Title " +
         // id3Data.title + " Release " + id3Data.title);
      }
      final ChromaPrint chromaprint = AcoustID.chromaprint(mp3File, fpcalc);
      System.out.println("\"" + mp3File.getName() + "\"");
      final String musicbrainzId = AcoustID.lookup(chromaprint);
      if (null != musicbrainzId) {
         final TrackInformation trackInformation = MusicBrainz.lookup(musicbrainzId);
         if (null != trackInformation) {
            final TrackInformation finalTrackInformation = trackInformationStrategy.merge(id3Data, trackInformation);
            final File outputFileName = namingStrategy.name(outputDirectory, finalTrackInformation);
            System.out.println("Recommended Filename: " + outputFileName.toString());
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
               }
            }
         }
      }
   }

   /**
    * mp3 dirs
    */
   private static final String INPUTDIR = "inputdir";
   private static final String OUTPUTDIR = "outputdir";
   /**
    * fpcalc dir
    */
   private static final String FPCALC = "fpcalc";
}
