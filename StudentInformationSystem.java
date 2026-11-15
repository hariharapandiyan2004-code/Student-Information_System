import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Student {
    private String name;
    private int age;
    private double grade;
    private String studentId;
    private String contact;

    public Student(String name, int age, double grade, String studentId, String contact) {
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.studentId = studentId;
        this.contact = contact;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public double getGrade() { return grade; }
    public String getStudentId() { return studentId; }
    public String getContact() { return contact; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGrade(double grade) { this.grade = grade; }
    public void setContact(String contact) { this.contact = contact; }

    public void displayInfo() {
        System.out.printf("%-12s %-20s %-8d %-10.2f %-18s%n",
                studentId, name, age, grade, contact);
    }

    public String toFileFormat() {
        return studentId + "|" + name + "|" + age + "|" + grade + "|" + contact;
    }
}

public class StudentInformationSystem {
    private static final ArrayList<Student> students = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);
    private static final Path DATA_FILE = Paths.get("students.txt");

    public static void main(String[] args) {
        loadStudents();
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1: addStudent(); break;
                case 2: viewStudents(); break;
                case 3: searchStudent(); break;
                case 4: updateStudent(); break;
                case 5: deleteStudent(); break;
                case 6:
                    saveStudents();
                    running = false;
                    System.out.println("Thank you for using the Student Information System!");
                    break;
                default:
                    System.out.println("Invalid menu choice. Enter a number from 1 to 6.");
            }
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\\n==========================================");
        System.out.println("       STUDENT INFORMATION SYSTEM");
        System.out.println("==========================================");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student (ID or name)");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Save and Exit");
        System.out.println("------------------------------------------");
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();
            if (!value.isEmpty() && !value.contains("|")) return value;
            System.out.println("Input cannot be empty or contain the | character.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static int readPositiveAge() {
        while (true) {
            int age = readInt("Enter age: ");
            if (age > 0) return age;
            System.out.println("Age must be a positive number.");
        }
    }

    private static double readGrade() {
        while (true) {
            System.out.print("Enter grade (0-100): ");
            try {
                double grade = Double.parseDouble(sc.nextLine().trim());
                if (grade >= 0 && grade <= 100) return grade;
                System.out.println("Grade must be between 0 and 100.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric grade.");
            }
        }
    }

    private static Student findById(String id) {
        for (Student student : students) {
            if (student.getStudentId().equalsIgnoreCase(id)) return student;
        }
        return null;
    }

    private static void addStudent() {
        System.out.println("\\n--- ADD STUDENT ---");
        String id = readNonEmpty("Enter student ID: ");
        if (findById(id) != null) {
            System.out.println("That student ID already exists.");
            return;
        }

        String name = readNonEmpty("Enter name: ");
        int age = readPositiveAge();
        double grade = readGrade();
        String contact = readNonEmpty("Enter contact: ");

        students.add(new Student(name, age, grade, id, contact));
        saveStudents();
        System.out.println("Student added successfully.");
    }

    private static void viewStudents() {
        System.out.println("\\n--- ALL STUDENTS ---");
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }
        System.out.printf("%-12s %-20s %-8s %-10s %-18s%n",
                "STUDENT ID", "NAME", "AGE", "GRADE", "CONTACT");
        System.out.println("--------------------------------------------------------------------------");
        for (Student student : students) student.displayInfo();
    }

    private static void searchStudent() {
        System.out.print("\\nEnter student ID or full name: ");
        String query = sc.nextLine().trim();
        boolean found = false;

        for (Student student : students) {
            if (student.getStudentId().equalsIgnoreCase(query)
                    || student.getName().equalsIgnoreCase(query)) {
                if (!found) {
                    System.out.printf("%-12s %-20s %-8s %-10s %-18s%n",
                            "STUDENT ID", "NAME", "AGE", "GRADE", "CONTACT");
                    System.out.println("--------------------------------------------------------------------------");
                }
                student.displayInfo();
                found = true;
            }
        }
        if (!found) System.out.println("No matching student found.");
    }

    private static void updateStudent() {
        System.out.print("\\nEnter student ID to update: ");
        Student student = findById(sc.nextLine().trim());
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Press Enter to keep the current value.");
        System.out.print("New name [" + student.getName() + "]: ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty() && !name.contains("|")) student.setName(name);
        else if (name.contains("|")) System.out.println("Invalid name; old value retained.");

        while (true) {
            System.out.print("New age [" + student.getAge() + "]: ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) break;
            try {
                int age = Integer.parseInt(input);
                if (age > 0) { student.setAge(age); break; }
            } catch (NumberFormatException ignored) { }
            System.out.println("Enter a positive whole number, or press Enter to keep the old value.");
        }

        while (true) {
            System.out.print("New grade [" + student.getGrade() + "]: ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) break;
            try {
                double grade = Double.parseDouble(input);
                if (grade >= 0 && grade <= 100) { student.setGrade(grade); break; }
            } catch (NumberFormatException ignored) { }
            System.out.println("Enter a number from 0 to 100, or press Enter to keep the old value.");
        }

        System.out.print("New contact [" + student.getContact() + "]: ");
        String contact = sc.nextLine().trim();
        if (!contact.isEmpty() && !contact.contains("|")) student.setContact(contact);
        else if (contact.contains("|")) System.out.println("Invalid contact; old value retained.");

        saveStudents();
        System.out.println("Student record updated.");
    }

    private static void deleteStudent() {
        System.out.print("\\nEnter student ID to delete: ");
        String id = sc.nextLine().trim();
        Student student = findById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        students.remove(student);
        saveStudents();
        System.out.println("Student record deleted.");
    }

    private static void saveStudents() {
        try (BufferedWriter writer = Files.newBufferedWriter(DATA_FILE)) {
            for (Student student : students) {
                writer.write(student.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save records: " + e.getMessage());
        }
    }

    private static void loadStudents() {
        if (!Files.exists(DATA_FILE)) return;
        try {
            List<String> lines = Files.readAllLines(DATA_FILE);
            for (String line : lines) {
                String[] fields = line.split("\\\\|", -1);
                if (fields.length != 5) continue;
                try {
                    String id = fields[0].trim();
                    String name = fields[1].trim();
                    int age = Integer.parseInt(fields[2].trim());
                    double grade = Double.parseDouble(fields[3].trim());
                    String contact = fields[4].trim();
                    if (id.isEmpty() || name.isEmpty() || contact.isEmpty()
                            || age <= 0 || grade < 0 || grade > 100
                            || findById(id) != null) continue;
                    students.add(new Student(name, age, grade, id, contact));
                } catch (NumberFormatException ignored) {
                    System.out.println("Skipped an invalid record in the data file.");
                }
            }
            System.out.println(students.size() + " student record(s) loaded.");
        } catch (IOException e) {
            System.out.println("Could not load records: " + e.getMessage());
        }
    }
}
