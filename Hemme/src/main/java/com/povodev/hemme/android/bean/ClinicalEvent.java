package com.povodev.hemme.android.bean;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Stefano on 01/04/14.
 */
public class ClinicalEvent implements Serializable {

    public int id;
    private int author;
    private Date date;
    private String therapy;
    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTherapy() {
        return therapy;
    }

    public void setTherapy(String therapy) {
        this.therapy = therapy;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }
}
