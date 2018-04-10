package com.example.vedan.olxforvjti;

/**
 * Created by vedan on 18-03-2018.
 */

public class Registration_details {
    public String name;
    public String email;
    public String password;
    public String phone;
    public String branch;
    public String year;

    public Registration_details() {
    }

    public Registration_details(String name, String email, String password, String phone, String branch, String year) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.branch = branch;
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
