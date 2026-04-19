# Hazy RSPS

RuneScape Private Server gebaseerd op TwistedScape (SwiftPk) met een RuneLite-based client.

## Vereisten

- **Java JDK 17+** (aanbevolen: [Amazon Corretto 17](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html))
- **Git LFS** (nodig om de cache bestanden te downloaden)

## Installatie

### 1. Clone de repo (met LFS)
```bash
git lfs install
git clone https://github.com/denizsenol1996-art/claude-XRSPS.git
cd claude-XRSPS
```

### 2. Server builden
```bash
cd SwiftPk-Server
mvn clean package -DskipTests
```
Of gebruik de meegeleverde `server.jar`.

### 3. Client builden
```bash
cd Hazy-Client
gradlew.bat :game:run
```

## Opstarten

Gebruik `START.bat` om alles in de juiste volgorde op te starten:

1. **JS5 Cache Server** (`hazy-swift/app.jar`) — serveert de game cache
2. **Game Server** (`SwiftPk-Server/server.jar`) — de RSPS server
3. **Client** (`Hazy-Client`) — de RuneLite client

Of start ze handmatig in die volgorde.

### Handmatig starten

**JS5 (terminal 1):**
```bash
cd hazy-swift
java -jar app.jar
```

**Server (terminal 2):**
```bash
cd SwiftPk-Server
java --add-opens java.base/java.time=ALL-UNNAMED -jar server.jar
```

**Client (terminal 3):**
```bash
cd Hazy-Client
gradlew.bat :game:run
```

## Projectstructuur

| Map | Beschrijving |
|---|---|
| `SwiftPk-Server/` | Game server (TwistedScape) |
| `Hazy-Client/` | RuneLite-based game client |
| `hazy-swift/` | JS5 cache server + game cache bestanden |
| `SwiftFUP/` | SwiftFUP cache packer tool |
| `custom-models/` | Custom 3D modellen |
| `casino-work/` | Casino gerelateerde assets |
