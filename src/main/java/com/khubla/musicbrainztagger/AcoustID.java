package com.khubla.musicbrainztagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
   private static String getMusicBrainzID(String json) {
      final JsonElement jelement = new JsonParser().parse(json);
      JsonObject jobject = jelement.getAsJsonObject();
      final JsonElement statusElement = jobject.get("status");
      if (statusElement.getAsString().compareTo("ok") == 0) {
         final JsonArray jarray = jobject.getAsJsonArray("results");
         jobject = jarray.get(0).getAsJsonObject();
         final JsonElement idElement = jobject.get("id");
         return idElement.getAsString();
      } else {
         return null;
      }
   }

   public static String lookup(ChromaPrint chromaprint) throws ClientProtocolException, IOException {
      final Properties properties = new Properties();
      properties.load(AcoustID.class.getResourceAsStream(PROPERTIES));
      final CloseableHttpClient httpclient = HttpClients.createDefault();
      final String URL = properties.getProperty("url") + "?client=" + properties.getProperty("client") + "&fingerprint=" + chromaprint.chromaprint + "&duration=" + chromaprint.duration;
      final HttpGet httpGet = new HttpGet(URL);
      final CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
      try {
         final HttpEntity httpEntity = httpResponse.getEntity();
         final String json = EntityUtils.toString(httpEntity);
         return getMusicBrainzID(json);
      } finally {
         httpResponse.close();
      }
   }

   private final static String PROPERTIES = "/acoustid.properties";
}
