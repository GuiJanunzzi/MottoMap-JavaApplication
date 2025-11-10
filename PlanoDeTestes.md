## 📋 Parte A: Plano de Testes Manuais

Esta seção descreve os casos de teste manuais (CTs) para as funcionalidades principais do sistema MotoMap, conforme solicitado para a 4ª Sprint.

### 1. Teste de Acesso (Login)

**CT-001: Login com ADM_GERAL (Caminho Feliz)**
* **1) Teste Planejado:** Verificar se o usuário `ADM_GERAL` consegue se autenticar com credenciais válidas.
* **2) Dados de Entrada:**
    * `Usuário (Email)`: admin@mottomap.com
    * `Senha`: admin
* **3) Dados de Saída (Esperado):** O usuário é redirecionado para a página principal (Dashboard), visualizando todos os menus (Filiais, Usuários, Motos, Posições).
* **4) Procedimento (Passos):**
    1.  Acessar a URL da aplicação (`/login`).
    2.  Digitar "admin@mottomap.com" no campo de usuário.
    3.  Digitar "admin" no campo de senha.
    4.  Clicar no botão "Entrar".
    5.  **Verificar** se a URL mudou para (`/`) e se os menus de ADM_GERAL estão visíveis.

**CT-002: Login com Senha Inválida (Falha)**
* **1) Teste Planejado:** Verificar se o sistema impede o login com uma senha incorreta.
* **2) Dados de Entrada:**
    * `Usuário (Email)`: admin@mottomap.com
    * `Senha`: senhaerrada
* **3) Dados de Saída (Esperado):** O usuário permanece na página de login e uma mensagem de erro (ou a URL `/login?error`) é exibida.
* **4) Procedimento (Passos):**
    1.  Acessar a URL da aplicação (`/login`).
    2.  Digitar "admin@mottomap.com" no campo de usuário.
    3.  Digitar "senhaerrada" no campo de senha.
    4.  Clicar no botão "Entrar".
    5.  **Verificar** se o sistema permaneceu na página de login e se uma indicação de erro foi exibida.

### 2. Gerenciamento (CRUD)

**CT-003: Cadastrar uma Nova Moto (ADM_LOCAL)**
* **1) Teste Planejado:** Verificar se o usuário `ADM_LOCAL` consegue cadastrar uma nova moto para a sua própria filial.
* **2) Dados de Entrada:**
    * *Login:* `local@mottomap.com` / `local123`
    * *Dados da Moto:*
        * `Placa`: "TESTE01"
        * `Chassi`: "9C2KD0101TESTE001"
        * `Modelo`: "POP_110I"
        * `Ano`: "2024"
        * `Status`: "ATIVA"
* **3) Dados de Saída (Esperado):** O usuário é redirecionado para a lista de motos (`/motos`) e uma mensagem de "Moto salva com sucesso!" é exibida. O campo "Filial" (Sede SP) deve ter sido preenchido automaticamente.
* **4) Procedimento (Passos):**
    1.  Fazer login como `local@mottomap.com`.
    2.  No menu, navegar para "Motos".
    3.  Clicar no botão "Nova Moto".
    4.  Preencher os campos de Placa, Chassi, Modelo e Ano conforme os dados de entrada.
    5.  Clicar no botão "Salvar".
    6.  **Verificar** se a URL mudou para `/motos`.
    7.  **Verificar** se a mensagem de sucesso apareceu.
    8.  **Verificar** se a nova moto "TESTE01" está visível na lista.

### 3. Funcionalidade Principal (Pátio)

**CT-004: Alocar Moto em Vaga (COL_PATIO)**
* **1) Teste Planejado:** Verificar se o `COL_PATIO` consegue alocar uma moto (que está sem posição) em uma vaga livre no mapa do pátio.
* **2) Dados de Entrada:**
    * *Login:* `patio@mottomap.com` / `patio123`
    * *Pré-condição:* Deve existir pelo menos 1 moto com status "Sem Posição" e 1 vaga com status "Vaga Livre" (verde).
* **3) Dados de Saída (Esperado):** A vaga clicada (que estava verde) deve mudar para "Ocupada" (vermelha). A moto selecionada deve desaparecer da lista "Motos Sem Posição".
* **4) Procedimento (Passos):**
    1.  Fazer login como `patio@mottomap.com`.
    2.  No menu, navegar para "Meu Pátio".
    3.  Na lista "Motos Sem Posição", clicar em uma moto disponível (ex: "ABC1234").
    4.  O cursor deve indicar que uma moto está selecionada.
    5.  Clicar em uma vaga verde (Livre) no mapa do pátio.
    6.  **Verificar** se a vaga clicada ficou vermelha e agora exibe a placa "ABC1234".
    7.  **Verificar** se a moto "ABC1234" desapareceu da lista "Motos Sem Posição".