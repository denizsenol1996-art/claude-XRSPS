# ============================================================
#  HAZY RSPS - FULL REBUILD (FALLBACK)
#  Gebruik ALLEEN als MASTER_FIX.ps1 faalt bij Fase 3
#  Dit rebuildt de hele server met Maven + JDK 25
# ============================================================

$ErrorActionPreference = "Stop"
$base = "C:\hazy"
$server = "$base\SwiftPk-Server"

Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host "  HAZY RSPS - FULL REBUILD" -ForegroundColor Cyan
Write-Host "============================================`n" -ForegroundColor Cyan

# ----------------------------------------------------------
# STAP 1: Herstel broncode vanuit git
# ----------------------------------------------------------
Write-Host "[STAP 1] Broncode herstellen..." -ForegroundColor Yellow
cd $base
git checkout -- .
Write-Host "  [OK] Broncode hersteld" -ForegroundColor Green

# ----------------------------------------------------------
# STAP 2: AccountSelection.java fix
# ----------------------------------------------------------
Write-Host "`n[STAP 2] AccountSelection.java patchen..." -ForegroundColor Yellow
$asFile = "$server\src\com\twisted\game\content\account\AccountSelection.java"
$asContent = Get-Content $asFile -Raw
$asContent = $asContent.Replace(
    'public static void open(Player player) {
        player.getInterfaceManager().open(42400);',
    'public static void open(Player player) {
        player.unlock(); // FIX: unlock zodat buttons werken
        player.getInterfaceManager().open(42400);')
[System.IO.File]::WriteAllText($asFile, $asContent)
Write-Host "  [OK] unlock() toegevoegd" -ForegroundColor Green

# ----------------------------------------------------------
# STAP 3: pom.xml fixen voor Maven build
# ----------------------------------------------------------
Write-Host "`n[STAP 3] pom.xml fixen voor Maven CLI build..." -ForegroundColor Yellow
$pom = "$server\pom.xml"
$pomContent = Get-Content $pom -Raw

# Fix 1: Voeg sourceDirectory toe aan <build> (broncode staat in src/, niet src/main/java/)
if ($pomContent -notmatch '<sourceDirectory>src</sourceDirectory>') {
    $pomContent = $pomContent.Replace(
        '<build>
        <resources>',
        '<build>
        <sourceDirectory>src</sourceDirectory>
        <resources>')
    Write-Host "  [OK] sourceDirectory=src toegevoegd" -ForegroundColor Green
}

# Fix 2: Fix Kotlin plugin sourceDirs (ook src/, niet src/main/kotlin/)
$pomContent = $pomContent.Replace(
    '<sourceDir>${project.basedir}/src/main/kotlin</sourceDir>
                                <sourceDir>${project.basedir}/src/main/java</sourceDir>',
    '<sourceDir>${project.basedir}/src</sourceDir>')
Write-Host "  [OK] Kotlin sourceDirs gefixed" -ForegroundColor Green

# Fix 3: Zorg dat release 22 staat
$pomContent = $pomContent -replace '<release>\d+</release>', '<release>22</release>'
$pomContent = $pomContent -replace '<source>\d+</source>', '<source>22</source>'
$pomContent = $pomContent -replace '<target>\d+</target>', '<target>22</target>'
Write-Host "  [OK] Release/source/target = 22" -ForegroundColor Green

# Fix 4: Verwijder eventuele enable-preview
$pomContent = $pomContent -replace '\s*<compilerArgs>\s*<arg>--enable-preview</arg>\s*</compilerArgs>\s*', ''

[System.IO.File]::WriteAllText($pom, $pomContent)
Write-Host "  [OK] pom.xml opgeslagen" -ForegroundColor Green

# ----------------------------------------------------------
# STAP 4: Maven installeren (als het nog niet is)
# ----------------------------------------------------------
Write-Host "`n[STAP 4] Maven checken..." -ForegroundColor Yellow
$mavenHome = "C:\maven\apache-maven-3.9.6"
$mvn = "$mavenHome\bin\mvn.cmd"

if (-not (Test-Path $mvn)) {
    Write-Host "  Maven niet gevonden, downloaden..." -ForegroundColor Yellow
    
    $urls = @(
        "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip",
        "https://dlcdn.apache.org/maven/maven-3/3.9.8/binaries/apache-maven-3.9.8-bin.zip"
    )
    
    $downloaded = $false
    foreach ($url in $urls) {
        try {
            Invoke-WebRequest $url -OutFile "$env:TEMP\maven.zip" -ErrorAction Stop
            $downloaded = $true
            break
        } catch {
            Write-Host "  URL $url mislukt, probeer volgende..." -ForegroundColor Yellow
        }
    }
    
    if ($downloaded) {
        Expand-Archive "$env:TEMP\maven.zip" "C:\maven" -Force
        # Zoek de juiste map
        $mavenHome = (Get-ChildItem "C:\maven" -Directory -Filter "apache-maven-*" | Select-Object -First 1).FullName
        $mvn = "$mavenHome\bin\mvn.cmd"
        Write-Host "  [OK] Maven geinstalleerd: $mavenHome" -ForegroundColor Green
    } else {
        Write-Host "  [FAIL] Maven downloaden mislukt!" -ForegroundColor Red
        Read-Host "Druk Enter"
        exit 1
    }
} else {
    Write-Host "  [OK] Maven al aanwezig" -ForegroundColor Green
}

# ----------------------------------------------------------
# STAP 5: Build met JDK 25 (ondersteunt --release 22)
# ----------------------------------------------------------
Write-Host "`n[STAP 5] Server builden met Maven + JDK 25..." -ForegroundColor Yellow
Write-Host "  Dit kan 1-2 minuten duren..." -ForegroundColor Yellow

$env:JAVA_HOME = "C:\Program Files\Java\jdk-25.0.2"
$env:PATH = "$mavenHome\bin;$env:JAVA_HOME\bin;$env:PATH"

cd $server
$buildOutput = & $mvn package -DskipTests 2>&1

# Check resultaat
$buildResult = $buildOutput | Select-String "BUILD (SUCCESS|FAILURE)" | Select-Object -Last 1
$errorLines = $buildOutput | Select-String "ERROR" | Select-Object -First 10

if ($buildResult -match "BUILD SUCCESS") {
    $compiledCount = ($buildOutput | Select-String "Compiling \d+ source" | ForEach-Object { 
        if ($_ -match '(\d+) source') { $matches[1] }
    }) -join ", "
    Write-Host "  [OK] BUILD SUCCESS! ($compiledCount source files)" -ForegroundColor Green
    
    # Verify classes
    if (Test-Path "$server\target\classes\com\twisted\GameServer.class") {
        $classCount = (Get-ChildItem "$server\target\classes" -Recurse -Filter "*.class").Count
        Write-Host "  [OK] $classCount class files gecompileerd" -ForegroundColor Green
    }
} else {
    Write-Host "  [FAIL] BUILD FAILED!" -ForegroundColor Red
    $errorLines | ForEach-Object { Write-Host "  $_" -ForegroundColor Red }
    Write-Host "`n  Neem een screenshot en stuur naar Claude" -ForegroundColor Yellow
    Read-Host "Druk Enter"
    exit 1
}

# ----------------------------------------------------------
# STAP 6: Saves directory
# ----------------------------------------------------------
Write-Host "`n[STAP 6] Saves directory..." -ForegroundColor Yellow
New-Item -ItemType Directory -Path "$server\data\saves\characters" -Force | Out-Null
Write-Host "  [OK] Saves directory klaar" -ForegroundColor Green

# ----------------------------------------------------------
# STAP 7: START.bat
# ----------------------------------------------------------
Write-Host "`n[STAP 7] START.bat aanmaken..." -ForegroundColor Yellow

$startBat = @'
@echo off
title Hazy RSPS Launcher
color 0A
echo.
echo  ============================================
echo         HAZY RSPS - Starting up...
echo  ============================================
echo.

:: --- Kill oude Java processen ---
taskkill /F /IM java.exe >nul 2>&1
timeout /t 2 /nobreak >nul

:: --- JS5 Server (JDK 17) ---
echo  [1/3] Starting JS5 server...
set "JS5_JAVA=C:\Program Files\Amazon Corretto\jdk17.0.18_9\bin\java.exe"
start "HAZY - JS5" cmd /k "cd /d C:\hazy\hazy-swift && "%JS5_JAVA%" -jar app.jar"
timeout /t 4 /nobreak >nul

:: --- Game Server (JDK 25) ---
echo  [2/3] Starting Game Server...
set "SRV_JAVA=C:\Program Files\Java\jdk-25.0.2\bin\java.exe"
start "HAZY - Server" cmd /k "cd /d C:\hazy\SwiftPk-Server && "%SRV_JAVA%" -cp "target\classes;target\TwistedScape-GameServer-1.1.1-shaded.jar;resources" com.twisted.GameServer"
timeout /t 6 /nobreak >nul

:: --- Client (Gradle met JDK 17) ---
echo  [3/3] Starting Client...
start "HAZY - Client" cmd /k "cd /d C:\hazy\Hazy-Client && set "JAVA_HOME=C:\Program Files\Amazon Corretto\jdk17.0.18_9" && call gradlew.bat :game:run"

echo.
echo  ============================================
echo   Alle 3 gestart! Wacht op login scherm.
echo  ============================================
echo.
pause
'@

Set-Content "$base\START.bat" $startBat -Encoding ASCII
Write-Host "  [OK] START.bat aangemaakt" -ForegroundColor Green

# ----------------------------------------------------------
# KLAAR
# ----------------------------------------------------------
Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host "  FULL REBUILD COMPLEET!" -ForegroundColor Green
Write-Host "  Dubbelklik C:\hazy\START.bat om te starten" -ForegroundColor Green
Write-Host "============================================`n" -ForegroundColor Cyan
Read-Host "Druk Enter om te sluiten"
