README - Projet Jeu de Dames International
Présentation
Ce projet est une implémentation complète du jeu de dames international sur un plateau de 10x10 cases, développée en Java. L'application dispose d'une interface graphique réalisée avec Swing, permettant à deux joueurs de s'affronter localement. Le moteur de jeu respecte les règles officielles, notamment la prise obligatoire et les déplacements spécifiques des dames.

Fonctionnalités principales
Plateau de 10x10 cases conforme aux standards internationaux.

Gestion du tour par tour entre les joueurs Noir et Blanc.

Règles de déplacement des pions : diagonale avant pour les mouvements simples.

Règles de déplacement des dames : déplacement de plusieurs cases dans toutes les directions.

Système de prise obligatoire.

Gestion des rafales : possibilité d'enchaîner plusieurs prises avec la même pièce.

Promotion automatique d'un pion en dame lorsqu'il atteint la dernière rangée adverse.

Historique des coups affiché en temps réel dans l'interface.

Détection automatique de la fin de partie lorsqu'un joueur n'a plus de pièces ou de mouvements possibles.

Architecture du projet
Le projet est structuré de manière orientée objet avec les classes suivantes :

Logique et Moteur (Modèle)
Jeu.java : Classe de contrôle principale faisant le lien entre le plateau et l'interface.

Plateau.java : Gère la logique métier, la validation des coups, les prises et les changements de tours.

Case.java : Représente une cellule du plateau et la pièce associée.

Entités (Données)
Piece.java : Classe abstraite définissant la structure de base d'une pièce.

Pion.java : Représente un pion standard.

Dame.java : Représente une dame.

Joueur.java : Stocke le nom et la couleur des participants.

Interface Graphique (Vue)
FenetreJeu.java : Gère l'affichage visuel du damier et les interactions utilisateur.

Main.java : Point d'entrée pour lancer l'application.

Règles implémentées
Mouvement simple : Un pion se déplace d'une case en diagonale vers l'avant.

Prise (Saut) : Un pion peut capturer une pièce adverse en sautant par-dessus vers une case vide, en avant ou en arrière.

Priorité de saut : Si une prise est possible, elle est obligatoire.

Dames : Elles se déplacent sur n'importe quelle distance en diagonale tant que le chemin est dégagé.

Promotion : Un pion devient une dame lorsqu'il atteint la ligne de fond adverse (ligne 0 pour les Blancs, ligne 9 pour les Noirs).

Installation et Lancement
Pour compiler et exécuter le projet, utilisez les commandes suivantes dans votre terminal :

Compilation :
javac *.java

Lancement :
java Main

Utilisation
Sélection : Cliquez sur une pièce de votre couleur pour la sélectionner (une bordure rouge apparaît).

Mouvement : Cliquez sur la case de destination pour valider le coup.

Rafale : Si une autre prise est possible après un saut, la pièce reste sélectionnée pour continuer la séquence.

Historique : Consultez la zone de texte latérale pour voir la liste des coups joués.
