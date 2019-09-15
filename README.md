# PronoteLib

alors ca sera une library au final mais pour l'instant y'a un main

pour l'utiliser => java -jar PronoteLib.jar urlPronote NomUtilisateur MotDePasse


léégere documentation sur l'authentification

déja tout commence par la requete de la page d'acceuil
le serveur envoi un session id dans une balise script
ensuite le js utilise ca pour appeler les différents "endpoint"
en fait c'est meme pas des endpoints
mais bon
en gros a chaque requete, le js fais une requete post vers https://(urlponote)/(genreEspace)/(sessionId)/(numOrdre)
genreEspace c'est 3 pour l'espace eleve
ensuite le session id je l'ai dis
et enfin le numéro d'ordre
c'est pour éviter de pouvoir replay une requete plusieur fois
 
le numéro d'ordre
c'est juste un int
qui est incrémenté a chaque requete
mais par 2
parce que le serv l'incrémente de 1

comme ca ils savent si le serveur a bien répondu a la bonne requete
mais du coup

ils l'envoient pas direct en chiffre
ca serait trop simple
ils utilisent donc du chiffrage AES
mais attention
c'est pas fini
en fait ils ont un AES assez bizarre
c'est a dire qu'ils ont une fonction
ObjetCryptageAES.Encrypter

la fonction fait
clé => string
iv (initialisation Vector) => byte[]
et chaine => string
ca c'est les parametres
ensuite dedans
clé = md5(clé)
déja
ensuite iv = md5(iv)
et ensuite ils chiffrent la chaine avec ca

ensuite
ils renvoient le résultat mais en hex
et voila ta chaine chiffrée
mais le truc
c'est que pour la premiere requete
le serv et le client se sont pas mis d'accord sur une clé
donc
la premiere clé utilisé c'est ca :
(rien du tout)
xd
et le premier iv c'est 0
donc ce qui veut dire que le premiere numeroOrdre c'est toujours le meme
et c'est le cas
en plus leur api a un format bizarre
c'est a dire que ils envoient et recoive du json a chaque fois
du style
```javascript
{
donneesSec:
    {
        donnees:
            {
                Uuid: "machin machin"
            }
    }
nom: "FonctionParametres"
session: 45654654
numeroOrdre: "a5D5eD5as6d5F"
}
```
du coup le premier c'est toujours le meme
au lieu de faire simple
du style
/api/emploiDuTemps
ils font toujours au meme endpoint
et la demande est dans le json
donc la par exemple c'est FonctionParametres
c'est le premiere truc qui est fait
pour mettre le serveur au courant de l'iv utilisé
du coup
le js génere un iv random
de 16 byte du coup
et la attend 2 sec
je me souviens plus trop comment in font pour l'uuid
je vérifie dans mon code
ah oui voila
donc le js prend in iv random
et utilise 2 autres parametres fournit dans la requete initiale
MR & ER

en gros c'est le modulo et l'exposant du la clé publique rsa du serveur
avec ca ils chiffrent l'iv
et encodent le tout en base64
et c'est ca l'id du coup
donc
apres la premiere requete
la la page attend
que tu te connecte
 

quand tu te connecte
le js  envoi déja une requete quasiment sans rien
requete de type "Identification"
et le serv lui renvoi plusieurs trucs utilse
alea => une chaine random
challenge => une chaine en hex préparée
avec ca le js vas faire bcp de trucs
déja
il vas hasher ton mdp
en utilisant l'alea
c du sha256
mais le truc c'est que l'alea c'est par serveur
donc c'est pas aléatoire a chaque requete
c'est aléatoire entre chaque etablissement
donc déja ca c'est bete

donc apres avoir hashé ton mdp en utilisant l'aléa pas aléatoire
le js construit un string du type
username + mdpHashé
juste a la suite
et
le nom d'utilisateur est en minuscule
donc c'est pas case sensitive
je précise parce que j'ai bloqué pendant 3 jours a cause de ca 
 
et du coup cette chaine ca vas etre la clé aes pour la suite
dooonc
leur truc de challenge est assez compliqué xd
en fait
ils font
decryptaes(Hex.decodeHex(challenge),key.getBytes(),iv)
la je t'ai copié collé un truc de mon code
ils décryptent le challenge mais décodé en byte
avec la clé que je t'ai dis
et l'iv random choisi tt a l'heure
 
mais le truc c'est que apres
pour décrypter aussi ils font le md5 des trucs xd
donc en fait l'iv utilisé c'est le md5  de l'iv
pareil pour la clé
du coup apres ils ont un résultat en byte
que je vais appeler
result
ensuite
(parce que sinon c'est pas drole)
ils enlevent de l'aléatoire de ce result
mais
l'aléatoire
c'est un caractere sur 2
genre
msoftf qdzed gpfassasseg
t'enleve les caracteres impaires
paires pardon
et pouf
t'a un autre résultat
que je vais appeler res2
et Eeeeeeeeeeeeeeeeeeefin
ils chiffrent ce res2 en aes avec la cle de tt a l'heure et l'iv pareil
et encode ca en hex
et ENFIN
ils envoit ca au serveur
dans une requete "Authentification"
mais on a pas encore fini
parce que la le serv vas répondre avec une clé
qui est un truc en hex
le js vas donc déchiffrer ce hex en aes avec la clé d'avant et l'iv pareil
et CECI
sera la clé pour la suite
et voila l'authentification est finie

du cooup on est connecté la
mais
ils ont encore fait un dernier truc pour énerver les gens
c'est a dire que
si tu fais direct une requete
de type
PageEmploiDuTemps
bah le serv vas te dire non
parce que ce qu'il faut faire
c'est faire une requete de type Navigation
avec comme parametres
l'onglet cible
et l'onglet précédent
le serv te repond oui
et Ensuite
tu fais ton truc emploi du temps
parce que c'est pensé pour une interface
donc ils te laissent pas demander l'emploi du temps si t'a pas cliqué sur le truc emploi du temps
du coup le serveur doit savoir t'es sur quel onglet
et
HEUREUSEMENT
les numéros d'onglet sont constant
la page d'acceuil c 7 par exemple
et emploi du temps c 89
et
dernier truc
apres l'authentification

dans chaque requete
y'a un champs en plus
`_Signature_`
oui xd
et dedans
y'a un champ onglet
avec l'onglet actuel
pareil si c'est pas présent ou si c'est pas valide
le serv te repond non
non => cad  la page a éxpiré
