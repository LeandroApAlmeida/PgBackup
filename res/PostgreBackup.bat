@echo off

chcp 1252

cd /d "%~dp0"

goto startapp

:startapp

start javaw.exe -Xms512m -Xmx1024m -jar "%~dp0PostgreBackup.jar"

goto end

:end