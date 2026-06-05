@echo off
echo ðŸš€ Running Hero Demo...
cd examples\Demo
call mvn compile exec:java -Dexec.mainClass=fastfilesearch.Demo
cd ..\..
pause
