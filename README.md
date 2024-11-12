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


## Contribuer

Si vous souhaitez contribuer à ce projet, vous pouvez forker le dépôt, créer une branche, effectuer vos modifications, puis envoyer une pull request.

### Changements apportés :
- **Détection des secousses** : Une nouvelle fonctionnalité a été ajoutée pour détecter les secousses du téléphone lorsque la musique est en cours de lecture. Cela permet de créer une interaction supplémentaire avec l'application, par exemple, pour changer de chanson en secouant le téléphone.
