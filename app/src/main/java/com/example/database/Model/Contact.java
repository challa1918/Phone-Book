package com.example.database.Model;

import androidx.annotation.NonNull;

public class Contact {
    int id;
    String name;
    String phnno;

    public Contact() {
    }

    public Contact(int id, String name, String phnno) {
        this.id = id;
        this.name = name;
        this.phnno = phnno;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhnno() {
        return phnno;
    }

    public void setPhnno(String phnno) {
        this.phnno = phnno;

    }

    @NonNull
    @Override
    public String toString() {
        return id+" "+name+" "+phnno;
    }
}
