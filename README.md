# Library Management System

<img src="https://www.skoolbeep.com/blog/wp-content/uploads/2020/12/WHAT-IS-THE-PURPOSE-OF-A-LIBRARY-MANAGEMENT-SYSTEM-min.png">

## Auteurs : BAUDET Quentin, BRONCARD Aurélien, CARDONA Quentin, DAFFARA Riccardo, DUPONT Nicolas & LARMAILLARD-NOIREN Joris


## Description

Ce projet est un système de gestion de bibliothèque implémenté en Scala 3. Il fournit une solution backend uniquement
(sans interface utilisateur) pour gérer les livres, les utilisateurs et les transactions (prêts/réservations) d'une
bibliothèque ou d'une institution similaire. Les principales fonctionnalités sont les suivantes :
- Gestion des livres : ajouter, supprimer, répertorier, rechercher.
- Gestion des utilisateurs : ajouter, supprimer des membres, des bibliothécaires et des enseignants.
- Gestion des transactions : enregistrer les prêts et les réservations avec validation.
- Recommandations : suggérer des livres en fonction de l'historique des utilisateurs.
- Statistiques : générer des résumés sur l'utilisation de la bibliothèque.
- I/O JSON : conserver les livres, les utilisateurs et les transactions à l'aide de `uPickle`.
---

## Prérequis
Les pré-requis nécessaires pour ce projet sont présentés ci-après. Ils couvrent les outils et les versions
indispensables pour assurer le bon fonctionnement et la compréhension du projet.
- Java 11 ou version ultérieure
- sbt 1.6+ (outil de compilation Scala)
- Connexion Internet pour télécharger les dépendances
---

## Technologies et bibliothèques
Ce projet utilise les technologies et bibliothèques suivantes :
- Version de Scala : `Scala 3.7.1`, langage de programmation principal du projet
- Version sbt : `sbt 1.6.x`, outil de gestion de projet et de compilation
- `uPickle (3.1.0)` pour la sérialisation JSON
- `ScalaTest (3.2.17)` framework de tests unitaires
- `ScalaCheck (1.17.0)` bibliothèque pour la programmation fonctionnelle
- `Cats Core (2.10.0)` pour les utilitaires fonctionnels
---

## Project Structure
Ci-après se trouve la structure choisie pour la mise en place de notre projet.
```pgsql
project-root/
├── build.sbt                # Configuration du SBT
├── data/                    # Dossier contenant les données utilisateurs et des livres
├── docs/                    # Dossier contenant la documentation du projet
├── src/
│   ├── main/
│   │   └── scala/
│   │       ├── models/      # Entités de domaine et codecs JSON
│   │       ├── services/    # Logique métier (catalogue, E/S)
│   │       ├── utils/       # Validateurs, assistants d'I/O
│   │       └── main         # Application
│   └── test/
│       ├── scala/
│       │   ├── models/      # Tests unitaires pour les entités
│       │   ├── services/    # Tests du catalogue
│       │   └── utils/       # Tests d'I/O et de validation
│       └── resources/       # JSON fixtures for tests
└── README.md                # Aperçu du projet et instructions
```
---

## Configuration et fonctionnement
1. **Compiler le projet**
    ```bash
   sbt compile
    ```
2. **Exécuter les tests unitaires**
    ```bash
   sbt test
   ```
3. **Exécutez l'application** Modifiez l'objet `main` dans `src/main/scala/main.scala` selon vos besoins, puis :
    ```bash
   sbt run
    ```
   Cela permettra d'initialiser un catalogue type, de montrer comment ajouter des livres/utilisateurs, effectuer des
   transactions et imprimer des statistiques.
---