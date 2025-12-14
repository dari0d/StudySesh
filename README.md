# Study Session Tracker – Android App

Study Session Tracker is an Android app (Java) that helps students plan and log focused study sessions, then review their progress over time. Users can:

- Create sessions tied to specific classes  
- Record how long they studied  
- View summary statistics (total time per class and overall study time)  
- Run **live sessions** with an optional **phone-pickup counter** to capture distractions

---

## Architecture & Implementation

The app is built around an on-device **Room** database so all data is stored locally and persists across launches. The project follows an **MVC-style** structure:

### Model
- Room entities and DAOs for:
  - Classes
  - Study sessions
- Queries to aggregate statistics (e.g., total time per class, overall time)

### View
- XML layouts for:
  - Home screen
  - Create-session form
  - Class manager
  - Stats dashboard

### Controller
- Activities that handle:
  - Input validation
  - Navigation between screens (Intents)
  - CRUD operations (create, edit, delete sessions and classes)

---

## Features Demonstrated

- Local persistence with **Room**
- Multi-screen navigation with **Intents**
- Form validation for user input
- Derived statistics generated from stored data
- Usability-tested interface based on user feedback collected via **SUS-style questionnaires**
