musicbrainztagger
======

musicbrainztagger is a java command-line application which tags mp3 files using [AcoustID](https://acoustid.org/) and [MusicBrainz](https://musicbrainz.org/).  musicbrainztagger is written as a command-line application and handles files on a one-by-one basis in order to enable it to handle very large mp3 collections, such as those with 10's or 100's of thousands of files.  

Installation
-----

musicbrainztagger requires the command-line application [fpcalc](https://acoustid.org/fingerprinter) from AcoustId. You will have to download the application onto your filesystem.

Invocation
-----

musicbrainztagger takes two command line arguments

* --dir.  The location of the mp3 files
* --fpcalc.  The location of the fpcalc application
