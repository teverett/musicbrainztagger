package com.khubla.musicbrainztagger.audiodb;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;

import com.khubla.musicbrainztagger.TrackInformation;
import com.khubla.musicbrainztagger.http.HTTPUtil;

/**
 * @author tom
 */
public class AudioDB {
   /**
    * get the musicbrainz id
    */
   // private static MusicBrainzResult getResults(String json) {
   // final Gson gson = new Gson();
   // final MusicBrainzResult result = gson.fromJson(json, MusicBrainzResult.class);
   // return result;
   // }
   public static TrackInformation lookup(String recordingId) throws ClientProtocolException, IOException {
      final Properties properties = new Properties();
      properties.load(AudioDB.class.getResourceAsStream(PROPERTIES));
      final String url = properties.getProperty("url") + "/" + properties.getProperty("apikey") + "/track-mb.php?i=" + recordingId;
      HTTPUtil.get(url);
      // final MusicBrainzResult musicBrainzResult = getResults(json);
      // if (null != musicBrainzResult) {
      // return new TrackInformation(musicBrainzResult.getArtistcredit().get(0).getName(), musicBrainzResult.getTitle(), musicBrainzResult.getReleases().get(0).getTitle(), recordingId, null);
      // } else {
      // return null;
      // }
      return null;
   }

   private final static String PROPERTIES = "/audiodb.properties";
}
