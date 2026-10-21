package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.user; // Canviat a 'user' amb minúscula
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDao userDao = new UserDao();

    // Canviat el paràmetre a 'user adminUser'
    public static void showAdminMenu(user adminUser) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n========================================");
            System.out.println("      CAMPUS GEARLEND HUB - ADMIN       ");
            // Utilitza adminUser.name() tal com està definit al teu model
            System.out.println("      Welcome, " + adminUser.name() + "!");
            System.out.println("========================================");
            System.out.println("1. Manage Equipment & Inventory");
            System.out.println("2. View All Borrow Requests");
            System.out.println("3. Manage Users / Accounts");
            System.out.println("4. View System Reports");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            }
            scanner.nextLine(); // Consumir el salt de línia

            switch (choice) {
                case 1 -> handleManageEquipment();
                case 2 -> handleViewBorrowRequests();
                case 3 -> handleManageUsers();
                case 4 -> handleViewReports();
                case 5 -> {
                    System.out.println("\nLogging out from Admin session...");
                    loggedIn = false;
                }
                default -> System.out.println("\n[!] Invalid choice. Please select between 1 and 5.");
            }
        }
    }

    private static void handleManageEquipment() {
        System.out.println("\n--- Manage Equipment Inventory ---");
        System.out.println("[1] Add New Equipment");
        System.out.println("[2] Update Equipment Status");
        System.out.println("[3] Delete Equipment");
        System.out.println("[4] Back to Admin Menu");
        System.out.print("Select action: ");

        if (scanner.hasNextInt()) {
            scanner.nextInt();
        }
        scanner.nextLine();

        System.out.println("\n[Info] Equipment management module coming soon.");
    }

    private static void handleViewBorrowRequests() {
        System.out.println("\n--- Pending & Active Borrow Requests ---");
        System.out.println("[Info] No active borrow requests to display right now.");
    }

    private static void handleManageUsers() {
        boolean managing = true;
        while (managing) {
            System.out.println("\n--- User Account Management ---");
            System.out.println("[1] View All Registered Users");
            System.out.println("[2] Change User Status / Approve Pending Accounts");
            System.out.println("[3] Back to Admin Menu");
            System.out.print("Select action: ");

            int action = -1;
            if (scanner.hasNextInt()) {
                action = scanner.nextInt();
            }
            scanner.nextLine();

            switch (action) {
                case 1 -> {
                    List<user> allUsers = userDao.getAllUsers();
                    renderUserTable(allUsers, "ALL REGISTERED SYSTEM USERS");
                }
                case 2 -> {
                    List<user> pendingUsers = userDao.getPendingUsers();
                    renderUserTable(pendingUsers, "PENDING ACCOUNT REGISTRATIONS");
                    if (!pendingUsers.isEmpty()) {
                        System.out.print("Enter User ID to APPROVE (Enter 0 to cancel): ");
                        int targetId = scanner.nextInt();
                        scanner.nextLine();

                        if (targetId != 0) {
                            boolean success = userDao.approveUser(targetId);
                            if (success) {
                                System.out.println("Success: User ID " + targetId + " status updated to APPROVED.");
                            } else {
                                System.out.println("Failed to update user status.");
                            }
                        }
                    }
                }
                case 3 -> managing = false;
                default -> System.out.println("\n[!] Invalid action. Choose 1-3.");
            }
        }
    }

    public static void renderUserTable(List<user> users, String title) {
        System.out.println("\n===========================================================================");
        System.out.println(" " + title);
        System.out.println("===========================================================================");
        if (users.isEmpty()) {
            System.out.println(" No records found.");
            System.out.println("===========================================================================");
            return;
        }

        System.out.printf("%-5s | %-20s | %-20s | %-12s | %-10s\n", "ID", "Name", "Email", "Role", "Status");
        System.out.println("---------------------------------------------------------------------------");
        for (user u : users) {
            System.out.printf("%-5d | %-20s | %-20s | %-12s | %-10s\n",
                    u.id(),
                    u.name(),
                    u.email(),
                    u.role(),
                    u.status()
            );
        }
        System.out.println("===========================================================================");
    }

    private static void handleViewReports() {
        System.out.println("\n--- System Reports & Logs ---");
        System.out.println("[Info] Report generation module coming soon.");
    }
}