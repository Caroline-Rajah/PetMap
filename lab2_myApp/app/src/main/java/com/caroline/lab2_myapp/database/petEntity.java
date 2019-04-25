package com.caroline.lab2_myapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "pets")
public class petEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String date;
    private String name;
    private String image;
    private String breed;
    private String age;
    private String gender;
    private String size;
    private String description;
    private String phone;
    private String email;
    private String address;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public petEntity(long id, String date, String name, String image) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.image = image;
    }

    @Ignore
    public petEntity() {
    }

    @Ignore
    public petEntity(String date, String name) {
        this.date = date;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "petEntity{" +
                "id=" + id +
                ", date=" + date +
                ", name='" + name + '\'' +
                '}';
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
