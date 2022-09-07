package ru.yandex.model;

import java.util.ArrayList;

public class EpicTask extends Task {

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    private ArrayList<SubTask> subTasks;

    public EpicTask(String name, String description, ArrayList<SubTask> subTasks, TypeTask typeTask) {
        this.subTasks = subTasks;
        this.name = name;
        this.description = description;
        this.typeTask = typeTask;
        changeStatus();
    }

    public void changeStatus() {
        Status status;
        if (subTasks.size() == 0) {
            this.status = Status.NEW;
            return;
        }
        boolean isDone = false;
        boolean isInProgress = false;
        boolean isNew = false;
        for (SubTask subTask : subTasks) {
            switch (subTask.getStatus()) {
                case NEW:
                    isNew = true;
                    break;
                case DONE:
                    isDone = true;
                    break;
                case INPROGRESS:
                    isInProgress = true;
                    break;
            }
        }
        if (isNew && !isDone && !isInProgress) {
            this.status = Status.NEW;
        } else if (!isNew && isDone && !isInProgress) {
            this.status = Status.DONE;
        } else this.status = Status.INPROGRESS;
    }

    @Override
    public String toString() {
        String substring = "";
        for (int i = 0; i < subTasks.size(); i++) {
            substring += "\t";
            substring += subTasks.get(i).toString();
            if (i != subTasks.size() - 1) {
                substring += ",\n";
            } else substring += "\n\t]\n}";
        }
        return "EpicTask{\n" +
                "\tName='" + name + '\'' +
                ", ID=" + id +
                ", Description='" + description + '\'' +
                ", Status=" + status +
                ",\nSubTasks=[\n" + substring;
    }
}

