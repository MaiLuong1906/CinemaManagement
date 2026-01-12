package model;

import java.time.LocalDate;

public class UserProfile {
    private int userId;
    private String fullName;
    private String phoneNumber;
    private boolean gender;
    private String address;
    private LocalDate dateOfBirth;

    public UserProfile() {}

    public UserProfile(int userId, String fullName, String phoneNumber, boolean gender, String address, LocalDate dateOfBirth) {
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public boolean isGender() { return gender; }
    public void setGender(boolean gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
}

