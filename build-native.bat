@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-25
call "C:\Program Files (x86)\Microsoft Visual Studio\2022\BuildTools\VC\Auxiliary\Build\vcvars64.bat"
cl /LD /I"%JAVA_HOME%\include" /I"%JAVA_HOME%\include\win32" /Fe:build\fastfilesearch.dll native\FastFileSearch.cpp /link advapi32.lib
