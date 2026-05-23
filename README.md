# Karton Predmeta

Ovaj projekat predstavlja Spring Boot aplikaciju za upravljanje kartonom predmeta.

Glavni entitet aplikacije je predmet. Predmet može biti dodat, pregledan, izmenjen i obrisan.

Pored predmeta, aplikacija omogućava rad sa nastavnicima i ishodima. Nastavnici se koriste kod izvođenja nastave, a ishodi predstavljaju šifarnik ishoda učenja koji se vezuju za predmete.

Predmet može imati osnovne podatke, ishode učenja, literaturu i izvođenja nastave. Literatura i izvođenja se mogu dodavati i uklanjati sa predmeta.

U aplikaciji postoji i jednostavan frontend napravljen kroz HTML, CSS i JavaScript. Frontend koristi REST endpoint-e backend aplikacije.

Projekat je razvijen pomoću Spring Boot-a, MySQL baze podataka, Liquibase-a za migracije baze, Swagger-a za testiranje endpoint-a, kao i biblioteka Spring Data JPA, MapStruct, Lombok i Bean Validation.

# Struktura projekta

Aplikacija ima sledeću strukturu:

Controller
Service
Repository
DTO
Entity
Mapper
Exception

Kontroleri koriste DTO klase umesto Entity klasa za primanje i vraćanje podataka. Dodat je i globalni exception handler za obradu grešaka.

# Baza i pokretanje

Potrebna je MySQL baza podataka pod nazivom karton_predmeta.

Pre pokretanja treba proveriti podešavanja u application.properties fajlu.

Aplikacija se pokreće preko klase KartonPredmetaApplication.

Pri pokretanju aplikacije Liquibase kreira tabele i ubacuje početne podatke za testiranje.

# Frontend

Frontend fajlovi se nalaze u folderu:

src/main/resources/static

Dostupne stranice su:

http://localhost:8080/predmeti.html
http://localhost:8080/nastavnici.html
http://localhost:8080/ishodi.html
http://localhost:8080/predmet-forma.html

Frontend omogućava pregled, dodavanje, izmenu i brisanje podataka kroz jednostavne stranice.

# Endpoint-i

Aplikacija uključuje endpoint-e za predmete:

GET /api/predmeti
GET /api/predmeti/{id}
POST /api/predmeti
PUT /api/predmeti/{id}
DELETE /api/predmeti/{id}
DELETE /api/predmeti/literatura/{literaturaId}
DELETE /api/predmeti/izvodjenja/{izvodjenjeId}

Endpoint-i za nastavnike:

GET /api/nastavnici
GET /api/nastavnici/{id}
POST /api/nastavnici
PUT /api/nastavnici/{id}
DELETE /api/nastavnici/{id}

Endpoint-i za ishode:

GET /api/ishodi
GET /api/ishodi/{id}
POST /api/ishodi
DELETE /api/ishodi/{id}

# Swagger

Swagger dokumentacija se može pronaći na linku:

http://localhost:8080/swagger-ui/index.html

Kroz Swagger se mogu testirati dostupni endpoint-i aplikacije.
