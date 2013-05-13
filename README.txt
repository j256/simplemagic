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

    // create a magic utility using the internal magic file
    ContentTypeUtil util = new ContentTypeUtil();
    // if you want to use a different config file(s), you can load them by hand:
    // ContentTypeUtil util = new ContentTypeUtil("/etc/magic");
    ...
    ContentType contentType = util.findMatch("/tmp/upload.tmp");
    // or
    ContentType contentType = util.findMatch(inputStream);
    // or
    ContentType contentType = util.findMatch(contentByteArray);
