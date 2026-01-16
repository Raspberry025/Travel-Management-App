# Travel Management App

## Overview

**Travel Management App** is a JavaFX-based desktop application for managing tourism operations such as attractions, tourists, guides, and bookings. It uses a clean separation between controllers, models, repositories, and utilities, with file-based persistence for simplicity and transparency.

The application supports authentication, session handling, basic statistics, multilingual UI, and theming. It is built with Java and Maven, and uses JavaFX with FXML for the UI layer. The structure is designed to be understandable and easy to extend for experienced developers.

---

## Techniques and Patterns

* **JavaFX with FXML-based UI composition**
  UI layouts are defined declaratively using FXML files under [`src/main/resources/com/example/app`](./src/main/resources/com/example/app). Controllers handle logic separately, keeping layout and behavior decoupled.
  JavaFX FXML documentation: [https://openjfx.io/javadoc/](https://openjfx.io/javadoc/)

* **Controller-driven MVC-style structure**
  The project follows a pragmatic MVC-style separation:

  * `controller` for UI logic
  * `model` for domain entities
  * `repo` for data access
  * `util` for shared utilities

* **Plain-text persistence layer**
  Application data is stored in text files under [`app-data`](./app-data) and managed via utilities like [`FileHandler`](./src/main/java/com/example/app/util/FileHandler.java). This keeps data flow explicit and easy to inspect.

* **Password hashing utilities**
  Authentication avoids plain-text passwords by using helpers such as [`PasswordHashGenerator`](./src/main/java/com/example/app/util/PasswordHashGenerator.java) and [`PasswordUtils`](./src/main/java/com/example/app/util/PasswordUtils.java).

* **Session management**
  User state is centralized in [`Session`](./src/main/java/com/example/app/util/Session.java), reducing duplicated state handling across controllers.

* **Internationalization (i18n)**
  Language support is implemented using Java `ResourceBundle` property files:

  * `messages_en.properties`
  * `messages_ne.properties`
    Documentation: [https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html](https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html)

* **Runtime language switching**
  The [`LangSwitch`](./src/main/java/com/example/app/util/LangSwitch.java) utility enables changing UI language at runtime, which requires careful state and view updates in JavaFX.

* **Custom JavaFX theming**
  Styling is controlled via JavaFX CSS in `styles/nepali-theme.css`.
  JavaFX CSS reference: [https://openjfx.io/javadoc/](https://openjfx.io/javadoc/)

---

## Technologies and Libraries

* **Java (JPMS)** — Uses `module-info.java`, indicating use of the Java Platform Module System
  [https://openjdk.org/projects/jigsaw/](https://openjdk.org/projects/jigsaw/)

* **JavaFX** — Desktop UI framework
  [https://openjfx.io/](https://openjfx.io/)

* **Apache Maven** — Build automation and dependency management via [`pom.xml`](./pom.xml)
  [https://maven.apache.org/](https://maven.apache.org/)

* **Maven Wrapper** — Ensures consistent Maven builds across environments
  [https://maven.apache.org/wrapper/](https://maven.apache.org/wrapper/)

* **Embedded Font: Noto Sans Devanagari** — Used for Nepali language support
  Font file: [`NotoSansDevanagari-Regular.ttf`](./src/main/resources/Fonts/NotoSansDevanagari-Regular.ttf)
  Font info: [https://fonts.google.com/noto/specimen/Noto+Sans+Devanagari](https://fonts.google.com/noto/specimen/Noto+Sans+Devanagari)

---

## Project Structure

```plaintext
/
├── .idea
├── .mvn/wrapper
├── app-data
├── src/main
├── mvnw
├── mvnw.cmd
├── pom.xml
```

### Directory Notes

* **.mvn/wrapper/** — Maven Wrapper binaries and configuration
* **app-data/** — Plain-text storage for users, tourists, guides, bookings, and attractions
* **src/main/java/** — Controllers, models, repositories, utilities, and application entry point
* **src/main/resources/** — FXML views, styles, fonts, images, and localization files

Notable subdirectories inside `resources`:

* `Fonts/` — Embedded font assets
* `images/` — UI background and dashboard images
* `styles/` — JavaFX CSS themes
* `com/example/app/` — FXML view files

---

## Build and Run

From the repository root:

```bash
./mvnw clean javafx:run
```

On Windows:

```bat
mvnw.cmd clean javafx:run
```

---

## Extending the Project

This project is intentionally straightforward. Common next steps include:

* Replacing text-file persistence with a database
* Introducing repository interfaces
* Adding automated tests
* Improving validation and error handling
* Packaging as a native desktop application

---

## License

No license is currently specified. Add one if you plan to reuse or distribute this code.
