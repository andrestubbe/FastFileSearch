@echo off
setlocal
echo [+] Fast Launching Demo 2 (Skipping Ecosystem Sync)...

:: 1. Compile only the demo (fast)
cd examples\Demo
call mvn compile -q

:: 2. Run with pre-defined library paths
mvn exec:java -Dexec.mainClass="fastfilesearch.Demo2" -Djava.library.path="..\..\fastfilesearch-core\build;..\..\..\FastFileIndex\build;..\..\..\FastTheme\src\main\resources\native"

cd ..\..
endlocal
