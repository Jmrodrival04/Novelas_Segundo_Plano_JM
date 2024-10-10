package com.example.myapplication;
public class Novel {

    private String title;
    private String author;

    // Constructor sin argumentos necesario para Firestore
    public Novel() {
        // Este constructor está vacío a propósito
    }

    // Constructor con argumentos que puedes usar para crear instancias manualmente
    public Novel(String title, String author) {
        this.title = title;
        this.author = author;
    }

    // Getters y setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
