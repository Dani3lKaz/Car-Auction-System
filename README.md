# MotoTrade - System Aukcyjny

MotoTrade to aplikacja webowa typu full-stack służąca do wystawiania pojazdów na aukcje oraz prowadzenia licytacji online. Projekt został wykonany w ramach **Projektu Indywidualnego** na 4. semestrze studiów.

Aplikacja składa się z backendu REST/WebSocket napisanego w Spring Boot, frontendu SPA w React oraz bazy danych PostgreSQL uruchamianej lokalnie przez Docker Compose.

## Najważniejsze funkcje

- rejestracja i logowanie użytkowników z wykorzystaniem tokenów JWT,
- role użytkowników: `ADMIN`, `SELLER`, `USER`,
- przeglądanie listy aukcji i szczegółów pojazdu,
- tworzenie aukcji przez administratora lub sprzedawcę,
- wgrywanie zdjęć pojazdów,
- składanie ofert w czasie rzeczywistym przez WebSocket/STOMP,
- historia ofert dla aukcji,
- automatyczna aktualizacja aktualnej ceny po złożeniu oferty,
- obsługa salda użytkownika oraz zwrot środków po przebiciu oferty,
- panel konta użytkownika,
- panel zarządzania użytkownikami dla administratora,
- walidacja logiki aukcji po stronie backendu,
- blokowanie optymistyczne dla operacji wrażliwych na współbieżność.

## Stack technologiczny

### Backend

- Java 25
- Spring Boot 4.0.3
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring Security
- JWT
- Spring WebSocket / STOMP
- PostgreSQL
- Lombok
- Maven
- JUnit 5, Mockito, Spring Security Test

### Frontend

- React 19
- Vite
- React Router
- SockJS
- STOMP.js
- Bootstrap 5 i Bootstrap Icons
- ESLint

### Infrastruktura

- Docker
- Docker Compose
- PostgreSQL 15

## Struktura projektu

```text
.
├── backend/        # aplikacja Spring Boot: REST API, WebSocket, logika biznesowa
├── frontend/       # aplikacja React uruchamiana przez Vite
├── init-db/        # skrypt inicjalizujący strukturę bazy PostgreSQL
├── docker-compose.yml
└── README.md
```

## Wymagania

Do uruchomienia projektu lokalnie potrzebne są:

- Java 25,
- Node.js i npm,
- Docker oraz Docker Compose.

Projekt zawiera Maven Wrapper, więc lokalna instalacja Mavena nie jest wymagana.

## Uruchomienie projektu

### 1. Uruchomienie bazy danych

W katalogu głównym projektu uruchom PostgreSQL:

```bash
docker-compose up -d
```

Kontener wystawia bazę na porcie `5432` i używa poniższej konfiguracji:

```text
POSTGRES_DB=auction_platform
POSTGRES_USER=admin
POSTGRES_PASSWORD=admin
```

Skrypt `init-db/init.sql` tworzy strukturę tabel przy pierwszym starcie kontenera. Dodatkowe dane testowe, takie jak użytkownicy, pojazdy i aktywne aukcje, są dodawane przez klasę `DataSeeder` podczas uruchamiania backendu.

### 2. Uruchomienie backendu

W drugim terminalu przejdź do katalogu backendu i uruchom aplikację:

```bash
cd backend
./mvnw spring-boot:run
```

Backend będzie dostępny pod adresem:

```text
http://localhost:8080
```

### 3. Uruchomienie frontendu

W trzecim terminalu przejdź do katalogu frontendu, zainstaluj zależności i uruchom serwer deweloperski:

```bash
cd frontend
npm install
npm run dev
```

Frontend będzie dostępny pod adresem:

```text
http://localhost:5173
```

## Konta testowe

Po pierwszym uruchomieniu backendu aplikacja tworzy przykładowe konta:

| Rola | E-mail | Hasło |
| --- | --- | --- |
| Administrator | `admin@mail.com` | `Admin1234` |
| Sprzedawca | `seller@mail.com` | `Seller1234` |
| Użytkownik | `user@mail.com` | `User1234` |

