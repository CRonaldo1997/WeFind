package com.example.c_ronaldo.we_finder;

/**
 * Created by meng_ on 5/8/2017.
 */

public class User {
    public String username;
    public String email;
    public String year;
    public String zodiac;
    public String gender;

    public User() {};

    public User(String username, String email, String year, String zodiac, String gender) {
        this.username = username;
        this.email = email;
        this.year = year;
        this.zodiac = zodiac;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String newUsername) {
        username = newUsername;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String newEmail) {
        username = newEmail;
    }
    public String getYear() {
        return year;
    }

    public void setYear(String newYear) {
        username = newYear;
    }
    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String newZodiac) {
        username = newZodiac;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String newGender) {
        username = newGender;
    }

}
