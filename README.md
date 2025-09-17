# InPost JVM SDK (Kotlin)

Modularne SDK do integracji z API InPost, zaprojektowane dla JVM z naciskiem na czytelne API, asynchroniczność i rozszerzalność.

## Moduły
- `inpost-core`: klient HTTP, modele danych, autoryzacja.
- `inpost-spring-starter`: automatyczna konfiguracja Spring Boot.
- `inpost-contract-tests`: testy kontraktowe, integracja z Postman/Pact/WireMock.
- `inpost-bom`: Bill of Materials do wyrównania wersji SDK.

## Wymagania
- Kotlin (JVM), domyślna toolchain: Java 17 (konfigurowalna przez `gradle.properties`).

## Instalacja (Maven/Gradle)
Publikacja do Maven Central: w toku. Artefakty będą dostępne jako `com.tlumaczeo.inpost:*`.

## Szybki start
```kotlin
val client = InpostClient.builder()
    .baseUrl("https://api.inpost.pl")
    .credentials(AuthCredentials.ApiKey("YOUR_KEY"))
    .build()
val resp = client.trackShipment("ABC123") // suspend
```

### Spring Boot
```properties
inpost.base-url=https://api.inpost.pl
inpost.auth.type=apiKey
inpost.auth.api-key=YOUR_KEY
```

## Testy
- Jednostkowe: JUnit 5, MockK, Kotest (assertions).
- Kontraktowe: moduł `inpost-contract-tests` korzysta z kolekcji Postman umieszczonych w katalogu `postman_collection/` oraz WireMock/Pact.

## Rozwój
- Formatowanie: Spotless + ktlint.
- Dokumentacja: Dokka.
- Wersjonowanie: SemVer (MAJOR.MINOR.PATCH).
- Publikacja: Sonatype (nexus-publish). Ustaw dane w `~/.gradle/gradle.properties` lub zmiennych środowiskowych.

## Architektura
- Rdzeń inspirowany AWS SDK v2 (czytelne interfejsy, asynchroniczność przez coroutines).
- Modułowość: `core` niezależny od frameworków; integracje jako oddzielne moduły.

## Postman
Umieść pliki `*.postman_collection.json` i `*environment*.postman_environment.json` w katalogu `postman_collection/` w repo. Moduł `inpost-contract-tests` kopiuje je do `inpost-contract-tests/postman` (zadanie `copyPostmanCollections`). Test `PostmanCollectionParsingTest` sprawdza możliwość parsowania kolekcji.

