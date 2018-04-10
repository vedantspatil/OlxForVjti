package com.example.vedan.olxforvjti;

/**
 * Created by vedan on 24-03-2018.
 */

public class Allbooks {
    private String Book_Name, Book_Description, Book_Author, Book_Edition, Book_Price, Book_Subject, Book_Condition, Book_Semester, Book_Image,USER_UID;

    public  Allbooks(){}
    public Allbooks(String book_Name, String book_Description, String book_Author, String book_Edition, String book_Price, String book_Subject, String book_Condition, String book_Semester, String book_Image, String User_UID) {
        Book_Name = book_Name;
        Book_Description = book_Description;
        Book_Author = book_Author;
        Book_Edition = book_Edition;
        Book_Price = book_Price;
        Book_Subject = book_Subject;
        Book_Condition = book_Condition;
        Book_Semester = book_Semester;
        Book_Image = book_Image;
        USER_UID = User_UID;
    }

    public String getUSER_UID() {
        return USER_UID;
    }

    public void setUSER_UID(String USER_UID) {
        this.USER_UID = USER_UID;
    }

    public String getBook_Name() {
        return Book_Name;
    }

    public void setBook_Name(String book_Name) {
        Book_Name = book_Name;
    }

    public String getBook_Description() {
        return Book_Description;
    }

    public void setBook_Description(String book_Description) {
        Book_Description = book_Description;
    }

    public String getBook_Author() {
        return Book_Author;
    }

    public void setBook_Author(String book_Author) {
        Book_Author = book_Author;
    }

    public String getBook_Edition() {
        return Book_Edition;
    }

    public void setBook_Edition(String book_Edition) {
        Book_Edition = book_Edition;
    }

    public String getBook_Price() {
        return Book_Price;
    }

    public void setBook_Price(String book_Price) {
        Book_Price = book_Price;
    }

    public String getBook_Subject() {
        return Book_Subject;
    }

    public void setBook_Subject(String book_Subject) {
        Book_Subject = book_Subject;
    }

    public String getBook_Condition() {
        return Book_Condition;
    }

    public void setBook_Condition(String book_Condition) {
        Book_Condition = book_Condition;
    }

    public String getBook_Semester() {
        return Book_Semester;
    }

    public void setBook_Semester(String book_Semester) {
        Book_Semester = book_Semester;
    }

    public String getBook_Image() {
        return Book_Image;
    }

    public void setBook_Image(String book_Image) {
        Book_Image = book_Image;
    }
}
