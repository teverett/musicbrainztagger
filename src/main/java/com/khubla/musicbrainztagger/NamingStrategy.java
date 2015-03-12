package com.khubla.musicbrainztagger;

/**
 * @author tom
 */
public interface NamingStrategy {
   String name(TrackInformation trackInformation);
}
