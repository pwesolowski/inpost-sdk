# Contributing

Dziękujemy za chęć kontrybuowania!

## Zasady
- Kotlin, styl: `official`, formatowanie przez Spotless/ktlint.
- Testy jednostkowe JUnit5 + MockK, assertions: Kotest.
- Komity: zwięzłe, opisowe; PR z krótkim opisem zmian.

## Uruchamianie
```bash
./gradlew build
```

## Wydania
- Wersjonowanie SemVer.
- Publikacja do Maven Central przez `nexusPublishing`.
- Artefakty podpisywane kluczem PGP (zmienne `signing.*`).

