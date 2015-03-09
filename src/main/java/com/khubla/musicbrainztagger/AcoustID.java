package com.khubla.musicbrainztagger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

/**
 * simple wrapper for AcoustID
 * 
 * @author tom
 */
public class AcoustID {
   // private final static String URL = "http://api.acoustid.org/v2/lookup";
   // private final static String CLIENTID = "";
   private final static String FPCALC = "/Users/tom/fpcalc";

   public static String chromaprint(File file) throws ExecuteException, IOException {
      String command = FPCALC + " \"" + file.getAbsolutePath() + "\"";
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      CommandLine cmdLine = CommandLine.parse(command);
      DefaultExecutor executor = new DefaultExecutor();
      executor.setExitValue(1);
      PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
      executor.setStreamHandler(streamHandler);
      int ret = executor.execute(cmdLine);
      if (0 == ret) {
         return (outputStream.toString());
      } else {
         return null;
      }
   }

   public static void lookup(String chromaprint) {
   }
}
