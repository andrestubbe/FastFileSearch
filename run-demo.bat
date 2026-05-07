@echo off
echo === FastFileSearch Demo ===
echo.
echo Building FastFileSearch...
call mvn clean package -f pom.xml
echo.
echo Building Demo...
cd examples\Demo
call mvn clean package -f pom.xml
echo.
echo Running Demo...
java --enable-native-access=ALL-UNNAMED -cp "target\fastfilesearch-demo-1.0.0.jar;..\..\target\fastfilesearch-0.1.0.jar;%USERPROFILE%\.m2\repository\io\github\andrestubbe\fastui\0.1.0\fastui-0.1.0.jar;..\..\..\FastFileIndex\target\fastfileindex-v1.0.0.jar;..\..\..\FastCore\target\fastcore-1.0.0.jar;..\..\..\FastTheme\target\fasttheme-0.2.0.jar" fastfilesearch.Demo
cd ..\..
echo.
echo === Demo Complete ===
pause
