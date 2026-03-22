@echo off
title Hazy RSPS Launcher
color 0A
echo.
echo  ============================================
echo         HAZY RSPS - Starting up...
echo  ============================================
echo.

taskkill /F /IM java.exe >nul 2>&1
timeout /t 2 /nobreak >nul

echo [1/3] Starting JS5 server...
start "HAZY - JS5" /min cmd /c "cd /d C:\hazy\hazy-swift && "C:\Program Files\Amazon Corretto\jdk17.0.18_9\bin\java.exe" -jar app.jar && pause"
timeout /t 4 /nobreak >nul

echo [2/3] Starting Game Server...
start "HAZY - Server" /min cmd /c "cd /d C:\hazy\SwiftPk-Server && "C:\Program Files\Java\jdk-25.0.2\bin\java.exe" --add-opens java.base/java.time=ALL-UNNAMED -cp "target\classes;target\TwistedScape-GameServer-1.1.1-shaded.jar;resources" com.twisted.GameServer && pause"
timeout /t 6 /nobreak >nul

echo [3/3] Starting Client...
start "HAZY - Client" cmd /c "cd /d C:\hazy\Hazy-Client && set "JAVA_HOME=C:\Program Files\Amazon Corretto\jdk17.0.18_9" && call gradlew.bat :game:run && pause"

echo.
echo  ============================================
echo   JS5 + Server starten geminimaliseerd.
echo   Client venster toont de game.
echo  ============================================
pause
