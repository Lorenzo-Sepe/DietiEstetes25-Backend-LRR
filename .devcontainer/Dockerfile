# Usa un'immagine base di Ubuntu con OpenJDK 21
FROM ubuntu:22.04

# Installa OpenJDK 21, Maven e PostgreSQL
RUN apt-get update && apt-get install -y openjdk-21-jdk maven postgresql-client


# Imposta la directory di lavoro
WORKDIR /workspace

# Copia tutto il contenuto del progetto nella directory di lavoro
COPY . .

# Esponi la porta 8081
EXPOSE 8081

# Comando per avviare l'applicazione
CMD ["mvn", "spring-boot:run"]

#COMANDI CHE STO USANDO PER FAR FUNZIONARE IL TUTTO
# mvn clean install -DskipTests
#