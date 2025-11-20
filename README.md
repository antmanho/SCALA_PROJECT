# 🎮 Tusmo - Jeu de Mots en Scala

Un clone du jeu **Tusmo** (version française de Wordle) développé en Scala avec **http4s**, **Cats Effect** et **Circe**.

> Ce projet implémente un serveur HTTP en Scala avec Cats Effect incluant :
>
> **Logique métier** :

- **Sélection aléatoire** d'un mot français de 6 lettres
- **Validation** des tentatives (longueur, première lettre, existence dans le dictionnaire)
- **Évaluation** des lettres (Correct ✅ / Présent 🟡 / Absent ❌)
- **Gestion de l'état** (victoire, défaite après 6 tentatives)
- **Révélation du mot** en cas de défaite
- **Statistiques** : Suivi des séries de victoires (current/best streak)PI REST complète (GET, POST)
  > - ✅ WebSocket pour communication temps réel
  > - ✅ Stockage en mémoire (sans base de données)
  > - ✅ Jeu Tusmo avec validation de 336k mots français
  > - ✅ Système de statistiques avec séries de victoires

## 🎯 Règles du jeu

**Tusmo** est un jeu de déduction de mots inspiré de Wordle, adapté pour le français.

### Objectif

Deviner un **mot de 6 lettres** en **6 tentatives maximum**.

### Comment jouer

1. **Indice initial** : La première lettre du mot à deviner vous est donnée
2. **Proposez un mot** : Entrez un mot de 6 lettres commençant par la lettre indiquée
3. **Analysez les couleurs** : Après chaque tentative, les lettres sont colorées selon leur état :
   - � **VERT** (Correct) : La lettre est **correcte** et **bien placée**
   - 🟡 **JAUNE** (Présent) : La lettre est **dans le mot** mais **mal placée**
   - ⚫ **GRIS** (Absent) : La lettre **n'est pas** dans le mot
4. **Ajustez votre stratégie** : Utilisez les indices pour trouver le mot en 6 essais

### Exemple de partie

**Mot à deviner** : `MOUTON` (première lettre : **M**)

| Essai | Mot proposé | Résultat        |
| ----- | ----------- | --------------- |
| 1     | `MAISON`    | 🟢⚫⚫⚫🟡🟡    |
| 2     | `MOULIN`    | 🟢🟢🟢⚫⚫🟡    |
| 3     | `MOUTON`    | 🟢🟢🟢🟢🟢🟢 ✅ |

**Analyse** :

- Essai 1 : `M` est bien placé (vert), `O` et `N` sont dans le mot mais mal placés (jaune)
- Essai 2 : `M`, `O`, `U` sont bien placés (vert), `N` est toujours mal placé (jaune)
- Essai 3 : **Victoire !** Toutes les lettres sont correctes

### Contraintes

- ✅ Le mot doit contenir **exactement 6 lettres**
- ✅ Le mot doit **commencer par la lettre indiquée**
- ✅ Le mot doit **exister dans le dictionnaire français** (336 527 mots validés)
- ✅ Uniquement des **lettres** (pas de chiffres, espaces ou caractères spéciaux)

### Système de statistiques

Le jeu inclut un système de suivi de performance :

- 🔥 **Série actuelle** : Nombre de victoires consécutives
- 🏆 **Meilleur score** : Record personnel de victoires d'affilée
- Les statistiques sont réinitialisées après une défaite
- Les stats persistent pendant toute la session du serveur

### Victoire et défaite

- ✅ **Victoire** : Vous trouvez le mot en 6 essais ou moins
  - Animation de confettis 🎉
  - Mise à jour automatique des statistiques
  - Nouvelle partie lancée après 3 secondes
- ❌ **Défaite** : Vous épuisez vos 6 essais sans trouver le mot
  - Le mot correct est révélé
  - La série de victoires est remise à zéro
  - Possibilité de rejouer immédiatement

## �📋 Table des matières

