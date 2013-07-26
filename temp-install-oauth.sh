#!/bin/bash

# Run this file if maven can't download the oauth code
mvn install:install-file -DgroupId=net.oauth.core -DartifactId=oauth -Dversion=20090530 -Dpackaging=jar -Dfile=./lib/oauth-20090530.jar
mvn install:install-file -DgroupId=net.oauth.core -DartifactId=oauth-provider -Dversion=20090530 -Dpackaging=jar -Dfile=./lib/oauth-provider-20090530.jar
mvn install:install-file -DgroupId=oauth.signpost -DartifactId=oauth-signpost -Dversion=1.2.1.2 -Dpackaging=jar -Dfile=./lib/signpost-core-1.2.1.2.jar
