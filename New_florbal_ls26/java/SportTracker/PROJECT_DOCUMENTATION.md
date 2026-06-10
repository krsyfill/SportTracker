# Rozšířená dokumentace projektu SportTracker

Tento dokument poskytuje hloubkovou technickou analýzu a popis implementace projektu SportTracker.

## 1. Technologický zásobník (Tech Stack)

Projekt je postaven na moderní architektuře Java s využitím ekosystému Spring:

*   **Java 17 (LTS)**: Využívá moderní prvky jazyka, jako jsou Records (pokud by byly použity) a vylepšené API pro práci s kolekcemi.
*   **Spring Boot 4.0.6**: Poskytuje základní infrastrukturu pro auto-konfiguraci, dependency injection a vestavěný server (Tomcat).
*   **Spring Data JPA**: Abstraktní vrstva nad Hibernate pro snadnou manipulaci s PostgreSQL databází pomocí repozitářů.
*   **Spring Web MVC**: Implementace RESTful API s využitím anotací `@RestController`.
*   **PostgreSQL**: Produkční relační databáze zajišťující integritu dat.
*   **Jakarta Validation (Hibernate Validator)**: Zajišťuje, že data vstupující do systému splňují definovaná pravidla (např. `@NotBlank`, `@Positive`).
*   **Maven**: Správa životního cyklu projektu, závislostí a sestavení do spustitelného JAR souboru.

---

## 2. Architektura a struktura balíčků

Aplikace sleduje klasickou vícevrstvou architekturu:

1.  **`config`**: Globální nastavení aplikace nezávislé na business logice.
2.  **`entity`**: Datový model (Persistence Layer). Mapování objektů na tabulky v DB.
3.  **`repository`**: Abstrakce přístupu k datům (Data Access Object - DAO).
4.  **`service`**: Business logika (Service Layer). Zde probíhají výpočty a validace procesů.
5.  **`controller`**: Vstupní bod pro externí požadavky (API Layer).
6.  **`exception`**: Centralizované zpracování chybových stavů.

---

## 3. Detailní rozbor komponent

### 3.1. Datové entity (`entity`)

Každá entita je mapována na tabulku v databázi a obsahuje validační anotace pro zajištění kvality dat.

*   **`Player.java`**
    *   **Účel**: Evidence osobních údajů sportovců.
    *   **Kód**:
        ```java
        @Entity
        @Table(name = "players")
        public class Player {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            @NotBlank
            private String firstName;

            @NotBlank
            private String lastName;

            private String phone;
            private LocalDate birthDate;

            @Size(max = 255)
            private String note;
        }
        ```
    *   **Klíčová pole**: `firstName`, `lastName` (povinná přes `@NotBlank`), `phone`, `birthDate`, `note` (limit 255 znaků přes `@Size`).
    *   **Specifikum**: Základní stavební kámen, na který se vážou platby a docházka.

*   **`Training.java`**
    *   **Účel**: Definice tréninkových jednotek.
    *   **Kód**:
        ```java
        @Entity
        @Table(name = "trainings")
        public class Training {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            @NotNull
            private LocalDate trainingDate;

            @Enumerated(EnumType.STRING)
            private TrainingStatus status = TrainingStatus.PLANNED;
        }
        ```
    *   **Klíčová pole**: `trainingDate` (povinné), `status` (Enum `TrainingStatus`).
    *   **Specifikum**: Stav tréninku (`PLANNED` vs `FINISHED`) zásadně ovlivňuje možnost registrace hráčů.

*   **`Attendance.java`**
    *   **Účel**: Spojovací tabulka (Many-to-Many s daty) mezi `Player` a `Training`.
    *   **Kód**:
        ```java
        @Entity
        @Table(name = "attendance")
        public class Attendance {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            private Boolean plannedAttendance;
            private Boolean actualAttendance;

            @ManyToOne
            @JoinColumn(name = "player_id")
            private Player player;

            @ManyToOne
            @JoinColumn(name = "training_id")
            private Training training;
        }
        ```
    *   **Klíčová pole**: `plannedAttendance` (přihlášen), `actualAttendance` (skutečně přítomen).
    *   **Vazby**: `@ManyToOne` na Player i Training.
    *   **Specifikum**: Umožňuje sledovat rozdíl mezi "slíbenou" a "reálnou" účastí.

