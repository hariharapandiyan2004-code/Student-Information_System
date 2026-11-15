# Student Information System

A menu-driven Java console application for managing student records.

## Features
- Add, view, search, update and delete student records
- Student ID uniqueness check
- Age validation (positive whole number)
- Grade validation (0 to 100)
- Handles invalid numeric input
- Saves records to `students.txt` and loads them at startup
- Uses `ArrayList<Student>` for in-memory storage

## Requirements
- JDK 8 or later (JDK 11+ recommended)
- Terminal / Command Prompt or a Java IDE

## Run the application
Open a terminal in the project root folder and run:

```bash
javac -d out src/StudentInformationSystem.java
java -cp out StudentInformationSystem
```

The program creates `students.txt` in the current working directory when records are saved.

## Project structure
- `src/StudentInformationSystem.java` — Student class and application logic
- `docs/Project_Documentation.pdf` — project documentation
- `docs/screenshots/` — add your genuine application screenshots here
- `test_data/students.txt` — sample records for testing

## Data format
Each record is stored as:
`studentId|name|age|grade|contact`

## Notes
This is a learning project. It uses a local text file rather than a database.
