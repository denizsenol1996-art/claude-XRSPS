@echo off
title Hazy RSPS Launcher
color 0A

taskkill /F /IM java.exe >nul 2>&1
timeout /t 2 /nobreak >nul

echo [1/3] Starting JS5...
echo @echo off > "%TEMP%\h1.bat"
echo cd /d "C:\hazy\hazy-swift" >> "%TEMP%\h1.bat"
echo "C:\Program Files\Amazon Corretto\jdk17.0.18_9\bin\java.exe" -jar app.jar >> "%TEMP%\h1.bat"
echo pause >> "%TEMP%\h1.bat"
start "JS5" /min "%TEMP%\h1.bat"
timeout /t 4 /nobreak >nul

echo [2/3] Starting Server...
echo @echo off > "%TEMP%\h2.bat"
echo cd /d "C:\hazy\SwiftPk-Server" >> "%TEMP%\h2.bat"
echo "C:\Program Files\Java\jdk-25.0.2\bin\java.exe" --add-opens java.base/java.time=ALL-UNNAMED -cp "target\classes;target\TwistedScape-GameServer-1.1.1-shaded.jar;resources" com.twisted.GameServer >> "%TEMP%\h2.bat"
echo pause >> "%TEMP%\h2.bat"
start "Server" /min "%TEMP%\h2.bat"
timeout /t 6 /nobreak >nul

echo [3/3] Starting Client...
echo @echo off > "%TEMP%\h3.bat"
echo cd /d "C:\hazy\Hazy-Client" >> "%TEMP%\h3.bat"
echo set JAVA_HOME=C:\Program Files\Amazon Corretto\jdk17.0.18_9 >> "%TEMP%\h3.bat"
echo call gradlew.bat :game:run >> "%TEMP%\h3.bat"
echo pause >> "%TEMP%\h3.bat"
start "Client" "%TEMP%\h3.bat"

echo.
echo Alles gestart!
pause
