# ============================================================
#  HAZY RSPS - DIAGNOSE
#  Run dit EERST om te checken wat de huidige staat is
# ============================================================

$base = "C:\hazy"
$server = "$base\SwiftPk-Server"
$backup = "C:\Users\deniz\Desktop\xerox\rsps-gambling\twee rsps\Hazy Package\SwiftPk-Server\target\classes"

Write-Host "`n=== HAZY RSPS DIAGNOSE ===" -ForegroundColor Cyan

# JDK Check
Write-Host "`n--- JDK Installaties ---" -ForegroundColor Yellow
@(
    @("JDK 8 (Corretto)", "C:\Program Files\Amazon Corretto\jdk1.8.0_462"),
    @("JDK 17 (Corretto)", "C:\Program Files\Amazon Corretto\jdk17.0.18_9"),
    @("JDK 11 (Adoptium)", "C:\Program Files\Eclipse Adoptium\jdk-11.0.29.7-hotspot"),
    @("JDK 21 (Adoptium)", "C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot"),
    @("JDK 25 (Oracle)", "C:\Program Files\Java\jdk-25.0.2")
) | ForEach-Object {
    $ok = Test-Path "$($_[1])\bin\java.exe"
    $icon = if ($ok) { "[OK]" } else { "[MISSING]" }
    $color = if ($ok) { "Green" } else { "Red" }
    Write-Host "  $icon $($_[0]): $($_[1])" -ForegroundColor $color
}

# Server classes check
Write-Host "`n--- Server Classes ---" -ForegroundColor Yellow
$classesDir = "$server\target\classes\com\twisted\GameServer.class"
if (Test-Path $classesDir) {
    $classFile = Get-Item $classesDir
    $classCount = (Get-ChildItem "$server\target\classes" -Recurse -Filter "*.class").Count
    # Check class file version (byte 7 = major version)
    $bytes = [System.IO.File]::ReadAllBytes($classesDir)
    $majorVersion = [BitConverter]::ToUInt16(@($bytes[7], $bytes[6]), 0)
    $jdkVersion = $majorVersion - 44
    Write-Host "  [OK] GameServer.class bestaat ($classCount files totaal)" -ForegroundColor Green
    Write-Host "  Class file version: $majorVersion (JDK $jdkVersion)" -ForegroundColor White
    Write-Host "  Last modified: $($classFile.LastWriteTime)" -ForegroundColor White
} else {
    Write-Host "  [MISSING] Geen compiled classes!" -ForegroundColor Red
}

# Shaded JAR check
Write-Host "`n--- Shaded JAR ---" -ForegroundColor Yellow
$shadedJar = "$server\target\TwistedScape-GameServer-1.1.1-shaded.jar"
if (Test-Path $shadedJar) {
    $size = [math]::Round((Get-Item $shadedJar).Length / 1MB)
    Write-Host "  [OK] $shadedJar (${size}MB)" -ForegroundColor Green
} else {
    Write-Host "  [MISSING] Shaded JAR niet gevonden!" -ForegroundColor Red
}

# Backup check
Write-Host "`n--- Backup Classes ---" -ForegroundColor Yellow
if (Test-Path "$backup\com\twisted\GameServer.class") {
    $backupCount = (Get-ChildItem $backup -Recurse -Filter "*.class").Count
    $bytes2 = [System.IO.File]::ReadAllBytes("$backup\com\twisted\GameServer.class")
    $majorVersion2 = [BitConverter]::ToUInt16(@($bytes2[7], $bytes2[6]), 0)
    $jdkVersion2 = $majorVersion2 - 44
    Write-Host "  [OK] Backup classes bestaan ($backupCount files, JDK $jdkVersion2)" -ForegroundColor Green
} else {
    Write-Host "  [MISSING] Geen backup gevonden op:" -ForegroundColor Red
    Write-Host "    $backup" -ForegroundColor DarkYellow
}

