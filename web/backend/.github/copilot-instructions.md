# AutoCart Backend - Copilot Instructions

## Overview
AutoCart is a Spring Boot 4.0.4 e-commerce backend application using Java 17, PostgreSQL, and Maven with a **package-by-feature architecture**.
This document serves as a comprehensive guide for AI coding agents to understand the project's structure, conventions, and best practices. It covers architecture patterns, coding standards, REST API design, common tasks, database design notes, security settings, development workflow, anti-patterns to avoid, and resources for further learning.

### Project Structure
```
src/main/java/com/n2t/autocart/
├── AutocartApplication.java          # Spring Boot entry point
├── common/                           # Shared utilities
│   ├── ResponseFormater.java         # Global response wrapper
│   ├── ResponseWrapper.java          # Response DTO
│   └── anotation/                    # Custom annotations
│       └── ApiMessage.java           # API message annotation
├── config/                           # Spring configuration
├── exception/                        # Global exception handling
│   ├── GlobalExceptionHandler.java   # Exception interceptor
│   ├── ErrorCode.java                # Error code enum
│   └── ErrorResponse.java            # Error response DTO
├── modules/                          # Business features (package-by-feature)
│   ├── account/
│   ├── cart/
│   ├── discount/
│   ├── location/
│   ├── order/
│   ├── product/
│   ├── profile/
│   └── review/
└── security/                         # Security configuration
    └── SecurityConfiguration.java    # Auth/CORS setup
```

### Key Dependencies
- **Spring Boot Starters**: Data JPA, Security, WebMVC
- **Database**: PostgreSQL (JDBC driver)
- **ORM**: Hibernate (via Spring Data JPA)
- **Utils**: Lombok 1.18.30
- **Testing**: JUnit Jupiter + Spring Test modules

### Database Setup
- Use `application.yaml` for configuration
- Current: PostgreSQL on `localhost:5432`
- Credentials: `development_admin` / `900913`
- Hibernate DDL: `update` mode (auto-creates/updates schema)

---

## Architecture Patterns

### 1. Package-by-Feature Organization
Each business domain is a self-contained module under `modules/`:

```
modules/location/
├── controller/        # REST endpoints
├── service/          # Business logic
├── repository/       # Data access
├── entity/           # JPA entities
└── dto/              # Request/response DTOs
```

**Principle**: Keep related code together, minimize cross-module dependencies.

### 2. Layer Pattern: Controller → Service → Repository
- **Controller**: Validates input, calls service, returns response
- **Service**: Contains business logic, handles validation
- **Repository**: Extends `JpaRepository`, data access layer

### 3. Dependency Injection
Use **constructor injection** (required dependencies only):
```java
private final AddressRepository addressRepository;

public AddressService(AddressRepository addressRepository) {
    this.addressRepository = addressRepository;
}
```

Prefer `@RequiredArgsConstructor` from Lombok for brevity when possible.

### 4. Request/Response Types
**Request DTOs** (e.g., `AddressCreateRequest`):
- use records for immutable requests: `record UserCreateRequest(String name, String email) {}`

**Response**: Entity objects directly, wrapped by `ResponseFormater` globally

### 5. Entities
- Use Lombok: `@Entity`, `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Table`
- Use explicit column names: `@Column(name = "column_name")`
- Use `@JsonIgnore` on relationships to prevent circular serialization
- Use `FetchType.LAZY` for relationships (JPA best practice)

### 6. Repositories
Extend `JpaRepository<Entity, ID>` and add custom query methods as needed:
```java
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    // Custom queries via method naming or @Query
}
```

---

## Coding Conventions

### Naming
- **Packages**: lowercase, domain-focused (`modules.location`, `modules.account`)
- **Classes**: PascalCase (`AddressService`, `AddressController`)
- **Methods**: camelCase, verb-first (`handleCreateAddress`, `findById`)
- **Constants**: UPPER_SNAKE_CASE (rarely used in this project)

### Annotations & Imports
- Use Spring annotations: `@RestController`, `@Service`, `@Repository`
- Use Lombok: `@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@AllArgsConstructor`
- Use JPA: `@Entity`, `@Column`, `@ManyToOne`, `@FetchType.LAZY`
- Always use explicit imports (no star imports)

### Method Naming Convention
- Business logic methods: `handle` prefix (e.g., `handleCreateAddress`, `handleDeleteAddress`)
- Utility methods: action verb (e.g., `isValidAddress`, `mapToDTO`)

### Response Format
All successful responses automatically wrapped by `ResponseFormater`:
```json
{
  "success": true,
  "message": "Call API Success",
  "timestamp": "2026-03-22T10:30:00Z",
  "data": { /* entity */ }
}
```

