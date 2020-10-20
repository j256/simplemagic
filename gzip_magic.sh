#
# Script called by ant from mvn to compress our magic file into place
#

gzip -9 -c $1 > $2
