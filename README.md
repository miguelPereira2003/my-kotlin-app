# Carpooling Platform (Trabalho Final AD 2025/2026)

Implementação de referência para a vertente **Car Pooling** com arquitetura **microserviços** (Spring Boot / Spring Cloud), **Eureka**, **API Gateway**, **OpenFeign**, **Circuit Breakers**, **Swagger**, **Actuator**, **segurança JWT**, **Thymeleaf** (front-end web) e **Docker Compose**.

O enunciado recomenda microserviços para: autenticação/perfis, viagens, veículos (para escolha do condutor), localização/GPS (simulado), pagamentos/custos, administração, front-end, e discovery/gateway.

## Como executar (Docker)
1. Ir para a pasta `infra/`:
   ```bash
   cd infra
   ```
2. Subir tudo com build:
   ```bash
   docker compose up -d --build
   ```
3. (Opcional) replicar o trip-service:
   ```bash
   docker compose up -d --build --scale trip-service=2
   ```

## URLs
- Eureka: http://localhost:8761
- Gateway (entrypoint): http://localhost:8080
- Front-end Web (também acessível via gateway em `/`): http://localhost:8081
- Swagger (exemplos; via acesso direto ao container na rede docker):
  - Auth: http://localhost:8080/api/auth/swagger-ui.html (nota: swagger exposto por serviço, acesso ideal direto)
  - Trip: http://localhost:8084/swagger-ui.html (se expuseres portas; por default no compose não expomos)

## Credenciais seed
- Admin (criado no arranque do auth-profile-service):
  - email: `admin@local`
  - password: `Admin123!`

## Fluxo mínimo (API)
1. Registar condutor:
   `POST /api/auth/register`
2. Criar veículo:
   `POST /api/vehicles`
3. Criar viagem:
   `POST /api/trips`
4. Pesquisar viagem:
   `GET /api/trips/search`
5. Reservar:
   `POST /api/trips/{tripId}/bookings`
6. Aprovar:
   `POST /api/bookings/{bookingId}/approve`
7. Concluir viagem (calcula custo via payment):
   `POST /api/trips/{tripId}/complete`
8. Pagar:
   `POST /api/payments/{tripId}/pay`

> Nota: O serviço de Localização é simulado (haversine). O cálculo de custos é simulado com preços configuráveis por env var.