Use `@ApiMessage` annotation on controller methods to customize the message:
```java
@PostMapping("/addresses")
@ApiMessage("Address created successfully")
public ResponseEntity<Address> createAddress(@RequestBody AddressCreateRequest request) {
    return ResponseEntity.ok(addressService.handleCreateAddress(request));
}
```

### Error Handling
Exceptions are caught by `GlobalExceptionHandler` and formatted. Currently all return `500 INTERNAL_SERVER_ERROR`.

To add error codes: define in `ErrorCode` enum and update handler as needed.

---

## REST API Patterns

### Endpoint Structure
```
[METHOD] /api/v1/{module}/{resource}[/{id}]
```

Examples:
- `POST /api/v1/locations/addresses` - Create address
- `GET /api/v1/locations/addresses/{id}` - Get address (if implemented)
- `PUT /api/v1/locations/addresses/{id}` - Update address (if implemented)
- `DELETE /api/v1/locations/addresses/{id}` - Delete address (if implemented)

### Request/Response Codes
- `200 OK` - GET, PUT, PATCH successful
- `201 CREATED` - POST successful (set via `ResponseEntity.status(HttpStatus.CREATED)`)
- `204 NO CONTENT` - DELETE successful
- `500 INTERNAL_SERVER_ERROR` - All exceptions (default behavior)

---

## Common Tasks

### Adding a New API Endpoint
1. Create/update `{Entity}CreateRequest.java` or `{Entity}UpdateRequest.java` in `dto/`
2. Add method to service interface/implementation
3. Add `@PostMapping` / `@PutMapping` / `@DeleteMapping` to controller
4. Use `ResponseEntity.ok()` or `.status(HttpStatus.CREATED)` for responses
5. Implement validation in service layer

### Example: Adding PUT endpoint
```java
// Controller
@PutMapping("/addresses/{id}")
public ResponseEntity<Address> updateAddress(@PathVariable Integer id, @RequestBody AddressUpdateRequest request) {
    return ResponseEntity.ok(addressService.handleUpdateAddress(id, request));
}

// Service
public Address handleUpdateAddress(Integer addressId, AddressUpdateRequest request) {
    Address address = addressRepository.findById(addressId)
        .orElseThrow(() -> new RuntimeException("Not found"));
    // Update fields
    return addressRepository.save(address);
}
```

## Database Design Notes

### Current Setup
- PostgreSQL with Hibernate auto-schema generation (`ddl-auto: update`)
- Entities: `Address`, `District`, `Province`, `Ward`, etc.
- Relationships: Address references District, Province, Ward (ManyToOne)

### Soft Deletes
If implementing soft deletes (e.g., `is_deleted` field), update repository queries to filter them out:
```java
@Query("SELECT a FROM Address a WHERE a.isDeleted = false")
List<Address> findAllActive();
```

---

## Security Settings

### Current Configuration
- CSRF disabled (` csrf.disable()`)
- All routes permitted (`auth.anyRequest().permitAll()`)
- Migration needed for authentication/authorization

### Future Improvements
- Implement JWT authentication
- Add proper authorization based on roles
- Restrict endpoints by user role
- Add API key validation

---

## Development Workflow

### IDE Setup
- **IntelliJ IDEA** or **VS Code** recommended
- Install Lombok plugin to avoid annotation processing errors
- Enable annotation processing in IDE settings

### Hot Reload
- Spring DevTools included
- Restart server via IDE or `Ctrl+Shift+F9` for full rebuild


## Anti-Patterns to Avoid

❌ **DO NOT**:
- Use `@Autowired` field injection (use constructor injection instead)
- Use `FetchType.EAGER` on relationships (causes N+1 query issues)
- Throw generic `RuntimeException` (create custom exceptions for clarity)
- Direct database access in controllers (use services)
- Hardcode credentials (use `application.yaml` configuration)
- Use star imports (`import com.example.*`)
- Create circular dependencies between modules

---

## Resources & Next Steps

### Documentation
- [Spring Boot 4 Guide](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Lombok Documentation](https://projectlombok.org/*.html)

## Quick Examples

### Creating a New Module
```bash
# Structure
src/main/java/com/n2t/autocart/modules/product/
├── controller/ProductController.java
├── service/ProductService.java
├── repository/ProductRepository.java
├── entity/Product.java
└── dto/ProductCreateRequest.java
```

### Making API Changes
1. **Add field to entity**:
   ```java
   @Column(name = "price")
   private BigDecimal price;
   ```

2. **Add to DTO**:
   ```java
   public record ProductCreateRequest(String name, BigDecimal price) {}
   ```

3. **Update service**:
   ```java
   public Product handleCreateProduct(ProductCreateRequest request) {
       // Validation & mapping
       Product product = new Product();
       product.setName(request.name());
       product.setPrice(request.price());
       return productRepository.save(product);
   }
   ```

---

**Last Updated**: March 22, 2026  
**Project**: AutoCart Backend v0.0.1  
**Team**: Spring Boot Development Team
