package com.khubla.musicbrainztagger.id3;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;

/**
 * @author tom
 */
public class ID3 {
   /**
    * read ID3 data from file
    */
   public static ID3Data readTag(File file) throws Exception {
      try {
         Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
         final AudioFile f = AudioFileIO.read(file);
         final Tag tag = f.getTag();
         if (false == tag.isEmpty()) {
            final ID3Data ret = new ID3Data();
            ret.artist = tag.getFirst(FieldKey.ARTIST);
            ret.title = tag.getFirst(FieldKey.TITLE);
            ret.album = tag.getFirst(FieldKey.ALBUM);
            if (null != tag.getFirstArtwork()) {
               ret.coverart = tag.getFirstArtwork().getBinaryData();
            } else {
               ret.coverart = null;
            }
            return ret;
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
         final AudioFile f = AudioFileIO.read(file);
         final Tag tag = f.getTag();
         tag.setField(FieldKey.ARTIST, id3Data.artist);
         tag.setField(FieldKey.TITLE, id3Data.title);
         tag.setField(FieldKey.ALBUM, id3Data.album);
         if (null != id3Data.musicbrainzid) {
            tag.setField(FieldKey.MUSICBRAINZ_TRACK_ID, id3Data.musicbrainzid);
         }
         if (null != id3Data.coverart) {
            final Artwork artWork = new Artwork();
            artWork.setBinaryData(id3Data.coverart);
            tag.setField(artWork);
         }
         f.setTag(tag);
         f.commit();
      } catch (final Exception e) {
         throw new Exception("Exception reading tag", e);
      }
   }
}
