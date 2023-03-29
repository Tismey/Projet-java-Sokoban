# Projet-java-Sokoban


## Bienvenue sur notre Projet java "Parabox-like-game" ##

inclus dans se repertoire sont:
    -Un dossier ClasseEssai contenant des idées de classes qui n'ont pas aboutie, ou réécriture de classe existente afin de simplier la lecture(incomplet);
    -Un dossier FichierNiveauPerso on sont inclus les niveaux que nous avons utilisé pour tester le jeu;
    -Un dossier Interface contenant les interfaces des differentes classes écrite au debut du projet pour aiguillé les différents membres;
    -Les differentes classe utiliser par le jeu (AplicationSokoban.java contient le main : point d'entré du programmme);
    -Un makefile
    -Differente image utiliser pour l'interface graphique.


## COMPILATION ##

    Si vous avez la commande make:
        taper "make" dans le répertoire avec ApplicationSokoban.java
       
    Sans la commande make:
        taper "javac *.java" dans le le répertoire avec ApplicationSokoban.java
        
## LANCEMENT ##

    taper "java ApplicationSokoban <param1> <param2>" pour lancer le programme

    option pour <param1>:
        -"classique"
        -"récursive"
        -"ascii"
        -"graphique"
    
    option pour <param2>
        - vous devez écrire le chemin du niveaux que vous voulez ouvrire (chemin relatif) ex: "FichierNiveauxPerso/nivClassique"

## CONTROLE ##
    interaction terminale:
        "q" : mouvement GAUCHE
        "z" : mouvement HAUT
        "d" : mouvement DROITE
        "s" : mouvement BAS
    interaction interface graphique:
        utiliser les flèches directionelles
        NOTE : ne marche pas avec l'option "récursif" il faut utiliser le terminal (l'interface graphique représente quand meme les changements)

## NOTE/PRESENTATION ##
    Le jeu est dans l'idée, sensée avoir le meme gameplay que le jeu "PATRICK'S PARABOX", et il respecte scrupuleusement les spécifications CASL données sur le moodle;
    le projet est complet malgrès que certaine addition n'ont pu etre implementer : voir le rapport rendu pour plus de détails;

## CREDIT ##
    on travaillé sur se projet:
        Lyes AHFIR
        Timothée M'BASSIDJE
        Yanis BEN KAOUDJT
        Hafid KARABADJI


