package com.khubla.musicbrainztagger;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;

/**
 * @author tom
 */
public class MusicBrainz {
   public static String lookup(String recordingId) throws ClientProtocolException, IOException {
      final Properties properties = new Properties();
      properties.load(AcoustID.class.getResourceAsStream(PROPERTIES));
      final String url = properties.getProperty("url") + recordingId + "?inc=artist-credits+isrcs+releases&fmt=json";
      return HTTPUtil.get(url);
   }

   private final static String PROPERTIES = "/musicbrainz.properties";
}
