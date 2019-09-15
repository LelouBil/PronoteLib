<!--
*** Thanks for checking out this README Template. If you have a suggestion that would
*** make this better, please fork the PronoteLib and create a pull request or simply open
*** an issue with the tag "enhancement".
*** Thanks again! Now go create something AMAZING! :D
***
***
***
*** To avoid retyping too much info. Do a search and replace for the following:
*** LelouBil, PronoteLib, twitter_handle, bilel.medimegh@gmail.com
-->





<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
![GitHub stars](https://img.shields.io/github/stars/LelouBil/PronoteLib?style=social)
![GitHub issues](https://img.shields.io/github/issues/LelouBil/PronoteLib?style=flat-square)
[![GitHub forks](https://img.shields.io/github/forks/LelouBil/PronoteLib)](https://github.com/LelouBil/PronoteLib/network)
![GitHub](https://img.shields.io/github/license/LelouBil/PronoteLib?style=flat-square)
![JitPack](https://img.shields.io/jitpack/v/github/LelouBil/PronoteLib?style=flat-square)
![GitHub last commit](https://img.shields.io/github/last-commit/LelouBil/PronoteLib?style=flat-square)
![Discord](https://img.shields.io/badge/Discord-LelouBil%239388-%237289DA?style=flat-square&logo=discord)
<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/LelouBil/PronoteLib">
    <img src="https://imgur.com/c5iq9U3.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">PronoteLib</h3>

  <p align="center">
    Bibliothèque Java pour communiquer avec un serveur PRONOTE 
    <br />(https://www.index-education.com/fr/logiciel-gestion-vie-scolaire.php)
    <br />
    <br />
    <a href="https://github.com/LelouBil/PronoteLib"><strong>Documentation »</strong></a>
    <br />
    <br />
    <a href="https://github.com/LelouBil/PronoteLib">Exemples</a>
    ·
    <a href="https://github.com/LelouBil/PronoteLib/issues">Signaler un Bug</a>
    ·
    <a href="https://github.com/LelouBil/PronoteLib/issues">Proposer une fonctionnalité</a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
## Sommaire

* [À propos](#À-propos)
* [Démarrage](#démarrage)
  * [Installation](#installation)
* [Usage](#usage)
* [Roadmap](#Roadmap)
* [Contributions](#contributions)
* [Licence](#licence)
* [Contact](#contact)



<!-- ABOUT THE PROJECT -->
## À-propos


PronoteLib est une bibliothèque java qui communique avec un serveur PRONOTE d'index-education  
(https://www.index-education.com/fr/logiciel-gestion-vie-scolaire.php)


<!-- GETTING STARTED -->
## Démarrage

Voici les étapes d'installation de PronoteLib

### Installation

Pour ajouter PronoteLib en dépendance de votre projet il suffit de faire ceci
 
- gradle
```groovy
repositories {
  maven { 
    url 'https://jitpack.io' 
  }
}

dependencies {
  implementation 'com.github.LelouBil:PronoteLib:0.1'
}
```
- maven
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>

	<dependency>
    <groupId>com.github.LelouBil</groupId>
    <artifactId>PronoteLib</artifactId>
    <version>0.1</version>
</dependency>
```



<!-- USAGE EXAMPLES -->
## Usage

L'API n'est pas utilisable en tant que bibliothèque  pour l'instant, mais vous pouvez la tester de cette manière :  
  
``sh
java -jar PronoteLib.jar <url> <identifiant> <motdepasse>
``  

ou url est le lien vers la page élève du serveur PRONOTE
ex : https://demo.index-education.net/pronote/eleve.html

_Pour plus d'exemples (à l'avenir), veuillez consulter la [Documentation](https://github.com/LelouBil/PronoteLib/wiki)_



<!-- ROADMAP -->
## Roadmap

Veuillez aller voir les [issues ouvertes](https://github.com/LelouBil/PronoteLib/issues) pour une liste des fonctionnalités proposées  
et veuillez voir le  [tableau Projet](https://github.com/LelouBil/PronoteLib/projects) pour les fonctionnalités en cours d'implémentation

<!-- CONTRIBUTING -->
## Contribution

Toutes les contributions sont les bienvenues

1. Forkez le Repo  
2. Creez une branche de fonctionnalité (`git checkout -b feature/TrucGenial`)  
3. Commitez vos ajouts (`git commit -m 'Ajout de TrucGenial'`)  
4. Pushez votre branche (`git push origin feature/TrucGenial`)  
5. Ouvrez une Pull Request  


<!-- LICENSE -->
## Licence

Distribué sous la Licence MIT. Voir `LICENSE` pour plus d'informations.



<!-- CONTACT -->
## Contact

LelouBil - Discord: LelouBil#9388 - bilel.medimegh@gmail.com

Lien du projet: [https://github.com/LelouBil/PronoteLib](https://github.com/LelouBil/PronoteLib)




<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
