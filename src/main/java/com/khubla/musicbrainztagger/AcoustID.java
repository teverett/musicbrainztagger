package com.khubla.musicbrainztagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * simple wrapper for AcoustID
 *
 * @author tom
 */
public class AcoustID {
   /**
    * chromaprint
    */
   public static class ChromaPrint {
      private final String chromaprint;
      private final String duration;

      public String getChromaprint() {
         return chromaprint;
      }

      public String getDuration() {
         return duration;
      }

      public ChromaPrint(String chromaprint, String duration) {
         this.duration = duration;
         this.chromaprint = chromaprint;
      }
   }

   /**
    * recording
    */
   private static class Recording {
      private final String id;

      public String getId() {
         return id;
      }

      public Recording(String id) {
         this.id = id;
      }
   }

   /**
    * result
    */
   private static class Result {
      String id;
      List<Recording> recordings = new ArrayList<Recording>();
      String score;
   }

   /**
    * results
    */
   private static class Results {
      List<Result> results = new ArrayList<Result>();
   };

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
   private static AcoustID.Results getResults(String json) {
      final JsonElement jelement = new JsonParser().parse(json);
      JsonObject jobject = jelement.getAsJsonObject();
      final JsonElement statusElement = jobject.get("status");
      if (statusElement.getAsString().compareTo("ok") == 0) {
         final AcoustID.Results results = new AcoustID.Results();
         final JsonArray jarray = jobject.getAsJsonArray("results");
         /*
          * walk the results
          */
         for (int i = 0; i < jarray.size(); i++) {
            final AcoustID.Result result = new AcoustID.Result();
            jobject = jarray.get(i).getAsJsonObject();
            result.id = jobject.get("id").getAsString();
            result.score = jobject.get("score").getAsString();
            final JsonArray recordingsArray = jobject.getAsJsonArray("recordings");
            /*
             * walk the recordings
             */
            for (int j = 0; j < recordingsArray.size(); j++) {
               final JsonObject recordingJsonObject = recordingsArray.get(j).getAsJsonObject();
               String id = recordingJsonObject.get("id").getAsString();
               final AcoustID.Recording recording = new AcoustID.Recording(id);
               result.recordings.add(recording);
            }
            results.results.add(result);
         }
         return results;
      } else {
         return null;
      }
   }

   public static String lookup(ChromaPrint chromaprint) throws ClientProtocolException, IOException {
      final Properties properties = new Properties();
      properties.load(AcoustID.class.getResourceAsStream(PROPERTIES));
      final String url = properties.getProperty("url") + "?client=" + properties.getProperty("client") + "&meta=recordingids" + "&fingerprint=" + chromaprint.chromaprint + "&duration="
            + chromaprint.duration;
      final String json = HTTPUtil.get(url);
      System.out.println(json);
      final AcoustID.Results results = getResults(json);
      return results.results.get(0).recordings.get(0).id;
   }

   private final static String PROPERTIES = "/acoustid.properties";
}
