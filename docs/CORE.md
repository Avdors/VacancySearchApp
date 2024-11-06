# Core module documentation

The **Core** module serves as the fundamental layer of the application, responsible for network
queries,
database access, and the overall infrastructure for other modules. It provides a set of classes and
interfaces that manage data retrieval,
storage, and transformation between the various layers of the application.

### Classes and their description

### 1. **KtorClientProvider**.

- **Purpose**: Provides a customized instance of `HttpClient` from the Ktor library to execute
  network requests.
- **Use**: Used in repositories to make HTTP requests to remote servers.
- **Main Settings**:
- **Logging**: Sets logging with `LogLevel.ALL` level for detailed logging of all requests and
  responses.
- **Content Negotiation**: Configures JSON serialization and deserialization
  with `kotlinx.serialization`.
- **Timeouts**: Specifies the wait time for requests, connections, and data reads.
- **Exception Handling**: Handles common network exceptions such as no connection or timeouts.
- **Response Validation**: Checks response statuses and throws exceptions for server errors (5xx
  codes).

### 2. **UrlProvider**.

- **Purpose**: Manages URLs used for network requests.
- **Use**: Provides URLs based on a key, such as for retrieving job data.
- **Functionality**: Contains a map of URLs and a `getUrl` method to retrieve them.

### 3. **DAO Interfaces**.

DAO (Data Access Object) interfaces define methods for interacting with a SQLite database through
the Room library.

#### a. **FavoritesDao**.

- **Purpose**: Manages operations on favorite jobs.
- **Methods**:
- `upsertItem`: Inserts or updates a vacancy in a favorite.
- `getVacancyByIdfromFavorite`: Retrieves a vacancy from a favorite by its ID.
- `getItemList`: Returns a flow (`Flow`) of the list of all favorite vacancies.
- `delete`: Deletes a job from the favorites.

#### b. **OfferDao**.

- **OfferDao**: Processes transactions related to offers (offers).
- **Methods**:
- `getAllOffers`: Gets a list of all offers.
- `getAllOffersFlow`: Gets the flow of all offers.
- `insertOffers`: Inserts a list of offers into the database.

#### c. **VacancyDao**.

- **VacancyDao**: Manages job vacancy data in the database.
- **Methods**:
- `getVacancyById`: Retrieves a vacancy by its ID.
- `getAllVacancies`: Gets a list of all vacancies.
- `getAllVacanciesFlow`: Returns the flow of all vacancies.
- `insertVacancies`: Inserts a list of vacancies into the database.

### 4. **Database Entities**.

These data classes represent tables in the Room database.

#### a. **FavoriteVacModelDataBase**.

- **FavoriteVacancy**: Represents a favorite vacancy in the database.
- **Fields**: Includes job information such as `id`, `title`, `company` and others.
- **Nested Classes**:
- `AddressFModelDataBase`.
- `ExperienceFModelDataBase`.
- `SalaryFModelDataBase`.

#### b. **OfferModelDataBase**.

- **Purpose**: Represents an offer in the database.
- **Fields**: `id`, `title`, `link` and optional `button`.
- **Nested class**:
- `ButtonModDataBase`.

#### c. **VacancyModelDataBase**

- **Purpose**: Represents a vacancy in the database.
- **Fields**: Similar to `FavoriteVacModelDataBase` but for all vacancies.
- **Nested Classes**:
- `AddressModelDataBase`.
- `ExperienceModelDataBase`.
- `SalaryModelDataBase`.

### 5. **AppDatabase**.

- **AppDatabase**: Room main database class providing access to the DAO.
- **Entities**: Contains tables for jobs, offers, and favorite jobs.
- **Methods**:
- `vacancyDao()`: Returns an instance of `VacancyDao`.
- `offerDao()`: Returns an instance of `OfferDao`.
- `favoritesDao()`: Returns an instance of `FavoritesDao`.
- **Features**:
- Uses the Singleton pattern to provide a single database instance via `getDataBase`.

### 6. **Converters**.

- **Purpose**: Provides type converters for Room, allowing complex data types such as lists to be
  stored.
- **Methods**:
- `fromStringList`: Converts a JSON string to a list of strings.
- `toStringList`: Converts a list of strings to a JSON string.

### 7. **Network Data Models**.

These data classes represent the JSON structure retrieved from the network.

