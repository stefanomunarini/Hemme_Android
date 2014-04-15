package com.povodev.hemme.android.bean;

import java.io.File;
import java.sql.Date;

/**
 * Created by gbonadiman.stage on 02/04/2014.
 */
public class Document {

    private int id;
    private Date date;
    private String file;
    private String note;
    private File uploaded;

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public String getFile() { return file; }

    public void setFile(String file) { this.file = file; }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public File getUploaded() {
        return uploaded;
    }

    public void setUploaded(File uploaded) {
        this.uploaded = uploaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
