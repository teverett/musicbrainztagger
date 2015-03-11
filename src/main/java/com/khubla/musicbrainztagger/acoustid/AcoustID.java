package com.khubla.musicbrainztagger.acoustid;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.khubla.musicbrainztagger.HTTPUtil;

/**
 * simple wrapper for AcoustID
 *
 * @author tom
 */
public class AcoustID {
   /**
    * Chromaprint the file passed in
    */
   public static ChromaPrint chromaprint(File file, String fpcalc) throws IOException {
      final ProcessBuilder processBuilder = new ProcessBuilder(fpcalc, null);
      processBuilder.redirectErrorStream(true);
      processBuilder.command().set(1, file.getAbsolutePath());
      final Process fpcalcProc = processBuilder.start();
      final BufferedReader br = new BufferedReader(new InputStreamReader(fpcalcProc.getInputStream()));
      String line;
      String chromaprint = null;
      String duration = null;
      while ((line = br.readLine()) != null) {
         if (line.startsWith("FINGERPRINT=")) {
            chromaprint = line.substring("FINGERPRINT=".length());
         } else if (line.startsWith("DURATION=")) {
            duration = line.substring("DURATION=".length());
         }
      }
      return new ChromaPrint(chromaprint, duration);
   }

   /**
    * get the musicbrainz id
    */
   private static Results getResults(String json) {
      final Gson gson = new Gson();
      final Results results = gson.fromJson(json, Results.class);
      return results;
   }

   public static String lookup(ChromaPrint chromaprint) throws ClientProtocolException, IOException {
      final Properties properties = new Properties();
      properties.load(AcoustID.class.getResourceAsStream(PROPERTIES));
      final String url = properties.getProperty("url") + "?client=" + properties.getProperty("client") + "&meta=recordingids" + "&fingerprint=" + chromaprint.chromaprint + "&duration="
            + chromaprint.duration;
      final String json = HTTPUtil.get(url);
      System.out.println(json);
      final Results results = getResults(json);
      return results.results.get(0).recordings.get(0).id;
   }

   private final static String PROPERTIES = "/acoustid.properties";
}
