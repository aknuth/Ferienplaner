#!/bin/bash

source /home/bob/.prepareEnvironmentForJava6.sh

stopport=$(cat /tmp/logweb.stopport)
mvn org.codehaus.mojo:exec-maven-plugin:1.2:java \
	-Dexec.mainClass="de.dwpbank.tomcat.TomcatStop" \
	-Dstopport=$stopport