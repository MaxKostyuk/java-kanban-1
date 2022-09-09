package ru.yandex.model;

import ru.yandex.model.Status;

import java.util.ArrayList;

public class Task {
    protected String name;
    protected int id;
    protected String description;
    protected Status status;
    protected TypeTask typeTask;

    public Task() {
    }



    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                "}";
    }

    public Task(String name, String description, Status status, TypeTask typeTask) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.typeTask = typeTask;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
