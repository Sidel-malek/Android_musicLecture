# MusicLecture

## Description

MusicLecture est une application de lecteur de musique pour Android qui permet aux utilisateurs de lire des chansons enregistrées sur leur téléphone. Elle utilise un **service Foreground** pour garantir une lecture continue de la musique même lorsque l'application est en arrière-plan. L'application permet également à l'utilisateur de passer d'une chanson à une autre, de mettre en pause ou de lancer la musique, ainsi que de gérer une liste de favoris avec stockage dans une base de données interne.

### Objective
  Manipulation des Services + BDD + Programmation concurrente

### Fonctionnalités

#### 1. **Lecture de musique avec un service Foreground**

- L'application utilise un **service Foreground** pour garantir que la musique continue de jouer même lorsque l'application est en arrière-plan.
- L'accès aux fichiers MP3 est effectué de manière **asynchrone** pour garantir une performance fluide, évitant ainsi de bloquer l'interface utilisateur pendant le chargement des fichiers.

#### 2. **Contrôle de la lecture via notification**

- L'utilisateur peut **passer d'une chanson à une autre** soit à partir de l'application, soit directement à partir de la notification.
- La lecture de musique peut être **mise en pause ou lancée** depuis l'application ou la notification persistante qui est créée par le service Foreground.

#### 3. **Gestion des favoris**

- L'utilisateur peut **ajouter des chansons à une liste de favoris** en cliquant sur l'icône appropriée dans l'interface de l'application.
- Les chansons favorites sont **sauvegardées dans une base de données locale** et sont accessibles de manière **asynchrone**.
- L'utilisateur peut également **supprimer des chansons de la liste des favoris**.

#### 4. **Consultation et lecture des favoris**

- L'utilisateur peut accéder à une **autre activité** pour consulter ses chansons favorites.
- Lorsqu'une chanson favorite est sélectionnée, elle est immédiatement **lue** dans l'application.

#### 5. **Détection des secousses du smartphone**

- Le service Foreground est capable de détecter les **secousses du smartphone** et d'effectuer une action (par exemple, changer de chanson) lorsque le téléphone est secoué.
  
## Structure du Code

- **MainActivity.java** : Gère l'interface principale de l'application, permet à l'utilisateur de lancer, mettre en pause, changer de chanson, et gérer les favoris.
- **MusicService.java** : Le service **Foreground** qui gère la lecture de musique en arrière-plan et la détection des secousses.
- **FavoritesActivity.java** : Affiche la liste des chansons favorites et permet de les écouter.
- **DatabaseHelper.java** : Permet d'accéder à la base de données interne pour stocker et récupérer les chansons favorites.
- **MusicPlayer.java** : Contient la logique de lecture de musique, y compris les méthodes pour lire, mettre en pause, et changer de chanson.
- **NotificationHelper.java** : Gère la création et la mise à jour de la notification de lecture de musique.
- **ShakeDetector.java** : Détecte les secousses du téléphone et envoie des commandes pour changer la chanson ou effectuer une autre action.

## Fonctionnement

1. **Lecture de musique** : Lorsque l'utilisateur lance l'application, le service **Foreground** commence à lire la chanson en cours de manière asynchrone. L'utilisateur peut mettre en pause, reprendre ou changer la chanson via l'interface ou la notification.
   
2. **Notifications** :
   - Une notification **persistante** est affichée lorsque la musique est en cours de lecture, permettant de contrôler la lecture de la musique sans revenir à l'application.
   - Les utilisateurs peuvent contrôler la lecture (pause, reprise, changement de chanson) directement à partir de la notification.

3. **Gestion des favoris** :
   - L'utilisateur peut ajouter des chansons à ses **favoris** en cliquant sur l'icône de cœur dans l'interface principale.
   - Les chansons favorites sont stockées dans une base de données interne et peuvent être consultées dans une autre activité, où l'utilisateur peut écouter une chanson favorite en la sélectionnant.

4. **Détection des secousses** :
   - Lorsque le service est actif, le téléphone peut être secoué pour **changer de chanson**. Le service Foreground utilise un capteur d'accéléromètre pour détecter les secousses et effectuer une action (comme passer à la chanson suivante).
   


## Contribuer

Si vous souhaitez contribuer à ce projet, vous pouvez forker le dépôt, créer une branche, effectuer vos modifications, puis envoyer une pull request.

### Changements apportés :
- **Détection des secousses** : Une nouvelle fonctionnalité a été ajoutée pour détecter les secousses du téléphone lorsque la musique est en cours de lecture. Cela permet de créer une interaction supplémentaire avec l'application, par exemple, pour changer de chanson en secouant le téléphone.