*   **`Payment.java`**
    *   **Účel**: Finanční evidence.
    *   **Kód**:
        ```java
        @Entity
        @Table(name = "payments")
        public class Payment {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            @Positive
            @NotNull
            private BigDecimal amount;

            @NotNull
            private LocalDate paymentDate;

            private String note;

            @ManyToOne
            @JoinColumn(name = "player_id")
            private Player player;
        }
        ```
    *   **Klíčová pole**: `amount` (musí být kladné `@Positive`), `paymentDate`, `note`.
    *   **Specifikum**: Používá `BigDecimal` pro finanční přesnost, což zamezuje chybám při zaokrouhlování u `double`.

---

### 3.2. Servisní vrstva (`service`) - Business Logika

Zde se nachází nejdůležitější kód aplikace.

*   **`PlayerService.java`**
    *   `getAttendanceRate(Long playerId)`: Vypočítává procentuální docházku. 
        *   **Kód**:
            ```java
            public double getAttendanceRate(Long playerId) {
                if (!playerRepository.existsById(playerId)) {
                    throw new PlayerNotFoundException("Player not found");
                }
                long totalAttendance = attendanceRepository.countByPlayer_Id(playerId);
                if (totalAttendance == 0) {
                    return 0;
                }
                long actualAttendance = attendanceRepository.countByPlayer_IdAndActualAttendanceTrue(playerId);
                return ((double) actualAttendance / totalAttendance) * 100;
            }
            ```
        *   *Logika*: `(počet skutečných účastí / počet celkových registrací) * 100`.
        *   *Ošetření*: Pokud hráč nemá žádnou registraci, vrací 0 (zamezení dělení nulou).

*   **`TrainingService.java`**
    *   `registerPlayerToTraining(...)`: Zajišťuje integritu registrací.
        *   **Kód**:
            ```java
            public void registerPlayerToTraining(Long trainingId, Long playerId) {
                Player player = playerRepository.findById(playerId)
                        .orElseThrow(() -> new PlayerNotFoundException("Player not found"));
                Training training = trainingRepository.findById(trainingId)
                        .orElseThrow(() -> new TrainingNotFoundException("Training not found"));

                if (training.getStatus() == TrainingStatus.FINISHED) {
                    throw new TrainingClosedException("Training is already finished");
                }
                if (attendanceRepository.existsByPlayerAndTraining(player, training)) {
                    throw new AttendanceAlreadyExistsException("Attendance already exists");
                }

                Attendance attendance = new Attendance();
                attendance.setPlannedAttendance(true);
                attendance.setActualAttendance(false);
                attendance.setPlayer(player);
                attendance.setTraining(training);
                attendanceRepository.save(attendance);
            }
            ```
        *   *Kontroly*: Zda trénink/hráč existuje, zda trénink není uzavřen (`FINISHED`) a zda hráč již není registrován (`AttendanceAlreadyExistsException`).
    *   `unregisterPlayerFromTraining(...)`: Odstraní záznam o docházce, pokud je trénink stále ve stavu `PLANNED`.
    *   `closeTraining(...)`: Přepne stav na `FINISHED`. Po tomto kroku jsou registrace uzamčeny.

*   **`PaymentService.java`**
    *   `getPlayerBalance(Long playerId)`: Sumace všech plateb hráče.
        *   **Kód**:
            ```java
            public BigDecimal getPlayerBalance(Long playerId) {
                if (!playerRepository.existsById(playerId)) {
                    throw new PlayerNotFoundException("Player not found with id: " + playerId);
                }
                List<Payment> payments = paymentRepository.findByPlayer_Id(playerId);
                return payments.stream()
                        .map(Payment::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            ```
        *   *Technologie*: Využívá Java Streams (`reduce(BigDecimal.ZERO, BigDecimal::add)`) pro čistý a funkcionální zápis součtu.

---

### 3.3. REST Kontrolery (`controller`)

Zprostředkovávají přístup k funkcím přes HTTP.

