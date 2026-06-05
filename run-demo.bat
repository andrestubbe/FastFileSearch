@echo off
chcp 65001 >nul
cd /d "%~dp0"
echo [FastFileSearch] Running Demo (via JitPack)...
cd examples\Demo
call mvn compile exec:java -Dexec.mainClass=fastfilesearch.Demo
cd ..\..
pause
