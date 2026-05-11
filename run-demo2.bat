@echo off
setlocal
echo ==========================================
echo   FastJava Ecosystem - FastFileSearch Demo 2
echo ==========================================
echo.

:: 1. Build all modules locally to ensure latest DLLs and classes are in .m2
echo [+] Synchronizing Ecosystem...
cd ..\FastCore && call mvn install -DskipTests -q
cd ..\FastFileIndex && call mvn install -DskipTests -q
cd ..\FastUI && call mvn install -DskipTests -q
cd ..\FastTheme && call mvn install -DskipTests -q
cd ..\FastFileSearch && call mvn install -DskipTests -q

echo [+] Building Demo 2...
cd examples\Demo
call mvn clean compile -q

echo.
echo [+] Running Demo 2 (Premium Overlay Mode)...
echo.
:: Run with the build directories in library path as a fallback
mvn exec:java -Dexec.mainClass="fastfilesearch.Demo2" -Djava.library.path="..\..\build;..\..\..\FastFileIndex\build;..\..\..\FastTheme\src\main\resources\native"

cd ..\..
echo.
echo === Demo 2 Complete ===
pause
endlocal
