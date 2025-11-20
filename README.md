# 🎮 Tusmo - Jeu de Mots en Scala# 🎮 Tusmo - Jeu de Mots en Scala

Un clone du jeu **Tusmo** (version française de Wordle) développé en Scala avec **http4s**, **Cats Effect** et **Circe**.Un clone du jeu **Tusmo** (version française de Wordle) développé en Scala avec **http4s**, **Cats Effect** et **Circe**.

---> Ce projet implémente un serveur HTTP en Scala avec Cats Effect incluant :

>

## 🎯 Règles du jeu> **Logique métier** :

**Tusmo** est un jeu de déduction de mots inspiré de Wordle, adapté pour le français.- **Sélection aléatoire** d'un mot français de 6 lettres

- **Validation** des tentatives (longueur, première lettre, existence dans le dictionnaire)

### 🎲 Objectif- **Évaluation** des lettres (Correct ✅ / Présent 🟡 / Absent ❌)

- **Gestion de l'état** (victoire, défaite après 6 tentatives)

Deviner un **mot français** en **6 tentatives maximum**. La longueur du mot varie à chaque partie (généralement entre 5 et 7 lettres).- **Révélation du mot** en cas de défaite

- **Statistiques** : Suivi des séries de victoires (current/best streak)PI REST complète (GET, POST)

### 🕹️ Comment jouer > - ✅ WebSocket pour communication temps réel

> - ✅ Stockage en mémoire (sans base de données)

1. **Indice initial** : La première lettre du mot à deviner vous est donnée > - ✅ Jeu Tusmo avec validation de 336k mots français

2. **Proposez un mot** : Entrez un mot de la **même longueur** commençant par la lettre indiquée > - ✅ Système de statistiques avec séries de victoires

3. **Analysez les couleurs** : Après chaque tentative, les lettres sont colorées selon leur état :

   - 🟢 **VERT** (Correct) : La lettre est **correcte** et **bien placée**## 🎯 Règles du jeu

   - 🟡 **JAUNE** (Présent) : La lettre est **dans le mot** mais **mal placée**

   - ⚫ **GRIS** (Absent) : La lettre **n'est pas** dans le mot**Tusmo** est un jeu de déduction de mots inspiré de Wordle, adapté pour le français.

4. **Ajustez votre stratégie** : Utilisez les indices pour trouver le mot en 6 essais maximum

### Objectif

### 📝 Exemple de partie

Deviner un **mot de 6 lettres** en **6 tentatives maximum**.

**Mot à deviner** : `MOUTON` (6 lettres, première lettre : **M**)

### Comment jouer

| Essai | Mot proposé | Résultat | Analyse |

|-------|-------------|---------------------|---------|1. **Indice initial** : La première lettre du mot à deviner vous est donnée

| 1 | `MAISON` | 🟢⚫⚫⚫🟡🟡 | M correct, O et N présents mais mal placés |2. **Proposez un mot** : Entrez un mot de 6 lettres commençant par la lettre indiquée

| 2 | `MOULIN` | 🟢🟢🟢⚫⚫🟡 | M, O, U corrects, N mal placé |3. **Analysez les couleurs** : Après chaque tentative, les lettres sont colorées selon leur état :

| 3 | `MOUTON` | 🟢🟢🟢🟢🟢🟢 **✅** | **VICTOIRE !** Toutes les lettres sont correctes | - � **VERT** (Correct) : La lettre est **correcte** et **bien placée**

- 🟡 **JAUNE** (Présent) : La lettre est **dans le mot** mais **mal placée**

### ✅ Contraintes et règles - ⚫ **GRIS** (Absent) : La lettre **n'est pas** dans le mot

4. **Ajustez votre stratégie** : Utilisez les indices pour trouver le mot en 6 essais

