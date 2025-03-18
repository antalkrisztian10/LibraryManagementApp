package com.example.librarymanagementapp.loans;

public class LoanRecord {

    private int id;
    private int bookId;
    private int userId;
    private String bookName;
    private String userName;

    public LoanRecord() {}
    public LoanRecord(int id, int bookId, int userId, String bookName, String userName) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.bookName= bookName;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
