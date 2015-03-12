package com.khubla.musicbrainztagger;

/**
 * @author tom
 */
public class TrackInformation {
	private final String artist;
	private final String title;
	private final String release;
	private final String musicbrainzid;
	private final byte[] artwork;

	public TrackInformation(String artist, String title, String release,
			String musicbrainzid, byte[] artwork) {
		this.artist = artist;
		this.title = title;
		this.release = release;
		this.musicbrainzid = musicbrainzid;
		this.artwork = artwork;
	}

	public byte[] getArtwork() {
		return artwork;
	}

	public String getMusicbrainzid() {
		return musicbrainzid;
	}

	public String getArtist() {
		return artist;
	}

	public String getRelease() {
		return release;
	}

	public String getTitle() {
		return title;
	}
}