# Source check
Write-Host "`n--- Source Files ---" -ForegroundColor Yellow
$javaCount = (Get-ChildItem "$server\src" -Recurse -Filter "*.java" -ErrorAction SilentlyContinue).Count
$ktCount = (Get-ChildItem "$server\src" -Recurse -Filter "*.kt" -ErrorAction SilentlyContinue).Count
Write-Host "  Java files: $javaCount" -ForegroundColor White
Write-Host "  Kotlin files: $ktCount" -ForegroundColor White

# AccountSelection check
Write-Host "`n--- AccountSelection Fix ---" -ForegroundColor Yellow
$asFile = "$server\src\com\twisted\game\content\account\AccountSelection.java"
if (Test-Path $asFile) {
    $hasUnlock = Select-String -Path $asFile -Pattern "player\.unlock\(\)" | Where-Object { $_.LineNumber -lt 35 }
    if ($hasUnlock) {
        Write-Host "  [OK] unlock() aanwezig in open() (regel $($hasUnlock.LineNumber))" -ForegroundColor Green
    } else {
        Write-Host "  [NEEDS FIX] unlock() ONTBREEKT in open()" -ForegroundColor Red
    }
}

# pom.xml check
Write-Host "`n--- pom.xml ---" -ForegroundColor Yellow
$pom = Get-Content "$server\pom.xml" -Raw
$releaseMatch = [regex]::Match($pom, '<release>(\d+)</release>')
$hasSourceDir = $pom -match '<sourceDirectory>src</sourceDirectory>'
$hasPreview = $pom -match 'enable-preview'
Write-Host "  Release: $($releaseMatch.Groups[1].Value)" -ForegroundColor White
Write-Host "  sourceDirectory=src: $hasSourceDir" -ForegroundColor White
Write-Host "  enable-preview: $hasPreview" -ForegroundColor $(if ($hasPreview) { "Red" } else { "Green" })

# Saves check
Write-Host "`n--- Saves ---" -ForegroundColor Yellow
$savesDir = "$server\data\saves\characters"
if (Test-Path $savesDir) {
    $saveCount = (Get-ChildItem $savesDir -Filter "*.json" -ErrorAction SilentlyContinue).Count
    Write-Host "  [OK] Saves dir bestaat ($saveCount saves)" -ForegroundColor Green
} else {
    Write-Host "  [NEEDS FIX] Saves dir ontbreekt" -ForegroundColor Red
}

# Maven check
Write-Host "`n--- Maven ---" -ForegroundColor Yellow
$mavenPaths = @("C:\maven\apache-maven-3.9.6", "C:\maven\apache-maven-3.9.8", "C:\maven\apache-maven-3.9.9")
$mvnFound = $false
foreach ($mp in $mavenPaths) {
    if (Test-Path "$mp\bin\mvn.cmd") {
        Write-Host "  [OK] Maven gevonden: $mp" -ForegroundColor Green
        $mvnFound = $true
        break
    }
}
if (-not $mvnFound) { Write-Host "  [MISSING] Maven niet gevonden" -ForegroundColor Red }

# Running processes
Write-Host "`n--- Draaiende Java Processen ---" -ForegroundColor Yellow
$procs = Get-Process java -ErrorAction SilentlyContinue
if ($procs) {
    $procs | ForEach-Object { Write-Host "  PID $($_.Id): $($_.ProcessName) ($([math]::Round($_.WorkingSet64/1MB))MB)" -ForegroundColor White }
} else {
    Write-Host "  Geen Java processen actief" -ForegroundColor White
}

# Ports check
Write-Host "`n--- Poorten ---" -ForegroundColor Yellow
@(43594, 43595) | ForEach-Object {
    $conn = Get-NetTCPConnection -LocalPort $_ -ErrorAction SilentlyContinue
    if ($conn) {
        Write-Host "  Port $_: IN GEBRUIK (PID $($conn.OwningProcess | Select-Object -First 1))" -ForegroundColor Yellow
    } else {
        Write-Host "  Port $_: vrij" -ForegroundColor Green
    }
}

Write-Host "`n=== DIAGNOSE KLAAR ===" -ForegroundColor Cyan
Write-Host "Kopieer deze output en stuur naar Claude als er issues zijn.`n" -ForegroundColor White
Read-Host "Druk Enter om te sluiten"
