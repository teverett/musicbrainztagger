package com.khubla.musicbrainztagger;

import com.khubla.musicbrainztagger.id3.ID3Data;

/**
 * @author tom TrackInformationStrategy defines how we combine MusicBrainzData with ID3 data to get the final data for Tagging and Naming
 */
public interface TrackInformationStrategy {
   TrackInformation merge(ID3Data id3Data, TrackInformation trackInformation);
}
