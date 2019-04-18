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
}