- **ResponseOfferAndVacancies**: Contains lists of `offers` and `vacancies`.
- **Offer**, **Button**, **Vacancy**, **Address**, **Experience**, **Salary**: Represent different
  parts of the job and offer data.

### 8. **VacancyRepositoryImpl**.

- **Purpose**: Implements the `VacancyRepository` interface to retrieve data from the network.
- **Methods**:
- `getData`: Executes a network query to retrieve jobs and offers.
- **Features**:
- Handles various exceptions related to network queries and provides informative error messages.

### 9. **KoinModule**

- **Purpose**: Defines dependencies to enforce dependencies using Koin.
- **Components**:
- Register `HttpClient`, `AppDatabase`, DAO, `KtorClientProvider`, `VacancyRepositoryImpl` and use
  case `GetMappedVacanciesUseCase`.

### 10. **Domain Models**.

These classes represent the business logic of the application.

#### a. **OfferDomainModel**.

- **OfferDomainModel**: Represents an offering at the domain level.
- **Fields**: `id`, `title`, `link`, optional `button`.

#### b. **Button**.

- **Purpose**: Represents a button associated with a sentence.
- **Field**: `text`.

#### c. **VacancyDomainModel**.

- **Vacancy**: Represents a vacancy at the domain level.
- **Fields**: Includes all necessary details of the vacancy.
- **Nested Classes**:
- `AddressDomainModel`.
- `ExperienceDomainModel`.
- `SalaryDomainModel`.

### 11. **VacancyRepository**.

- **VacancyRepository**: An interface that defines a contract to retrieve vacancy data.
- **Methods**:
- `getData`: Retrieves data about vacancies and offers.

### 12. **GetMappedVacanciesUseCase**.

- **Purpose**: Provides methods to retrieve and convert vacancies and offers into domain models.
- **Methods**:
- `getVacancy`: Gets and mapped vacancies.
- `getOffers`: Gets and mapps offers.

### 13. **InProgressFragment**.

- **Purpose**: A fragment representing a screen that is in development or awaiting implementation.
- **Methods**:
- `onCreateView`: Inflects the layout for the fragment.

## Class interactions and workflow

1. **Fetching data from the network**:

- `VacancyRepositoryImpl` uses `HttpClient` from `KtorClientProvider` to make HTTP requests.
- The URLs for the requests are provided by the `UrlProvider`.

2. **Data Processing and Mapping**:

- Data received from the network (`ResponseOfferAndVacancies`) is passed
  to `GetMappedVacanciesUseCase`.
- `GetMappedVacanciesUseCase` maps this data into domain
  models (`VacancyDomainModel`, `OfferDomainModel`).

3. **Storage of data in the database**:

- Mapped data can be stored in the database through appropriate
  DAOs (`VacancyDao`, `OfferDao`, `FavoritesDao`).
- DAOs interact with database entities (e.g., `VacancyModelDataBase`).

4. **Providing data in the UI**:

- Other modules or ViewModel can use `GetMappedVacanciesUseCase` to retrieve data and provide it to
  the UI.
- Using `Flow` ensures that the UI is reactively updated when data in the database changes.

5. **Favorites Jobs Processing**:

- `FavoritesDao` manages the addition and removal of jobs from favorites.
- Favorite jobs are stored separately in the database.

6. **Exception Handling**:

- `VacancyRepositoryImpl` handles network exceptions and provides informative messages to the user.

7. **Dependency Management**:

- All dependencies are managed using Koin, making it easy to test and scale the application.

## Application Usage

- **Data Layer**: The Core module is responsible for retrieving and storing data, providing a single
  source of truth for the application.
- **Domain Layer**: Domain models and use cases provide the business logic for the application.
- **Presentation Layer**: Data from the Core module is utilized by the ViewModel and provided to the
  View for display to the user.
- **Architecture**: Utilizing Clean Architecture MVVM architecture provides separation of
  responsibilities and
- makes it easier to maintain and extend the application.

## Conclusion

The **Core** module is a key component of the application that provides network and database
interaction.
It utilizes modern technologies and libraries such as Ktor for network queries, Room for database
operations,
Coroutines and Flow for asynchronous programming, and Koin for dependency enforcement. This code
separation and
organization contributes to a scalable, testable, and maintainable application.