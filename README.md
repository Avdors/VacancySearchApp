# Description

An example of a job search app. You have the opportunity to view lists of vacancies, with detailed information. There is additional functionality - vacancies near you, raise your resume in the search. Ability to add a vacancy to your favorites list. Ability to view and edit the list of favorites. There is a function to respond to a vacancy. Write a cover letter or ask a question from the template of the vacancy card. Ability to see how many people have responded and how many people are looking at the vacancy. Data is received from the server in json format, cached in the database. Authorization when opening the application, receiving and entering a verification code.

- **Navigation**:
    
    - `androidx.navigation.fragment.ktx`, `androidx.navigation.ui.ktx`, `androidx.navigation.compose` to work with the navigation graph (`nav_graph.xml`) and deep links to access fragments that are not in the menu stack.
- **Ktor**:
    
    - Used to execute HTTP requests and process network data:
- **Koin**:
    
    - Dependencies are managed using the Koin library
- **Libraries for data storage and databases**:
    
    - `Room` 
    - Support for asynchronous operations and data streams is provided using`Flow` и `Coroutines`.
- **Coroutines и Flow**:
    
    - `kotlinx.coroutines.android`, `kotlinx.coroutines.test` for asynchronous task execution and working with data flows (`Flow`).
- **Testing**:
    
    - Libraries are used for unit tests `JUnit`, `truth` (for the assertors), `mockk`, as well as testing tools Koin и corunine.
    - Testing of Android-specific components is done with the help of `androidx.test.runner`, `androidx.espresso.core`.
- **Parcelize**:
    
    - `kotlin.parcelize` to simplify data transfer between Android components.
- **Serialization**:
    
    - `kotlinx.serialization` for serializing JSON data, particularly in conjunction with `ktor`.
- **MVVM**:
    
    - The MVVM model is implemented to orchestrate the interaction between UI and logic, where ViewModel manages state by providing the UI with up-to-date data via Flow.
    
 
      ![image](https://github.com/user-attachments/assets/cd4dc31d-86e0-4b0a-aa4b-91c4fc430359)

      ![vacancySearch320](https://github.com/user-attachments/assets/e79b2137-040c-45f3-8ead-a042927a4489)

      [Core Module Documentation](docs/CORE.md) / [Core Module Documentation_RUS](docs/CORE_RUS.md)

      



      
