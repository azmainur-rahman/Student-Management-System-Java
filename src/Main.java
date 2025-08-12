import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        login();
    }
    public static void login() {
        System.out.println("Hello and welcome, To Student Management System:");
        System.out.println("-----------------------------------------------");
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter UserName : ");
        String userName = sc.nextLine().trim();
        System.out.print("Enter Password : ");
        String userPass = sc.nextLine().trim();
        boolean LogIn = false;

        try (RandomAccessFile raf = new RandomAccessFile("user.txt", "r")) {
            String line;
            while ((line = raf.readLine()) != null) {
                String[] arr = line.split(",");
                if (arr.length>=2 && arr[0].trim().equals(userName) && arr[1].trim().equals(userPass)) {
                    LogIn = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("user.txt not found. Please create it...");
            return;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }
        if (LogIn) AdminPanel();
        else System.out.println("\nWrong Username or Password! Please Try Again...");
    }

    public static void AdminPanel() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\nWelcome to Admin Panel...");
            System.out.println("--------------------------");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. View Student Profile by ID");
            System.out.println("4. Course Advising");
            System.out.println("5. Exit");
            System.out.print("\nEnter your Option : ");
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                sc.next();
                continue;
            }
            int choose = sc.nextInt();
            sc.nextLine();
            switch (choose) {
                case 1: addStudent(); break;
                case 2: viewAllStudents(); break;
                case 3: viewStudentById(); break;
                case 4: courseAdvising(); break;
                case 5: System.out.println("Exiting Admin Panel. Goodbye!"); return;
                default: System.out.println("Invalid Option! Please choose between 1-5."); break;
            }
        }
    }

    public static void addStudent() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Add Student Panel ---");
        System.out.print("Enter ID: ");
        long id = sc.nextLong();
        sc.nextLine();
        if (isIdExists(id)) {
            System.out.println("Error: A student with this ID (" + id + ") already exists.");
            return;
        }
        System.out.print("Enter name: "); String name = sc.nextLine();
        System.out.print("Enter Dept: "); String dept = sc.nextLine();
        System.out.print("Enter Batch: "); int batch = sc.nextInt();
        System.out.print("Enter Phone Number (without country code): "); long phoneNumber = sc.nextLong(); sc.nextLine();
        System.out.print("Enter Blood Group: "); String bloodGroup = sc.nextLine();
        Student one = new Student(name, id, batch, dept, phoneNumber, bloodGroup);
        writeStudentFile(one);
    }

    public static boolean isIdExists(long id) {
        try (RandomAccessFile raf = new RandomAccessFile("students.txt", "r")) {
            String line;
            while ((line = raf.readLine()) != null) {
                if (Long.parseLong(line.split(",")[2].trim()) == id)
                    return true;
            }
        } catch (IOException e) {
            System.out.println("System went Wrong");
        }
        return false;
    }

    public static void writeStudentFile(Student student) {
        try (RandomAccessFile raf = new RandomAccessFile("students.txt", "rw")) {
            raf.seek(raf.length());
            String password = UniquePassword(8);
            String line = String.join(",", student.getName(), student.getDept(), String.valueOf(student.getId()), String.valueOf(student.getBatch()), String.valueOf(student.getPhoneNumber()), student.getBloodGroup(), password) + "\n";
            raf.writeBytes(line);
            System.out.println("\nStudent Added Successfully!\nGenerated Secure Password for " + student.getName() + " is: " + password);
        } catch (IOException ex) {
            System.err.println("File writing failed: "+ex.getMessage());
        }
    }

    public static void viewAllStudents() {
        System.out.println("\n--- All Student List (Sorted by ID) ---");
        List<Student> studentList = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile("students.txt", "r")) {
            String line;
            while ((line = raf.readLine()) != null) {
                String[] data = line.split(",");
                studentList.add(new Student(data[0], Long.parseLong(data[2].trim()), 0, data[1], Long.parseLong(data[4].trim()), ""));
            }
        } catch (IOException e) { System.out.println("No student data found or error reading file."); return; }

        Collections.sort(studentList);
        for (Student s : studentList) {
            System.out.println("ID: " + s.getId() + ", Name: " + s.getName() + ", Dept: " + s.getDept() + ", Phone: +880" + s.getPhoneNumber());
        }
        System.out.println("----------------------------------------\n");
    }

    public static void viewStudentById() {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter Student ID to view profile: ");
        long searchId = sc.nextLong();
        sc.nextLine();
        boolean found = false;
        try (RandomAccessFile raf = new RandomAccessFile("students.txt", "r")) {
            String line;
            while ((line = raf.readLine()) != null) {
                String[] data = line.split(",");
                if (Long.parseLong(data[2].trim()) == searchId) {
                    System.out.println("\n--- Student Profile ---");
                    System.out.println("Name: " + data[0]);
                    System.out.println("Department: " + data[1]);
                    System.out.println("ID: " + data[2]);
                    System.out.println("Batch: " + data[3]);
                    System.out.println("Phone Number: +880" + data[4]);
                    System.out.println("Blood Group: " + data[5]);
                    found = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("File not Exists");
        }
        if (!found) {
            System.out.println("\nStudent with ID " + searchId + " not found.");
            return;
        }

        System.out.println("--- Advised Courses ---");
        if (!takenCourse(searchId, null)) System.out.println("No courses advised yet.");
        System.out.println("-----------------------\n");
    }

    public static void courseAdvising() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Course Advising Panel ---");
        System.out.print("Enter Student ID: ");
        long studentId = sc.nextLong(); sc.nextLine();
        if (!isIdExists(studentId)) {
            System.out.println("Error: Student ID not found.");
            return;
        }

        System.out.println("\nAvailable Courses:");
        try (RandomAccessFile raf = new RandomAccessFile("courselist.txt", "r")) {
            String line;
            while ((line = raf.readLine()) != null) System.out.println("- " + line.replace(",", " (Credit: ") + ")");
        } catch (IOException e) {
            System.out.println("Could not load course list.");
            return;
        }

        System.out.print("\nEnter Course Code to Add (e.g., CSE115): ");
        String courseCode = sc.nextLine().trim().toUpperCase();

        if (takenCourse(studentId, courseCode)) {
            System.out.println("Error: Student has already taken course " + courseCode);
            return;
        }
        int courseCredit = getCourseCredit(courseCode);
        if (courseCredit == -1) { System.out.println("Error: Invalid course code."); return; }

        int currentCredits = currentCredits(studentId);
        if ((currentCredits + courseCredit) > 14) {
            System.out.println("Error: Credit limit exceeded! Current credits: " + currentCredits + ". Cannot add a " + courseCredit + " credit course.");
            return;
        }
        addCourseToStudent(studentId, courseCode);
    }

    public static boolean takenCourse(long studentId, String courseCode) {
        try (RandomAccessFile raf = new RandomAccessFile("student_courses.txt", "r")) {
            String line;
            while ((line = raf.readLine()) != null) {
                String[] data = line.split(",");
                if (Long.parseLong(data[0].trim()) == studentId) {
                    if (courseCode == null) {
                        for (int i = 1; i < data.length; i++) System.out.println("- " + data[i].trim());
                        return true;
                    }
                    for (int i = 1; i < data.length; i++) {
                        if (data[i].trim().equalsIgnoreCase(courseCode)) return true;
                    }
                    return false;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public static int getCourseCredit(String courseCode) {
        try (RandomAccessFile raf = new RandomAccessFile("courselist.txt", "r")) {
            String line;
            while ((line = raf.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equalsIgnoreCase(courseCode)) return Integer.parseInt(data[1].trim());
            }
        } catch (IOException e) {
            return -1;
        }
        return -1;
    }

    public static int currentCredits(long studentId) {
        int totalCredits = 0;
        try (RandomAccessFile raf = new RandomAccessFile("student_courses.txt", "r")) {
            String line;
            while ((line = raf.readLine()) != null) {
                String[] data = line.split(",");
                if (Long.parseLong(data[0].trim()) == studentId) {
                    for (int i = 1; i < data.length; i++) totalCredits += getCourseCredit(data[i].trim());
                    return totalCredits;
                }
            }
        } catch (IOException e) {
            return 0; }
        return 0;
    }

    public static void addCourseToStudent(long studentId, String courseCode) {
        File file = new File("student_courses.txt");
        List<String> lines = new ArrayList<>();
        boolean studentFound = false;
        if (file.exists()) {
            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                String line;
                while ((line = raf.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading student_courses.txt");
                return; }
        }

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith(studentId + ",")) {
                lines.set(i, lines.get(i) + "," + courseCode);
                studentFound = true;
                break;
            }
        }
        if (!studentFound) lines.add(studentId + "," + courseCode);

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(0);
            for (String line : lines) {
                raf.writeBytes(line + System.lineSeparator());
            }
            System.out.println("Course " + courseCode + " added successfully for student " + studentId);
        } catch (IOException e) {
            System.err.println("Error student_courses.txt");
        }
    }

    public static String UniquePassword(int length) {
        final String AN = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) password.append(AN.charAt(random.nextInt(AN.length())));
        return password.toString();
    }
}