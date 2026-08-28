@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-21.0.12.1
call "C:\Program Files\Microsoft Visual Studio\18\Community\VC\Auxiliary\Build\vcvars64.bat"
if not exist build mkdir build
cl /LD /EHsc /O2 /I"%JAVA_HOME%\include" /I"%JAVA_HOME%\include\win32" /Fe:build\fastfilesearch.dll native\FastFileSearch.cpp /link advapi32.lib