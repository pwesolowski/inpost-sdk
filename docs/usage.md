# Użycie SDK

## Core
```kotlin
val client = InpostClient.builder()
  .baseUrl("https://api.inpost.pl")
  .credentials(AuthCredentials.ApiKey("YOUR_KEY"))
  .build()
```

## Spring Boot
```properties
inpost.base-url=https://api.inpost.pl
inpost.auth.type=apiKey
inpost.auth.api-key=YOUR_KEY
```
```kotlin
@RestController
class ShipmentController(private val client: InpostClient) {
  @GetMapping("/track/{id}")
  suspend fun track(@PathVariable id: String) = client.trackShipment(id)
}
```

## CLI
```bash
./gradlew :inpost-cli:run --args="track ABC123"
```

## Testy kontraktowe i Postman
- Umieść pliki `*.postman_collection.json` i environment w katalogu `postman_collection/` w repo.
- Zadanie `:inpost-contract-tests:copyPostmanCollections` skopiuje je do `inpost-contract-tests/postman`.
- Test `PostmanCollectionParsingTest` demonstruje parsowanie kolekcji (Jackson).

