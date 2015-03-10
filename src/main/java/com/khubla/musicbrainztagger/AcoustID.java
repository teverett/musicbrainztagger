package com.khubla.musicbrainztagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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
      public String chromaprint;
      public String duration;
   }

   /**
    * recording
    */
   private static class Recording {
      String id;
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
      final ChromaPrint ret = new ChromaPrint();
      while ((line = br.readLine()) != null) {
         if (line.startsWith("FINGERPRINT=")) {
            ret.chromaprint = line.substring("FINGERPRINT=".length());
         } else if (line.startsWith("DURATION=")) {
            ret.duration = line.substring("DURATION=".length());
         }
      }
      return ret;
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
               final AcoustID.Recording recording = new AcoustID.Recording();
               final JsonObject recordingJsonObject = recordingsArray.get(j).getAsJsonObject();
               recording.id = recordingJsonObject.get("id").getAsString();
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
      final CloseableHttpClient httpclient = HttpClients.createDefault();
      final String URL = properties.getProperty("url") + "?client=" + properties.getProperty("client") + "&meta=recordingids" + "&fingerprint=" + chromaprint.chromaprint + "&duration="
            + chromaprint.duration;
      final HttpGet httpGet = new HttpGet(URL);
      final CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
      try {
         final HttpEntity httpEntity = httpResponse.getEntity();
         final String json = EntityUtils.toString(httpEntity);
         System.out.println(json);
         final AcoustID.Results results = getResults(json);
         return results.results.get(0).recordings.get(0).id;
      } finally {
         httpResponse.close();
      }
   }

   private final static String PROPERTIES = "/acoustid.properties";
}
