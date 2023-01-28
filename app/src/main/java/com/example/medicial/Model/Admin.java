package com.example.medicial.Model;

public class Admin extends User {
    int adminID;
    public Admin(int id, String username, String firstname, String lastname, String email, String password, int adminID)
    {
        super(id, username, firstname, lastname, email, password);
        this.adminID = adminID;
    }

    public int getAdminID() {
        return adminID;
    }
}
