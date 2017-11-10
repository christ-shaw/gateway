@echo off
setlocal enabledelayedexpansion
taskkill /FI "IMAGENAME eq ztev*" /F /T
pm2 delete all -s