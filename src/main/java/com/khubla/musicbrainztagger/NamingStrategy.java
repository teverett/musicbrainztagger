package com.khubla.musicbrainztagger;

import java.io.File;

/**
 * @author tom
 */
public interface NamingStrategy {
   File name(File rootDirectory, TrackInformation trackInformation);
}
