package com.khubla.musicbrainztagger.strategy;

import java.io.File;

import com.khubla.musicbrainztagger.NamingStrategy;
import com.khubla.musicbrainztagger.TrackInformation;

/**
 * @author tom
 */
public class DefaultNamingStrategy implements NamingStrategy {
	/**
	 * the default strategy is <artist>/<release>/<title>.mp3
	 */
	@Override
	public File name(File rootDirectory, TrackInformation trackInformation) {
		String filename = rootDirectory.getAbsolutePath().toString();
		filename += File.pathSeparator;

		if (null != trackInformation.getArtist()) {
			filename += trackInformation.getArtist() + File.pathSeparator;
		}
		if (null != trackInformation.getRelease()) {
			filename += "-" + trackInformation.getRelease()
					+ File.pathSeparator;
		}
		if (null != trackInformation.getTitle()) {
			filename += "-" + trackInformation.getTitle() + ".mp3";
		}
		return new File(filename);
	}
}
