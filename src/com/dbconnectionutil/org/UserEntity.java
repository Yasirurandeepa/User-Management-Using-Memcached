package com.dbconnectionutil.org;

import java.io.Serializable;

public class UserEntity implements Serializable{

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    private String firstname;
    private String lname;
    private String email;
    private int userID;

    @Override
    public String toString() {

        return "{" +
                "id: '" + userID + '\'' +
                "firstname: '" + firstname + '\'' +
                ", lname: '" + lname + '\'' +
                ", email: '" + email + '\'' +
                '}';

    }

    public UserEntity(int userID, String firstname, String lname, String email) {
        this.userID = userID;
        this.firstname = firstname;
        this.lname = lname;
        this.email = email;
    }

}
