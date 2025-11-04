# --- Estágio 1: Build (Compilação e Testes) ---
# Usamos uma imagem oficial do Gradle com o JDK 17
# 'AS builder' dá um nome a este estágio para que possamos copiá-lo mais tarde.
FROM gradle:8.5-jdk17-alpine AS builder

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia os arquivos de build primeiro para aproveitar o cache do Docker
# Se esses arquivos não mudarem, o Docker reutiliza a camada de dependências
COPY build.gradle settings.gradle ./
COPY gradle/ ./gradle/

# Baixa todas as dependências do projeto
# --no-daemon é recomendado para ambientes de CI/CD
RUN gradle dependencies --no-daemon

# Copia o resto do código-fonte
COPY src/ ./src/

# Roda o build completo: 
# 1. Compila o código
# 2. Roda os testes (atendendo ao requisito VI: "execução de testes")
# 3. Empacota o .jar (atendendo ao requisito V: "geração do artefato")
RUN gradle build --no-daemon


# --- Estágio 2: Runtime (Execução) ---
# Usamos uma imagem JRE (Java Runtime Environment) mínima e segura.
# 'focal' é baseado no Ubuntu 20.04 LTS. É uma base estável.
FROM eclipse-temurin:17-jre-focal

# Define o diretório de trabalho
WORKDIR /app

# Copia APENAS o .jar compilado do estágio 'builder'
# O Gradle geralmente coloca o jar em build/libs/
# A sintaxe *.jar pega o arquivo sem precisarmos saber o nome exato da versão
COPY --from=builder /app/build/libs/MottoMap-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta padrão que o Spring Boot usa
EXPOSE 8080

# Comando para executar a aplicação quando o contêiner iniciar
# Este é o ponto de entrada da sua aplicação no Azure Web App
# ENTRYPOINT ["java", "-jar", "app.jar"]