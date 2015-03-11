package com.khubla.musicbrainztagger.id3;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * @author tom
 */
public class ID3 {
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
            return ret;
         } else {
            return null;
         }
      } catch (final Exception e) {
         throw new Exception("Exception reading tag", e);
      }
   }
}
