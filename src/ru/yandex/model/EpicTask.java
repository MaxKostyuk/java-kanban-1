package ru.yandex.model;

import java.util.ArrayList;
import java.util.HashMap;

public class EpicTask extends Task {

    private ArrayList<Integer> subTasksID;

    public ArrayList<Integer> getSubTasksID() {
        return subTasksID;
    }
    public void removeSubtuskID(Integer subTaskId){
        subTasksID.remove(subTaskId);
    }

    public EpicTask(String name, String description, TypeTask typeTask) {

        this.name = name;
        this.description = description;
        this.typeTask = typeTask;
        subTasksID = new ArrayList<>();
    }

    public  void addSubTaskId (int subTaskId)
    {
        subTasksID.add(subTaskId);
    }
    public void clearAllSubTasksId(){
        subTasksID.clear();
    }
    @Override
    public String toString() {

        return "EpicTask{\n" +
                "\tName='" + name + '\'' +
                ", ID=" + id +
                ", Description='" + description + '\'' +
                ", Status=" + status +
                ",\n\tSubTasks=" + subTasksID.toString()
                +"\n}";
    }
}

