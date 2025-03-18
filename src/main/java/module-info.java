module com.example.librarymanagementapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.xerial.sqlitejdbc;

    opens com.example.librarymanagementapp to javafx.fxml;
    exports com.example.librarymanagementapp;

    exports com.example.librarymanagementapp.dao;
    opens com.example.librarymanagementapp.dao to javafx.fxml;

    exports com.example.librarymanagementapp.books;
    opens com.example.librarymanagementapp.books to javafx.fxml;

    exports com.example.librarymanagementapp.users;
    opens com.example.librarymanagementapp.users to javafx.fxml, javafx.base;

    exports com.example.librarymanagementapp.loans;
    opens com.example.librarymanagementapp.loans to javafx.fxml, javafx.base;
}
