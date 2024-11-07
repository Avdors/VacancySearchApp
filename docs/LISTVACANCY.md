## Feature: `listVacancy`

### Clean Architecture Structure

The `listVacancy` feature is organized following Clean Architecture principles, divided
into `data`, `domain`, and `presentation` layers:

- **Data Layer:** Manages data operations and sources (network and database). It contains models for
  networking and database storage and
- includes repositories for managing data flow.
- **Domain Layer:** Contains the business logic, including mappers, models, and use cases that
  operate independently
- of any external data sources or frameworks.
- **Presentation Layer:** Manages UI state, interaction, and business logic orchestration through
  ViewModel and UI-specific models.

### Layers and Classes Documentation

---

### **Data Layer**

This layer interacts with network and database sources, providing data to the domain layer.

#### Models

1. **`ModelListResponseOfferAndVacancies`**  
   A model representing the network response that includes lists of offers and vacancies. Serialized
   with `@Serializable` for network transfer.

2. **`DataModelListVacancyOffer`** Represents an offer in the network model.
   Includes fields such as `id`, `title`, `link`, and optional `button`.

3. **`DataModelListVacancy`** Represents a vacancy in the network model. Contains properties
   like `id`, `lookingNumber`, `title`, `address`, `company`, etc.

4. **`Button`, `DataModelListAddress`, `DataModelListExperience`, `DataModelListSalary`** These are
   nested data classes used within `DataModelListVacancy` to represent specific components like
   address, experience, salary, and button details.

#### Repository

1. **`ListVacancyRepositoryImpl`** Implements `ListVacancyRepository`, managing data retrieval from
   the local
   database (`LocalDataSource`) and network (`RemoteDataSource`). It also:

    - Fetches vacancies and offers from both DB and network.
    - Launches network load operations to refresh the cache.
    - Maps models to domain objects using `listVacancyDataMapper` and `listVacOfferDataMapper`.
    - Provides methods for saving and removing favorites.
2. **`LocalDataSource`** Manages database access for vacancies, offers, and favorite data, providing
   Flow-based access to
   vacancy and offer lists from Room DAO interfaces.

3. **`RemoteDataSource`** Handles data fetching from remote sources using `HttpClient`
   and `UrlProvider` for URL management.
   It parses responses using JSON deserialization and manages exception handling for network issues.

---

### **Domain Layer**

The domain layer encapsulates business logic and data transformations.

#### Mappers

1. **`ListVacancyDataMapper`**

    - Maps between network, database, and domain vacancy models.
    - Supports methods to convert:
        - Network to domain.
        - Database to domain.
        - Domain to database for caching and persistence.
2. **`ListVacOfferDataMaper`**

    - Maps offer data between the network model, database model, and domain model.
    - Similar to `ListVacancyDataMapper`, it provides methods for mapping across layers to maintain
      data consistency.

#### Models

1. **`ListOfferDomainModel`, `ListButton`** Domain models representing an offer and button for
   consistent usage within
   the domain and presentation layers.

2. **`ListVacancyDomainModel` and Nested Models** Represent a vacancy in the domain layer. Includes
   submodels
   for address, experience, and salary, ensuring clear separation from UI and data layer models.

#### Repository Interface

1. **`ListVacancyRepository`** Defines the contract for `ListVacancyRepositoryImpl`, specifying the
   data operations
   for retrieving vacancies and offers, managing favorites, and launching network updates.

#### Use Cases

1. **`FavoriteUseCase`** Provides business logic to manage favorite vacancies by adding or removing
   vacancies in
   the repository. Abstracts the database operations from the UI.

2. **`ListVacancyUseCase`** Orchestrates vacancy and offer data retrieval from the repository, and
   launches network
   load operations. Ensures that only necessary data transformations and business logic reside
   within this layer.

---

### **Presentation Layer**

Handles user interactions, view state, and data binding to the UI components.

#### Adapters

1. **`ListVacancyAdapter`** Manages a list of vacancies in a `RecyclerView`. Supports user
   interactions like:

    - Viewing more vacancies (with pagination).
    - Applying for a vacancy.
    - Adding/removing vacancies from favorites.
    - Each item view is handled by separate view holders, `VacancyViewHolder` for vacancy items
      and `ShowMoreViewHolder` for pagination.
2. **`ListVacOfferAdapter`** Displays a horizontal list of offers. Each offer includes optional
   details, such as a title, link, and
   an action button. Supports click events to handle interactions.

#### Models

1. **`ListVacancyModel` and Nested Models** UI-specific representations of vacancy data for binding
   in `ListVacancyAdapter`.
   Includes nested models for `Address`, `Experience`, and `Salary`.

2. **`ListOfferModel`, `ButtonModel`** UI-specific models for offer data used
   in `ListVacOfferAdapter`.

#### ViewModel

1. **`ListVacancyViewModel`** Coordinates between the domain use cases and UI components, providing:
    - Observables for vacancy and offer lists using `StateFlow`.
    - Methods to update the favorite status of vacancies.
    - Functions to observe data flows from `getVacanciesFromDB` and `getOffersFromDB` use cases.
    - Error handling to manage network or repository issues.
2. **`ListVacanciesFragment`** UI component responsible for displaying the list of vacancies and
   offers,
   observing data from `ListVacancyViewModel` and binding to `ListVacancyAdapter`
   and `ListVacOfferAdapter`.
   It initializes the UI, manages view interactions, and handles data updates.

---

### Dependency Injection with Koin

**`listVacancyModule`** defines Koin bindings:

- Registers `HttpClient`, `RemoteDataSource`, and `LocalDataSource` for data operations.
- Provides instances of mappers and the repository implementation.
- Binds use cases and `ListVacancyViewModel`, ensuring dependencies are injected following the Clean
  Architecture layers.