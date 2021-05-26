/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.organizerClient.gui;


public class TasksModel {

    private final int id;
    private String title;
    private Boolean completed;

    public TasksModel(int id, String title, boolean completed) {
        this.title = title;
        this.completed = completed;
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TasksModel{" +
                "title='" + title + '\'' +
                ", completed=" + completed +
                '}';
    }
}
