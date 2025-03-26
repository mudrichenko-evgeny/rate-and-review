# Rate And Review
This project is a non-commercial Android project created solely to demonstrate my skills and knowledge in Android development. The app uses mock data and does not feature a backend or design work from a designer. 
The project includes several example tests to demonstrate best practices in Android development, including unit tests and instrumented tests.

# Movie rating
Movie rating is application is used to rate and review movies. The user has the ability to view the average rating of the movie and see reviews from other users.

<p align = "center">
  <img src="https://github.com/user-attachments/assets/98997855-fcd2-4135-9b16-2312e789e056" width="250">
  <img src="https://github.com/user-attachments/assets/6244fe95-a506-4df6-a99b-a6efa58f145a" width="250">
  <img src="https://github.com/user-attachments/assets/c03fe9b6-5a8a-4a0e-a07d-49f4e149a57c" width="250">
</p>
<p align = "center">
  <img src="https://github.com/user-attachments/assets/46f225dc-23a0-42e7-80fc-3007c42996e9" width="250">
  <img src="https://github.com/user-attachments/assets/79246777-470a-41b8-af53-3e0c09fdd7eb" width="250">
  <img src="https://github.com/user-attachments/assets/81b95a35-5243-4f24-82a9-355a6902dfbc" width="250">
</p>

# Modules
The project is architecturally divided into several modules:
1) core:common – Contains utility functions and shared resources that can be used across the entire project.
2) core:storage – Manages data storage solutions, including local database operations.
3) core:network – Handles all network-related operations, including API requests and responses.
4) core:ui – Contains common UI components and styles, ensuring consistency across screens.
5) core:model – Holds the data models that represent the entities in the application.
6) feature:user – Manages user-related functionality, including login, registration, and user preferences.
7) feature:settings – Manages the application’s settings and configurations.
8) app:movierating – The main app module that ties all features and components together, showcasing the functionality of the Movie Rating app.

# Architecture
The project utilizes the MVVM architecture with elements of MVI. The UI directly interacts with the ViewModel through function calls, while the ViewModel manages and exposes state using Flows to update the UI reactively.

# Technologies Used
- Kotlin;
- Coroutines;
- Hilt;
- Jetpack's Navigation component;
- Compose;
- Room;
- Retrofit
