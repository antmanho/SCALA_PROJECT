# 🎮 Tusmo - Jeu de Mots en Scala

Un clone du jeu **Tusmo** (version française de Wordle) développé en Scala avec **http4s**, **Cats Effect** et **Circe**.

> Ce projet implémente un serveur HTTP en Scala avec Cats Effect incluant :
> - ✅ API REST complète (GET, POST)
> - ✅ WebSocket pour communication temps réel
> - ✅ Stockage en mémoire (sans base de données)
> - ✅ Jeu Tusmo avec validation de 336k mots français

## 📋 Table des matières

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

| Méthode | Route | Description |
|---------|-------|-------------|
| `POST` | `/api/game/new` | Crée une nouvelle partie avec un mot aléatoire |
| `GET` | `/api/game/:id` | Récupère l'état d'une partie existante |
| `POST` | `/api/game/:id/guess` | Soumet une tentative et obtient l'évaluation |

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

## 2. API exposée

### Ressource `Message`

Un `Message` est défini par :

```json
{
  "id": 1,
  "content": "Bonjour"
}
```

### Endpoints REST

Tous les endpoints sont préfixés par `/api`.

#### 2.1. GET /api/messages

Retourne la liste complète des messages en mémoire.

- Méthode : `GET`
- URL : `http://localhost:8080/api/messages`
- Réponse 200 (exemple) :

```json
[
  { "id": 1, "content": "Hello" },
  { "id": 2, "content": "World" }
]
```

#### 2.2. GET /api/messages/:id

Retourne un message par son identifiant.

- Méthode : `GET`
- URL : `http://localhost:8080/api/messages/1`
- Réponses :
  - `200 OK` + JSON du message
  - `404 Not Found` si l'id n'existe pas, par ex. :

```json
{ "error": "Message 42 not found" }
```

#### 2.3. POST /api/messages

Crée un nouveau message.

- Méthode : `POST`
- URL : `http://localhost:8080/api/messages`
- Headers : `Content-Type: application/json`
- Body JSON :

```json
{
  "content": "Mon premier message"
}
```

- Réponse 201 Created :

```json
{
  "id": 1,
  "content": "Mon premier message"
}
```

L'identifiant est généré en mémoire (compteur monotone).

### Endpoint WebSocket (bonus)

- URL : `ws://localhost:8080/ws/echo`
- Comportement :
  - Le serveur envoie `"tick"` toutes les secondes au client.
  - Tout message texte envoyé par le client est loggé côté serveur.

Exemple de test avec `wscat` :

```bash
npm install -g wscat

# Connexion au WebSocket
wscat -c ws://localhost:8080/ws/echo
```

Vous verrez alors passer des messages `"tick"`.

## 3. Lancer le projet

### 3.1. Prérequis

- JDK 11+
- sbt (1.8+ recommandé)

### 3.2. Démarrer le serveur

Depuis la racine du projet :

```bash
sbt run
```

Le serveur démarre sur :

- REST : `http://localhost:8080/api/messages`
- WS   : `ws://localhost:8080/ws/echo`

Pour arrêter le serveur : `Ctrl + C`.

## 4. Tests manuels effectués

Voici les commandes utilisées pour tester le projet.

### 4.1. API REST

1. Lister les messages (au démarrage, liste vide)

```bash
curl -i http://localhost:8080/api/messages
```

2. Créer un message

```bash
curl -i -X POST http://localhost:8080/api/messages   -H "Content-Type: application/json"   -d '{"content":"Hello http4s"}'
```

3. Récupérer un message par id

```bash
curl -i http://localhost:8080/api/messages/1
```

4. Message inexistant

```bash
curl -i http://localhost:8080/api/messages/999
```

### 4.2. WebSocket

Testé avec `wscat` :

```bash
wscat -c ws://localhost:8080/ws/echo
```

- Le client reçoit périodiquement `"tick"`.
- Les messages saisis dans `wscat` apparaissent dans la console du serveur.

## 5. Comment ce projet répond au sujet

- Librairie utilisée côté Cats : http4s + cats-effect.
- API REST :
  - `GET /api/messages`
  - `GET /api/messages/:id`
  - `POST /api/messages`
- Stockage :
  - Structure en mémoire (`Ref[IO, Map[Long, Message]]`).
- WebSocket :
  - Endpoint bonus sur `ws://localhost:8080/ws/echo`.
- Projet :
  - Documenté via ce README.
  - Testé manuellement (curl + wscat).
