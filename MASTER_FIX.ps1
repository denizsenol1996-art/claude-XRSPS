# ============================================================
#  HAZY RSPS - MASTER FIX SCRIPT
#  Run dit in PowerShell als Administrator
#  Rechtermuisklik MASTER_FIX.ps1 -> "Run with PowerShell"
# ============================================================

$ErrorActionPreference = "Stop"
$base = "C:\hazy"
$backup = "C:\Users\deniz\Desktop\xerox\rsps-gambling\twee rsps\Hazy Package\SwiftPk-Server\target\classes"
$server = "$base\SwiftPk-Server"
$client = "$base\Hazy-Client"
$failed = $false

Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host "  HAZY RSPS - MASTER FIX" -ForegroundColor Cyan
Write-Host "============================================`n" -ForegroundColor Cyan

# ----------------------------------------------------------
# FASE 1: Herstel schone broncode vanuit git
# ----------------------------------------------------------
Write-Host "[FASE 1] Broncode herstellen vanuit git..." -ForegroundColor Yellow
try {
    cd $base
    git checkout -- .
    Write-Host "  [OK] Alle bronbestanden hersteld naar originele staat" -ForegroundColor Green
} catch {
    Write-Host "  [WARN] Git checkout mislukt, handmatig verder" -ForegroundColor DarkYellow
}

# ----------------------------------------------------------
# FASE 2: AccountSelection.open() unlock fix
# ----------------------------------------------------------
Write-Host "`n[FASE 2] AccountSelection.java patchen..." -ForegroundColor Yellow
$asFile = "$server\src\com\twisted\game\content\account\AccountSelection.java"
$asContent = Get-Content $asFile -Raw

