# TMA #
### — Como executar localmente

Este projeto se trata de um projeto de desenvolvimento feito por Diego Vitali e Guilherme Ludgero como pre-requisito para obtenção de menção na matéria de GPD do curso de Análise e Desenvolvimetno de Sistemas do Instituto Federal de São Paulo - Campus Guarulhos. Ele combina um backend desenvolvido em Python com FastAPI, um frontend em Java e um banco de dados MySQL, permitindo executar localmente um ambiente completo para testes e desenvolvimento.

Este documento explica como preparar o ambiente, instalar as dependências e iniciar todos os serviços para execução local.
---

## Pré-requisitos

Antes de começar, garanta que você tenha os seguintes softwares instalados:
* **Python** (3.8 ou superior) e **pip**
* **Java JDK** (11 ou superior)
* **MySQL** (Servidor)
* **Eclipse IDE foi Enterprise Java Developers**
* **Apache Tomcat 9+** Configurado no Eclipse
* **git**

---

## Configuração Inicial
Para começar, clone o repositório oficial executando no terminal:
```
git clone https://github.com/Diego-Vitali/TMA.git
cd TMA
```
Dentro do diretório do projeto, você encontrará três principais pastas:
* backend-fastapi: código do backend em Python;
* frontend-java: código do frontend em Java (Dynamic Web Project);
* database: scripts e estrutura do banco de dados MySQL.

## MySQL (ATENÇÃO, por enquanto, esta etapa é OPCIONAL, visto que o projeto ainda não está integrado ao Banco de Dados. Este aviso será retirado quando necessário)
Primeiro, o banco de dados precisa estar online e configurado.

1.  **Inicie seu servidor MySQL** local.
2.  Crie o banco de dados para o projeto. (Use um cliente SQL ou o terminal):
    ```sql
    CREATE DATABASE tma CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;;
    ```
3.  Acesse a pasta `database` do projeto e execute o script para criar as tabelas (`schema.sql`).
4.  Configure as credenciais. O backend e o frontend precisarão acessar o banco. Crie um arquivo `.env` (ou configure variáveis de ambiente) em `backend-fastapi` e configure no `frontend-java` (em `application.properties` ou similar) com base nisto:
    ```
    DB_HOST=localhost
    DB_PORT=3306
    DB_NAME=tma
    DB_USER=seu_usuario_mysql
    DB_PASSWORD=sua_senha_mysql
    ```

---

## Execução do Backend (FastAPI)

Com o banco rodando, inicie o servidor Python.

1.  Abra um terminal e navegue até a pasta do backend:
    ```bash
    cd backend-fastapi
    ```
2.  Crie e ative um ambiente virtual:
    ```bash
    # Criar ambiente
    python -m venv .venv
    
    # Ativar (Linux/macOS)
    source .venv/bin/activate
    
    # Ativar (Windows)
    .venv\Scripts\activate
    ```
3.  Instale as dependências Python necessárias:
    ```bash
    pip install -r requirements.txt
    ```
4.  Inicie o servidor FastAPI:
    ```bash
    uvicorn api/main:app --reload
    ```
5.  *Sucesso!* O backend estará rodando em `http://localhost:8000`. Você pode ver a documentação da API em `http://localhost:8000/docs`.

---

## Execução do Frontend (Java com Tomcat no Eclipse)

1. Abra o **Eclipse IDE**.
2. Vá em **File > Import > Existing Projects into Workspace**.
3. Selecione o diretório `frontend-java` dentro do projeto clonado e conclua a importação.
4. Confirme que o projeto foi reconhecido como **Dynamic Web Project**. Caso contrário, ajuste em *Project Properties > Project Facets* e marque as opções **Dynamic Web Module** e **Java**.
5. Adicione o servidor **Apache Tomcat** no Eclipse em **Window > Preferences > Server > Runtime Environments** e aponte para o diretório de instalação do Tomcat.
6. Clique com o botão direito no projeto e escolha **Run As > Run on Server**. Selecione o servidor Tomcat configurado e aguarde o deploy.
7. Após iniciado, o projeto ficará acessível em [http://localhost:8080](http://localhost:8080) ou conforme a porta configurada.

O frontend se comunica com o backend FastAPI e o banco de dados MySQL para exibir os dados corretamente.

---

## Testando a Aplicação

Com os três componentes em execução (MySQL, backend e Tomcat), abra o navegador e acesse http://localhost:8080. O frontend deve se conectar automaticamente ao backend e exibir as informações processadas. Você também pode testar a API do FastAPI diretamente via http://localhost:8000/docs.

* **`http://localhost:8080`**

O frontend Java (porta 8080) deve carregar e se comunicar automaticamente com o backend FastAPI (porta 8000), que por sua vez se comunica com o banco de dados MySQL.
