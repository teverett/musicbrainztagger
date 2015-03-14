package com.khubla.musicbrainztagger.musicbrainz;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.khubla.musicbrainztagger.TrackInformation;
import com.khubla.musicbrainztagger.http.HTTPUtil;

/**
 * @author tom
 *         <p>
 *         https://musicbrainz.org/doc/Development/XML_Web_Service/Version_2
 *         </p>
 */
public class MusicBrainz {
	/**
	 * get the musicbrainz id
	 */
	private static MusicBrainzResult getResults(String json) {
		final Gson gson = new Gson();
		final MusicBrainzResult result = gson.fromJson(json,
				MusicBrainzResult.class);
		return result;
	}

	public static TrackInformation lookup(String recordingId)
			throws ClientProtocolException, IOException {
		final Properties properties = new Properties();
		properties.load(MusicBrainz.class.getResourceAsStream(PROPERTIES));
		final String url = properties.getProperty("url") + recordingId
				+ "?inc=artist-credits+isrcs+releases&fmt=json";
		final HTTPUtil.Response response = HTTPUtil.get(url);
		if (response.responseCode == 200) {
			final String json = response.response;
			final MusicBrainzResult musicBrainzResult = getResults(json);
			if (null != musicBrainzResult) {
				return getTrackInformation(recordingId, musicBrainzResult);
			} else {
				return null;
			}
		} else {
			System.out.println("MusicBrainz result: " + response.responseCode);
			return null;
		}
	}

	/**
	 * get TrackInformation from MusicBrainzResult
	 */
	private static TrackInformation getTrackInformation(String recordingId,
			MusicBrainzResult musicBrainzResult) {
		String artist = musicBrainzResult.getArtistcredit().get(0).getName();
		String title = musicBrainzResult.getTitle();
		String release = musicBrainzResult.getReleases().get(0).getTitle();
		String isrc = null;
		return new TrackInformation(artist, title, release, recordingId, null,
				isrc);
	}

	private final static String PROPERTIES = "/musicbrainz.properties";
}
