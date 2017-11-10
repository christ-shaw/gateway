@ECHO OFF
if not exist "%JAVA_HOME%\bin\ztev_processkeeper.exe" (
    copy "%JAVA_HOME%\bin\java.exe"  "%JAVA_HOME%\bin\\ztev_processkeeper.exe"
)else (
 echo "ztev_processkeeper.exe already exists"
)
start ztev_processkeeper   -Xmx384m -Xms128m -Xmn128m -Xss128k -jar  processkeeper-0.1.jar %1 %2
exit 

