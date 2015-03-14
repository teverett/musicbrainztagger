package com.khubla.musicbrainztagger.id3;

import java.io.File;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.tag.id3.ID3v24Tag;

/**
 * @author tom
 *         <p>
 *         http://picard.musicbrainz.org/docs/mappings/
 *         </p>
 */
public class ID3 {
	/**
	 * disable logging output
	 */
	private static void disableLogging() {
		Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
	}

	/**
	 * read ID3 data from file
	 */
	public static ID3Data readTag(File file) throws Exception {
		try {
			ID3.disableLogging();
			final AudioFile audioFile = AudioFileIO.read(file);
			if (null != audioFile) {
				final Tag tag = audioFile.getTag();
				if ((null != tag) && (false == tag.isEmpty())) {
					final ID3Data ret = new ID3Data();
					ret.artist = tag.getFirst(FieldKey.ARTIST);
					ret.title = tag.getFirst(FieldKey.TITLE);
					ret.album = tag.getFirst(FieldKey.ALBUM);
					ret.isrc = tag.getFirst(FieldKey.ISRC);
					if (null != tag.getFirstArtwork()) {
						ret.coverart = tag.getFirstArtwork().getBinaryData();
					} else {
						ret.coverart = null;
					}
					return ret;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (final Exception e) {
			throw new Exception("Exception reading tag", e);
		}
	}

	/**
	 * write ID3 data to file
	 */
	public static void writeTag(File file, ID3Data id3Data) throws Exception {
		try {
			Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
			final AudioFile audioFile = AudioFileIO.read(file);
			if (null != audioFile) {
				/*
				 * use the existing tag or make a new one
				 */
				Tag tag = audioFile.getTag();
				if (null == tag) {
					tag = new ID3v24Tag();
				} else {
					tag = copyTag(tag);
					if (tag == null) {
						tag = new ID3v24Tag();
					}
				}
				/*
				 * artist, title, album
				 */
				setField(tag, FieldKey.ARTIST, id3Data.artist);
				setField(tag, FieldKey.TITLE, id3Data.title);
				setField(tag, FieldKey.ALBUM, id3Data.album);

				/*
				 * trackid
				 */

				setField(tag, FieldKey.MUSICBRAINZ_TRACK_ID,
						id3Data.musicbrainzid);
				/*
				 * isrc
				 */
				setField(tag, FieldKey.ISRC, id3Data.isrc);

				/*
				 * art
				 */
				if (null != id3Data.coverart) {
					final Artwork artWork = new Artwork();
					artWork.setBinaryData(id3Data.coverart);
					tag.setField(artWork);
				}
				audioFile.setTag(tag);
				audioFile.commit();
			}
		} catch (final Exception e) {
			throw new Exception("Exception writing tag", e);
		}
	}

	/**
	 * copy existing tag into a nice new ID3v24Tag tag
	 * 
	 * @throws FieldDataInvalidException
	 */
	private static ID3v24Tag copyTag(Tag tag) throws FieldDataInvalidException {
		ID3v24Tag ret = new ID3v24Tag();
		Iterator<TagField> iter = null;
		try {
			iter = tag.getFields();
		} catch (UnsupportedOperationException e) {
			// seems like some old tag types don't support iteration
			return null;
		}
		while (iter.hasNext()) {
			TagField tagField = iter.next();
			ret.setField(tagField);
		}
		return ret;
	}

	/**
	 * set a value in a tag
	 */
	private static void setField(Tag tag, FieldKey fieldKey, String value)
			throws KeyNotFoundException, FieldDataInvalidException {
		if (null != value) {
			tag.setField(fieldKey, value);
		}
	}
}
