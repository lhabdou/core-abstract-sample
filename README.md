<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Introduction](#introduction)
  - [Modifications apportées à l'architecture du projet](#Modifications apportées à l'architecture du projet)
  - [Utilisation de l'API ](#Utilisation de l'API)
	  - [Prerequis du poste](#Prerequis du poste)
	  - [Cloner le projet en local](#Cloner le projet en local)
	  - [Builder le projet  et l'importer le projet](#Builder le projet  et l'importer le projet)
	  - [Démarrer l'application](#Démarrer l'application)
	  - [Tester les différents service](#Tester les différents service)
	  - [SWAGGER](#SWAGGER)


<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Introduction

Cette api présente une vitrine où l'on peut consulter le stock de chaussures. 
L'API permet de:  
- Consulter un modèle de chaussure
- Consulter le stock
- Mettre à jour le stock
- Retirer un modèle de chaussure
- Ajouter un nouveau modèle ou mettre à jour un modèle existant

# Modifications apportées à l'architecture du projet

Les principales modifications apportées sont:  
-L'ajout de deux modules: data (la partie dao contenant les entités et les repositories), core-stock (la partie métier contenant le service pour gérer les différentes opérations liées aux stocks) 
-L'ajout d'un swagger permettant de tester plus facilement nos différents services.

# Utilisation de l'API 

# Prerequis du poste
Le poste doit avoir: 
 - installation de  jdk14 (à ajouter dans les variables d'environnements)
 - maven ( à ajouter dans les variable d'environnement)

# Cloner le projet en local 
  Pour cloner le projet dans votre poste, ouvrez par exemple Git bash et executer la commande suivante.
  -> git clone https://github.com/lhabdou/core-abstract-sample.git
# Builder le projet  et l'importer le projet
	Executer la commande suivante pour builder le projet (lancement par la même occasion des tests unitaires)
  -> 'mvn clean install'
  
# Démarrer l'application
  Ensuite la commande suivante pour lancer l'application
  -> java -jar controller/target/controller-1.0.jar
  Autrement lancer l'application via Eclipse après avoir importé le projet (maven)
  cliquez droit sur la classe: MultipleCoreImplemSampleApplication.java 
  et run Spring Boot App
 
# Tester les différents service
  Une fois l'application démarrée, ce lien permet d'accéder au swagger: http://localhost:8080/swagger-ui/index.html
  
  # SWAGGER
  
  le swagger expose les différents services de notre API
  
  Dans ShoeController: il s'agit de l'implémentation qui était déjà présente
  
  Dans StockController: c'est là que j'ai apporté les modifications 
------  
 GET
​/shoes​/stock
 Ce service retourne le stock et son statut
------
PATCH
​/shoes​/stock
 Le service permet de mettre à jour le stock
------
GET
​/shoes​/stock​/shoe
 Ce service retourne le model de chaussure saisi
------
PUT
​/shoes​/stock​/shoe
 Ce service permet d'ajouter un nouveau modèle ou mettre à jour un modèle existant
------
DELETE
​/shoes​/stock​/shoe​/remove
 Ce dernier permet de retirer une paire de chaussure du stock
 
 Plus bas dans le swagger dans l'onglet Models, on a les différents modèles de reponses et de données d'entrées ainsi que les contraintes à respecter
  
