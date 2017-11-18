[![Travis](https://travis-ci.org/teverett/musicbrainztagger.svg?branch=master)](https://travis-ci.org/teverett/musicbrainztagger)
[![Coverity Scan](https://scan.coverity.com/projects/13300/badge.svg)](https://scan.coverity.com/projects/teverett-musicbrainztagger)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3e0e8536940048af8d31a6f45a5025f6)](https://www.codacy.com/app/teverett/musicbrainztagger?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=teverett/musicbrainztagger&amp;utm_campaign=Badge_Grade)

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