- [Règles du jeu](#-règles-du-jeu)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Structure du projet](#structure-du-projet)
- [Composants principaux](#composants-principaux)
- [API REST](#api-rest)
- [WebSocket](#websocket)
- [Validation du dictionnaire](#validation-du-dictionnaire)
- [Installation et lancement](#installation-et-lancement)

---

## 🏗️ Architecture

Le projet suit une **architecture fonctionnelle** avec les principes suivants :

- **Programmation fonctionnelle pure** avec Cats Effect (`IO` monad)
- **Gestion d'état immutable** avec `Ref[IO, Map[...]]`
- **Séparation des responsabilités** : Routes HTTP, Services, Modèles
- **API REST** pour le jeu Tusmo
- **WebSocket** pour la messagerie en temps réel
- **Fichiers statiques** servis directement (HTML/CSS/JS)

### Diagramme de l'architecture

```
┌─────────────────────────────────────────────────────────────┐
│                       Client (Browser)                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ tusmo.html   │  │ HTTP Client  │  │  WebSocket   │      │
│  │ (Interface)  │  │   (Fetch)    │  │   Client     │      │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘      │
└─────────┼──────────────────┼──────────────────┼─────────────┘
          │                  │                  │
          │ GET /            │ POST/GET         │ WS Connect
          ▼                  ▼                  ▼
┌─────────────────────────────────────────────────────────────┐
│                    Ember HTTP Server (Port 8080)             │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Router (Main.scala)                │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │   │
│  │  │ StaticRoutes│  │ TusmoRoutes │  │  WSRoutes   │  │   │
│  │  │  (GET /)    │  │ (/api/game) │  │  (/ws/echo) │  │   │
│  │  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘  │   │
│  └─────────┼────────────────┼────────────────┼─────────┘   │
│            │                │                │              │
│  ┌─────────▼───────┐ ┌──────▼──────┐ ┌──────▼──────┐       │
│  │  tusmo.html     │ │ GameState   │ │  Messages   │       │
│  │  (Resources)    │ │   (Ref)     │ │    (Ref)    │       │
│  └─────────────────┘ └──────┬──────┘ └─────────────┘       │
│                             │                               │
│                    ┌────────▼────────┐                      │
│                    │ DictionaryService│                      │
│                    │  (336k mots)    │                      │
│                    └─────────────────┘                      │
└─────────────────────────────────────────────────────────────┘
```

---

## 🛠️ Technologies

### Backend

- **Scala 2.13.12** - Langage fonctionnel sur JVM
- **Cats Effect** - Gestion d'effets et IO monad
- **http4s 0.23.23** - Framework HTTP fonctionnel
  - `http4s-ember-server` - Serveur HTTP/WebSocket
  - `http4s-dsl` - DSL pour routes HTTP
  - `http4s-circe` - Intégration JSON
- **Circe 0.14.6** - Parsing et encodage JSON
- **SBT 1.9.8** - Build tool

### Frontend

- **HTML5 + CSS3** - Interface utilisateur
- **JavaScript (Vanilla)** - Logique client
- **Fetch API** - Requêtes HTTP
- **WebSocket API** - Communication temps réel

### Données

- **Dictionnaire Gutenberg** - 336 527 mots français
- **Stockage en mémoire** - Aucune base de données via `Ref[IO, Map[...]]`

---

## 📂 Structure du projet

```
scala-http-kc/
├── build.sbt                          # Configuration SBT et dépendances
├── README.md                          # Ce fichier
├── project/
│   └── build.properties               # Version de SBT
└── src/main/
    ├── scala/com/barbedet/kc/
    │   ├── Main.scala                 # Point d'entrée de l'application
    │   ├── model/
    │   │   ├── Message.scala          # Modèles pour les messages WebSocket
    │   │   └── Game.scala             # Modèles du jeu Tusmo
    │   ├── http/
    │   │   ├── TusmoRoutes.scala      # Routes API REST Tusmo
    │   │   ├── MessageRoutes.scala    # Routes API REST Messages
    │   │   ├── WebSocketRoutes.scala  # Routes WebSocket
    │   │   ├── StaticRoutes.scala     # Serveur de fichiers statiques
    │   │   └── CorsMiddleware.scala   # Middleware CORS
    │   └── service/
    │       └── DictionaryService.scala # Validation mots français
    └── resources/
        ├── tusmo.html                 # Interface du jeu
        └── french-words.txt           # 336k mots français
```

---

## 🧩 Composants principaux

### 1️⃣ **Main.scala** - Point d'entrée

**Rôle** : Initialise et orchestre tous les composants de l'application.

**Responsabilités** :

- Créer les **références d'état** (`Ref`) pour stocker les données en mémoire
  - `gamesRef` : Map des parties Tusmo en cours
  - `messagesRef` : Map des messages WebSocket
  - `idRef` : Compteur d'IDs pour les messages
- Configurer le **serveur Ember** sur le port 8080
- Assembler le **routeur** principal avec toutes les routes
- Afficher les informations de démarrage (nombre de mots dans le dictionnaire)

```scala
// Initialisation des états en mémoire
gamesRef    <- Ref.of[IO, Map[String, GameState]](Map.empty)
messagesRef <- Ref.of[IO, Map[Long, Message]](Map.empty)
idRef       <- Ref.of[IO, Long](0L)

// Assemblage du routeur
Router(
  "/api/messages" -> apiRoutes,     // API Messages
  "/api/game"     -> tusmoRoutes,   // API Tusmo
  "/ws"           -> wsRoutes,      // WebSocket
  "/"             -> staticRoutes   // Fichiers statiques
)
```

---

### 2️⃣ **TusmoRoutes.scala** - API du jeu

**Rôle** : Gère toute la logique du jeu Tusmo via une API REST.

**Endpoints** :

| Méthode | Route                 | Description                                    |
| ------- | --------------------- | ---------------------------------------------- |
| `POST`  | `/api/game/new`       | Crée une nouvelle partie avec un mot aléatoire |
| `GET`   | `/api/game/:id`       | Récupère l'état d'une partie existante         |
| `POST`  | `/api/game/:id/guess` | Soumet une tentative et obtient l'évaluation   |
| `GET`   | `/api/game/stats`     | Récupère les statistiques du joueur            |

**Logique métier** :

- **Sélection aléatoire** d'un mot français de 6 lettres
- **Validation** des tentatives (longueur, première lettre, existence dans le dictionnaire)
- **Évaluation** des lettres (Correct ✅ / Présent 🟡 / Absent ❌)
- **Gestion de l'état** (victoire, défaite après 6 tentatives)
- **Révélation du mot** en cas de défaite

**Exemple d'évaluation** :

```scala
def evaluateGuess(guess: String, target: String): Attempt = {
  // Compare chaque lettre et retourne : Correct / Present / Absent
  // MAISON vs JARDIN
  //   M → Absent
  //   A → Present (A existe dans JARDIN mais pas à cette position)
  //   I → Present
  //   S → Absent
  //   O → Absent
  //   N → Correct (N est à la bonne position)
}
```

---

### 3️⃣ **WebSocketRoutes.scala** - Communication temps réel

**Rôle** : Fournit un canal de communication **bidirectionnel en temps réel** via WebSocket.

**Endpoint** : `WS /ws/echo`

**Fonctionnalités** :

- **Connexion persistante** entre client et serveur
- **Echo des messages** : Renvoie les messages reçus au client
- **Stockage des messages** dans `messagesRef` pour historique
- **Ping automatique** (désactivé actuellement pour éviter les messages intempestifs)

**Cas d'usage** (potentiels) :

- 💬 Chat en direct entre joueurs
- 📊 Notifications de parties en cours
- 🔔 Alertes de nouveaux records
- 👥 Liste des joueurs connectés

**Architecture WebSocket** :

```
Client                          Server
  │                               │
  │─────── WS Connect ───────────▶│  Établissement connexion
  │                               │
  │◀──── Connection OK ───────────│
  │                               │
  │─────── { text: "Hello" } ────▶│  Client envoie message
  │                               │
  │                               │  Serveur traite et stocke
  │                               │
  │◀──── { id: 1, text: "..." }──│  Serveur répond avec écho
  │                               │
  │─────── Close connection ─────▶│  Fin de session
  │                               │
```

**Code simplifié** :

```scala
def routes(ws: WebSocketBuilder2[IO]): HttpRoutes[IO] = {
  case GET -> Root / "echo" =>
    val receive: Pipe[IO, WebSocketFrame, Unit] =
      _.collect { case Text(msg, _) => msg }
       .evalMap { text =>
         // Traiter et stocker le message
         storeMessage(text)
       }

    val send: Stream[IO, WebSocketFrame] =
      // Envoyer des messages au client
      messagesStream.map(msg => Text(msg.asJson.noSpaces))

    ws.build(send, receive)
}
```

**État actuel** : La WebSocket est fonctionnelle mais utilisée comme démo. Le jeu Tusmo utilise principalement l'API REST.

---

### 4️⃣ **DictionaryService.scala** - Validation des mots

**Rôle** : Valide si un mot existe en français en utilisant le dictionnaire Gutenberg.

**Dictionnaire** :

- **336 527 mots français** extraits du projet Gutenberg
- Chargé **une seule fois** au démarrage (lazy val)
- Stocké dans un `Set[String]` pour recherche O(1)

**Méthode principale** :

```scala
def isValidFrenchWord(word: String): IO[Boolean] = {
  val normalizedWord = word.toLowerCase.trim
  val isValid = frenchWords.contains(normalizedWord)
  IO.println(s"[Dict] '$word': ${if (isValid) "✓" else "✗"}").as(isValid)
}
```

**Avantages** :

- ✅ Validation **instantanée** (pas de latence réseau)
- ✅ **100% fiable** (pas de dépendance externe)
- ✅ Couvre **tous les mots courants** du français

---

### 5️⃣ **MessageRoutes.scala** - API Messages

**Rôle** : API REST complémentaire pour gérer les messages (historique WebSocket).

**Endpoints** :

- `GET /api/messages` - Liste tous les messages
- `GET /api/messages/:id` - Récupère un message spécifique
- `POST /api/messages` - Crée un nouveau message

---

### 6️⃣ **StaticRoutes.scala** - Serveur de fichiers

**Rôle** : Sert les fichiers statiques (HTML, CSS, JS) depuis `src/main/resources/`.

**Routes** :

- `GET /` → Redirige vers `/tusmo.html`
- `GET /tusmo.html` → Interface du jeu

---

### 7️⃣ **CorsMiddleware.scala** - Gestion CORS

**Rôle** : Ajoute les headers CORS pour permettre les requêtes cross-origin.

**Headers** :

- `Access-Control-Allow-Origin: *`
- `Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS`
- `Access-Control-Allow-Headers: Content-Type, Authorization`

---

## 🌐 API REST

### API Tusmo - Jeu de mots

#### Créer une partie

```http
POST /api/game/new
Content-Type: application/json

Response 201:
{
  "gameId": "b9a4e483-a904-4ffd-8858-502304ad43c3",
  "firstLetter": "M",
  "attempts": [],
  "maxAttempts": 6,
  "isWon": false,
  "isLost": false
}
```

#### Soumettre une tentative

```http
POST /api/game/:id/guess
Content-Type: application/json

{
  "word": "MAISON"
}

Response 200:
{
  "firstLetter": "M",
  "attempts": [
    {
      "word": "MAISON",
      "letters": [
        { "char": "M", "state": "CORRECT" },
        { "char": "A", "state": "PRESENT" },
        { "char": "I", "state": "ABSENT" },
        { "char": "S", "state": "ABSENT" },
        { "char": "O", "state": "PRESENT" },
        { "char": "N", "state": "CORRECT" }
      ]
    }
  ],
  "maxAttempts": 6,
  "isWon": false,
  "isLost": false
}
```

#### Récupérer une partie

```http
GET /api/game/:id

Response 200:
{
  "firstLetter": "M",
  "attempts": [...],
  "maxAttempts": 6,
  "isWon": false,
  "isLost": false,
  "revealedWord": "MOUTON"  // Seulement si isLost = true
}
```

#### Obtenir les statistiques

```http
GET /api/game/stats

Response 200:
{
  "currentStreak": 5,
  "bestStreak": 12
}
```

### API Messages - Historique

#### GET /api/messages

Retourne la liste complète des messages en mémoire.

```http
GET /api/messages

Response 200:
[
  { "id": 1, "content": "Hello" },
  { "id": 2, "content": "World" }
]
```

#### GET /api/messages/:id

Retourne un message par son identifiant.

```http
GET /api/messages/1

Response 200:
{ "id": 1, "content": "Hello" }

Response 404 (si inexistant):
{ "error": "Message 42 not found" }
```

#### POST /api/messages

Crée un nouveau message.

```http
POST /api/messages
Content-Type: application/json

{
  "content": "Mon premier message"
}

Response 201:
{
  "id": 1,
  "content": "Mon premier message"
}
```

L'identifiant est généré en mémoire (compteur monotone).

---

## 🔌 WebSocket

### Connexion

La WebSocket permet une **communication bidirectionnelle temps réel** entre le client et le serveur.

**Endpoint** : `ws://localhost:8080/ws/echo`

### Utilisation avec JavaScript

```javascript
const ws = new WebSocket("ws://localhost:8080/ws/echo");

ws.onopen = () => {
  console.log("✅ WebSocket connecté");
};

ws.onmessage = (event) => {
  const message = JSON.parse(event.data);
  console.log("📨 Message reçu:", message);
};

ws.onerror = (error) => {
  console.error("❌ Erreur WebSocket:", error);
};

ws.onclose = () => {
  console.log("🔌 WebSocket fermé");
};

// Envoyer un message
ws.send(JSON.stringify({ text: "Hello from client!" }));
```

### Format des messages

**Client → Serveur** :

```json
{
  "text": "Contenu du message",
  "sender": "Nom de l'utilisateur"
}
```

**Serveur → Client** (echo) :

```json
{
  "id": 1,
  "text": "Contenu du message",
  "sender": "Nom de l'utilisateur",
  "timestamp": 1700000000000
}
```

### Test avec wscat

```bash
# Installation
npm install -g wscat

# Connexion au WebSocket
wscat -c ws://localhost:8080/ws/echo

# Envoyer un message
> {"text":"Hello","sender":"Player1"}

# Le serveur répond avec l'écho
< {"id":1,"text":"Hello","sender":"Player1"}
```

### Cas d'usage de la WebSocket

- 💬 **Chat en direct** entre joueurs
- 📊 **Notifications** de parties en cours
- 🔔 **Alertes** de nouveaux records
- 👥 **Liste** des joueurs connectés en temps réel
- 🎮 **Synchronisation** multi-joueurs

---

## 📖 Validation du dictionnaire

Le projet utilise le **dictionnaire français Gutenberg** contenant **336 527 mots**.

### Source

- **Projet** : [OpenLexicon](https://github.com/chrplr/openlexicon)
- **Fichier** : `liste.de.mots.francais.frgut.txt`
- **Licence** : Open source

### Chargement

```scala
private lazy val frenchWords: Set[String] = {
  val source = Source.fromResource("french-words.txt")
  try {
    source.getLines().map(_.toLowerCase.trim).toSet
  } finally {
    source.close()
  }
}
```

Le dictionnaire est chargé **une seule fois** au premier appel (lazy) et reste en mémoire pour toute la durée de vie de l'application.

---

## 🚀 Installation et lancement

### Prérequis

- **Java 11+** (recommandé : Java 17)
- **SBT 1.9.8+**

### Installation

```bash
# Cloner le projet
git clone <url-du-repo>
cd scala-http-kc

# Compiler
sbt compile
```

### Lancement

```bash
# Démarrer le serveur
sbt run
```

Le serveur démarre sur **http://localhost:8080**

```
📚 Dictionnaire français chargé : 336527 mots (Gutenberg)
✅ Serveur TUSMO démarré sur http://localhost:8080
   🎯 Jeu TUSMO : http://localhost:8080
   📝 API Game  : http://localhost:8080/api/game
   📨 API Msg   : http://localhost:8080/api/messages
```

Pour arrêter le serveur : `Ctrl + C`

### Tests

#### Interface Web

Ouvrir dans le navigateur : **http://localhost:8080**

#### Tests API REST avec curl

```bash
# Créer une nouvelle partie
curl -X POST http://localhost:8080/api/game/new

# Récupérer l'état d'une partie
curl http://localhost:8080/api/game/<GAME_ID>

# Soumettre un mot
curl -X POST http://localhost:8080/api/game/<GAME_ID>/guess \
  -H "Content-Type: application/json" \
  -d '{"word":"MAISON"}'

# Obtenir les statistiques
curl http://localhost:8080/api/game/stats

# Lister les messages
curl http://localhost:8080/api/messages

# Créer un message
curl -X POST http://localhost:8080/api/messages \
  -H "Content-Type: application/json" \
  -d '{"content":"Hello http4s"}'

# Récupérer un message
curl http://localhost:8080/api/messages/1
```

#### Test WebSocket

```bash
# Installation de wscat
npm install -g wscat

# Connexion au WebSocket
wscat -c ws://localhost:8080/ws/echo

# Envoyer un message
> {"text":"Test message","sender":"Player1"}
```

---

## 🎮 Guide de démarrage rapide

### Lancer une partie

1. **Démarrer le serveur** : `sbt run`
2. **Ouvrir le navigateur** : http://localhost:8080
3. Le jeu démarre automatiquement avec un mot aléatoire

### Interface du jeu

```
┌─────────────────────────────────────────────┐
│  🔥 5    Meilleur: 12       🎯 TUSMO        │
│                                             │
│  📊 Statistiques            Essais: 2/6     │
│  🔥 Série actuelle: 5       Longueur: 6     │
│  🏆 Meilleur score: 12                      │
│                                             │
│  Première lettre: M                         │
│                                             │
│  ┌──┬──┬──┬──┬──┬──┐                       │
│  │M │A │I │S │O │N │  (Essai 1)            │
│  └──┴──┴──┴──┴──┴──┘                       │
│  🟢 ⚫ ⚫ ⚫ 🟡 🟡                            │
│                                             │
│  ┌──┬──┬──┬──┬──┬──┐                       │
│  │M │O │U │L │I │N │  (Essai 2)            │
│  └──┴──┴──┴──┴──┴──┘                       │
│  🟢 🟢 🟢 ⚫ ⚫ 🟡                            │
│                                             │
│  [Votre mot...] [Valider]                  │
│                                             │
│  Clavier virtuel AZERTY                     │
└─────────────────────────────────────────────┘
```

### Stratégie gagnante

1. **Premier essai** : Utilisez un mot avec des lettres courantes (A, E, I, O, U, R, S, T, N)
2. **Analysez les indices** : Notez les lettres vertes (bien placées) et jaunes (mal placées)
3. **Éliminez** : Ignorez les lettres grises dans vos prochains essais
4. **Affinez** : Repositionnez les lettres jaunes et conservez les vertes
5. **Validez** : Seuls les mots du dictionnaire français sont acceptés

### Fonctionnalités

- 🎯 **Grille interactive** : Visualisation claire de vos essais
- ⌨️ **Clavier virtuel** : Clavier AZERTY avec états des lettres
- 📊 **Statistiques en temps réel** : Suivez votre série de victoires
- 🎉 **Animations** : Confettis lors des victoires
- 🔄 **Nouvelle partie auto** : Lance automatiquement un nouveau défi après victoire
- 📱 **Responsive** : Jouable sur mobile, tablette et desktop

---

## 📝 Logs

Le serveur affiche des logs détaillés pour comprendre le flux :

```
[Tusmo] Nouvelle partie créée : 7d2f8a3c (mot: MAISON)
[Tusmo] Mot reçu: PALIER
[Tusmo] Mot normalisé: PALIER pour game 7d2f8a3c
[Tusmo] Vérification du mot 'PALIER' dans dictionnaire local...
[Dict] Vérification 'PALIER': ✓ VALIDE (local)
[Tusmo] Mot 'PALIER' validé, évaluation...
[Tusmo] Mot évalué, isWon=false, isLost=false
```

---

## 🏆 Fonctionnalités

### Jeu Tusmo

- ✅ Jeu Tusmo complet (6 lettres, 6 tentatives)
- ✅ Validation en temps réel avec 336k mots français
- ✅ Interface web responsive et moderne
- ✅ Indice de la première lettre
- ✅ Révélation du mot en cas de défaite
- ✅ Clavier virtuel AZERTY avec états des touches
- ✅ Animations et feedback visuel (confettis, transitions)
- ✅ Système de statistiques (série actuelle, meilleur score)
- ✅ Nouvelle partie automatique après victoire

### Architecture technique

- ✅ API REST complète (GET, POST)
- ✅ WebSocket pour communication temps réel
- ✅ Gestion d'état fonctionnelle avec Cats Effect
- ✅ Serveur de fichiers statiques
- ✅ Middleware CORS
- ✅ Stockage en mémoire sans base de données

---

## 📚 Architecture Cats Effect

Le projet utilise le monad `IO` de Cats Effect pour gérer tous les effets de manière pure et fonctionnelle :

```scala
// Création d'un état partagé thread-safe
gamesRef <- Ref.of[IO, Map[String, GameState]](Map.empty)

// Modification atomique de l'état
gamesRef.update(_ + (gameId -> newGame))

// Lecture de l'état
gamesRef.get.map(_.get(gameId))

// Composition d'effets
IO.println("Log") *>
saveGame(game) *>
Ok(response.asJson)
```

**Avantages** :

- 🔒 **Thread-safe** : Gestion concurrente sécurisée
- 🔄 **Composable** : Chaînage d'effets avec `*>`, `>>`, `flatMap`
- 🎯 **Pure** : Séparation description/exécution
- 🚀 **Performant** : Optimisations runtime

---

## ✅ Conformité au sujet

Ce projet répond à toutes les exigences :

### ✅ Serveur HTTP avec Cats

- Framework : **http4s** + **Cats Effect**
- Runtime : **IO monad** pour tous les effets

### ✅ API REST (GET et POST)

- **GET** :
  - `/api/messages` - Liste des messages
  - `/api/messages/:id` - Message spécifique
  - `/api/game/:id` - État d'une partie
- **POST** :
  - `/api/messages` - Créer un message
  - `/api/game/new` - Nouvelle partie
  - `/api/game/:id/guess` - Soumettre une tentative

### ✅ Stockage en mémoire (sans BDD)

- `Ref[IO, Map[String, GameState]]` pour les parties Tusmo
- `Ref[IO, Map[Long, Message]]` pour les messages
- `Ref[IO, Long]` pour le compteur d'IDs

### ✅ WebSocket (bonus)

- Endpoint : `ws://localhost:8080/ws/echo`
- Communication bidirectionnelle temps réel
- Stockage des messages échangés

### ✅ Documentation

- README complet avec architecture détaillée
- Explication de tous les composants
- Diagrammes et exemples de code

### ✅ Tests

- Tests manuels avec curl
- Tests WebSocket avec wscat
- Interface web interactive

---

## 🤝 Contribution

Les contributions sont les bienvenues !

1. Fork le projet
2. Créer une branche (`git checkout -b feature/amélioration`)
3. Commit les changements (`git commit -am 'Ajout fonctionnalité'`)
4. Push vers la branche (`git push origin feature/amélioration`)
5. Créer une Pull Request

---

## 📄 Licence

Ce projet est open source et disponible sous licence MIT.

---

## 🙏 Remerciements

- **http4s** - Framework HTTP fonctionnel
- **Cats Effect** - Gestion d'effets pure
- **Projet Gutenberg** - Dictionnaire français
- **OpenLexicon** - Base de données linguistique

---

**Développé avec ❤️ en Scala fonctionnel**
