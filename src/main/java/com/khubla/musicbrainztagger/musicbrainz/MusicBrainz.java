package com.khubla.musicbrainztagger.musicbrainz;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.khubla.musicbrainztagger.HTTPUtil;
import com.khubla.musicbrainztagger.acoustid.AcoustID;

/**
 * @author tom
 */
public class MusicBrainz {
   /**
    * get the musicbrainz id
    */
   private static MusicBrainzResult getResults(String json) {
      final Gson gson = new Gson();
      final MusicBrainzResult result = gson.fromJson(json, MusicBrainzResult.class);
      return result;
   }

   public static MusicBrainzResult lookup(String recordingId) throws ClientProtocolException, IOException {
      final Properties properties = new Properties();
      properties.load(AcoustID.class.getResourceAsStream(PROPERTIES));
      final String url = properties.getProperty("url") + recordingId + "?inc=artist-credits+isrcs+releases&fmt=json";
      final String json = HTTPUtil.get(url);
      return getResults(json);
   }

   private final static String PROPERTIES = "/musicbrainz.properties";
}
