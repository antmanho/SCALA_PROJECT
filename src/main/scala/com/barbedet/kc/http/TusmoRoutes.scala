package com.barbedet.kc.http

import cats.effect.IO
import cats.effect.kernel.Ref
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

import com.barbedet.kc.model._
import com.barbedet.kc.service.DictionaryService
import java.util.UUID
import scala.util.Random

/**
 * Routes pour le jeu Tusmo
 *   - POST /game/new    : Démarrer une nouvelle partie
 *   - POST /game/:id/guess : Soumettre un mot
 *   - GET  /game/:id    : Obtenir l'état de la partie
 *   - GET  /stats       : Obtenir les statistiques du joueur
 */
class TusmoRoutes(
    gamesRef: Ref[IO, Map[String, GameState]],
    statsRef: Ref[IO, PlayerStats]
) extends Http4sDsl[IO] {

  // Liste de mots français de 6 lettres pour le jeu
  private val wordList = List(
    "MAISON", "JARDIN", "CHEMIN", "SOLEIL", "NUAGES",
    "FLEURS", "BONBON", "PIRATE", "DRAGON", "PRINCE",
    "REINE", "ETOILE", "SAPIN", "LAPIN", "CHATON",
    "PAPIER", "CRAYON", "COUSIN", "VOISIN", "POTION",
    "MUSIQUE", "DANSE", "GUITAR", "VIOLON", "CLOCHE",
    "PLANTE", "ARBRE", "NATURE", "FORÊT", "MOUTON",
    "CHEVAL", "OISEAU", "POISSON", "TIGRE", "SOURIS"
  ).map(_.toUpperCase)
  
  // Dictionnaire étendu de mots français valides (pour validation des essais)
  private val validWords = (wordList ++ List(
    "ABCÈS", "ABSENT", "ACCÈS", "ACCORD", "ACTEUR", "ACTION",
    "AGNEAU", "AIGLES", "AIMANT", "ALARME", "ALBUM", "ALCOOL",
    "ALLER", "ALLEZ", "ALLURE", "ALPAGA", "AMANDE", "AMANTE",
    "AMENER", "AMICAL", "AMITIÉ", "AMOURS", "ANCIEN", "ANIMAL",
    "ANNEAU", "ANNÉE", "ANORAK", "APERCU", "APOTRE", "APPARU",
    "APPELS", "APPORT", "ARGENT", "ARMADA", "ARMÉES", "ARÔME",
    "ARRÊT", "ARRIVE", "ARTÈRE", "ARTISTE", "ASPECT", "ASSAUT",
    "ASSEOIR", "ASSISE", "ATOME", "ATTAQUE", "AUBADE", "AUCUNE",
    "AURORE", "AUTANT", "AUTEUR", "AUTRES", "AVANCE", "AVENIR",
    "AVENUE", "AVEUGLE", "AVIONS", "BAGAGE", "BALCON", "BALLET",
    "BALLON", "BAMBOU", "BANANA", "BANDES", "BATEAU", "BÂTON",
    "BÉBÉ", "BEAUTÉ", "BIJOUX", "BILLET", "BISOUS", "BLAGUE",
    "BLASON", "BLOCUS", "BONDIR", "BONHEUR", "BONJOUR", "BONSOIR",
    "BORDEL", "BORGNE", "BORNE", "BOUCHE", "BOUDER", "BOULES",
    "BOURRÉ", "BOURSE", "BOUTON", "BRAISE", "BRANCHE", "BRAVER",
    "BRÈCHE", "BRETON", "BRIBES", "BRIDGE", "BRISER", "BRONZE",
    "BROUET", "BRUANT", "BRUITS", "BRÛLER", "BRUMES", "BRUTAL",
    "BUDGET", "BUFFET", "BUREAU", "CABANE", "CÂBLES", "CACHÉE",
    "CACHER", "CADEAU", "CADRES", "CAFARD", "CAHIER", "CAISSE",
    "CALICE", "CALMER", "CAMION", "CANADA", "CANARD", "CANCER",
    "CANDEUR", "CANETTE", "CANYON", "CAPITA", "CAPOTE", "CAPTIF",
    "CARAFE", "CARÊME", "CARESSE", "CARNET", "CAROTTE", "CARRES",
    "CARTER", "CARTON", "CASQUE", "CASSER", "CASTOR", "CAVALE",
    "CAVEAU", "CENTRE", "CERCLE", "CERISE", "CESSER", "CHACUN",
    "CHAINE", "CHAISE", "CHÂLE", "CHALET", "CHALEUR", "CHAMBRE",
    "CHANCE", "CHANGER", "CHANTER", "CHAOS", "CHARGE", "CHARME",
    "CHASSE", "CHÂTEAU", "CHATTE", "CHAUME", "CHEMISE", "CHÊNE",
    "CHÉRIE", "CHEVEU", "CHÈVRE", "CHIENS", "CHIFFRE", "CHOEUR",
    "CHOISI", "CHOSES", "CHOUETTE", "CHUTER", "CIBLES", "CIDRE",
    "CIGALE", "CIGARE", "CINÉMA", "CINTRE", "CIRQUE", "CISEAUX",
    "CITRON", "CLAIRE", "CLAMER", "CLASSE", "CLIENT", "CLIMAT",
    "CLOCHER", "CLÔTURE", "CLOUER", "COBAYE", "COCHON", "COEURS",
    "COFFRE", "COGNAC", "COIFFE", "COLÈRE", "COLLET", "COLLINE",
    "COMBAT", "COMBLE", "COMÉDIE", "COMÈTE", "COMMUN", "COMPTE",
    "COMTÉ", "CONFIT", "CONGÉS", "CONQUE", "CONSUL", "CONTER",
    "CONTRE", "COPAIN", "COPIER", "CORAIL", "CORDON", "CORNER",
    "CORPSE", "CORTÈGE", "COSTAUD", "COTEAU", "COTON", "COUCHE",
    "COUDRE", "COULER", "COUPE", "COURIR", "COURSE", "COUSIN",
    "COÛTER", "COUTURE", "COUVER", "CRÂNES", "CRAQUER", "CRAVATE",
    "CRÈCHE", "CRÉDIT", "CRÊPES", "CREUSE", "CREVER", "CRIARD",
    "CRIBLE", "CRIER", "CRIMES", "CRIQUE", "CRISTAL", "CROIRE",
    "CROIX", "CROQUER", "CROSSE", "CROÛTE", "CRUEL", "CUBES",
    "CUEILLIR", "CUISINE", "CUISSE", "CULTE", "CULTIVE", "CUPIDE",
    "CURIEUX", "CURSIF", "CYCLER", "CYLINDRE", "CYPRÈS", "DAGUES",
    "DANGER", "DANSER", "DATEUR", "DAUPHIN", "DÉBAT", "DÉBILE",
    "DEBOUT", "DEBRIS", "DÉBUT", "DÉCÈS", "DÉCHIRE", "DÉCIDE",
    "DÉCIME", "DÉCLIC", "DÉCORE", "DÉCRET", "DÉDALE", "DÉFAUT",
    "DÉFIER", "DÉFILÉ", "DÉGÂT", "DEGRÉ", "DEHORS", "DÉLAI",
    "DÉLICE", "DÉLIT", "DEMAIN", "DÉMÊLE", "DÉMON", "DÉNIER",
    "DÉPART", "DÉPENS", "DÉPIT", "DÉPLIÉ", "DÉPÔT", "DEPUIS",
    "DÉPUTÉ", "DERNIER", "DÉSERT", "DÉSIR", "DESSERT", "DESSIN",
    "DESTIN", "DÉTAIL", "DÉTOUR", "DÉTRUIT", "DETTES", "DEUIL",
    "DEVANT", "DEVENIR", "DEVOIR", "DIABLE", "DIÈTE", "DIÈSE",
    "DIGÉRÉ", "DIGNE", "DIMANCHE", "DINER", "DIRECT", "DISCRET",
    "DISQUE", "DIVERS", "DIVIN", "DIVISE", "DIZAINE", "DOCILE",
    "DOCTEUR", "DOGME", "DOIGTS", "DOMAINE", "DONJON", "DONNER",
    "DORADE", "DORMEUR", "DORMIR", "DOSAGE", "DOUBLE", "DOUCE",
    "DOUCHE", "DOUTER", "DOUZAINE", "DRAGUE", "DRAPEAU", "DRESSER",
    "DRIVER", "DROITE", "DRÔLE", "DRUIDE", "DUCHÉ", "DUPLEX",
    "DURANT", "DURÉE", "DURCIR", "ÉCHECS", "ÉCHELLE", "ÉCHO",
    "ÉCLAIR", "ÉCLATER", "ÉCOLE", "ÉCOSSE", "ÉCOUTE", "ÉCRAN",
    "ÉCRIRE", "ÉCRIT", "ÉDIFICE", "ÉDITER", "EFFET", "EFFORT",
    "ÉGLISE", "ÉLÈVE", "ÉLEVER", "ÉLIRE", "ÉLITE", "EMBARQUE",
    "EMBOUT", "ÉMEUTE", "ÉMIGRÉ", "EMPIRE", "EMPLOI", "EMPORTER",
    "ENCENS", "ENCLOS", "ENCORE", "ENCRE", "ÉNERGIQUE", "ENFANCE",
    "ENFANT", "ENFER", "ENFIN", "ENFLER", "ENFUIT", "ENGAGE",
    "ENGIN", "ÉNORME", "ENQUÊTE", "ENSEIGNE", "ENTIER", "ENTRÉE",
    "ENVERS", "ENVIE", "ENVOI", "ÉPAIS", "ÉPAVES", "ÉPÉE",
    "ÉPERDU", "ÉPICES", "ÉPINE", "ÉPIQUE", "ÉPOQUE", "ÉPOUSE",
    "ÉPREUVE", "ÉQUIPE", "ERRANT", "ERREUR", "ESCAPE", "ESPACE",
    "ESPÈCE", "ESPOIR", "ESPRIT", "ESSAIS", "ESTIME", "ESTOMAC",
    "ÉTABLE", "ÉTAGES", "ÉTALON", "ÉTANGS", "ÉTAPES", "ÉTENDU",
    "ÉTERNEL", "ÉTHIQUE", "ÉTIQUE", "ÉTOFFE", "ÉTRANGE", "ÉTRAVE",
    "ÉTREINTE", "ÉTROIT", "ÉTUDE", "ÉVADER", "ÉVEIL", "ÉVÉNEMENT",
    "ÉVITER", "EXAMEN", "EXCÈS", "EXCUSE", "EXEMPLE", "EXIGER",
    "EXISTE", "EXODE", "EXPERT", "EXPIRE", "EXPOSÉ", "EXPRÈS",
    "FABLES", "FAÇADE", "FÂCHER", "FACILE", "FAÇONS", "FACTEUR",
    "FACTURE", "FAIBLIR", "FAIBLE", "FAILLIR", "FAILLE", "FAMINE",
    "FAMILLE", "FAMINE", "FAUCON", "FAUNE", "FAUTE", "FAUTIF",
    "FAVEUR", "FÉERIE", "FÉLINS", "FEMELLE", "FEMMES", "FENDRE",
    "FENÊTRE", "FERMER", "FERMER", "FÉROCE", "FESSES", "FESTIN",
    "FÊTES", "FEUILLE", "FÉVRIER", "FIABLE", "FIANCÉ", "FIBRES",
    "FICELER", "FICHIER", "FIDÈLE", "FIERTÉ", "FIÈVRE", "FIGURE",
    "FILANT", "FILETS", "FILLES", "FINALE", "FINIR", "FIXÉ"
  )).map(_.toUpperCase).toSet

  implicit private val guessRequestDecoder = jsonOf[IO, GuessRequest]

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {

    // POST /game/new - Nouvelle partie
    case POST -> Root / "new" =>
      for {
        gameId <- IO(UUID.randomUUID().toString)
        word   <- IO(wordList(Random.nextInt(wordList.length)))
        game = GameState(
          gameId = gameId,
          wordToGuess = word,
          attempts = List.empty,
          maxAttempts = 6,
          isWon = false,
          isLost = false,
          firstLetter = word.head
        )
        _ <- gamesRef.update(_ + (gameId -> game))
        resp <- Ok(GameState.toPublic(game).asJson)
      } yield resp

    // GET /stats - Obtenir les statistiques du joueur (AVANT /:gameId pour éviter conflit)
    case GET -> Root / "stats" =>
      for {
        stats <- statsRef.get
        _ <- IO.println(s"[Tusmo] Stats demandées: streak=${stats.currentStreak}, best=${stats.bestStreak}")
        resp <- Ok(stats.asJson)
      } yield resp

    // GET /game/:id - État de la partie
    case GET -> Root / gameId =>
      for {
        games <- gamesRef.get
        resp <- games.get(gameId) match {
          case Some(game) =>
            Ok(GameState.toPublic(game).asJson)
          case None =>
            NotFound(Map("error" -> s"Game $gameId not found").asJson)
        }
      } yield resp

    // POST /game/:id/guess - Soumettre un mot
    case req @ POST -> Root / gameId / "guess" =>
      for {
        guessReq <- req.as[GuessRequest]
        _        <- IO.println(s"[Tusmo] Mot reçu: ${guessReq.word}")
        games    <- gamesRef.get
        resp <- games.get(gameId) match {
          case Some(game) if game.isWon || game.isLost =>
            IO.println(s"[Tusmo] Partie déjà terminée") *>
            BadRequest(Map("error" -> "La partie est déjà terminée").asJson)

          case Some(game) =>
            val word = guessReq.word.toUpperCase.trim
            
            // Validations de base
            IO.println(s"[Tusmo] Mot normalisé: $word pour game $gameId") *>
            (if (word.length != game.wordToGuess.length) {
              IO.println(s"[Tusmo] Erreur longueur: ${word.length} != ${game.wordToGuess.length}") *>
              BadRequest(Map("error" -> s"Le mot doit contenir ${game.wordToGuess.length} lettres").asJson)
            } else if (!word.head.equals(game.firstLetter)) {
              IO.println(s"[Tusmo] Erreur première lettre: ${word.head} != ${game.firstLetter}") *>
              BadRequest(Map("error" -> s"Le mot doit commencer par ${game.firstLetter}").asJson)
            } else if (!word.forall(_.isLetter)) {
              IO.println(s"[Tusmo] Erreur caractères invalides") *>
              BadRequest(Map("error" -> "Le mot ne doit contenir que des lettres").asJson)
            } else {
              // Toujours vérifier dans le dictionnaire local (336k mots Gutenberg)
              IO.println(s"[Tusmo] Vérification du mot '$word' dans dictionnaire local...") *>
              DictionaryService.isValidFrenchWord(word).flatMap { isValid =>
                if (!isValid) {
                  IO.println(s"[Tusmo] Mot '$word' rejeté (non trouvé dans dictionnaire)") *>
                  BadRequest(Map("error" -> "Ce mot n'existe pas en français").asJson)
                } else {
                  IO.println(s"[Tusmo] Mot '$word' validé, évaluation...") *>
                  evaluateAndRespond(word, game, gameId)
                }
              }
            })

          case None =>
            IO.println(s"[Tusmo] Game $gameId introuvable") *>
            NotFound(Map("error" -> s"Partie $gameId introuvable").asJson)
        }
      } yield resp
  }

  /**
   * Évalue le mot et retourne la réponse
   */
  private def evaluateAndRespond(word: String, game: GameState, gameId: String): IO[org.http4s.Response[IO]] = {
    val attempt = evaluateGuess(word, game.wordToGuess)
    val newAttempts = game.attempts :+ attempt
    val isWon = word == game.wordToGuess
    val isLost = !isWon && newAttempts.length >= game.maxAttempts
    
    val updatedGame = game.copy(
      attempts = newAttempts,
      isWon = isWon,
      isLost = isLost
    )
    
    // Mettre à jour les statistiques en fonction du résultat
    val updateStats = if (isWon) {
      statsRef.update(PlayerStats.recordWin) *>
      IO.println(s"[Tusmo] 🔥 Victoire ! Série mise à jour")
    } else if (isLost) {
      statsRef.update(PlayerStats.recordLoss) *>
      IO.println(s"[Tusmo] 💔 Défaite... Série remise à 0")
    } else {
      IO.unit
    }
    
    IO.println(s"[Tusmo] Mot évalué, isWon=$isWon, isLost=$isLost") *>
    updateStats *>
    gamesRef.update(_ + (gameId -> updatedGame)) *>
    Ok(GameState.toPublic(updatedGame).asJson)
  }

  /**
   * Évalue un mot deviné par rapport au mot cible
   */
  private def evaluateGuess(guess: String, target: String): Attempt = {
    val letters = guess.toList.zipWithIndex.map { case (char, index) =>
      val state = if (target(index) == char) {
        LetterState.Correct
      } else if (target.contains(char)) {
        LetterState.Present
      } else {
        LetterState.Absent
      }
      Letter(char, state)
    }
    
    Attempt(guess, letters)
  }
}
