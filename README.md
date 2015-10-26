musicbrainztagger
======

musicbrainztagger is a java command-line application which tags mp3 files using [AcoustID](https://acoustid.org/) and [MusicBrainz](https://musicbrainz.org/).  musicbrainztagger is written as a command-line application and handles files on a one-by-one basis in order to enable it to handle very large mp3 collections, such as those with 10's or 100's of thousands of files.  

Installation
-----

musicbrainztagger requires the command-line application [fpcalc](https://acoustid.org/fingerprinter) from AcoustId. You will have to download the application onto your filesystem.

Invocation
-----

musicbrainztagger takes these command line arguments

* --inputdir.  The input location of the mp3 files
* --outputdir. The output location 
* --fpcalc.  The location of the fpcalc application
* --delete.  Delete the input files after processing them

For example:

`java -jar musicbrainztagger.jar --inputdir=/home/mymusic --outputdir=/home/mynewmusic --fpcalc=/home/fpcalc`

Travis Status
---------

<a href="https://travis-ci.org/teverett/musicbrainztagger"><img src="https://api.travis-ci.org/teverett/musicbrainztagger.png"></a>

