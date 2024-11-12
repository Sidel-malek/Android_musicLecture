# MusicLecture

**Description**  
MusicLecture est une application Android qui permet aux utilisateurs de lire des chansons stockées sur leur téléphone. Elle utilise un service Foreground pour garantir une lecture continue de la musique, même en arrière-plan. L'application permet de contrôler la lecture, de gérer une liste de favoris, et de stocker ces favoris dans une base de données locale.

**Objectif**  
Manipulation des Services + BDD + Programmation concurrente

## Fonctionnalités

1. **Lecture de musique avec service Foreground**  
   - Lecture continue même en arrière-plan.  
   - Accès asynchrone aux fichiers MP3 pour une performance fluide.

2. **Contrôle via notification**  
   - Changer de chanson, mettre en pause ou lancer la musique à partir de l'application ou de la notification.

3. **Gestion des favoris**  
   - Ajouter ou supprimer des chansons des favoris.  
   - Sauvegarde des favoris dans une base de données locale.

4. **Consultation des favoris**  
   - Accéder aux chansons favorites dans une autre activité et les lire directement.

## Contribuer

Si vous souhaitez contribuer, vous pouvez forker ce dépôt, créer une branche, y apporter vos modifications, puis envoyer une pull request.

### Changements apportés  
- **Détection des secousses** : Ajout d'une fonctionnalité pour détecter les secousses du téléphone et changer de chanson en secouant l'appareil.
