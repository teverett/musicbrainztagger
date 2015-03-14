package com.khubla.musicbrainztagger;

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
         /*
          * tag
          */
         final Tagger tagger = new Tagger(inputDir, outputDir, fpcalc);
         tagger.tag();
      } catch (final Exception e) {
         e.printStackTrace();
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
