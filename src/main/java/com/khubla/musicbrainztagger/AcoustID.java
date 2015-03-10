package com.khubla.musicbrainztagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * simple wrapper for AcoustID
 * 
 * @author tom
 */
public class AcoustID {
   // private final static String URL = "http://api.acoustid.org/v2/lookup";
   // private final static String CLIENTID = "";
   public static String chromaprint(File file, String fpcalc) throws IOException {
      ProcessBuilder processBuilder = new ProcessBuilder(fpcalc, null);
      processBuilder.redirectErrorStream(true);
      processBuilder.command().set(1, file.getAbsolutePath());
      Process fpcalcProc = processBuilder.start();
      BufferedReader br = new BufferedReader(new InputStreamReader(fpcalcProc.getInputStream()));
      String line;
      while ((line = br.readLine()) != null) {
         if (line.startsWith("FINGERPRINT=")) {
            return line.substring("FINGERPRINT=".length());
         }
      }
      return null;
   }

   public static void lookup(String chromaprint) {
   }
}
