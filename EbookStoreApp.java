package com.sla;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EbookStoreApp {

private List<Book> books;
    private Scanner scanner;
    private Connection connection;
    
    public EbookStoreApp() {
        books = new ArrayList<>();
        scanner = new Scanner(System.in);
        
        // Initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebook1","root","annai1508");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author name: ");
        String author = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter version: ");
        float version= scanner.nextFloat();
        scanner.nextLine(); // Consume newline left-over
        
        // Insert into database
        try {
            String sql = "INSERT INTO books (title, author, price,version) VALUES (?, ?, ?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setDouble(3, price);
            statement.setFloat(4, version);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Book added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void displayBooks() {
        // Fetch all books from database
        try {
            String sql = "SELECT * FROM books";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            
            books.clear(); // Clear existing list
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                double price = resultSet.getDouble("price");
                float version = resultSet.getFloat("version");
                Book book = new Book(title, author, price,version);
                books.add(book);
            }
            
            if (books.isEmpty()) {
                System.out.println("No books in the store.");
            } else {
                System.out.println("Listing all books:");
                for (Book book : books) {
                    System.out.println(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void searchByTitle() {
        System.out.print("Enter book title to search: ");
        String searchTitle = scanner.nextLine();
        
        // Search book by title in database
        try {
            String sql = "SELECT * FROM books WHERE title LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + searchTitle + "%");
            ResultSet resultSet = statement.executeQuery();
            
            books.clear(); // Clear existing list
            boolean found = false;
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                double price = resultSet.getDouble("price");
                float version = resultSet.getFloat("Version");
                Book book = new Book(title, author, price,version);
                books.add(book);
                found = true;
            }
            
            if (found) {
                System.out.println("Books found:");
                for (Book book : books) {
                    System.out.println(book);
                }
            } else {
                System.out.println("Book not found in the store.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public void updateBook() {
        try {
            // Input book details to update
            System.out.print("Enter title of the book to update: ");
            String titleToUpdate = scanner.nextLine();
            
            // Check if the book exists
            String checkSql = "SELECT * FROM books WHERE title = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, titleToUpdate);
            ResultSet resultSet = checkStatement.executeQuery();
            
            if (!resultSet.next()) {
                System.out.println("Book with title '" + titleToUpdate + "' not found.");
                return;
            }
            
            // Book found, now update it
            System.out.print("Enter new title: ");
            String newTitle = scanner.nextLine();
            System.out.print("Enter new author name: ");
            String newAuthor = scanner.nextLine();
            System.out.print("Enter new price: ");
            double newPrice = scanner.nextDouble();
            scanner.nextLine(); // Consume newline left-over
            System.out.print("Enter new version: ");
            String newVersion = scanner.nextLine();
            
            // Prepare update statement
            String updateSql = "UPDATE books SET title = ?, author = ?, price = ?, version = ? WHERE title = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, newTitle);       // Set new title
            updateStatement.setString(2, newAuthor);      // Set new author
            updateStatement.setDouble(3, newPrice);       // Set new price
            updateStatement.setString(4, newVersion);     // Set new version
            updateStatement.setString(5, titleToUpdate);  // Specify the book to update (original title)

            // Execute update
            int rowsUpdated = updateStatement.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Book updated successfully.");
            } else {
                System.out.println("Failed to update book with title '" + titleToUpdate + "'.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook() {
        try {
            // Input book details to delete
            System.out.print("Enter version of the book to delete: ");
            String versionToDelete = scanner.nextLine();
            
            // Check if the book exists
            String checkSql = "SELECT * FROM books WHERE version = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
			checkStatement.setString(1, versionToDelete);
            ResultSet resultSet = checkStatement.executeQuery();
            
            if (!resultSet.next()) {
                System.out.println("Book with version '" + versionToDelete + "' not found.");
                return;
            }
            
            // Book found, now delete it
            String deleteSql = "DELETE FROM books WHERE version = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setString(1, versionToDelete);
            
            // Execute delete
            int rowsDeleted = deleteStatement.executeUpdate();
            
            if (rowsDeleted > 0) {
                System.out.println("Book deleted successfully.");
            } else {
                System.out.println("Failed to delete book with version '" + versionToDelete + "'.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    
    
    
    public void run() {
        int choice = 0;
        do {
            System.out.println("\nE-Book Store Menu:");
            System.out.println("1. Add a new book");
            System.out.println("2. Display all books");
            System.out.println("3. Search for a book by title");
            System.out.println("4. Update a book by title");
            System.out.println("5. Delete a book by version");
            
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over
            
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    displayBooks();
                    break;
                case 3:
                    searchByTitle();
                    break;
                case 4:
                    updateBook();
                    break;
                case 5:
                    deleteBook();
                    break;
                case 6:
                    System.out.println("Exiting the E-Book Store. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                    break;
            }
        } while (choice != 6);
        
        scanner.close();
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
   
	public static void main(String[] args) {
        EbookStoreApp app = new EbookStoreApp();
        app.run();
    }
}