Set-Location soap-webservice
mvn clean compile assembly:single
Set-Location ..
docker compose down -v
docker compose up --build