- ✅ Le mot doit contenir **le nombre de lettres indiqué** (affiché dans l'interface)

- ✅ Le mot doit **commencer par la lettre indiquée** (indice)### Exemple de partie

- ✅ Le mot doit **exister dans le dictionnaire français** (336 527 mots validés)

- ✅ Uniquement des **lettres** (pas de chiffres, espaces ou caractères spéciaux)**Mot à deviner** : `MOUTON` (première lettre : **M**)

- ⚠️ Vous avez **6 tentatives maximum** pour trouver le mot

| Essai | Mot proposé | Résultat |

### 📊 Système de statistiques| ----- | ----------- | --------------- |

| 1 | `MAISON` | 🟢⚫⚫⚫🟡🟡 |

Le jeu inclut un système de suivi de performance :| 2 | `MOULIN` | 🟢🟢🟢⚫⚫🟡 |

| 3 | `MOUTON` | 🟢🟢🟢🟢🟢🟢 ✅ |

- 🔥 **Série actuelle** : Nombre de victoires consécutives

- 🏆 **Meilleur score** : Record personnel de victoires d'affilée**Analyse** :

- 📈 Les statistiques sont **affichées en temps réel** dans l'interface

- 🔄 Les stats sont **réinitialisées** après une défaite- Essai 1 : `M` est bien placé (vert), `O` et `N` sont dans le mot mais mal placés (jaune)

- 💾 Les stats **persistent** pendant toute la session du serveur- Essai 2 : `M`, `O`, `U` sont bien placés (vert), `N` est toujours mal placé (jaune)

- Essai 3 : **Victoire !** Toutes les lettres sont correctes

### 🏆 Victoire et défaite

### Contraintes

**✅ Victoire** : Vous trouvez le mot en 6 essais ou moins

- Animation de confettis 🎉- ✅ Le mot doit contenir **exactement 6 lettres**

- Mise à jour automatique des statistiques (+1 à la série)- ✅ Le mot doit **commencer par la lettre indiquée**

- Nouvelle partie lancée automatiquement après 3 secondes- ✅ Le mot doit **exister dans le dictionnaire français** (336 527 mots validés)

- ✅ Uniquement des **lettres** (pas de chiffres, espaces ou caractères spéciaux)

**❌ Défaite** : Vous épuisez vos 6 essais sans trouver le mot

- Le mot correct est révélé### Système de statistiques

- La série de victoires est remise à **zéro**

- Possibilité de rejouer immédiatementLe jeu inclut un système de suivi de performance :

### 💡 Stratégie gagnante- 🔥 **Série actuelle** : Nombre de victoires consécutives

- 🏆 **Meilleur score** : Record personnel de victoires d'affilée

1. **Premier essai** : Utilisez un mot avec des lettres courantes (A, E, I, O, U, R, S, T, N)- Les statistiques sont réinitialisées après une défaite

2. **Analysez les indices** : Notez les lettres vertes (bien placées) et jaunes (mal placées)- Les stats persistent pendant toute la session du serveur

3. **Éliminez** : Ignorez les lettres grises dans vos prochains essais

4. **Affinez** : Repositionnez les lettres jaunes et conservez les vertes### Victoire et défaite

5. **Validez** : Seuls les mots du dictionnaire français sont acceptés

- ✅ **Victoire** : Vous trouvez le mot en 6 essais ou moins

--- - Animation de confettis 🎉

- Mise à jour automatique des statistiques

## 🚀 Installation et lancement rapide - Nouvelle partie lancée après 3 secondes

- ❌ **Défaite** : Vous épuisez vos 6 essais sans trouver le mot

### Prérequis - Le mot correct est révélé

- La série de victoires est remise à zéro

- **Java 11+** (recommandé : Java 17) - Possibilité de rejouer immédiatement

- **SBT 1.9.8+**

## �📋 Table des matières

### Démarrage en 3 étapes

- [Règles du jeu](#-règles-du-jeu)

```bash- [Architecture](#architecture)

# 1. Cloner le projet- [Technologies](#technologies)

git clone <url-du-repo>- [Structure du projet](#structure-du-projet)

cd scala-http-kc- [Composants principaux](#composants-principaux)

- [API REST](#api-rest)

# 2. Compiler le projet- [WebSocket](#websocket)

sbt compile- [Validation du dictionnaire](#validation-du-dictionnaire)

- [Installation et lancement](#installation-et-lancement)

# 3. Lancer le serveur

sbt run---

```

## 🏗️ Architecture

Le serveur démarre sur **http://localhost:8080**

Le projet suit une **architecture fonctionnelle** avec les principes suivants :

```

📚 Dictionnaire français chargé : 336527 mots (Gutenberg)- **Programmation fonctionnelle pure** avec Cats Effect (`IO` monad)

✅ Serveur TUSMO démarré sur http://localhost:8080- **Gestion d'état immutable** avec `Ref[IO, Map[...]]`

   🎯 Jeu TUSMO : http://localhost:8080- **Séparation des responsabilités** : Routes HTTP, Services, Modèles

   📝 API Game  : http://localhost:8080/api/game- **API REST** pour le jeu Tusmo

   📨 API Msg   : http://localhost:8080/api/messages- **WebSocket** pour la messagerie en temps réel

   🔥 Stats     : http://localhost:8080/api/game/stats- **Fichiers statiques** servis directement (HTML/CSS/JS)

```

### Diagramme de l'architecture

### 🎮 Jouer

````

1. Ouvrir **http://localhost:8080** dans votre navigateur┌─────────────────────────────────────────────────────────────┐

2. Le jeu démarre automatiquement avec un mot aléatoire│                       Client (Browser)                       │

3. Suivez les règles ci-dessus et amusez-vous ! 🎯│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │

│  │ tusmo.html   │  │ HTTP Client  │  │  WebSocket   │      │

Pour arrêter le serveur : `Ctrl + C`│  │ (Interface)  │  │   (Fetch)    │  │   Client     │      │

│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘      │

---└─────────┼──────────────────┼──────────────────┼─────────────┘

          │                  │                  │

## 📋 Table des matières technique          │ GET /            │ POST/GET         │ WS Connect

          ▼                  ▼                  ▼

- [Architecture](#-architecture)┌─────────────────────────────────────────────────────────────┐

- [Technologies](#️-technologies)│                    Ember HTTP Server (Port 8080)             │

- [Structure du projet](#-structure-du-projet)│  ┌──────────────────────────────────────────────────────┐   │

- [Composants principaux](#-composants-principaux)│  │                    Router (Main.scala)                │   │

- [API REST](#-api-rest)│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │   │

- [WebSocket](#-websocket)│  │  │ StaticRoutes│  │ TusmoRoutes │  │  WSRoutes   │  │   │

- [Validation du dictionnaire](#-validation-du-dictionnaire)│  │  │  (GET /)    │  │ (/api/game) │  │  (/ws/echo) │  │   │

- [Tests et développement](#-tests-et-développement)│  │  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘  │   │

│  └─────────┼────────────────┼────────────────┼─────────┘   │

---│            │                │                │              │

│  ┌─────────▼───────┐ ┌──────▼──────┐ ┌──────▼──────┐       │

## 🏗️ Architecture│  │  tusmo.html     │ │ GameState   │ │  Messages   │       │

│  │  (Resources)    │ │   (Ref)     │ │    (Ref)    │       │

Le projet suit une **architecture fonctionnelle** avec les principes suivants :│  └─────────────────┘ └──────┬──────┘ └─────────────┘       │

│                             │                               │

- **Programmation fonctionnelle pure** avec Cats Effect (`IO` monad)│                    ┌────────▼────────┐                      │

- **Gestion d'état immutable** avec `Ref[IO, Map[...]]`│                    │ DictionaryService│                      │

- **Séparation des responsabilités** : Routes HTTP, Services, Modèles│                    │  (336k mots)    │                      │

- **API REST** pour le jeu Tusmo│                    └─────────────────┘                      │

- **WebSocket** pour la messagerie en temps réel└─────────────────────────────────────────────────────────────┘

- **Fichiers statiques** servis directement (HTML/CSS/JS)```



### Diagramme de l'architecture---



```## 🛠️ Technologies

┌─────────────────────────────────────────────────────────────┐

│                       Client (Browser)                       │### Backend

│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │

│  │ tusmo.html   │  │ HTTP Client  │  │  WebSocket   │      │- **Scala 2.13.12** - Langage fonctionnel sur JVM

│  │ (Interface)  │  │   (Fetch)    │  │   Client     │      │- **Cats Effect** - Gestion d'effets et IO monad

│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘      │- **http4s 0.23.23** - Framework HTTP fonctionnel

└─────────┼──────────────────┼──────────────────┼─────────────┘  - `http4s-ember-server` - Serveur HTTP/WebSocket

          │                  │                  │  - `http4s-dsl` - DSL pour routes HTTP

          │ GET /            │ POST/GET         │ WS Connect  - `http4s-circe` - Intégration JSON

          ▼                  ▼                  ▼- **Circe 0.14.6** - Parsing et encodage JSON

┌─────────────────────────────────────────────────────────────┐- **SBT 1.9.8** - Build tool

│                    Ember HTTP Server (Port 8080)             │

│  ┌──────────────────────────────────────────────────────┐   │### Frontend

│  │                    Router (Main.scala)                │   │

│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │   │- **HTML5 + CSS3** - Interface utilisateur

│  │  │ StaticRoutes│  │ TusmoRoutes │  │  WSRoutes   │  │   │- **JavaScript (Vanilla)** - Logique client

│  │  │  (GET /)    │  │ (/api/game) │  │  (/ws/echo) │  │   │- **Fetch API** - Requêtes HTTP

│  │  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘  │   │- **WebSocket API** - Communication temps réel

│  └─────────┼────────────────┼────────────────┼─────────┘   │

│            │                │                │              │### Données

│  ┌─────────▼───────┐ ┌──────▼──────┐ ┌──────▼──────┐       │

│  │  tusmo.html     │ │ GameState   │ │  Messages   │       │- **Dictionnaire Gutenberg** - 336 527 mots français

│  │  (Resources)    │ │   (Ref)     │ │    (Ref)    │       │- **Stockage en mémoire** - Aucune base de données via `Ref[IO, Map[...]]`

│  └─────────────────┘ └──────┬──────┘ └─────────────┘       │

│                             │                               │---

│                    ┌────────▼────────┐                      │

│                    │ DictionaryService│                      │## 📂 Structure du projet

│                    │  (336k mots)    │                      │

│                    └─────────────────┘                      │```

└─────────────────────────────────────────────────────────────┘scala-http-kc/

```├── build.sbt                          # Configuration SBT et dépendances

├── README.md                          # Ce fichier

---├── project/

│   └── build.properties               # Version de SBT

## 🛠️ Technologies└── src/main/

    ├── scala/com/barbedet/kc/

### Backend    │   ├── Main.scala                 # Point d'entrée de l'application

    │   ├── model/

- **Scala 2.13.12** - Langage fonctionnel sur JVM    │   │   ├── Message.scala          # Modèles pour les messages WebSocket

- **Cats Effect 3.5.0** - Gestion d'effets et IO monad    │   │   └── Game.scala             # Modèles du jeu Tusmo

- **http4s 0.23.23** - Framework HTTP fonctionnel    │   ├── http/

  - `http4s-ember-server` - Serveur HTTP/WebSocket    │   │   ├── TusmoRoutes.scala      # Routes API REST Tusmo

  - `http4s-dsl` - DSL pour routes HTTP    │   │   ├── MessageRoutes.scala    # Routes API REST Messages

  - `http4s-circe` - Intégration JSON    │   │   ├── WebSocketRoutes.scala  # Routes WebSocket

- **Circe 0.14.6** - Parsing et encodage JSON    │   │   ├── StaticRoutes.scala     # Serveur de fichiers statiques

- **SBT 1.9.8** - Build tool    │   │   └── CorsMiddleware.scala   # Middleware CORS

    │   └── service/

### Frontend    │       └── DictionaryService.scala # Validation mots français

    └── resources/

- **HTML5 + CSS3** - Interface utilisateur        ├── tusmo.html                 # Interface du jeu

- **JavaScript (Vanilla)** - Logique client        └── french-words.txt           # 336k mots français

- **Fetch API** - Requêtes HTTP```

- **WebSocket API** - Communication temps réel

---

### Données

## 🧩 Composants principaux

- **Dictionnaire Gutenberg** - 336 527 mots français

- **Stockage en mémoire** - Aucune base de données via `Ref[IO, Map[...]]`### 1️⃣ **Main.scala** - Point d'entrée



---**Rôle** : Initialise et orchestre tous les composants de l'application.



## 📂 Structure du projet**Responsabilités** :



```- Créer les **références d'état** (`Ref`) pour stocker les données en mémoire

scala-http-kc/  - `gamesRef` : Map des parties Tusmo en cours

├── build.sbt                          # Configuration SBT et dépendances  - `messagesRef` : Map des messages WebSocket

├── README.md                          # Ce fichier  - `idRef` : Compteur d'IDs pour les messages

├── .gitignore                         # Fichiers ignorés par Git- Configurer le **serveur Ember** sur le port 8080

├── project/- Assembler le **routeur** principal avec toutes les routes

│   └── build.properties               # Version de SBT- Afficher les informations de démarrage (nombre de mots dans le dictionnaire)

└── src/main/

    ├── scala/com/barbedet/kc/```scala

    │   ├── Main.scala                 # Point d'entrée de l'application// Initialisation des états en mémoire

    │   ├── model/gamesRef    <- Ref.of[IO, Map[String, GameState]](Map.empty)

    │   │   ├── Message.scala          # Modèles pour les messages WebSocketmessagesRef <- Ref.of[IO, Map[Long, Message]](Map.empty)

    │   │   └── Game.scala             # Modèles du jeu TusmoidRef       <- Ref.of[IO, Long](0L)

    │   ├── http/

    │   │   ├── TusmoRoutes.scala      # Routes API REST Tusmo// Assemblage du routeur

    │   │   ├── MessageRoutes.scala    # Routes API REST MessagesRouter(

    │   │   ├── WebSocketRoutes.scala  # Routes WebSocket  "/api/messages" -> apiRoutes,     // API Messages

    │   │   ├── StaticRoutes.scala     # Serveur de fichiers statiques  "/api/game"     -> tusmoRoutes,   // API Tusmo

    │   │   └── CorsMiddleware.scala   # Middleware CORS  "/ws"           -> wsRoutes,      // WebSocket

    │   └── service/  "/"             -> staticRoutes   // Fichiers statiques

    │       └── DictionaryService.scala # Validation mots français)

    └── resources/```

        ├── tusmo.html                 # Interface du jeu

        └── french-words.txt           # 336k mots français---

````

### 2️⃣ **TusmoRoutes.scala** - API du jeu

---

**Rôle** : Gère toute la logique du jeu Tusmo via une API REST.

## 🧩 Composants principaux

**Endpoints** :

### 1️⃣ **Main.scala** - Point d'entrée

| Méthode | Route | Description |

**Rôle** : Initialise et orchestre tous les composants de l'application.| ------- | --------------------- | ---------------------------------------------- |

| `POST` | `/api/game/new` | Crée une nouvelle partie avec un mot aléatoire |

**Responsabilités** :| `GET` | `/api/game/:id` | Récupère l'état d'une partie existante |

| `POST` | `/api/game/:id/guess` | Soumet une tentative et obtient l'évaluation |

- Créer les **références d'état** (`Ref`) pour stocker les données en mémoire| `GET` | `/api/game/stats` | Récupère les statistiques du joueur |

  - `gamesRef` : Map des parties Tusmo en cours

  - `statsRef` : Statistiques du joueur (série actuelle, meilleur score)**Logique métier** :

  - `messagesRef` : Map des messages WebSocket

  - `idRef` : Compteur d'IDs pour les messages- **Sélection aléatoire** d'un mot français de 6 lettres

- Configurer le **serveur Ember** sur le port 8080- **Validation** des tentatives (longueur, première lettre, existence dans le dictionnaire)

- Assembler le **routeur** principal avec toutes les routes- **Évaluation** des lettres (Correct ✅ / Présent 🟡 / Absent ❌)

- Afficher les informations de démarrage (nombre de mots dans le dictionnaire)- **Gestion de l'état** (victoire, défaite après 6 tentatives)

- **Révélation du mot** en cas de défaite

````scala

// Initialisation des états en mémoire**Exemple d'évaluation** :

gamesRef    <- Ref.of[IO, Map[String, GameState]](Map.empty)

statsRef    <- Ref.of[IO, PlayerStats](PlayerStats.empty)```scala

messagesRef <- Ref.of[IO, Map[Long, Message]](Map.empty)def evaluateGuess(guess: String, target: String): Attempt = {

idRef       <- Ref.of[IO, Long](0L)  // Compare chaque lettre et retourne : Correct / Present / Absent

  // MAISON vs JARDIN

// Assemblage du routeur  //   M → Absent

Router(  //   A → Present (A existe dans JARDIN mais pas à cette position)

  "/api/messages" -> apiRoutes,     // API Messages  //   I → Present

  "/api/game"     -> tusmoRoutes,   // API Tusmo  //   S → Absent

  "/ws"           -> wsRoutes,      // WebSocket  //   O → Absent

  "/"             -> staticRoutes   // Fichiers statiques  //   N → Correct (N est à la bonne position)

)}

````

---

### 2️⃣ **TusmoRoutes.scala** - API du jeu### 3️⃣ **WebSocketRoutes.scala** - Communication temps réel

**Rôle** : Gère toute la logique du jeu Tusmo via une API REST.**Rôle** : Fournit un canal de communication **bidirectionnel en temps réel** via WebSocket.

**Endpoints** :**Endpoint** : `WS /ws/echo`

| Méthode | Route | Description |**Fonctionnalités** :

| ------- | --------------------- | ---------------------------------------------- |

| `POST` | `/api/game/new` | Crée une nouvelle partie avec un mot aléatoire |- **Connexion persistante** entre client et serveur

| `GET` | `/api/game/stats` | Récupère les statistiques du joueur |- **Echo des messages** : Renvoie les messages reçus au client

| `GET` | `/api/game/:id` | Récupère l'état d'une partie existante |- **Stockage des messages** dans `messagesRef` pour historique

| `POST` | `/api/game/:id/guess` | Soumet une tentative et obtient l'évaluation |- **Ping automatique** (désactivé actuellement pour éviter les messages intempestifs)

**Logique métier** :**Cas d'usage** (potentiels) :

- **Sélection aléatoire** d'un mot français (longueur variable : 5-7 lettres)- 💬 Chat en direct entre joueurs

- **Validation** des tentatives (longueur, première lettre, existence dans le dictionnaire)- 📊 Notifications de parties en cours

- **Évaluation** des lettres (Correct ✅ / Présent 🟡 / Absent ❌)- 🔔 Alertes de nouveaux records

- **Gestion de l'état** (victoire, défaite après 6 tentatives)- 👥 Liste des joueurs connectés

- **Mise à jour des statistiques** (série de victoires)

- **Révélation du mot** en cas de défaite**Architecture WebSocket** :

**Exemple d'évaluation** :```

Client Server

````scala │                               │

def evaluateGuess(guess: String, target: String): Attempt = {  │─────── WS Connect ───────────▶│  Établissement connexion

  // Compare chaque lettre et retourne : Correct / Present / Absent  │                               │

  // MAISON vs MOUTON  │◀──── Connection OK ───────────│

  //   M → Correct (même position)  │                               │

  //   A → Absent  │─────── { text: "Hello" } ────▶│  Client envoie message

  //   I → Absent  │                               │

  //   S → Absent  │                               │  Serveur traite et stocke

  //   O → Present (existe mais pas à cette position)  │                               │

  //   N → Correct (même position)  │◀──── { id: 1, text: "..." }──│  Serveur répond avec écho

}  │                               │

```  │─────── Close connection ─────▶│  Fin de session

  │                               │

---```



### 3️⃣ **DictionaryService.scala** - Validation des mots**Code simplifié** :



**Rôle** : Valide si un mot existe en français en utilisant le dictionnaire Gutenberg.```scala

def routes(ws: WebSocketBuilder2[IO]): HttpRoutes[IO] = {

**Dictionnaire** :  case GET -> Root / "echo" =>

    val receive: Pipe[IO, WebSocketFrame, Unit] =

- **336 527 mots français** extraits du projet Gutenberg      _.collect { case Text(msg, _) => msg }

- Chargé **une seule fois** au démarrage (lazy val)       .evalMap { text =>

- Stocké dans un `Set[String]` pour recherche O(1)         // Traiter et stocker le message

         storeMessage(text)

**Méthode principale** :       }



```scala    val send: Stream[IO, WebSocketFrame] =

def isValidFrenchWord(word: String): IO[Boolean] = {      // Envoyer des messages au client

  val normalizedWord = word.toLowerCase.trim      messagesStream.map(msg => Text(msg.asJson.noSpaces))

  val isValid = frenchWords.contains(normalizedWord)

  IO.println(s"[Dict] '$word': ${if (isValid) "✓" else "✗"}").as(isValid)    ws.build(send, receive)

}}

````

**Avantages** :**État actuel** : La WebSocket est fonctionnelle mais utilisée comme démo. Le jeu Tusmo utilise principalement l'API REST.

- ✅ Validation **instantanée** (pas de latence réseau)---

- ✅ **100% fiable** (pas de dépendance externe)

- ✅ Couvre **tous les mots courants** du français### 4️⃣ **DictionaryService.scala** - Validation des mots

---**Rôle** : Valide si un mot existe en français en utilisant le dictionnaire Gutenberg.

### 4️⃣ **WebSocketRoutes.scala** - Communication temps réel**Dictionnaire** :

**Rôle** : Fournit un canal de communication **bidirectionnel en temps réel** via WebSocket.- **336 527 mots français** extraits du projet Gutenberg

- Chargé **une seule fois** au démarrage (lazy val)

**Endpoint** : `WS /ws/echo`- Stocké dans un `Set[String]` pour recherche O(1)

**Fonctionnalités** :**Méthode principale** :

- **Connexion persistante** entre client et serveur```scala

- **Echo des messages** : Renvoie les messages reçus au clientdef isValidFrenchWord(word: String): IO[Boolean] = {

- **Stockage des messages** dans `messagesRef` pour historique val normalizedWord = word.toLowerCase.trim

  val isValid = frenchWords.contains(normalizedWord)

--- IO.println(s"[Dict] '$word': ${if (isValid) "✓" else "✗"}").as(isValid)

}

## 🌐 API REST```

### API Tusmo - Jeu de mots**Avantages** :

#### Créer une partie- ✅ Validation **instantanée** (pas de latence réseau)

- ✅ **100% fiable** (pas de dépendance externe)

```http- ✅ Couvre **tous les mots courants** du français

POST /api/game/new

Content-Type: application/json---



Response 201:### 5️⃣ **MessageRoutes.scala** - API Messages

{

  "gameId": "b9a4e483-a904-4ffd-8858-502304ad43c3",**Rôle** : API REST complémentaire pour gérer les messages (historique WebSocket).

  "firstLetter": "M",

  "wordLength": 6,**Endpoints** :

  "attempts": [],

  "maxAttempts": 6,- `GET /api/messages` - Liste tous les messages

  "isWon": false,- `GET /api/messages/:id` - Récupère un message spécifique

  "isLost": false,- `POST /api/messages` - Crée un nouveau message

  "debugWord": "MOUTON"

}---

```

### 6️⃣ **StaticRoutes.scala** - Serveur de fichiers

#### Soumettre une tentative

**Rôle** : Sert les fichiers statiques (HTML, CSS, JS) depuis `src/main/resources/`.

````http

POST /api/game/:id/guess**Routes** :

Content-Type: application/json

- `GET /` → Redirige vers `/tusmo.html`

{- `GET /tusmo.html` → Interface du jeu

  "word": "MAISON"

}---



Response 200:### 7️⃣ **CorsMiddleware.scala** - Gestion CORS

{

  "gameId": "b9a4e483-a904-4ffd-8858-502304ad43c3",**Rôle** : Ajoute les headers CORS pour permettre les requêtes cross-origin.

  "firstLetter": "M",

  "wordLength": 6,**Headers** :

  "attempts": [

    {- `Access-Control-Allow-Origin: *`

      "word": "MAISON",- `Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS`

      "letters": [- `Access-Control-Allow-Headers: Content-Type, Authorization`

        { "char": "M", "state": "correct" },

        { "char": "A", "state": "absent" },---

        { "char": "I", "state": "absent" },

        { "char": "S", "state": "absent" },## 🌐 API REST

        { "char": "O", "state": "present" },

        { "char": "N", "state": "correct" }### API Tusmo - Jeu de mots

      ]

    }#### Créer une partie

  ],

  "maxAttempts": 6,```http

  "isWon": false,POST /api/game/new

  "isLost": falseContent-Type: application/json

}

```Response 201:

{

#### Récupérer une partie  "gameId": "b9a4e483-a904-4ffd-8858-502304ad43c3",

  "firstLetter": "M",

```http  "attempts": [],

GET /api/game/:id  "maxAttempts": 6,

  "isWon": false,

Response 200:  "isLost": false

{}

  "firstLetter": "M",```

  "wordLength": 6,

  "attempts": [...],#### Soumettre une tentative

  "maxAttempts": 6,

  "isWon": false,```http

  "isLost": false,POST /api/game/:id/guess

  "revealedWord": "MOUTON"  // Seulement si isLost = trueContent-Type: application/json

}

```{

  "word": "MAISON"

#### Obtenir les statistiques}



```httpResponse 200:

GET /api/game/stats{

  "firstLetter": "M",

Response 200:  "attempts": [

{    {

  "currentStreak": 5,      "word": "MAISON",

  "bestStreak": 12      "letters": [

}        { "char": "M", "state": "CORRECT" },

```        { "char": "A", "state": "PRESENT" },

        { "char": "I", "state": "ABSENT" },

---        { "char": "S", "state": "ABSENT" },

        { "char": "O", "state": "PRESENT" },

## 🔌 WebSocket        { "char": "N", "state": "CORRECT" }

      ]

### Connexion    }

  ],

**Endpoint** : `ws://localhost:8080/ws/echo`  "maxAttempts": 6,

  "isWon": false,

### Utilisation avec JavaScript  "isLost": false

}

```javascript```

const ws = new WebSocket("ws://localhost:8080/ws/echo");

#### Récupérer une partie

ws.onopen = () => {

  console.log("✅ WebSocket connecté");```http

};GET /api/game/:id



ws.onmessage = (event) => {Response 200:

  const message = JSON.parse(event.data);{

  console.log("📨 Message reçu:", message);  "firstLetter": "M",

};  "attempts": [...],

  "maxAttempts": 6,

// Envoyer un message  "isWon": false,

ws.send(JSON.stringify({ text: "Hello from client!" }));  "isLost": false,

```  "revealedWord": "MOUTON"  // Seulement si isLost = true

}

---```



## 📖 Validation du dictionnaire#### Obtenir les statistiques



Le projet utilise le **dictionnaire français Gutenberg** contenant **336 527 mots**.```http

GET /api/game/stats

### Source

Response 200:

- **Projet** : [OpenLexicon](https://github.com/chrplr/openlexicon){

- **Fichier** : `liste.de.mots.francais.frgut.txt`  "currentStreak": 5,

- **Licence** : Open source  "bestStreak": 12

}

### Chargement```



```scala### API Messages - Historique

private lazy val frenchWords: Set[String] = {

  val source = Source.fromResource("french-words.txt")#### GET /api/messages

  try {

    source.getLines().map(_.toLowerCase.trim).toSetRetourne la liste complète des messages en mémoire.

  } finally {

    source.close()```http

  }GET /api/messages

}

```Response 200:

[

---  { "id": 1, "content": "Hello" },

  { "id": 2, "content": "World" }

## 🧪 Tests et développement]

````

### Tests API REST avec curl

#### GET /api/messages/:id

````bash

# Créer une nouvelle partieRetourne un message par son identifiant.

curl -X POST http://localhost:8080/api/game/new

```http

# Soumettre un motGET /api/messages/1

curl -X POST http://localhost:8080/api/game/<GAME_ID>/guess \

  -H "Content-Type: application/json" \Response 200:

  -d '{"word":"MAISON"}'{ "id": 1, "content": "Hello" }



# Obtenir les statistiquesResponse 404 (si inexistant):

curl http://localhost:8080/api/game/stats{ "error": "Message 42 not found" }

````

### Test WebSocket#### POST /api/messages

````bashCrée un nouveau message.

# Installation de wscat

npm install -g wscat```http

POST /api/messages

# Connexion au WebSocketContent-Type: application/json

wscat -c ws://localhost:8080/ws/echo

{

# Envoyer un message  "content": "Mon premier message"

> {"text":"Test message","sender":"Player1"}}

````

Response 201:

---{

"id": 1,

## 🏆 Fonctionnalités "content": "Mon premier message"

}

### Jeu Tusmo```

- ✅ Jeu Tusmo complet avec mots de longueur variable (5-7 lettres)L'identifiant est généré en mémoire (compteur monotone).

- ✅ 6 tentatives maximum par partie

- ✅ Validation en temps réel avec 336k mots français---

- ✅ Interface web responsive et moderne

- ✅ Indice de la première lettre## 🔌 WebSocket

- ✅ Révélation du mot en cas de défaite

- ✅ Clavier virtuel AZERTY avec états des touches### Connexion

- ✅ Animations et feedback visuel (confettis, transitions)

- ✅ Système de statistiques (série actuelle, meilleur score)La WebSocket permet une **communication bidirectionnelle temps réel** entre le client et le serveur.

- ✅ Nouvelle partie automatique après victoire (3 secondes)

**Endpoint** : `ws://localhost:8080/ws/echo`

### Architecture technique

### Utilisation avec JavaScript

- ✅ API REST complète (GET, POST)

- ✅ WebSocket pour communication temps réel```javascript

- ✅ Gestion d'état fonctionnelle avec Cats Effectconst ws = new WebSocket("ws://localhost:8080/ws/echo");

- ✅ Serveur de fichiers statiques

- ✅ Middleware CORSws.onopen = () => {

- ✅ Stockage en mémoire sans base de données console.log("✅ WebSocket connecté");

};

---

ws.onmessage = (event) => {

## 📚 Architecture Cats Effect const message = JSON.parse(event.data);

console.log("📨 Message reçu:", message);

Le projet utilise le monad `IO` de Cats Effect pour gérer tous les effets de manière pure et fonctionnelle :};

````scalaws.onerror = (error) => {

// Création d'un état partagé thread-safe  console.error("❌ Erreur WebSocket:", error);

gamesRef <- Ref.of[IO, Map[String, GameState]](Map.empty)};



// Modification atomique de l'étatws.onclose = () => {

gamesRef.update(_ + (gameId -> newGame))  console.log("🔌 WebSocket fermé");

};

// Lecture de l'état

gamesRef.get.map(_.get(gameId))// Envoyer un message

ws.send(JSON.stringify({ text: "Hello from client!" }));

// Composition d'effets```

IO.println("Log") *>

saveGame(game) *>### Format des messages

Ok(response.asJson)

```**Client → Serveur** :



**Avantages** :```json

{

- 🔒 **Thread-safe** : Gestion concurrente sécurisée  "text": "Contenu du message",

- 🔄 **Composable** : Chaînage d'effets avec `*>`, `>>`, `flatMap`  "sender": "Nom de l'utilisateur"

- 🎯 **Pure** : Séparation description/exécution}

- 🚀 **Performant** : Optimisations runtime```



---**Serveur → Client** (echo) :



## ✅ Conformité au sujet```json

{

Ce projet répond à toutes les exigences :  "id": 1,

  "text": "Contenu du message",

### ✅ Serveur HTTP avec Cats  "sender": "Nom de l'utilisateur",

  "timestamp": 1700000000000

- Framework : **http4s** + **Cats Effect**}

- Runtime : **IO monad** pour tous les effets```



### ✅ API REST (GET et POST)### Test avec wscat



- **GET** :```bash

  - `/api/messages` - Liste des messages# Installation

  - `/api/messages/:id` - Message spécifiquenpm install -g wscat

  - `/api/game/:id` - État d'une partie

  - `/api/game/stats` - Statistiques du joueur# Connexion au WebSocket

- **POST** :wscat -c ws://localhost:8080/ws/echo

  - `/api/messages` - Créer un message

  - `/api/game/new` - Nouvelle partie# Envoyer un message

  - `/api/game/:id/guess` - Soumettre une tentative> {"text":"Hello","sender":"Player1"}



### ✅ Stockage en mémoire (sans BDD)# Le serveur répond avec l'écho

< {"id":1,"text":"Hello","sender":"Player1"}

- `Ref[IO, Map[String, GameState]]` pour les parties Tusmo```

- `Ref[IO, PlayerStats]` pour les statistiques

- `Ref[IO, Map[Long, Message]]` pour les messages### Cas d'usage de la WebSocket

- `Ref[IO, Long]` pour le compteur d'IDs

- 💬 **Chat en direct** entre joueurs

### ✅ WebSocket (bonus)- 📊 **Notifications** de parties en cours

- 🔔 **Alertes** de nouveaux records

- Endpoint : `ws://localhost:8080/ws/echo`- 👥 **Liste** des joueurs connectés en temps réel

- Communication bidirectionnelle temps réel- 🎮 **Synchronisation** multi-joueurs

- Stockage des messages échangés

---

### ✅ Documentation

## 📖 Validation du dictionnaire

- README complet avec règles du jeu en premier

- Architecture détailléeLe projet utilise le **dictionnaire français Gutenberg** contenant **336 527 mots**.

- Diagrammes et exemples de code

- Guide d'installation et tests### Source



---- **Projet** : [OpenLexicon](https://github.com/chrplr/openlexicon)

- **Fichier** : `liste.de.mots.francais.frgut.txt`

## 🤝 Contribution- **Licence** : Open source



Les contributions sont les bienvenues !### Chargement



1. Fork le projet```scala

2. Créer une branche (`git checkout -b feature/amélioration`)private lazy val frenchWords: Set[String] = {

3. Commit les changements (`git commit -am 'Ajout fonctionnalité'`)  val source = Source.fromResource("french-words.txt")

4. Push vers la branche (`git push origin feature/amélioration`)  try {

5. Créer une Pull Request    source.getLines().map(_.toLowerCase.trim).toSet

  } finally {

---    source.close()

  }

## 📄 Licence}

````

Ce projet est open source et disponible sous licence MIT.

Le dictionnaire est chargé **une seule fois** au premier appel (lazy) et reste en mémoire pour toute la durée de vie de l'application.

---

---

## 🙏 Remerciements

## 🚀 Installation et lancement

- **http4s** - Framework HTTP fonctionnel

- **Cats Effect** - Gestion d'effets pure### Prérequis

- **Projet Gutenberg** - Dictionnaire français

- **OpenLexicon** - Base de données linguistique- **Java 11+** (recommandé : Java 17)

- **SBT 1.9.8+**

---

### Installation

**Développé avec ❤️ en Scala fonctionnel**

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
