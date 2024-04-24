# System zarządzania lotami i pasażerami

Aplikacja CRUD pozwalająca na:
- odczyt, dodawanie, usuwanie oraz aktualizowanie lotów i pasażerów w bazie danych,
- dodawanie, usuwanie oraz odczyt pasażerów do danego lotu.

Stack technologiczny:
- Java Spring Boot,
- PostgreSQL,
- Gradle,
- Docker.

## Wymagania
- Java 21
- Docker

## Uruchomienie
1. Sklonuj repo.
2. Upewnij się, że Docker jest uruchomiony. Użyj komendy:
```bash
docker compose up 
```
3. Aplikacja jest uruchomiona na porcie `http://localhost:8080`.

## Testowanie
Testy jednostkowe sprawdzają poprawność metod serwisów odpowiedzialnych za operacje na lotach i pasażerach.

Aby uruchomić testy, użyj komendy:
```bash
./gradlew test
```

## Dokumentacja
Dokumentacja endpointów znajduje się pod adresem `http://localhost:8080/swagger-ui/index.html` po uruchomieniu aplikacji. Klasy i metody opatrzone są dokumentacją JavaDoc.
