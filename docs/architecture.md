# Architektura SDK

## Cele
- Prosty i czytelny interfejs.
- Asynchroniczność (suspend/Deferred).
- Modułowość i rozszerzalność.
- Brak twardych zależności na frameworki w `core`.

## Warstwy
- `inpost-core`
  - Konfiguracja klienta (`InpostClientConfig`).
  - Autoryzacja (`AuthCredentials`).
  - Interfejs klienta (`InpostClient`).
  - Transport HTTP: Ktor (wymienialny przy zachowaniu interfejsów).
  - Modele danych mapowane z Postman/OpenAPI.
- Integracje
  - `inpost-spring-starter`: autokonfiguracja, property binding, lifecycle.
  - `inpost-cli`: szybkie testy ręczne i przykłady.
- Testy kontraktowe
  - `inpost-contract-tests`: WireMock/Pact + kolekcje Postman.

## Stabilność API
- Publiczne typy w `core` traktowane jako kontrakt.
- Dodawanie pól opcjonalnych jest dozwolone; usuwanie/zmiana typów -> major.

## Konfiguracja
- `InpostClientConfig` przyjmuje `baseUrl`, `AuthCredentials` i timeouts.
- W Spring Boot: `InpostProperties` mapuje `application.properties`.

## Publikacja
- BOM (`inpost-bom`) zapewnia spójne wersje modułów.
- Publikacja do Maven Central z podpisywaniem artefaktów.

