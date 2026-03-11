@echo off
REM Script de démarrage BackOffice

REM 1. Variables d'environnement DB
set DB_URL=jdbc:mysql://localhost:3306/hotel
set DB_USER=root
set DB_PASSWORD=root

REM 2. Chemin Tomcat (modifiable si TOMCAT_HOME existe déjà)
if "%TOMCAT_HOME%"=="" (
    set TOMCAT_HOME=E:\ITU\A_Installer\apache-tomcat-10.1.28
)

set TOMCAT_WEBAPPS=%TOMCAT_HOME%\webapps

REM Vérifier si Tomcat existe
if not exist "%TOMCAT_HOME%\bin\startup.bat" (
    echo ❌ Tomcat introuvable: %TOMCAT_HOME%
    echo 👉 Definis TOMCAT_HOME puis relance, ex:
    echo    set TOMCAT_HOME=C:\chemin\vers\apache-tomcat-10.1.28
    pause
    exit /b
)

REM 3. Build WAR
echo 📦 Building BackOffice...
mvn clean package

REM 4. Copier le WAR dans Tomcat
echo 🚀 Deploiement vers Tomcat...
copy target\backofficehotel.war "%TOMCAT_WEBAPPS%\" /Y

REM 5. Lancer Tomcat (optionnel)
echo ▶ Demarrage Tomcat...
call "%TOMCAT_HOME%\bin\startup.bat"

echo.
echo ✅ WAR deploye. Tomcat va le deployer automatiquement.
echo 📍 URL: http://localhost:9090/backofficehotel
pause