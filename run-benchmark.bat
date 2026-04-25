@echo off
echo === FastFileSearch Benchmark ===
echo.
echo Building FastFileSearch...
call mvn clean package -f pom.xml
echo.
echo Building Benchmark...
call mvn clean compile -f examples\Benchmark\pom.xml
echo.
echo Running Benchmark...
java -cp "examples\Benchmark\target\classes;target\fastfilesearch-v1.0.0.jar" fastfilesearch.Benchmark
echo.
echo === Benchmark Complete ===
pause
