
#!/bin/bash
# Script de démarrage BackOffice

# 1. Variables d'environnement DB
export DB_URL="jdbc:mysql://localhost:3306/hotel"
export DB_USER="root"
export DB_PASSWORD="root"

# 2. Chemin Tomcat (modifiable via variable d'environnement TOMCAT_HOME)
TOMCAT_HOME="${TOMCAT_HOME:-/Users/oceanechristodoulou/Desktop/tout/S3/apache-tomcat-10.1.28}"
TOMCAT_WEBAPPS="$TOMCAT_HOME/webapps"

if [ ! -f "$TOMCAT_HOME/bin/startup.sh" ]; then
  echo "❌ Tomcat introuvable: $TOMCAT_HOME"
  echo "👉 Définis TOMCAT_HOME puis relance, ex:"
  echo "   export TOMCAT_HOME=/chemin/vers/apache-tomcat-10.1.28"
  exit 1
fi

# 3. Build WAR
echo "📦 Building BackOffice..."
mvn clean package

# 4. Copier le WAR dans Tomcat
echo "🚀 Déploiement vers Tomcat..."
cp target/backofficehotel.war "$TOMCAT_WEBAPPS/"

echo "✅ WAR déployé. Tomcat va le déployer automatiquement."
echo "📍 URL: http://localhost:8080/backofficehotel"
/home/carole/Documents/S6/MrNainaS6/Hotel/Hotel/BackOfficeHotel/src/main/java/com/hotel/backoffice/AppInitializer.java