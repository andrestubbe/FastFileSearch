@echo off
setlocal
cd /d "%~dp0"

echo ===========================================
echo FastFileSearch Benchmark
echo ===========================================
echo.

echo [+] Building FastFileSearch...
call mvn clean install -DskipTests -q
echo.

echo [+] Building Benchmark...
cd examples\Benchmark
call mvn clean compile -q
echo.

echo [+] Running Benchmark...
call mvn exec:java -Dexec.mainClass="fastfilesearch.Benchmark" -Djava.library.path="..\..\build;..\..\..\FastFileIndex\build;..\..\..\FastTheme\src\main\resources\native"

cd ..\..
echo.
echo === Benchmark Complete ===
pause
endlocal
