# 🏍️ MotoMap - Sistema de Gestão de Pátios

### 👨‍💻 Integrantes

* **Nome:** Caike Dametto **RM:** 558614
* **Nome:** Guilherme Janunzzi **RM:** 558461

## Sobre o Projeto

O **MotoMap** é uma aplicação web full-stack desenvolvida como solução para o Challenge de Java Advanced da FIAP. O projeto simula um sistema para a empresa Mottu, com foco na gestão de suas motocicletas, filiais e pátios.

A aplicação permite o controle de acesso baseado em perfis de usuário, o gerenciamento completo (CRUD) das principais entidades do sistema e oferece funcionalidades interativas, como um mapa visual do pátio para alocação e liberação de motos em tempo real.

--- 

### ✨ Principais Funcionalidades

* **Controle de Acesso por Perfil:** Sistema de autenticação e autorização com 4 perfis distintos (`ADM_GERAL`, `ADM_LOCAL`, `COL_PATIO`, `COL_MECANICO`), cada um com acesso a funcionalidades específicas.
* **Gerenciamento (CRUD):** Módulos completos para cadastrar, visualizar, editar e excluir/desativar Motos, Filiais, Usuários e Posições do Pátio.
* **Mapa do Pátio Interativo:** Visualização em grade do pátio de uma filial, com células coloridas por área, indicando posições vagas e ocupadas.
* **Alocação e Liberação de Motos:** Funcionalidade para alocar motos disponíveis em vagas vazias e liberar vagas ocupadas diretamente pelo mapa do pátio.
* **Gestão de Problemas:** Fluxo completo para registrar problemas em uma moto, visualizá-los em uma lista de pendências (para mecânicos) e marcá-los como resolvidos.
* **Gestão de Conta Pessoal:** Página para que qualquer usuário logado possa alterar sua própria senha de forma segura.

---

### 🛠️ Tecnologias Utilizadas

* **Backend:** Java 17, Spring Boot
* **Frontend:** Thymeleaf, Bootstrap 5
* **Persistência de Dados:** Spring Data JPA, Hibernate
* **Banco de Dados:** PostgreSQL (gerenciado via Docker Compose)
* **Versionamento de Banco de Dados:** Flyway
* **Segurança:** Spring Security
* **Build Tool:** Gradle (ou Maven)

---

### 🚀 Instalação e Execução

Siga os passos abaixo para executar a aplicação localmente.

#### Pré-requisitos

* [Git](https://git-scm.com/)
* [Java (JDK) 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) ou superior
* [Docker](https://www.docker.com/products/docker-desktop/) e Docker Compose (geralmente já vem com o Docker Desktop)

#### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/GuiJanunzzi/MottoMap-JavaApplication.git
    ```

2.  **Navegue até a pasta do projeto:**
    ```bash
    cd MottoMap-JavaApplication
    ```

3.  **Execute a aplicação:**
    O projeto está configurado com suporte nativo ao Docker Compose. Ao iniciar, um container PostgreSQL será criado e configurado automaticamente. O Flyway cuidará de criar todas as tabelas e popular o banco com dados de teste.

    * **Via IDE (Recomendado):**
        Abra o projeto em sua IDE (IntelliJ, VSCode, etc.) e execute a classe principal `MottoMapJavaApplication.java`.

    * **Via Terminal (Alternativa):**
        ```bash
        # Se você usa Gradle
        ./gradlew bootRun
        
        # Se você usa Maven
        ./mvnw spring-boot:run
        ```

A aplicação estará disponível em `http://localhost:8080`.

---

### 🔑 Acesso à Aplicação

Para testar as diferentes funcionalidades e perfis, utilize os usuários de teste abaixo.

| Usuário (Email)         | Senha      | Papel (Role)   | Principais Acessos                                                              |
| ----------------------- | ---------- | -------------- | ------------------------------------------------------------------------------- |
| `admin@mottomap.com`    | `admin` | `ADM_GERAL`    | Acesso total. Pode gerenciar Usuários, Filiais, Motos e Posições.                 |
| `local@mottomap.com`    | `local123` | `ADM_LOCAL`    | Gerencia Motos e Posições da sua filial. Não pode gerenciar Filiais ou Usuários.  |
| `patio@mottomap.com`    | `patio123` | `COL_PATIO`    | Acessa o "Meu Pátio", aloca motos sem posição e movimenta motos nas vagas.        |
| `mecanico@mottomap.com` | `mec123` | `COL_MECANICO` | Vê a lista de "Motos Pendentes", reporta e resolve problemas.                     |

---

### 🎬 Vídeo de Demonstração

Assista à demonstração da aplicação em funcionamento para ver suas principais funcionalidades:

* [**Assista ao vídeo no YouTube**](https://youtu.be/MVqHAdwQ_g4)

