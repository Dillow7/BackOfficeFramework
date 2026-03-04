# BackOfficeHotel

## Objectif Sprint 1
- Formulaire d’insertion de réservation
- Script d’insertion d’hôtels

## Framework
Le back-office doit utiliser le JAR/WAR fourni dans:
`/Users/oceanechristodoulou/Desktop/ProjetFramework`

Placez le JAR/WAR dans le projet (par ex. dossier `lib/`) et adaptez la configuration selon le framework.

## Structure proposée
- `sql/schema.sql` : création des tables
- `sql/seed_hotels.sql` : insertion d’hôtels
- `webapp/WEB-INF/jsp/reservation-form.jsp` : formulaire JSP
- `src/main/java/com/hotel/backoffice/*` : DAO + servlet de traitement

## Variables d’environnement DB
- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
