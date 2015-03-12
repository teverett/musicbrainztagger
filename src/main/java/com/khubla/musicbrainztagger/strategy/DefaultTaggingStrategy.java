package com.khubla.musicbrainztagger.strategy;

import com.khubla.musicbrainztagger.TaggingStrategy;
import com.khubla.musicbrainztagger.TrackInformation;
import com.khubla.musicbrainztagger.id3.ID3Data;

/**
 * 
 * @author teverett
 * 
 */
public class DefaultTaggingStrategy implements TaggingStrategy {

	@Override
	public ID3Data tag(TrackInformation trackInformation) {
		ID3Data ret = new ID3Data();
		ret.album = trackInformation.getRelease();
		ret.artist = trackInformation.getArtist();
		ret.title = trackInformation.getTitle();
		ret.musicbrainzid = trackInformation.getMusicbrainzid();
		return ret;
	}

}
