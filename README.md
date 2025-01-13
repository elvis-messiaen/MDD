# MDD

### MDD_BACK
## Description

        MDD est une application construite avec Spring Boot qui utilise Spring Security pour la gestion de la sécurité. 
        L'application utilise JWT pour l'authentification.
        L'application permet de gérer des utilisateurs.L'enregistrement et la connexion.
        L'application permet de gérer des thèmes, des articles, et des commentaires.

### Propriétés Spring

    Installation
    Pré-requis
    Java 17
    Docker
    Maven
    Installation des Dépendances

## Installation de docker sur votre machine :
https://docs.docker.com/get-docker/

# Installation de Docker

## Windows

1. Téléchargez l'installateur de Docker Desktop depuis [Docker Hub](https://www.docker.com/products/docker-desktop).
2. Suivez les instructions de l'installateur pour compléter l'installation.
3. Redémarrez votre ordinateur si nécessaire.
4. Lancez Docker Desktop depuis le menu Démarrer.

## MacOS

1. Téléchargez Docker Desktop pour Mac depuis [Docker Hub](https://www.docker.com/products/docker-desktop).
2. Ouvrez le fichier téléchargé et suivez les instructions pour déplacer Docker dans le dossier Applications.
3. Lancez Docker depuis le dossier Applications.
4. Suivez les instructions à l'écran pour terminer la configuration initiale.

## Linux

1. Suivez les instructions spécifiques à votre distribution Linux sur [la documentation Docker](https://docs.docker.com/engine/install/).

## Vérification

    Pour vérifier que Docker est correctement installé, ouvrez un terminal et tapez la commande suivante :
    docker --version

## commande docker pour lancer l'application

    docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=rootpassword -e MYSQL_DATABASE=MDD -e MYSQL_USER=elvis -e MYSQL_PASSWORD=123456 -p 3306:3306 -d mysql:latest

### MDD_FRONT

    cd mdd-front
    npm install
    ng serve



