#!/bin/bash

function check_port {
   port=$1
   netstat -t -l --numeric-ports | grep $port >/dev/null
}

function find_next_port {
   let startport=${1:-8005}

   while check_port $startport ; do
      let startport=$(($startport+1))
   done
   echo $startport
}
