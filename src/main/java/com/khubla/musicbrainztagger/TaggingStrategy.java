package com.khubla.musicbrainztagger;

import com.khubla.musicbrainztagger.id3.ID3Data;

/**
 * @author tom
 */
public interface TaggingStrategy {
   ID3Data tag(TrackInformation trackInformation);
}