*   **`PlayerController`**: Endpointy `/players`. Umožňuje CRUD operace a získání úspěšnosti docházky.
*   **`TrainingController`**: Endpointy `/trainings`. Obsahuje komplexnější operace jako `/register`, `/unregister` a `/close`.
*   **`AttendanceController`**: Endpoint `/attendance/{id}/actual`. Slouží trenérovi k potvrzení, že hráč na trénink skutečně dorazil.
*   **`PaymentController`**: Endpointy pod `/players/{playerId}/payments` a `/balance`. Sleduje finanční toky konkrétního hráče.

---

### 3.4. Správa výjimek (`exception`)

Aplikace využívá **`GlobalExceptionHandler`** s anotací `@RestControllerAdvice`.

*   **Princip**: Pokud jakákoliv část kódu vyhodí např. `PlayerNotFoundException`, tento handler ji zachytí a převede na standardizovanou JSON odpověď.
*   **Kód**:
    ```java
    @RestControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(PlayerNotFoundException.class)
        public ResponseEntity<Map<String, Object>> handlePlayerNotFoundException(PlayerNotFoundException ex) {
            return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String message) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", status.value());
            response.put("message", message);
            return new ResponseEntity<>(response, status);
        }
    }
    ```
*   **Struktura odpovědi**:
    ```json
    {
      "status": 404,
      "message": "Player not found"
    }
    ```
*   **Vlastní výjimky**: Každý chybový stav má svou třídu (např. `TrainingClosedException`), což umožňuje jemné ladění HTTP status kódů (404 vs 409 Conflict).

---

### 3.5. Datový přístup (`repository`)

Využívá sílu Spring Data JPA.

*   **`AttendanceRepository`**: Obsahuje klíčové metody pro statistiky:
    *   **Kód**:
        ```java
        public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
            boolean existsByPlayerAndTraining(Player player, Training training);
            long countByPlayer_Id(Long playerId);
            long countByPlayer_IdAndActualAttendanceTrue(Long playerId);
            Optional<Attendance> findByPlayer_IdAndTraining_Id(Long playerId, Long trainingId);
        }
        ```
    *   `existsByPlayerAndTraining`: Rychlá kontrola duplicity.
    *   `countByPlayer_IdAndActualAttendanceTrue`: Základ pro výpočet procentuální úspěšnosti.
    *   `findByPlayer_IdAndTraining_Id`: Vyhledání konkrétního vztahu pro odhlášení.

---

## 4. Konfigurace a ostatní soubory

*   **`WebConfig.java`**:
    *   **Kód**:
        ```java
        @Configuration
        public class WebConfig {
            @Bean
            public WebMvcConfigurer corsConfigurer() {
                return new WebMvcConfigurer() {
                    @Override
                    public void addCorsMappings(CorsRegistry registry) {
                        registry.addMapping("/**")
                                .allowedOrigins("*")
                                .allowedMethods("*");
                    }
                };
            }
        }
        ```
    *   Konfiguruje CORS. Aktuálně je nastaveno `allowedOrigins("*")`, což povoluje komunikaci s libovolným frontendem (např. React běžící na jiném portu).
*   **`application.properties`**:
    *   Obsahuje nastavení připojení k PostgreSQL a port aplikace (defaultně 8080).
*   **`data.sql`**:
    *   Obsahuje SQL inserty pro přednaplnění databáze testovacími daty při každém startu (pokud je tak Spring nastaven).
*   **`pom.xml`**:
    *   Definuje závislosti. Všimněte si `spring-boot-starter-validation`, který aktivuje validační procesy v entitách.

---

## 5. Klíčové procesní toky (Business Flows)

1.  **Životní cyklus tréninku**:
    *   Vytvoření (`PLANNED`) -> Registrace hráčů -> (Trénink proběhne) -> Zápis reálné docházky -> Uzavření tréninku (`FINISHED`).
2.  **Finanční bilance**:
    *   Hráč provádí platby (`Payment`), které jsou vázány na jeho ID. Bilance je kdykoliv vypočítatelná jako suma těchto plateb.
3.  **Výpočet docházky**:
    *   Probíhá v reálném čase. Jakákoliv změna v `Attendance` se okamžitě projeví v `getAttendanceRate` bez nutnosti ukládat mezivýsledky.
