package ru.yandex.model;

public class SubTask extends Task {
    private int epicId;
    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
    public int getEpicId() {
        return epicId;
    }
    public SubTask(String name, String description, Status status, TypeTask typeTask,int epicId) {
        super(name, description, status, typeTask);
        this.epicId=epicId;
    }
    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epicID=" + epicId +
                "}";
    }
}
