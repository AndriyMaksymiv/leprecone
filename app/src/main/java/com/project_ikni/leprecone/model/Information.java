package com.project_ikni.leprecone.model;

public class Information {

    private int id;
    private String name,position;
    private byte[] image;

    public Information(int id, String name, String position, byte[] image) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}