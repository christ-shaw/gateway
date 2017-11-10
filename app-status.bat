@echo off

curl http://localhost:12345/ztev/status | jq  -f app-status.filter
                                     
