ZTEVDIRNAME="./works"
ZTEVFILENAME="stop.sh"

find $ZTEVDIRNAME -name $ZTEVFILENAME -exec /bin/sh -c {} \;
ps -ef|grep 'processkeeper'|grep -v grep|cut -c 9-15|xargs --no-run-if-empty kill -9
