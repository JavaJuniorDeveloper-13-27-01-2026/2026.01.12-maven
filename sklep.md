# Sklep z Pamiątkami - Plan Implementacji

## 📋 Spis Treści
1. [Opis Projektu](#opis-projektu)
2. [Wymagania Funkcjonalne](#wymagania-funkcjonalne)
3. [Technologie](#technologie)
4. [Architektura Aplikacji](#architektura-aplikacji)
5. [Model Danych](#model-danych)
6. [Plan Implementacji](#plan-implementacji)
7. [Struktura Projektu](#struktura-projektu)
8. [Endpointy API](#endpointy-api)
9. [Widoki (Thymeleaf)](#widoki-thymeleaf)
10. [Bezpieczeństwo](#bezpieczeństwo)
11. [Testowanie](#testowanie)

---

## 🎯 Opis Projektu

Aplikacja webowa sklepu z pamiątkami umożliwiająca:
- Przeglądanie katalogu produktów
- Dodawanie produktów do koszyka
- Składanie zamówień
- Zarządzanie produktami przez administratora
- Rejestrację i logowanie użytkowników

---

## ⚙️ Wymagania Funkcjonalne

### Dla Klientów:
- ✅ Przeglądanie listy produktów
- ✅ Wyszukiwanie produktów po nazwie
- ✅ Filtrowanie produktów po kategorii
- ✅ Wyświetlanie szczegółów produktu
- ✅ Dodawanie produktów do koszyka
- ✅ Zarządzanie koszykiem (dodawanie, usuwanie, zmiana ilości)
- ✅ Składanie zamówienia
- ✅ Przeglądanie historii zamówień
- ✅ Rejestracja i logowanie

### Dla Administratora:
- ✅ Zarządzanie produktami (CRUD)
- ✅ Zarządzanie kategoriami
- ✅ Przeglądanie wszystkich zamówień
- ✅ Zmiana statusu zamówienia
- ✅ Zarządzanie użytkownikami

---

## 🛠️ Technologie

### Backend:
- **Java 21** - język programowania
- **Spring Boot 3.2.x** - framework aplikacyjny
- **Spring MVC** - warstwa webowa
- **Spring Data JPA** - warstwa dostępu do danych
- **Spring Security** - autoryzacja i uwierzytelnianie
- **Hibernate** - ORM (Object-Relational Mapping)
- **Maven** - zarządzanie zależnościami i build

### Frontend:
- **Thymeleaf** - silnik szablonów
- **HTML5** - struktura stron
- **CSS3** - stylowanie
- **Bootstrap 5** - responsywny framework CSS
- **JavaScript** - interakcje po stronie klienta

### Baza Danych:
- **H2 Database** - baza danych w pamięci (development)
- **MySQL/PostgreSQL** - baza danych produkcyjna (opcjonalnie)

### Dodatkowe:
- **Lombok** - redukcja boilerplate code
- **Validation API** - walidacja danych
- **JUnit 5** - testy jednostkowe
- **MockMvc** - testy integracyjne

---

## 🏗️ Architektura Aplikacji

Aplikacja będzie oparta na wzorcu **MVC (Model-View-Controller)** z podziałem na warstwy:

```
┌─────────────────────────────────────────┐
│           Warstwa Prezentacji           │
│  (Thymeleaf Templates + Controllers)    │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│          Warstwa Biznesowa              │
│            (Services)                   │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│       Warstwa Dostępu do Danych         │
│         (Repositories/DAO)              │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│           Baza Danych                   │
│           (H2/MySQL)                    │
└─────────────────────────────────────────┘
```

### Warstwy:

1. **Controllers** - obsługa requestów HTTP, zwracanie widoków
2. **Services** - logika biznesowa aplikacji
3. **Repositories** - dostęp do bazy danych (Spring Data JPA)
4. **Models/Entities** - encje domenowe mapowane na tabele
5. **DTOs** - obiekty transferu danych
6. **Views** - szablony Thymeleaf

---

## 💾 Model Danych

### Główne Encje:

#### 1. **Product (Produkt)**
```java
- id: Long (PK)
- name: String
- description: String
- price: BigDecimal
- stockQuantity: Integer
- imageUrl: String
- category: Category (ManyToOne)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

#### 2. **Category (Kategoria)**
```java
- id: Long (PK)
- name: String
- description: String
- products: List<Product> (OneToMany)
```

#### 3. **User (Użytkownik)**
```java
- id: Long (PK)
- username: String (unique)
- email: String (unique)
- password: String (encrypted)
- firstName: String
- lastName: String
- role: Role (ENUM: USER, ADMIN)
- orders: List<Order> (OneToMany)
- createdAt: LocalDateTime
```

#### 4. **Order (Zamówienie)**
```java
- id: Long (PK)
- orderNumber: String (unique)
- user: User (ManyToOne)
- orderItems: List<OrderItem> (OneToMany)
- totalAmount: BigDecimal
- status: OrderStatus (ENUM: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
- shippingAddress: String
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

#### 5. **OrderItem (Pozycja Zamówienia)**
```java
- id: Long (PK)
- order: Order (ManyToOne)
- product: Product (ManyToOne)
- quantity: Integer
- price: BigDecimal (cena w momencie zamówienia)
```

#### 6. **Cart (Koszyk)**
```java
- id: Long (PK)
- user: User (OneToOne)
- cartItems: List<CartItem> (OneToMany)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

#### 7. **CartItem (Pozycja Koszyka)**
```java
- id: Long (PK)
- cart: Cart (ManyToOne)
- product: Product (ManyToOne)
- quantity: Integer
```

### Relacje między Encjami:
- **Category** ↔ **Product**: One-to-Many
- **User** ↔ **Order**: One-to-Many
- **Order** ↔ **OrderItem**: One-to-Many
- **Product** ↔ **OrderItem**: Many-to-One
- **User** ↔ **Cart**: One-to-One
- **Cart** ↔ **CartItem**: One-to-Many
- **Product** ↔ **CartItem**: Many-to-One

---

## 📝 Plan Implementacji

### Etap 1: Konfiguracja Projektu (1-2 dni)
1. ✅ Utworzenie projektu Spring Boot
2. ✅ Konfiguracja Maven (pom.xml)
3. ✅ Dodanie zależności:
   - spring-boot-starter-web
   - spring-boot-starter-data-jpa
   - spring-boot-starter-thymeleaf
   - spring-boot-starter-security
   - spring-boot-starter-validation
   - h2 database
   - lombok
   - bootstrap (webjars)
4. ✅ Konfiguracja application.properties
5. ✅ Struktura pakietów

### Etap 2: Warstwa Modelu (2-3 dni)
1. ✅ Utworzenie encji Product
2. ✅ Utworzenie encji Category
3. ✅ Utworzenie encji User
4. ✅ Utworzenie encji Order i OrderItem
5. ✅ Utworzenie encji Cart i CartItem
6. ✅ Dodanie relacji między encjami
7. ✅ Dodanie walidacji (@NotNull, @Size, etc.)
8. ✅ Utworzenie enumów (Role, OrderStatus)

### Etap 3: Warstwa Repository (1 dzień)
1. ✅ Utworzenie interfejsów Repository dla każdej encji:
   - ProductRepository extends JpaRepository
   - CategoryRepository extends JpaRepository
   - UserRepository extends JpaRepository
   - OrderRepository extends JpaRepository
   - CartRepository extends JpaRepository
2. ✅ Dodanie custom query methods (findByName, findByCategory, etc.)

### Etap 4: Warstwa Service (3-4 dni)
1. ✅ ProductService (CRUD, wyszukiwanie, filtrowanie)
2. ✅ CategoryService (CRUD)
3. ✅ UserService (rejestracja, zarządzanie użytkownikami)
4. ✅ OrderService (tworzenie zamówień, historia, zarządzanie statusem)
5. ✅ CartService (dodawanie, usuwanie, aktualizacja koszyka)
6. ✅ Implementacja logiki biznesowej
7. ✅ Obsługa transakcji (@Transactional)

### Etap 5: Warstwa Controller (3-4 dni)
1. ✅ HomeController (strona główna)
2. ✅ ProductController (lista produktów, szczegóły)
3. ✅ CategoryController (produkty wg kategorii)
4. ✅ CartController (zarządzanie koszykiem)
5. ✅ OrderController (składanie zamówienia, historia)
6. ✅ UserController (rejestracja, login, profil)
7. ✅ AdminController (panel administracyjny)
8. ✅ SearchController (wyszukiwanie produktów)

### Etap 6: Widoki Thymeleaf (4-5 dni)
1. ✅ Layout template (header, footer, navigation)
2. ✅ index.html (strona główna)
3. ✅ products.html (lista produktów)
4. ✅ product-details.html (szczegóły produktu)
5. ✅ cart.html (koszyk)
6. ✅ checkout.html (finalizacja zamówienia)
7. ✅ order-history.html (historia zamówień)
8. ✅ login.html (logowanie)
9. ✅ register.html (rejestracja)
10. ✅ admin/products.html (zarządzanie produktami)
11. ✅ admin/orders.html (zarządzanie zamówieniami)
12. ✅ Dodanie CSS (Bootstrap + custom styles)

### Etap 7: Spring Security (2-3 dni)
1. ✅ Konfiguracja SecurityConfig
2. ✅ Implementacja UserDetailsService
3. ✅ Enkrypcja haseł (BCryptPasswordEncoder)
4. ✅ Konfiguracja autoryzacji (role USER, ADMIN)
5. ✅ Zabezpieczenie endpointów
6. ✅ Custom login page
7. ✅ Logout functionality

### Etap 8: Dane Testowe (1 dzień)
1. ✅ Utworzenie DataInitializer
2. ✅ Dodanie przykładowych kategorii
3. ✅ Dodanie przykładowych produktów
4. ✅ Utworzenie użytkownika testowego
5. ✅ Utworzenie admina testowego

### Etap 9: Walidacja i Obsługa Błędów (1-2 dni)
1. ✅ Walidacja formularzy (@Valid, BindingResult)
2. ✅ Obsługa wyjątków (@ExceptionHandler)
3. ✅ Strony błędów (404, 500)
4. ✅ Flash messages (success, error)

### Etap 10: Testy (2-3 dni)
1. ✅ Testy jednostkowe Service
2. ✅ Testy Repository
3. ✅ Testy integracyjne Controller (MockMvc)
4. ✅ Testy Security

### Etap 11: Optymalizacja i Deployment (1-2 dni)
1. ✅ Optymalizacja zapytań do bazy danych
2. ✅ Dodanie paginacji
3. ✅ Konfiguracja profili (dev, prod)
4. ✅ Przygotowanie do deploymentu
5. ✅ Dokumentacja (README.md)

---

## 📁 Struktura Projektu

```
src/
├── main/
│   ├── java/
│   │   └── com/comarch/szkolenia/sklep/
│   │       ├── SklepApplication.java
│   │       ├── config/
│   │       │   ├── SecurityConfig.java
│   │       │   └── WebConfig.java
│   │       ├── controller/
│   │       │   ├── HomeController.java
│   │       │   ├── ProductController.java
│   │       │   ├── CategoryController.java
│   │       │   ├── CartController.java
│   │       │   ├── OrderController.java
│   │       │   ├── UserController.java
│   │       │   └── admin/
│   │       │       ├── AdminProductController.java
│   │       │       └── AdminOrderController.java
│   │       ├── model/
│   │       │   ├── Product.java
│   │       │   ├── Category.java
│   │       │   ├── User.java
│   │       │   ├── Order.java
│   │       │   ├── OrderItem.java
│   │       │   ├── Cart.java
│   │       │   ├── CartItem.java
│   │       │   └── enums/
│   │       │       ├── Role.java
│   │       │       └── OrderStatus.java
│   │       ├── repository/
│   │       │   ├── ProductRepository.java
│   │       │   ├── CategoryRepository.java
│   │       │   ├── UserRepository.java
│   │       │   ├── OrderRepository.java
│   │       │   ├── OrderItemRepository.java
│   │       │   ├── CartRepository.java
│   │       │   └── CartItemRepository.java
│   │       ├── service/
│   │       │   ├── ProductService.java
│   │       │   ├── CategoryService.java
│   │       │   ├── UserService.java
│   │       │   ├── OrderService.java
│   │       │   ├── CartService.java
│   │       │   └── impl/
│   │       │       ├── ProductServiceImpl.java
│   │       │       ├── CategoryServiceImpl.java
│   │       │       ├── UserServiceImpl.java
│   │       │       ├── OrderServiceImpl.java
│   │       │       └── CartServiceImpl.java
│   │       ├── dto/
│   │       │   ├── ProductDTO.java
│   │       │   ├── OrderDTO.java
│   │       │   ├── UserRegistrationDTO.java
│   │       │   └── CartItemDTO.java
│   │       ├── security/
│   │       │   └── CustomUserDetailsService.java
│   │       ├── exception/
│   │       │   ├── ProductNotFoundException.java
│   │       │   ├── UserNotFoundException.java
│   │       │   └── GlobalExceptionHandler.java
│   │       └── util/
│   │           ├── DataInitializer.java
│   │           └── OrderNumberGenerator.java
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       ├── application-prod.properties
│       ├── static/
│       │   ├── css/
│       │   │   └── style.css
│       │   ├── js/
│       │   │   └── cart.js
│       │   └── images/
│       │       └── products/
│       └── templates/
│           ├── fragments/
│           │   ├── header.html
│           │   ├── footer.html
│           │   └── navigation.html
│           ├── index.html
│           ├── products.html
│           ├── product-details.html
│           ├── cart.html
│           ├── checkout.html
│           ├── order-confirmation.html
│           ├── order-history.html
│           ├── login.html
│           ├── register.html
│           ├── profile.html
│           ├── admin/
│           │   ├── dashboard.html
│           │   ├── products.html
│           │   ├── product-form.html
│           │   ├── orders.html
│           │   └── users.html
│           └── error/
│               ├── 404.html
│               └── 500.html
└── test/
    └── java/
        └── com/comarch/szkolenia/sklep/
            ├── service/
            │   ├── ProductServiceTest.java
            │   ├── OrderServiceTest.java
            │   └── CartServiceTest.java
            ├── repository/
            │   └── ProductRepositoryTest.java
            └── controller/
                └── ProductControllerTest.java
```

---

## 🌐 Endpointy API

### Publiczne (bez autoryzacji):
```
GET  /                          - Strona główna
GET  /products                  - Lista wszystkich produktów
GET  /products/{id}             - Szczegóły produktu
GET  /products/category/{id}    - Produkty z kategorii
GET  /products/search           - Wyszukiwanie produktów
GET  /login                     - Strona logowania
POST /login                     - Logowanie użytkownika
GET  /register                  - Strona rejestracji
POST /register                  - Rejestracja użytkownika
GET  /logout                    - Wylogowanie
```

### Dla zalogowanych użytkowników (USER):
```
GET  /cart                      - Wyświetlenie koszyka
POST /cart/add/{productId}      - Dodanie produktu do koszyka
POST /cart/update/{itemId}      - Aktualizacja ilości w koszyku
POST /cart/remove/{itemId}      - Usunięcie produktu z koszyka
POST /cart/clear                - Wyczyszczenie koszyka

GET  /checkout                  - Strona finalizacji zamówienia
POST /checkout                  - Złożenie zamówienia

GET  /orders                    - Historia zamówień
GET  /orders/{id}               - Szczegóły zamówienia

GET  /profile                   - Profil użytkownika
POST /profile/update            - Aktualizacja profilu
```

### Dla administratora (ADMIN):
```
GET  /admin                     - Panel administracyjny
GET  /admin/products            - Zarządzanie produktami
GET  /admin/products/new        - Formularz nowego produktu
POST /admin/products            - Utworzenie produktu
GET  /admin/products/edit/{id}  - Edycja produktu
POST /admin/products/update     - Aktualizacja produktu
POST /admin/products/delete/{id}- Usunięcie produktu

GET  /admin/orders              - Zarządzanie zamówieniami
POST /admin/orders/{id}/status  - Zmiana statusu zamówienia

GET  /admin/users               - Zarządzanie użytkownikami
POST /admin/users/{id}/role     - Zmiana roli użytkownika
```

---

## 🎨 Widoki (Thymeleaf)

### Kluczowe Widoki:

#### 1. **Layout (fragments)**
- **header.html** - nagłówek z logo, nawigacją, koszykiem
- **footer.html** - stopka z informacjami kontaktowymi
- **navigation.html** - menu nawigacyjne

#### 2. **Strony Publiczne**
- **index.html** - strona główna z promowanymi produktami
- **products.html** - lista produktów z filtrowaniem i paginacją
- **product-details.html** - szczegóły produktu, dodaj do koszyka
- **login.html** - formularz logowania
- **register.html** - formularz rejestracji

#### 3. **Strony Użytkownika**
- **cart.html** - koszyk zakupowy, podsumowanie
- **checkout.html** - formularz zamówienia (adres dostawy)
- **order-confirmation.html** - potwierdzenie złożenia zamówienia
- **order-history.html** - lista zamówień użytkownika
- **profile.html** - dane użytkownika, zmiana hasła

#### 4. **Panel Administratora**
- **admin/dashboard.html** - statystyki, podsumowanie
- **admin/products.html** - tabela produktów, akcje CRUD
- **admin/product-form.html** - formularz dodawania/edycji produktu
- **admin/orders.html** - lista zamówień, zmiana statusu
- **admin/users.html** - zarządzanie użytkownikami

#### 5. **Strony Błędów**
- **error/404.html** - nie znaleziono strony
- **error/500.html** - błąd serwera

---

## 🔒 Bezpieczeństwo

### Spring Security - Konfiguracja:

1. **Autoryzacja i Uwierzytelnianie**
   - Logowanie przez formularz
   - Enkrypcja haseł (BCrypt)
   - Session management

2. **Role i Uprawnienia**
   ```
   USER:
   - Przeglądanie produktów
   - Dodawanie do koszyka
   - Składanie zamówień
   - Przeglądanie własnych zamówień
   
   ADMIN:
   - Wszystkie uprawnienia USER
   - Zarządzanie produktami (CRUD)
   - Zarządzanie zamówieniami
   - Zarządzanie użytkownikami
   ```

3. **Zabezpieczenie Endpointów**
   ```java
   /admin/**        -> ADMIN
   /cart/**         -> USER, ADMIN
   /orders/**       -> USER, ADMIN
   /profile/**      -> USER, ADMIN
   /**              -> permitAll (publiczne)
   ```

4. **CSRF Protection**
   - Włączone dla wszystkich POST requestów
   - Token w formularzach Thymeleaf

5. **Password Validation**
   - Minimalna długość: 8 znaków
   - Wymagane: wielkie/małe litery, cyfry

---

## 🧪 Testowanie

### Rodzaje Testów:

#### 1. **Testy Jednostkowe (Unit Tests)**
```java
// Service Layer
ProductServiceTest
- testFindAllProducts()
- testFindProductById()
- testCreateProduct()
- testUpdateProduct()
- testDeleteProduct()

OrderServiceTest
- testCreateOrder()
- testCalculateTotalAmount()
- testUpdateOrderStatus()

CartServiceTest
- testAddToCart()
- testRemoveFromCart()
- testClearCart()
```

#### 2. **Testy Repository**
```java
ProductRepositoryTest
- testFindByCategory()
- testFindByNameContaining()
- testFindByPriceBetween()
```

#### 3. **Testy Integracyjne (Integration Tests)**
```java
ProductControllerTest (MockMvc)
- testGetAllProducts()
- testGetProductById()
- testAddProductToCart()

OrderControllerTest
- testCheckoutProcess()
- testOrderHistory()
```

#### 4. **Testy Security**
```java
SecurityConfigTest
- testAccessPublicPages()
- testAccessUserPages()
- testAccessAdminPages()
- testUnauthorizedAccess()
```

---

## 📦 Zależności Maven (pom.xml)

```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- Bootstrap WebJars -->
    <dependency>
        <groupId>org.webjars</groupId>
        <artifactId>bootstrap</artifactId>
        <version>5.3.0</version>
    </dependency>
    
    <dependency>
        <groupId>org.webjars</groupId>
        <artifactId>jquery</artifactId>
        <version>3.7.0</version>
    </dependency>
    
    <!-- DevTools -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    
    <!-- Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## ⚙️ Konfiguracja (application.properties)

```properties
# Application Name
spring.application.name=Sklep z Pamiatkami

# Server Configuration
server.port=8080

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:sklepdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Thymeleaf Configuration
spring.thymeleaf.cache=false
spring.thymeleaf.enabled=true
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

# Logging
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=INFO
logging.level.com.comarch.szkolenia=DEBUG

# File Upload (dla zdjęć produktów)
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB
```

---

## 🚀 Uruchomienie Aplikacji

### Krok po kroku:

1. **Sklonuj/Otwórz projekt w IDE**
2. **Uruchom aplikację:**
   ```bash
   mvn spring-boot:run
   ```
   lub uruchom klasę główną `SklepApplication.java`

3. **Otwórz przeglądarkę:**
   - Aplikacja: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console

4. **Dane testowe:**
   - Admin: username: `admin`, password: `admin123`
   - User: username: `user`, password: `user123`

---

## 📈 Możliwe Rozszerzenia (Future Features)

- 🔍 Zaawansowane wyszukiwanie i filtrowanie
- 💳 Integracja z systemem płatności (Stripe, PayPal)
- 📧 Wysyłanie emaili (potwierdzenia zamówień)
- 🖼️ Upload zdjęć produktów
- ⭐ System ocen i recenzji produktów
- 📊 Statystyki i raporty dla admina
- 🌍 Wsparcie dla wielu języków (i18n)
- 📱 REST API dla aplikacji mobilnej
- 🔔 Powiadomienia push
- 💰 System rabatów i kuponów
- 📦 Śledzenie przesyłek
- 🎨 Personalizacja strony

---

## 📚 Dodatkowe Zasoby

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Bootstrap 5](https://getbootstrap.com/docs/5.3/)

---

## ✅ Checklist Implementacji

- [ ] Etap 1: Konfiguracja Projektu
- [ ] Etap 2: Warstwa Modelu
- [ ] Etap 3: Warstwa Repository
- [ ] Etap 4: Warstwa Service
- [ ] Etap 5: Warstwa Controller
- [ ] Etap 6: Widoki Thymeleaf
- [ ] Etap 7: Spring Security
- [ ] Etap 8: Dane Testowe
- [ ] Etap 9: Walidacja i Obsługa Błędów
- [ ] Etap 10: Testy
- [ ] Etap 11: Optymalizacja i Deployment

---

**Data utworzenia:** 2026-01-13
**Autor:** Comarch Szkolenia
**Wersja:** 1.0

