package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    // Database connection details
    static final String URL = "jdbc:mysql://localhost:3306/ElevateTech_employee_db";
    static final String USER = "root";
    static final String PASSWORD = "system";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to Database!");
            System.out.println("WELCOME TO EMPLOYEE DATABASE APP");

            while (true) {
                System.out.println("\n *** Employee Management Menu ***");
                System.out.println("1. View Employees");
                System.out.println("2. Add Employee");
                System.out.println("3. Update Employee Salary");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> {
                        viewEmployees(connection);
                        pause(sc);
                    }
                    case 2 -> {
                        addEmployee(connection, sc);
                        pause(sc);
                    }
                    case 3 -> {
                        updateSalary(connection, sc);
                        pause(sc);
                    }
                    case 4 -> {
                        deleteEmployee(connection, sc);
                        pause(sc);
                    }
                    case 5 -> {
                        System.out.println("Exiting Employee DB App, Have a Great Day..");
                        return;
                    }
                    default -> {
                        System.out.println("Invalid choice. Try again, choose between 1-5...");
                        pause(sc);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }

    // View Employees list
    private static void viewEmployees(Connection connection) throws SQLException {
        String query = "SELECT * FROM ET_employees";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        System.out.println("\n ** Employee Records **");
        System.out.printf("%-5s %-20s %-20s %-10s%n", "ID", "Name", "Department", "Salary");
        System.out.println("-------------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%-5d %-20s %-20s %-10.2f%n",
                    rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getString("DEPARTMENT"),
                    rs.getDouble("SALARY"));
        }
    }

    // Adding Employee with validation
    private static void addEmployee(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        System.out.print("Enter Department: ");
        String dept = sc.nextLine().trim();
        if (dept.isEmpty()) {
            System.out.println("Department cannot be empty!");
            return;
        }

        System.out.print("Enter Salary: ");
        double salary = sc.nextDouble();
        if (salary <= 0) {
            System.out.println("Salary must be greater than 0!");
            return;
        }

        String query = "INSERT INTO ET_employees (name, department, salary) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.setString(2, dept);
        pstmt.setDouble(3, salary);
        pstmt.executeUpdate();

        System.out.println("Employee Information Added Successfully!");
    }

    // Update Salary
    private static void updateSalary(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to Update Salary: ");
        int id = sc.nextInt();
        System.out.print("Enter New Salary: ");
        double salary = sc.nextDouble();

        if (salary <= 0) {
            System.out.println("Salary must be greater than 0!");
            return;
        }

        String query = "UPDATE ET_employees SET salary=? WHERE id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setDouble(1, salary);
        pstmt.setInt(2, id);
        int rows = pstmt.executeUpdate();

        if (rows > 0) System.out.println("Salary Updated!");
        else System.out.println("Employee Not Found!");
    }

    // Delete Employee
    private static void deleteEmployee(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to Delete: ");
        int id = sc.nextInt();

        String query = "DELETE FROM ET_employees WHERE id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, id);
        int rows = pstmt.executeUpdate();

        if (rows > 0) System.out.println("Employee Deleted!");
        else System.out.println("Employee Not Found!");
    }

    // Pause for user to read output efficiently and then press Enter
    private static void pause(Scanner sc) {
        System.out.println("\n Press Enter to continue...");
        sc.nextLine();
    }
}
