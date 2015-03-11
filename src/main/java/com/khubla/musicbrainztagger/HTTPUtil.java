package com.khubla.musicbrainztagger;

import java.io.IOException;

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
public class HTTPUtil {
   public static String get(String url) throws ClientProtocolException, IOException {
      final CloseableHttpClient httpclient = HttpClients.createDefault();
      final HttpGet httpGet = new HttpGet(url);
      final CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
      try {
         final HttpEntity httpEntity = httpResponse.getEntity();
         return EntityUtils.toString(httpEntity);
      } finally {
         httpResponse.close();
      }
   }
}
