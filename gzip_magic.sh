#
# Script called by ant from mvn to compress our magic file into place
#

echo "Generating compressed magic file: $2"

set -x
gzip -9 -c $1 > $2
