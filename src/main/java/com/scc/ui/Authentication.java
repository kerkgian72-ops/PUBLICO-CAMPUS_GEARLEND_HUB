package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.user;

import java.util.List;
import java.util.Scanner;

public class Authentication {
    private final UserDao userDao;
    private final Scanner scanner;

    public Authentication(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            System.out.println("\n==================================");
            System.out.println("   CAMPUS GEARLEND HUB SYSTEM     ");
            System.out.println("==================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> handleLogin();
                case 2 -> handleRegister();
                case 3 -> {
                    System.out.println("Exiting application. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleRegister() {
        System.out.print("Enter full name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter department (e.g., BSIT, BSCE): ");
        String department = scanner.nextLine();

        System.out.println("Select Role:");
        System.out.println("1. STUDENT");
        System.out.println("2. TEACHER");
        System.out.println("3. ADMIN");
        System.out.print("Choice: ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();

        String role = switch (roleChoice) {
            case 2 -> "TEACHER";
            case 3 -> "ADMIN";
            default -> "STUDENT";
        };

        boolean success = userDao.registerUser(name, email, password, role, department);
        if (success) {
            System.out.println("Registration successful! Account status is PENDING.");
        }
    }

    private void handleLogin() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        user currentUser = userDao.login(email, password);
        if (currentUser != null) {
            if ("PENDING".equals(currentUser.status())) {
                System.out.println("\n[Access Denied] Your account is currently PENDING approval.");
                return;
            }

            System.out.println("\nLogin Successful! Welcome, " + currentUser.name() + " (" + currentUser.role() + ")");

            if ("ADMIN".equalsIgnoreCase(currentUser.role())) {
                AdminMenu.showAdminMenu(currentUser);
            } else if ("TEACHER".equals(currentUser.role())) {
                teacherDashboard();
            } else {
                studentDashboard(currentUser);
            }
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    private void teacherDashboard() {
        while (true) {
            System.out.println("\n--- Teacher Dashboard ---");
            System.out.println("1. View & Approve Pending Accounts");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                List<user> pendingUsers = userDao.getUsersByStatus("PENDING");
                if (pendingUsers.isEmpty()) {
                    System.out.println("No pending accounts.");
                    continue;
                }

                System.out.println("\n--- Pending Accounts ---");
                for (user u : pendingUsers) {
                    System.out.println("ID: " + u.id() + " | Name: " + u.name() + " | Email: " + u.email() + " | Role: " + u.role());
                }

                System.out.print("Enter User ID to APPROVE (0 to go back): ");
                int id = scanner.nextInt();
                scanner.nextLine();

                if (id != 0) {
                    userDao.updateStatus(id, "APPROVED");
                    System.out.println("Account approved successfully!");
                }
            } else if (choice == 2) {
                System.out.println("Logging out...");
                break;
            }
        }
    }

    private void studentDashboard(user currentUser) {
        while (true) {
            System.out.println("\n--- Student Dashboard ---");
            System.out.println("1. View Profile");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.println("Name: " + currentUser.name() + " | Email: " + currentUser.email() + " | Dept: " + currentUser.department() + " | Status: " + currentUser.status());
            } else if (choice == 2) {
                System.out.println("Logging out...");
                break;
            }
        }
    }
}