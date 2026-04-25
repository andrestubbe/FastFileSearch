@echo off
echo === FastFileSearch Demo ===
echo.
echo Building FastFileSearch...
call mvn clean package -f pom.xml
echo.
echo Building Demo...
call mvn clean compile -f examples\Demo\pom.xml
echo.
echo Running Demo...
java --enable-native-access=ALL-UNNAMED -cp "examples\Demo\target\classes;target\fastfilesearch-v1.0.0.jar" fastfilesearch.Demo
echo.
echo === Demo Complete ===
pause
