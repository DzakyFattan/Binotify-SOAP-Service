cd soap-webservice
mvn clean compile assembly:single
cd ..
docker compose down -v
docker compose up --build