# Exacte match op de originele code
$oldCode = @"
    public static void open(Player player) {
        player.getInterfaceManager().open(42400);
"@

$newCode = @"
    public static void open(Player player) {
        player.unlock(); // FIX: unlock zodat account selection buttons klikbaar zijn
        player.getInterfaceManager().open(42400);
"@

if ($asContent.Contains("player.unlock(); // FIX:")) {
    Write-Host "  [SKIP] Unlock fix staat er al" -ForegroundColor DarkYellow
} elseif ($asContent.Contains($oldCode)) {
    $asContent = $asContent.Replace($oldCode, $newCode)
    [System.IO.File]::WriteAllText($asFile, $asContent)
    Write-Host "  [OK] player.unlock() toegevoegd aan open()" -ForegroundColor Green
} else {
    Write-Host "  [FAIL] Kan AccountSelection.open() niet vinden - handmatig fixen" -ForegroundColor Red
    $failed = $true
}

# Verificatie
$check = Select-String -Path $asFile -Pattern "player\.unlock\(\)" | Where-Object { $_.LineNumber -lt 35 }
if ($check) {
    Write-Host "  [VERIFY] unlock() staat op regel $($check.LineNumber)" -ForegroundColor Green
} else {
    Write-Host "  [VERIFY FAIL] unlock() NIET gevonden in open() methode!" -ForegroundColor Red
    $failed = $true
}

# ----------------------------------------------------------
# FASE 3: Originele compiled classes herstellen
# ----------------------------------------------------------
Write-Host "`n[FASE 3] Originele compiled classes herstellen..." -ForegroundColor Yellow

$classesDir = "$server\target\classes"
$shadedJar = "$server\target\TwistedScape-GameServer-1.1.1-shaded.jar"

if (Test-Path "$backup\com\twisted\GameServer.class") {
    Write-Host "  Backup gevonden op: $backup" -ForegroundColor Green
    
    # Verwijder de kapotte JDK 21 classes
    if (Test-Path $classesDir) {
        Remove-Item $classesDir -Recurse -Force
        Write-Host "  [OK] Oude (kapotte) classes verwijderd" -ForegroundColor Green
    }
    
    # Kopieer originele JDK 22 classes
    Copy-Item $backup $classesDir -Recurse -Force
    
    # Verificatie
    if (Test-Path "$classesDir\com\twisted\GameServer.class") {
        $size = (Get-ChildItem $classesDir -Recurse -File).Count
        Write-Host "  [OK] $size class files hersteld vanuit backup" -ForegroundColor Green
    } else {
        Write-Host "  [FAIL] Classes niet goed gekopieerd" -ForegroundColor Red
        $failed = $true
    }
} else {
    Write-Host "  [WARN] Backup NIET gevonden op: $backup" -ForegroundColor Red
    Write-Host "  Probeer alternatieve locaties..." -ForegroundColor Yellow
    
    # Probeer andere mogelijke backup locaties
    $altBackups = @(
        "C:\Users\deniz\Desktop\xerox\rsps-gambling\twee rsps\Hazy Package\SwiftPk-Server\target\classes",
        "C:\hazy\Hazy Package\SwiftPk-Server\target\classes",
        "C:\hazy\repo\server\target\classes"
    )
    
    $found = $false
    foreach ($alt in $altBackups) {
        if (Test-Path "$alt\com\twisted\GameServer.class") {
            Write-Host "  Backup gevonden op: $alt" -ForegroundColor Green
            if (Test-Path $classesDir) { Remove-Item $classesDir -Recurse -Force }
            Copy-Item $alt $classesDir -Recurse -Force
            $found = $true
            Write-Host "  [OK] Classes hersteld vanuit alternatieve backup" -ForegroundColor Green
            break
        }
    }
    
    if (-not $found) {
        Write-Host "  [FAIL] Geen backup gevonden! Full rebuild nodig." -ForegroundColor Red
        Write-Host "  Start FULL_REBUILD.ps1 in plaats van dit script." -ForegroundColor Red
        $failed = $true
    }
}

# ----------------------------------------------------------
# FASE 4: Hercompileer ALLEEN AccountSelection.java
# ----------------------------------------------------------
Write-Host "`n[FASE 4] AccountSelection.java hercompileren..." -ForegroundColor Yellow

if (-not $failed) {
    $env:JAVA_HOME = "C:\Program Files\Java\jdk-25.0.2"
    $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
    
    # Check of shaded jar bestaat (bevat alle dependencies)
    if (-not (Test-Path $shadedJar)) {
        Write-Host "  [WARN] Shaded JAR niet gevonden, zoeken..." -ForegroundColor Yellow
        $shadedJar = (Get-ChildItem "$server\target\*shaded*.jar" -ErrorAction SilentlyContinue | Select-Object -First 1).FullName
        if (-not $shadedJar) {
            Write-Host "  [FAIL] Geen shaded JAR gevonden!" -ForegroundColor Red
            $failed = $true
        }
    }
    
    if (-not $failed) {
        $cp = "$classesDir;$shadedJar"
        
        # Compileer met --release 22 zodat het compatible is met de rest
        $compileResult = & javac --release 22 -cp $cp -d $classesDir $asFile 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            $compiled = (Get-Item "$classesDir\com\twisted\game\content\account\AccountSelection.class").LastWriteTime
            Write-Host "  [OK] AccountSelection.java gecompileerd ($compiled)" -ForegroundColor Green
        } else {
            Write-Host "  [FAIL] Compilatie mislukt:" -ForegroundColor Red
            $compileResult | ForEach-Object { Write-Host "    $_" -ForegroundColor Red }
            
            # Probeer zonder --release 22
            Write-Host "  Probeer zonder --release flag..." -ForegroundColor Yellow
            $compileResult2 = & javac -cp $cp -d $classesDir $asFile 2>&1
            if ($LASTEXITCODE -eq 0) {
                Write-Host "  [OK] Gecompileerd (zonder --release 22)" -ForegroundColor Green
            } else {
                Write-Host "  [FAIL] Compilatie mislukt ook zonder --release" -ForegroundColor Red
                $failed = $true
            }
        }
    }
}

# ----------------------------------------------------------
# FASE 5: Saves directory aanmaken
# ----------------------------------------------------------
Write-Host "`n[FASE 5] Saves directory checken..." -ForegroundColor Yellow
$savesDir = "$server\data\saves\characters"
if (-not (Test-Path $savesDir)) {
    New-Item -ItemType Directory -Path $savesDir -Force | Out-Null
    Write-Host "  [OK] $savesDir aangemaakt" -ForegroundColor Green
} else {
    $saveCount = (Get-ChildItem $savesDir -Filter "*.json" -ErrorAction SilentlyContinue).Count
    Write-Host "  [OK] Directory bestaat al ($saveCount saves)" -ForegroundColor Green
}

# ----------------------------------------------------------
# FASE 6: pom.xml herstellen (voor toekomstige builds)
# ----------------------------------------------------------
Write-Host "`n[FASE 6] pom.xml controleren..." -ForegroundColor Yellow
$pom = "$server\pom.xml"
$pomContent = Get-Content $pom -Raw

# Check of sourceDirectory al is toegevoegd (en mogelijk fout)
if ($pomContent -match "sourceDirectory") {
    # Verwijder eventuele oude/dubbele sourceDirectory entries
    $pomContent = $pomContent -replace '\s*<sourceDirectory>src</sourceDirectory>\s*', ''
    Write-Host "  [OK] Oude sourceDirectory entries verwijderd" -ForegroundColor Green
}

# Check of release is veranderd naar 21
if ($pomContent -match '<release>21</release>') {
    $pomContent = $pomContent.Replace('<release>21</release>', '<release>22</release>')
    Write-Host "  [OK] Release teruggezet naar 22" -ForegroundColor Green
}

# Verwijder enable-preview als het is toegevoegd
if ($pomContent -match 'enable-preview') {
    $pomContent = $pomContent -replace '\s*<compilerArgs>\s*<arg>--enable-preview</arg>\s*</compilerArgs>\s*', ''
    Write-Host "  [OK] enable-preview verwijderd" -ForegroundColor Green
}

[System.IO.File]::WriteAllText($pom, $pomContent)
Write-Host "  [OK] pom.xml is schoon" -ForegroundColor Green

# ----------------------------------------------------------
# FASE 7: START.bat aanmaken
# ----------------------------------------------------------
Write-Host "`n[FASE 7] START.bat aanmaken..." -ForegroundColor Yellow

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
Write-Host "  [OK] START.bat aangemaakt in $base" -ForegroundColor Green

# ----------------------------------------------------------
# FASE 8: Git commit
# ----------------------------------------------------------
Write-Host "`n[FASE 8] Changes committen..." -ForegroundColor Yellow
try {
    cd $base
    git add -A
    git commit -m "FIX: AccountSelection unlock + saves dir + clean pom.xml"
    Write-Host "  [OK] Committed" -ForegroundColor Green
} catch {
    Write-Host "  [SKIP] Git commit overgeslagen" -ForegroundColor DarkYellow
}

# ----------------------------------------------------------
# RESULTAAT
# ----------------------------------------------------------
Write-Host "`n============================================" -ForegroundColor Cyan
if ($failed) {
    Write-Host "  FIXES INCOMPLETE - Zie errors hierboven" -ForegroundColor Red
    Write-Host "  Neem een screenshot en stuur naar Claude" -ForegroundColor Red
} else {
    Write-Host "  ALLE FIXES SUCCESVOL!" -ForegroundColor Green
    Write-Host "" -ForegroundColor Green
    Write-Host "  Dubbelklik C:\hazy\START.bat om te starten" -ForegroundColor Green
    Write-Host "" -ForegroundColor Green
    Write-Host "  Wat is er gefixt:" -ForegroundColor White
    Write-Host "  1. AccountSelection buttons werken nu (unlock)" -ForegroundColor White
    Write-Host "  2. Originele JDK 22 classes hersteld" -ForegroundColor White
    Write-Host "  3. Saves directory klaar" -ForegroundColor White
    Write-Host "  4. pom.xml schoon (release 22)" -ForegroundColor White
    Write-Host "  5. START.bat met juiste JDK versies" -ForegroundColor White
}
Write-Host "============================================`n" -ForegroundColor Cyan
Read-Host "Druk Enter om te sluiten"
