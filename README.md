# WGU Student Scheduler

A native Android application for Western Governors University (WGU) students to organize and track academic terms, courses, assessments, mentors, and notes.

## Overview

WGU Student Scheduler provides a structured way to manage an entire academic degree plan from start to finish. The app uses a hierarchical data model where **Terms** contain **Courses**, and each **Course** can have related **Assessments**, **Mentors**, and **Notes**.

## Features

- **Term Management** — Create, update, and delete academic terms with start and end dates.
- **Course Tracking** — Associate courses with terms, track status, and set start/end date alerts.
- **Assessments** — Record objective and performance assessments with due dates and goal date reminders.
- **Mentors** — Save mentor contact information (name, phone, email) per course with quick dial support.
- **Notes** — Keep course-specific notes and share them via email.
- **Notification Alerts** — Receive local notifications for course start/end dates and assessment goal dates.
- **Tabbed Detail Views** — View course details across four tabs: Details, Assessments, Mentors, and Notes.
- **Data Persistence** — All data is stored locally in a SQLite database using Room with foreign key cascade deletes.

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java |
| Min SDK | 21 (Android 5.0) |
| Compile/Target SDK | 28 (Android 9.0) |
| Build System | Gradle 4.10.1 |
| Architecture Components | Room Persistence Library, LiveData, ViewModel (via Repository pattern) |
| UI | AppCompat, RecyclerView, CardView, TabLayout, ViewPager, Fragments |
| Database | SQLite via Room |

## Project Structure

```
app/src/main/java/com/example/trussell/wgustudentscheduler/
├── model/          # Room entities: Term, Course, Assessment, Mentor, Note
├── dao/            # Data Access Objects for Room queries
├── db/             # Room Database singleton (SchedulerDatabase)
├── repo/           # Repository classes abstracting data operations
├── adapter/        # RecyclerView adapters for list screens
├── fragment/       # Fragments used in tabbed detail views
├── receiver/       # AlarmReceiver for local notifications
├── util/           # Constants, converters, and helper utilities
├── TermActivity.java, AddTermActivity.java, ...    # Activity screens
└── DetailsCourseActivity.java, DetailsTermActivity.java, ...  # Detail screens
```

## Data Model

- **Term** — Top-level container with a name, start date, and end date.
- **Course** — Linked to a Term via foreign key. Tracks name, status, dates, and alert flags.
- **Assessment** — Linked to a Course. Tracks name, type (objective/performance), due date, goal date, and alert settings.
- **Mentor** — Linked to a Course. Stores name, phone number, and email address.
- **Note** — Linked to a Course. Stores a title and text entry.

## Building

This project uses the bundled Gradle wrapper. To build:

```bash
./gradlew assembleDebug
```

To install on a connected device or emulator:

```bash
./gradlew installDebug
```

## Permissions

- `CALL_PHONE` — Allows dialing a mentor's phone number directly from the app.

## Dependencies

Key dependencies include:
- Android Support Library (AppCompat, Design, RecyclerView, CardView)
- Room Persistence Library
- ConstraintLayout
- JUnit and Espresso for testing

See `app/build.gradle` for the full dependency list.

## License

This project is for educational purposes.
