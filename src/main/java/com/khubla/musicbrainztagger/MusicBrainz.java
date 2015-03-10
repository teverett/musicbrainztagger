package com.khubla.musicbrainztagger;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author tom
 */
public class MusicBrainz {
   public static String lookup(String recordingId) throws ClientProtocolException, IOException {
      final Properties properties = new Properties();
      properties.load(AcoustID.class.getResourceAsStream(PROPERTIES));
      final CloseableHttpClient httpclient = HttpClients.createDefault();
      final String URL = properties.getProperty("url") + recordingId + "?inc=artist-credits+isrcs+releases&fmt=json";
      final HttpGet httpGet = new HttpGet(URL);
      final CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
      try {
         final HttpEntity httpEntity = httpResponse.getEntity();
         final String json = EntityUtils.toString(httpEntity);
         return json;
      } finally {
         httpResponse.close();
      }
   }

   private final static String PROPERTIES = "/musicbrainz.properties";
}
