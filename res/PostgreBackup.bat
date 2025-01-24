@echo off

chcp 1252

goto startapp

:startapp

start javaw.exe -Xms512m -Xmx1024m -XX:+AlwaysPreTouch -XX:-HeapDumpOnOutOfMemoryError -jar "%~dp0PostgreBackup.jar" %1 %2 %3 %4 %5

goto end

:end