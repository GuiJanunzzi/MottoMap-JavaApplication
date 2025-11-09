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

* **Backend:** Java 17, Spring Boot, Spring Data JPA, Spring Security
* **Frontend:** Thymeleaf, Bootstrap 5
* **Banco de Dados:** **Azure SQL Server**
* **Versionamento de BD:** **Flyway** (com scripts T-SQL)
* **Build Tool:** Gradle
* **Cloud & DevOps:**
    * **Azure Web App (para Contêineres):** Hospedagem da aplicação.
    * **Azure DevOps Pipelines:** Orquestração de CI/CD (Modo Clássico).
    * **Azure Container Registry (ACR):** Armazenamento das imagens Docker.
    * **Docker:** Containerização da aplicação.

---

### 🚀 Acesso à Aplicação (Deploy)

A aplicação está implantada no Azure Web Apps e pode ser acessada através do link abaixo:

**URL:** `https://mottomap-app.azurewebsites.net/`

**Atenção:** Para fins de preservação dos créditos da nossa assinatura Azure, que serão necessários para a Global Solution, a aplicação (Azure Web App) encontra-se **desativada** (offline).

Para a avaliação, pedimos a gentileza de contatar o **Guilherme Janunzzi (RM 558461)** via Microsoft Teams. Ele irá habilitar o serviço na nuvem imediatamente para que a aplicação possa ser corrigida.

---

### 🎬 Vídeo de Demonstração

Assista à demonstração da aplicação em funcionamento para ver suas principais funcionalidades:

* [**Assista ao vídeo no YouTube**](https://youtu.be/9npmTT1md1c)

---

### 🔑 Usuários de Teste

Para testar as diferentes funcionalidades e perfis, utilize os usuários de teste abaixo.

| Usuário (Email) | Senha | Papel (Role) | Principais Acessos |
| :--- | :--- | :--- | :--- |
| `admin@mottomap.com` | `admin` | `ADM_GERAL` | Acesso total. Pode gerenciar Usuários, Filiais, Motos e Posições. |
| `local@mottomap.com` | `local123` | `ADM_LOCAL` | Gerencia Motos e Posições da sua filial (Sede SP). |
| `patio@mottomap.com` | `patio123` | `COL_PATIO` | Acessa o "Meu Pátio", aloca motos sem posição e movimenta motos nas vagas. |
| `mecanico@mottomap.com` | `mec123` | `COL_MECANICO` | Vê a lista de "Motos Pendentes" e resolve problemas. |
---

### 🔧 Executando Localmente (Para Desenvolvimento)

Embora a aplicação esteja configurada para a nuvem, é possível executá-la localmente para fins de desenvolvimento.

**Importante:** Esta aplicação **não** utiliza um banco de dados local (como o H2) ou o Docker para desenvolvimento. É **necessário provisionar um Banco de Dados SQL Server no Azure** e configurar o firewall dele para permitir o acesso da sua máquina local.

#### Pré-requisitos
* Java (JDK) 17
* Um Banco de Dados SQL Server ativo no Azure.
* Credenciais de acesso ao banco de dados Azure SQL Server.

#### Passo a Passo
1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/GuiJanunzzi/MottoMap-JavaApplication.git
    ```

2.  **Navegue até a pasta do projeto:**
    ```bash
    cd MottoMap-JavaApplication
    ```

3.  **Configure as Variáveis de Ambiente:**
    O projeto está configurado para ler as credenciais do banco a partir de variáveis de ambiente. Você deve configurá-las no seu sistema ou diretamente na sua IDE (IntelliJ, VSCode, etc.):
    * `DB_URL`: O nome do seu servidor (ex: `meu-servidor.database.windows.net`)
    * `DB_NAME`: O nome do banco (ex: `mottomap-db`)
    * `DB_USER`: O seu usuário de login (ex: `admin_mottomap`)
    * `DB_PSSWD`: Sua senha

4.  **Execute a aplicação:**
    * **Via IDE (Recomendado):** Inicie a classe principal `MottoMapJavaApplication.java`.
    * **Via Terminal:**
        ```bash
        ./gradlew bootRun
        ```