@echo off
echo === FastFileSearch Benchmark ===
echo.
echo Building FastFileSearch...
call mvn clean package -f pom.xml
echo.
echo Building Benchmark...
cd examples\Benchmark
call mvn clean package -f pom.xml
echo.
echo Running Benchmark...
java --enable-native-access=ALL-UNNAMED -cp "target\fastfilesearch-benchmark-v1.0.0.jar;..\..\target\fastfilesearch-v1.0.0.jar" fastfilesearch.Benchmark
cd ..\..
echo.
echo === Benchmark Complete ===
pause
