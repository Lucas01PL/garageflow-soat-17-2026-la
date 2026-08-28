# 🔧 GarageFlow — SOAT 17

> API para gerenciamento de Ordens de Serviço (OS) de uma oficina mecânica

Desenvolvida no contexto do **Tech Challenge da Pós-Tech FIAP**, utilizando **Java 25**, **Spring Boot 4**, **MongoDB** e uma organização baseada em **Clean Architecture + DDD**, com foco na **RepairOrder** como Aggregate Root.

![Java](https://img.shields.io/badge/Java-25-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.7-green?logo=spring-boot)
![MongoDB](https://img.shields.io/badge/MongoDB-7-success?logo=mongodb)
![Maven](https://img.shields.io/badge/Maven-3.9+-blue?logo=apache-maven)

## 📊 Visão Geral

### Fluxo Principal da OS

```
┌──────────┐   ┌────────────┐   ┌────────────────┐   ┌──────────┐   ┌──────────────┐   ┌──────────┐   ┌───────────┐
│ RECEIVED │──▶│IN_DIAGNOSIS│──▶│AWAITING_APPROVAL│──▶│ APPROVED │──▶│IN_EXECUTION  │──▶│ FINISHED │──▶│ DELIVERED │
└──────────┘   └────────────┘   └────────────────┘   └──────────┘   └──────────────┘   └──────────┘   └───────────┘
                                       │
                                       ▼
                                   REJECTED
```

### Fluxo de Serviços

Cada serviço segue seu próprio ciclo de vida:

```
WAITING_ATTENDING  ──▶  IN_EXECUTION  ──▶  FINISHED
```

> ⚠️ **Importante**: Quando todos os serviços da OS estão `FINISHED`, a RepairOrder passa automaticamente para esse estado.

## 📋 Regras de Domínio

| Regra | Descrição |
|-------|-----------|
| **Diagnóstico** | Uma OS só pode iniciar o diagnóstico quando estiver em `RECEIVED` |
| **Solicitação de Aprovação** | Uma OS só pode solicitar aprovação quando estiver em `IN_DIAGNOSIS` e possuir **no mínimo um serviço** |
| **Aprovação/Rejeição** | Uma OS só pode ser aprovada ou rejeitada quando estiver em `AWAITING_APPROVAL` |
| **Execução** | Uma OS só pode iniciar a execução quando estiver em `APPROVED` |
| **Início de Serviço** | Um serviço só pode ser iniciado quando a OS estiver em `IN_EXECUTION` |
| **Finalização de Serviço** | Um serviço só pode ser finalizado quando estiver em `IN_EXECUTION` |
| **Finalização da OS** | A OS só é finalizada quando **todos** seus serviços estiverem `FINISHED` |
| **Entrega** | Uma OS só pode ser entregue quando estiver em `FINISHED` |
| **Controle de Estoque** | Alterações de peças consultam o estoque e utilizam operações compensatórias em caso de falha |
| **Snapshots** | Peças e serviços adicionados à OS preservam dados relevantes do momento da inclusão |
| **Aprovação de Compra** | Listas de compra só podem ser aprovadas/rejeitadas quando estiverem `PENDING` |
| **Compra de Insumos** | Ao comprar a lista aprovada, o estoque de cada peça é incrementado automaticamente |

## 🏗️ Arquitetura

A estrutura segue a separação de responsabilidades da **Clean Architecture**:

```
src/main/java/br/com/fiap/tech/challeng/garageflow_soat_17_2026_la/
├── auth/                     # Segurança e autenticação
├── client/                   # Clientes da oficina
├── vehicle/                  # Veículos e placas
├── user/                     # Usuários do sistema
├── part/                     # Peças e estoque
├── workshopservice/          # Serviços de oficina
├── purchasing/               # Lista de compras e aprovação
├── repairorder/              # Aggregate Root principal
│   ├── application/
│   ├── domain/
│   └── presentation/
├── shared/                   # Código compartilhado
└── config/                   # Segurança e config gerais
```

### Componentes Principais

- **Domain (Domínio)**: Contém as entidades, value objects e regras de negócio
- **Application (Aplicação)**: Orquestra os casos de uso sem concentrar lógica de negócio
- **Presentation (Apresentação)**: Controllers, DTOs e mappers para exposição HTTP
- **Infrastructure**: Spring Data MongoDB mantém persistência atrás das interfaces de repository

## 🛠️ Tecnologias

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| **Java** | 25 | Linguagem principal |
| **Spring Boot** | 4.0.7 | Framework web e IoC |
| **Spring Web** | - | APIs REST |
| **Spring Validation** | - | Validação de dados |
| **Spring Data MongoDB** | - | Persistência de dados |
| **Springdoc OpenAPI** | - | Documentação automática (Swagger) |
| **Lombok** | - | Redução de boilerplate |
| **JUnit 5** | - | Framework de testes |
| **Mockito** | - | Mock objects para testes |
| **MongoDB** | 7 | Banco de dados NoSQL |
| **Maven** | 3.9+ | Gerenciamento de dependências |
| **Docker** | - | Containerização |

> Consulte `pom.xml` para versões específicas de todas as dependências.

## 🚀 Como Executar

### Pré-requisitos

- ✅ **Java 25** ou superior
- ✅ **Maven 3.9+** (ou Maven Wrapper disponível)
- ✅ **Docker** e **Docker Compose**

### 1️⃣ Subir o MongoDB

Na raiz do projeto, execute:

```bash
docker compose up -d
```

O MongoDB estará disponível em `localhost:27017`

**Credenciais padrão (desenvolvimento):**
- Usuário: `admin`
- Senha: `admin123`
- Database: `garageflow-soat-17-2026-db`
- Porta: `27017`

### 2️⃣ Executar a Aplicação

**Com Maven Wrapper (recomendado):**

```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
.\mvnw.cmd spring-boot:run
```

**Ou com Maven instalado localmente:**

```bash
mvn spring-boot:run
```

A aplicação iniciará em `http://localhost:8080/api`
## ✅ Testes

### Executar Todos os Testes

```bash
# Linux/Mac
./mvnw test

# Windows
.\mvnw.cmd test

# Ou com Maven instalado
mvn test
```

Nota rápida: a suíte de testes exige JDK 25 (projeto target=25). Se o ambiente local tiver JDK diferente, ajuste a instalação ou toolchain antes de executar os testes. Alguns testes unitários dependem do SecurityContext contendo um `AuthenticatedUser` (os testes do repositório já populam esse contexto automaticamente). Para testes manuais que exercitam endpoints protegidos, gere um token via `/auth/login` e envie em `Authorization: Bearer <token>` (o token agora inclui `userId`).


### Cobertura de Testes

A suíte contém cobertura completa:

✓ Testes de domínio  
✓ Casos de uso  
✓ Services e orquestração  
✓ Mappers e conversão de dados  
✓ Repositories e persistência  
✓ Controllers e APIs  
✓ Validações e exceções  

### Comportamentos Testados

- ✅ Transições de estado da OS
- ✅ Regras de quantidade e validação
- ✅ Inclusão e remoção de peças
- ✅ Controle de estoque
- ✅ Inclusão e remoção de serviços
- ✅ Finalização de serviços
- ✅ Finalização automática da OS
- ✅ Mapeamentos entre camadas
- ✅ Integração com MongoDB

## 📡 API REST

Os módulos da aplicação seguem uma API REST organizada por domínio. Além da RepairOrder, o projeto agora inclui autenticação, usuários, clientes, veículos, peças, serviços e compras.

### Autenticação e Usuários

| Método | Endpoint | Descrição                                                                         |
|--------|----------|-----------------------------------------------------------------------------------|
| `POST` | `/auth/login` | Autentica um usuário e retorna JWT                                                |
| `POST` | `/users` | Cria um usuário                                                                   |
| `GET` | `/users/{id}` | Busca usuário por ID                                                              |
| `GET` | `/users?fullName=...` | Busca usuários por nome ou lista todos os usuários caso o nome não seja informado |
| `PUT` | `/users/{id}` | Atualiza usuário                                                                  |
| `DELETE` | `/users/{id}` | Remove usuário                                                                    |

**Nota sobre autenticação:** os tokens JWT incluem agora o claim `userId`. Os controllers extraem o usuário autenticado via resolveCurrentUserId() (em `shared.presentation.controller.BaseController`) e esperam o principal como `AuthenticatedUser` com `userId` e `email`. Ao testar endpoints protegidos manualmente, inclua o header `Authorization: Bearer <token>` retornado por `/auth/login`. Nos testes unitários, o SecurityContext é populado com `AuthenticatedUser` para simular autenticação.

### Clientes e Veículos

| Método | Endpoint              | Descrição                                                                                  |
|--------|-----------------------|--------------------------------------------------------------------------------------------|
| `POST` | `/clients`            | Cria cliente                                                                               |
| `GET` | `/clients/{id}`       | Busca cliente por ID                                                                       |
| `GET` | `/clients?{document}` | Busca cliente por documento ou lista todos os clientes caso o documento não seja informado |
| `PUT` | `/clients/{id}`       | Atualiza cliente                                                                           |
| `DELETE` | `/clients/{id}`       | Remove cliente                                                                             |
| `POST` | `/vehicles`           | Cria veículo                                                                               |
| `GET` | `/vehicles/{id}`      | Busca veículo por ID                                                                       |
| `GET` | `/vehicles?{plate}`   | Busca veículo por placa ou lista todos os veículos caso a placa não seja informada         |
| `PUT` | `/vehicles/{id}`      | Atualiza veículo                                                                           |
| `DELETE` | `/vehicles/{id}`      | Remove veículo                                                                             |

### Peças e Estoque

| Método | Endpoint                                      | Descrição                                                                      |
|--------|-----------------------------------------------|--------------------------------------------------------------------------------|
| `POST` | `/parts`                                      | Cria peça                                                                      |
| `POST` | `/parts/{id}/stock/debit?quantityToDebit=...` | Debita estoque                                                                 |
| `POST` | `/parts/{id}/stock/add?quantityToAdd=...`     | Acrescenta estoque                                                             |
| `GET` | `/parts/{id}`                                 | Busca peça por ID                                                              |
| `GET` | `/parts?{code}`                               | Busca peça por código ou lista todas as peças caso o código não seja informado |
| `GET` | `/parts/low-stock?threshold=...`              | Lista peças em estoque baixo                                                   |
| `PUT` | `/parts/{id}`                                 | Atualiza peça                                                                  |
| `DELETE` | `/parts/{id}`                                 | Remove peça                                                                    |

### Serviços da Oficina

| Método | Endpoint                                   | Descrição                                                                          |
|--------|--------------------------------------------|------------------------------------------------------------------------------------|
| `POST` | `/workshop-services`                        | Cria serviço                                                                       |
| `GET` | `/workshop-services/{id}`                   | Busca serviço por ID                                                               |
| `GET` | `/workshop-services?description=...` | Busca por descrição ou lista todos os serviços caso a descrição não seja informada |
| `PUT` | `/workshop-services/{id}`                   | Atualiza serviço                                                                   |
| `DELETE` | `/workshop-services/{id}`                   | Remove serviço                                                                     |

### Compras e Estoque

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/purchase-lists` | Gera lista de compra |
| `GET` | `/purchase-lists` | Lista listas de compra |
| `GET` | `/purchase-lists/{id}` | Busca lista por ID |
| `PATCH` | `/purchase-lists/{id}/approve` | Aprova lista |
| `PATCH` | `/purchase-lists/{id}/reject` | Rejeita lista |
| `POST` | `/purchase-lists/{id}/purchase` | Confirma compra e repõe estoque |

### Ordens de Serviço

| Método   | Endpoint                                                                                   | Descrição                              |
|----------|--------------------------------------------------------------------------------------------|----------------------------------------|
| `POST`   | `/repair-orders`                                                                           | Cria uma nova OS (requer autenticação) |
| `GET`    | `/repair-orders/{id}`                                                                      | Obtém OS por ID                        |
| `GET`    | `/repair-orders`                                                                           | Lista todas as OS                      |
| `GET`    | `/repair-orders/customer/{customerId}`                                                     | Busca OS por cliente                   |
| `POST`   | `/repair-orders/{repairOrderId}/workshop-services`                                         | Adiciona um serviço                    |
| `DELETE` | `/repair-orders/{repairOrderId}/workshop-services/{workshopServicesId}`                    | Remove um serviço                      |
| `POST`   | `/repair-orders/{repairOrderId}/parts`                                                     | Adiciona uma peça                      |
| `DELETE` | `/repair-orders/{repairOrderId}/parts/{partId}`                                            | Remove uma peça                        |
| `PATCH`  | `/repair-orders/{repairOrderId}/status/in-diagnosis`                                       | Inicia diagnóstico                     |
| `PATCH`  | `/repair-orders/{repairOrderId}/status/awaiting-approval`                                  | Solicita aprovação                     |
| `PATCH`  | `/repair-orders/{repairOrderId}/status/approved`                                           | Aprova a OS                            |
| `PATCH`  | `/repair-orders/{repairOrderId}/status/rejected`                                           | Rejeita a OS                           |
| `PATCH`  | `/repair-orders/{repairOrderId}/status/in-execution`                                       | Inicia execução                        |
| `PATCH`  | `/repair-orders/{repairOrderId}/status/delivered`                                          | Entrega a OS                           |
| `PATCH`  | `/repair-orders/{repairOrderId}/workshop-services/{workshopServiceId}/status/in-execution` | Inicia serviço                         |
| `PATCH`  | `/repair-orders/{repairOrderId}/workshop-services/{workshopServiceId}/status/finished`     | Finaliza serviço                       |
| `PATCH`  | `/repair-orders/{repairOrderId}/status/cancelled`                                          | Cancela OS                             |
| `GET`    | `/repair-orders/monitoring/workshop-services/average-execution-time`                       | Calcula tempo médio de execução        |

Autenticação e criação de OS

- Ao efetuar login via `/auth/login` o JWT agora inclui o claim `userId` (além de email e role). Os controllers leem o usuário autenticado do SecurityContext e persistem o `userId` associado à OS.

Exemplo (gerar token):

```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com", "password":"admin123"}'
```

O retorno contém o token JWT. Para criar uma OS usando o usuário autenticado, envie o header Authorization:

```bash
curl -X POST http://localhost:8080/api/repair-orders \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"vehicleId":"<vehicle-id>", "customerId":"<customer-id>"}'
```

Notas importantes:
- Se o token não contiver `userId`, a criação falhará. O fluxo de login (`/auth/login`) já inclui `userId` no token gerado.
- Nos testes unitários, o SecurityContext é preenchido com um `AuthenticatedUser` (contendo userId e email) para simular autenticação.

### 📖 Documentação Interativa

Com a aplicação em execução:

- **Swagger UI**: http://localhost:8080/api/docs
- **Especificação OpenAPI**: http://localhost:8080/api/openapi

## 🗄️ MongoDB com Docker

O `docker-compose.yml` configura um container MongoDB com persistência:

```yaml
mongodb:
  ├─ Porta: 27017
  ├─ Usuário: admin
  ├─ Senha: admin123
  ├─ Database: garageflow-soat-17-2026-db
  └─ Volume: Persistência local
```

> ⚠️ **Nota de Segurança**: As credenciais acima são **apenas para desenvolvimento local**. Altere-as antes de usar em produção.

## 🎯 Decisões de Domínio

### Snapshots

A OS mantém snapshots de **cliente**, **veículo**, **peças** e **serviços**.

**Objetivo**: Preservar as informações utilizadas na OS, mesmo que os dados de origem sejam alterados posteriormente.

### Exceções de Negócio

As regras são expressas através de exceções específicas:

```
InvalidRepairOrderStateException
InvalidRepairOrderItemException
InvalidPartException
PartNotFoundException
WorkshopServiceNotFoundException
InsufficientQuantityException
PartStockOperationException
```

O tratamento HTTP é centralizado pelo mecanismo global de exceções da aplicação.

### Controle de Estoque

```
Adicionar Peça:
  1. Verifica disponibilidade em estoque
  2. Debita o estoque
  3. Adiciona peça à OS
  ❌ Se falha: Compensa (recompõe estoque)

Remover Peça:
  1. Recompõe o estoque
  2. Remove peça da OS
  ❌ Se falha: Compensa (debita novamente)
```

Essa abordagem reduz riscos de inconsistência entre estoque e OS sem introduzir, neste momento, uma transação distribuída.

## 📁 Estrutura dos Testes

Os testes acompanham a estrutura das camadas do sistema:

```
src/test/java/br/com/fiap/tech/challenge/garageflow_soat_17_2026_la/
├── repairorder/
│   ├── application/        # Testes de casos de uso
│   ├── domain/             # Testes de regras de negócio
│   └── presentation/       # Testes de controllers
├── part/                   # Testes do domínio de peças
└── workshopservice/        # Testes do domínio de serviços
```

### Cobertura De Casos

Todos os principais comportamentos possuem cobertura de **sucesso** e **falha**:

- ✅ Transições de estado
- ✅ Regras de quantidade e validação
- ✅ Inclusão e remoção de peças
- ✅ Controle de estoque com compensação
- ✅ Inclusão e remoção de serviços
- ✅ Finalização de serviços
- ✅ Finalização automática da OS
- ✅ Mapeamentos entre camadas
- ✅ Integração com repositórios
- ✅ Endpoints de controller
## 🔄 Fluxo de Negócio Completo

### Cenário de Aprovação (Sucesso)

```
1️⃣  Cliente/Veículo selecionados
    ↓
2️⃣  RepairOrder criada (RECEIVED)
    ↓
3️⃣  Diagnóstico iniciado (IN_DIAGNOSIS)
    ↓
4️⃣  Serviços e peças adicionados
    ↓
5️⃣  OS enviada para aprovação (AWAITING_APPROVAL)
    ↓
6️⃣  Cliente aprova (APPROVED)
    ↓
7️⃣  Execução iniciada (IN_EXECUTION)
    ↓
8️⃣  Serviços executados
    ↓
9️⃣  Serviços finalizados (todos FINISHED)
    ↓
🔟 OS finalizada automaticamente (FINISHED)
    ↓
1️⃣1️⃣ Veículo entregue (DELIVERED)
```

### Cenário de Rejeição (Alternativo)

```
AWAITING_APPROVAL
    ↓
Cliente não aprova
    ↓
REJECTED
```

> ℹ️ Uma OS rejeitada não retorna ao fluxo de diagnóstico. Para um novo atendimento, deve ser criada uma nova OS.

## ℹ️ Status do Projeto

Projeto acadêmico desenvolvido para o **Tech Challenge da Pós-Tech FIAP**.

### Foco Principal

- ✅ Clean Architecture
- ✅ Domain-Driven Design (DDD)
- ✅ Aggregate Root (RepairOrder)
- ✅ Regras de domínio bem definidas
- ✅ API REST
- ✅ Testes unitários abrangentes
- ✅ MongoDB
- ✅ Controle de estoque com compensação
- ✅ Docker e Docker Compose

## 📜 Licença

Este projeto foi desenvolvido para fins acadêmicos no contexto do **Tech Challenge da FIAP**.

---

**Desenvolvido com ❤️ pela equipe do SOAT 17**
