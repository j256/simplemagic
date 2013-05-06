This package provides some simple classes to read the magic files from Unix to simulate the file(1) command.

For more information, visit the home page:
	http://256.com/sources/simplemagic/

The git repository is:
	https://github.com/j256/simplemagic

Maven packages are published via the central repo:
	http://repo1.maven.org/maven2/com/j256/simplemagic/simplemagic/

Enjoy,
Gray Watson

----------------------------------------------------------------------

To get started you use the SimpleMagic package like the following:

	MagicUtil magicUtil = new MagicUtil();
	// if you are on a Unix box, this will try and load the system's magic files
	magicUtil.loadSystemMagicFiles();
	// if they are somewhere else, you can load them by hand:
	// magicUtil.loadMagicFileDirectory(new File("/etc/magic"));
	...
	ContentType contentType = magicUtil.contentTypeOfFile("/tmp/upload.tmp");
