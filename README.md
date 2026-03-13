# Karton Predmeta

Ovaj projekat predstavlja backend REST API aplikaciju čiji je cilj upravljanje kartonom predmeta.

Glavni entitet aplikacije je predmet koji može biti dodat, pregledan, izmenjen i obrisan.

U aplikaciji takođe postoji funkcionalnost pretrage predmeta na osnovu naslova literature.

Projekat je razvijen uz pomoć Spring Boot tehnologije, MySQL baze podataka, Liquibase za migracije baze podataka,

Swagger za testiranje endpoint-ova aplikacije kao i biblioteke kao što su Spring Data JPA, MapStruct, Lombok i

Bean Validation.

## Struktura projekta

Aplikacija ima sledeću strukturu: Controller, Service, Repository, DTO, Entity, Mapper, Exception.

U ovom projektu kontroler koristi DTO umesto Entity za primanje i obradu zahteva. Dodat

je i globalni exception handler.

## Baza i pokretanje

Potrebna je MySQL baza podataka karton_predmeta.

Treba da se provere podešavanja u application.properties, zatim pokrene klasa KartonPredmetaApplication.

Pri pokretanju aplikacije Liquibase će kreirati tabele i ubaciti početne podatke za testiranje.

## Endpoint-i

Aplikacija uključuje endpoint-ove:

- `GET /api/predmeti`
- `GET /api/predmeti/{id}`
- `POST /api/predmeti`
- `PUT /api/predmeti/{id}`
- `DELETE /api/predmeti/{id}`
- `GET /api/predmeti/trazi-po-literaturi?naslov=...`

## Swagger

Dokumentacija za Swagger se može pronaći na sledećem linku:

`http://localhost:8080/swagger-ui/index.html`

Kroz Swagger možemo testirati sve dostupne endpoint-ove.

