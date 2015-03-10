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

/**
 * simple wrapper for AcoustID
 *
 * @author tom
 */
public class AcoustID {
   /**
    * Chromaprint the file passed in
    */
   public static String chromaprint(File file, String fpcalc) throws IOException {
      final ProcessBuilder processBuilder = new ProcessBuilder(fpcalc, null);
      processBuilder.redirectErrorStream(true);
      processBuilder.command().set(1, file.getAbsolutePath());
      final Process fpcalcProc = processBuilder.start();
      final BufferedReader br = new BufferedReader(new InputStreamReader(fpcalcProc.getInputStream()));
      String line;
      while ((line = br.readLine()) != null) {
         if (line.startsWith("FINGERPRINT=")) {
            return line.substring("FINGERPRINT=".length());
         }
      }
      return null;
   }

   public static String lookup(String chromaprint) throws ClientProtocolException, IOException {
      Properties properties = new Properties();
      properties.load(AcoustID.class.getResourceAsStream(PROPERTIES));
      final CloseableHttpClient httpclient = HttpClients.createDefault();
      final String URL = properties.getProperty("url") + "?client=" + properties.getProperty("client") + "&fingerprint=" + chromaprint;
      final HttpGet httpGet = new HttpGet(URL);
      final CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
      try {
         final HttpEntity httpEntity = httpResponse.getEntity();
         return EntityUtils.toString(httpEntity);
      } finally {
         httpResponse.close();
      }
   }

   private final static String PROPERTIES = "/acoustid.properties";
}
