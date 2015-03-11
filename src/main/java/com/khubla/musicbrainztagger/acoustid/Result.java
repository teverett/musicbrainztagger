package com.khubla.musicbrainztagger.acoustid;

import java.util.ArrayList;
import java.util.List;

/**
 * result
 */
class Result {
   String id;
   List<Recording> recordings = new ArrayList<Recording>();
   String score;
}