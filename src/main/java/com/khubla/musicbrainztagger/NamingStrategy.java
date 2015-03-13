package com.khubla.musicbrainztagger;

import java.io.File;

/**
 * @author tom
 */
public interface NamingStrategy {
   String name(File rootDirectory, TrackInformation trackInformation);
}
