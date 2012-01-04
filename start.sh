#!/bin/bash -x

source /home/bob/.prepareEnvironmentForJava6.sh
source find_next_port.sh

stopport=$(find_next_port 8005)
echo $stopport >/tmp/logweb.stopport
nohup mvn org.codehaus.mojo:exec-maven-plugin:1.2:java -Dexec.mainClass="de.dwpbank.tomcat.TomcatStarter" -Dindexdir=/var/log/wpdirect -Dcmp_indexdir=/srv/maven/cmp/index -Dwpdirect_indexdir=/srv/maven/wpdirect/index -DtomcatConfig=tomcat-config.xml -Dstopport=${stopport} > tomcat.log &
