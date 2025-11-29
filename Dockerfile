# ===== STAGE 1: BUILD =====
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia il codice
COPY . .

# Compila il progetto (salta i test per velocizzare)
RUN mvn clean package -DskipTests


# ===== STAGE 2: RUN =====
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia il JAR compilato
COPY --from=build /app/target/*.jar app.jar

# Espone la porta del tuo backend
EXPOSE 8888

# Avvio del backend sulla porta 8888
CMD ["java", "-jar", "app.jar"]