## Konfiguracja

Podstawowa konfiguracja backendu znajduje się w pliku `backend/src/main/resources/application.properties`.

Najważniejsze ustawienia:

- adres bazy danych: `jdbc:postgresql://localhost:5432/auction_platform`,
- dane logowania do bazy: `admin` / `admin`,
- sekret i czas ważności JWT,
- katalog uploadu plików: `backend/uploads`,
- maksymalny rozmiar przesyłanego zdjęcia: `15MB`.

Frontend komunikuje się z backendem pod adresem `http://localhost:8080`.

## Główne endpointy API

### Uwierzytelnianie

- `POST /api/auth/register` - rejestracja użytkownika,
- `POST /api/auth/login` - logowanie i pobranie tokenu JWT.

### Konto użytkownika

- `GET /api/account` - dane aktualnie zalogowanego użytkownika,
- `PUT /api/account` - aktualizacja danych konta.

### Aukcje

- `GET /api/auctions` - lista aukcji,
- `GET /api/auctions?status=ACTIVE` - lista aukcji według statusu,
- `GET /api/auctions/{auctionId}` - szczegóły aukcji,
- `POST /api/auctions` - utworzenie aukcji, dostępne dla `ADMIN` i `SELLER`,
- `PUT /api/auctions` - aktualizacja aukcji, dostępna dla `ADMIN` i `SELLER`,
- `DELETE /api/auctions/{auctionId}` - usunięcie aukcji, dostępne dla `ADMIN` i `SELLER`.

### Pojazdy

- `GET /api/vehicles` - lista pojazdów,
- `GET /api/vehicles/{vehicleId}` - szczegóły pojazdu,
- `POST /api/vehicles` - dodanie pojazdu,
- `PUT /api/vehicles` - aktualizacja pojazdu,
- `DELETE /api/vehicles/{vehicleId}` - usunięcie pojazdu.

### Oferty

- `GET /api/bids` - lista ofert,
- `GET /api/bids/{bidId}` - szczegóły oferty,
- `GET /api/bids/auction/{auctionId}` - historia ofert dla aukcji,
- `DELETE /api/bids/{bidId}` - usunięcie oferty.

Składanie ofert odbywa się przez WebSocket, a nie przez klasyczny endpoint REST.

### Upload plików

- `POST /api/upload` - upload zdjęcia pojazdu w formacie `multipart/form-data`.

## WebSocket

Backend udostępnia endpoint WebSocket:

```text
/ws
```

Konfiguracja STOMP:

- prefix aplikacji: `/app`,
- broker: `/topic`, `/queue`,
- prefix wiadomości prywatnych użytkownika: `/user`.

Najważniejsze kanały:

- publikowanie oferty: `/app/bids/place`,
- aktualizacje ofert aukcji: `/topic/auctions/{auctionId}/bids`,
- potwierdzenie złożenia oferty: `/user/queue/bid-success`,
- informacja o przebiciu oferty: `/user/queue/outbid`,
- błędy licytacji: `/user/queue/errors`.

## Logika biznesowa licytacji

Backend sprawdza między innymi, czy:

- aukcja jest aktywna i nie minął jej czas zakończenia,
- sprzedawca nie licytuje własnej aukcji,
- użytkownik ma wystarczające saldo,
- nowa oferta spełnia minimalny wymagany krok,
- aktualny najwyższy licytant nie przebija samego siebie.

Po złożeniu poprawnej oferty system aktualizuje aktualną cenę aukcji, blokuje środki nowego licytanta oraz zwraca środki poprzedniemu najwyższemu licytantowi.

## Testy

Testy backendu można uruchomić poleceniem:

```bash
cd backend
./mvnw test
```

Testy obejmują między innymi warstwę serwisów oraz kontrolery REST.

Frontend można sprawdzić statycznie przez ESLint:

```bash
cd frontend
npm run lint
```
