# Aplikacja Spring Boot - Strona Wizytówka

## Opis projektu
Prosta aplikacja Spring Boot z dwoma podstronami:
- **Strona główna** (`/`) - Witamy użytkowników i prezentujemy informacje o firmie
- **Kontakt** (`/kontakt`) - Wyświetla dane kontaktowe firmy

## Technologie
- Java 21
- Spring Boot 3.2.1
- Spring MVC
- Thymeleaf (silnik szablonów)
- Maven

## Struktura projektu
```
src/
├── main/
│   ├── java/
│   │   └── com/comarch/szkolenia/
│   │       ├── Application.java (główna klasa aplikacji)
│   │       └── controller/
│   │           └── HomeController.java (kontroler obsługujący endpointy)
│   └── resources/
│       ├── templates/
│       │   ├── index.html (strona główna)
│       │   └── kontakt.html (strona kontaktowa)
│       └── application.properties (konfiguracja aplikacji)
```

## Jak uruchomić aplikację

### Wymagania
- Java 21 lub nowsza
- Maven 3.6+ lub użyj Maven z IDE

### Metoda 1: Z poziomu IDE (IntelliJ IDEA)
1. Otwórz projekt w IntelliJ IDEA
2. Poczekaj na automatyczne pobranie zależności Maven
3. Uruchom klasę `Application.java` (prawy przycisk myszy → Run 'Application.main()')
4. Aplikacja uruchomi się na porcie 8080

### Metoda 2: Z linii poleceń (jeśli Maven jest zainstalowany)
```bash
mvn clean install
mvn spring-boot:run
```

### Metoda 3: Uruchomienie JAR
```bash
mvn clean package
java -jar target/maven-1.0-SNAPSHOT.jar
```

## Dostęp do aplikacji
Po uruchomieniu aplikacji, otwórz przeglądarkę i wejdź na:
- **Strona główna**: http://localhost:8080/
- **Kontakt**: http://localhost:8080/kontakt

## Konfiguracja
Konfiguracja aplikacji znajduje się w pliku `src/main/resources/application.properties`:
- Port serwera: 8080
- Thymeleaf: włączony z wyłączonym cache (dla developmentu)

## Funkcjonalności
- ✅ Responsywny design
- ✅ Gradientowe tło
- ✅ Animacje CSS
- ✅ Nawigacja między stronami
- ✅ Integracja z Thymeleaf do dynamicznego renderowania treści
- ✅ Spring Boot DevTools dla hot reload (opcjonalnie)

## Modyfikacja treści
Aby zmienić treść strony, edytuj:
- **Dane kontaktowe**: `HomeController.java` (metoda `contact()`)
- **Wiadomość powitalna**: `HomeController.java` (metoda `home()`)
- **Wygląd stron**: pliki HTML w `src/main/resources/templates/`

## Autor
Comarch Szkolenia - Projekt demonstracyjny Spring Boot

