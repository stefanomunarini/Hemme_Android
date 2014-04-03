package com.povodev.hemme.android.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Stefano on 03/04/14.
 */
public class Result implements Serializable {

    private int id;
    private String grade;
    private int time;
    private Timestamp date;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
