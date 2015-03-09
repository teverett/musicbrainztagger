package com.khubla.musicbrainztagger;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 * @author tom
 */
public class MusicBrainsTagger {
   public static void main(String[] args) {
      try {
         System.out.println("khubla.com MusicBrainz Tagger");
         /*
          * options
          */
         final Options options = new Options();
         OptionBuilder.withArgName(DIR);
         OptionBuilder.isRequired(false);
         OptionBuilder.withType(String.class);
         OptionBuilder.hasArg();
         OptionBuilder.withDescription("mp3 directory");
         final Option o1 = OptionBuilder.create(DIR);
         options.addOption(o1);
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
          * get the dir
          */
         String dir = cmd.getOptionValue(DIR);
         if (null != dir) {
            /*
             * walk the dir
             */
            File rootDir = new File(dir);
            if (rootDir.exists() && (rootDir.isDirectory())) {
               walkDirectory(rootDir);
            }
         }
      } catch (final Exception e) {
         e.printStackTrace();
      }
   }

   /**
    * recursively walk dirs
    */
   private static void walkDirectory(File dir) throws Exception {
      final File[] files = dir.listFiles();
      for (File file : files) {
         if (false == file.isHidden()) {
            if (file.isDirectory()) {
               walkDirectory(file);
            } else if (file.isFile()) {
               if (file.getName().toLowerCase().endsWith(".mp3")) {
                  processMP3(file);
               }
            }
         }
      }
   }

   /**
    * process file
    */
   private static void processMP3(File mp3File) throws Exception {
      String fingerprint = AcoustID.chromaprint(mp3File);
      System.out.println(mp3File.getName() + " " + fingerprint);
   }

   /**
    * mp3 dir
    */
   private static final String DIR = "dir";
}
