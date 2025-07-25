**Library Management System – Design Document**

**1. Introduction**\
This Library Management System is a command-line application written in Scala, following a layered architecture to separate concerns and improve maintainability. It supports:

- Loading and persisting data (books, users) as JSON.
- Core catalog operations (search, borrow/return).
- Input validation and error handling.

**2. Project Structure**

```
scala_library-main/
├── src/
│   ├── data/            # JSON fixtures (books.json, users.json)
│   ├── main/scala/
│   │   ├── main.scala    # CLI entrypoint (menu-driven)
│   │   ├── models/       # Domain entities and type definitions
│   │   ├── services/     # Core business logic (Catalog)
│   │   └── utils/        # I/O (CatalogIO) and Validators
│   └── test/scala/       # Unit tests for each layer
├── build.sbt             # SBT build definition
└── README.md             # Overview & setup instructions
```

**3. Architectural Overview**\
The system is organized into four layers:

- **Model Layer**: Immutable data structures representing domain entities (Book, User, Transaction, Genre).
- **Service Layer**: `Catalog` object encapsulating all business rules (search by title/author/genre, borrow/return logic, availability checks).
- **Utility Layer**: Helpers for serialization (`CatalogIO`) and runtime input checking (`Validators`).
- **Application Layer**: `main.scala` orchestrates user interaction (menu loops, parsing, dispatch to services).

This separation ensures that each component has a single responsibility and can evolve independently.

**4. Model Layer**

- **Immutable Case Classes** (`models/*.scala`):
    - `BookEntity`: wraps a `Book` (ISBN, title, author, genre, available copies).
    - `User`: captures user details and current loans.
    - `Transaction`: records borrow/return events with timestamps.
    - **Value Objects**: `Genre` (sealed trait with subtypes), `ISBN`, `UserId` enforce domain constraints at compile-time.

**Design Decisions:**

- Use of Scala’s case classes and sealed traits promotes pattern matching and exhaustive checking.
- Strongly typed IDs reduce mixing of raw strings and prevent invalid states.

**5. Service Layer**

- Singleton (`services/Catalog.scala`):
    - Provides pure functions for:
        - Searching catalog by title, author, or genre.
        - Checking out and returning books (updating availability and user loans).
        - Retrieving borrow history.

**Design Decisions:**

- Stateless, pure functional methods improve testability.
- All mutations occur in the `CatalogIO` layer, keeping business logic free of side effects.

**6. Utility Layer**

- (`utils/CatalogIO.scala`):
    - Reads/writes JSON using built-in Scala libraries.
    - Ensures data consistency by atomically loading and saving entire collections.
- (`utils/Validators.scala`):
    - Encapsulates input parsing and validation (e.g., ISBN format, numeric menus).

**Design Decisions:**

- Centralizing I/O details abstracts persistence format from services.
- Validators prevent invalid user input from propagating into core logic.

**7. Application Layer (CLI)**

- Bootstraps file paths (relative to project root).
- Loads initial state via `CatalogIO`.
- Presents role-based menus (Student vs. Faculty), handles user commands in loops.
- Uses `parseGenre` helper for free-text genre input.

**Design Decisions:**

- A simple, imperative menu loop is sufficient for CLI tools; complex UI frameworks are avoided.
- Clear separation: parsing user choices in `main`, delegating to service methods.

**8. Data Persistence**

- JSON files under `src/data/` hold initial datasets.
- On each borrow/return event, `CatalogIO.saveBooks` and `saveUsers` overwrite files, ensuring ACID-like safety for this scale.

**9. Testing Strategy**

- **Unit Tests** in `src/test/scala/`:
    - **Models**: validate JSON (de)serialization, immutability invariants.
    - **Services**: business rules (e.g., cannot borrow if no copies available).
    - **Utilities**: I/O success/failure paths, input parsing edge cases.