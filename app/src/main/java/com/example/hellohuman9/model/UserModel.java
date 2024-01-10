package com.example.hellohuman9.model;

import android.util.Log;
import android.widget.CheckBox;

import com.google.firebase.Timestamp;

public class UserModel {

    private String phone;
    private String username;
    private Timestamp createdTimestamp;
    private String userId;
    private String fcmToken;
    private boolean checkBoxPetOwner;
    private boolean checkBoxCaretaker;
    private String gender;
    private String ownedPets;
    private String experience;
    private int age;
    private String location;
    private String description;
    private String position;

    public UserModel() {
    }

    public UserModel(String phone, String username, Timestamp createdTimestamp, String userId,
                     boolean checkBoxPetOwner, boolean checkBoxCaretaker,
                     String gender, String ownedPets, String experience, int age, String location, String description, String position) {
        this.phone = phone;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.checkBoxPetOwner = checkBoxPetOwner;
        this.checkBoxCaretaker = checkBoxCaretaker;
        this.gender = gender;
        this.ownedPets = ownedPets;
        this.experience = experience;
        this.age = age;
        this.location = location;
        this.description = description;
        this.position = position;

        //Log.i("TEST_ME", "this is a test message for checkbox: " + checkBoxPetOwner + " / " + checkBoxCaretaker);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public boolean isCheckBoxPetOwner() {
        return checkBoxPetOwner;
    }

    public void setCheckBoxPetOwner(boolean checkBoxPetOwner) {
        this.checkBoxPetOwner = checkBoxPetOwner;
    }

    public boolean isCheckBoxCaretaker() {
        return checkBoxCaretaker;
    }

    public void setCheckBoxCaretaker(boolean checkBoxCaretaker) {
        this.checkBoxCaretaker = checkBoxCaretaker;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOwnedPets() {
        return ownedPets;
    }

    public void setOwnedPets(String ownedPets) {
        this.ownedPets = ownedPets;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
