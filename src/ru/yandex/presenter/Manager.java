package ru.yandex.presenter;

import ru.yandex.model.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Вопрос : надо ли реализовать методы по работе с задачами только по айди без типа таски?
//это как бы не трудно просто в таком случае надо бегать по всем мапам. что как бы медленно.
public class Manager {
    private final HashMap<Integer, Task> tasks;

    private final HashMap<Integer, SubTask> subTasks;

    private final HashMap<Integer, EpicTask> epicTasks;
    int id;

    public Manager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epicTasks = new HashMap<>();
        id = 0;
    }


    ////////////////ОСНОВНЫЕ МЕТОДЫ////////////////////////////////
    public AbstractMap<Integer, ? extends Task> getTasksByType(TypeTask typeTask) {
        switch (typeTask) {
            case TASK: {
                if (!tasks.isEmpty()) {
                    return tasks;
                } else return null;
            }
            case SUBTASK: {

                if (!subTasks.isEmpty()) {
                    return subTasks;
                } else return null;
            }
            case EPICTASK: {

                if (!epicTasks.isEmpty()) {
                    return epicTasks;
                } else return null;
            }
        }
        return null;
    }

    public boolean deleteTasksByType(TypeTask typeTask) {
        switch (typeTask) {
            case TASK: {
                if (!tasks.isEmpty()) {
                    tasks.clear();
                    return true;
                } else return false;
            }
            case SUBTASK: {
                if (!subTasks.isEmpty()) {
                    subTasks.clear();

                  //   вызов апдейт эпиков
                    for(Map.Entry<Integer,EpicTask> epicTask : epicTasks.entrySet()){
                        epicTask.getValue().clearAllSubTasksId();
                        updateTask(epicTask.getKey(),epicTask.getValue());
                    }
                    return true;
                } else return false;
            }
            case EPICTASK: {
                if (!epicTasks.isEmpty()) {
                    epicTasks.clear();
                    //надо ли удалять сабтаски, так как подзадачи без эпика существовать по идее не должны?
                    subTasks.clear();
                    return true;
                } else return false;
            }
        }
        return false;
    }

    public Task getById(int id, TypeTask typeTask) {
        switch (typeTask) {
            case TASK: {
                boolean containsId = tasks.containsKey(id);
                if (containsId) {
                    return tasks.get(id);
                } else return null;
            }
            case SUBTASK: {
                boolean containsId = subTasks.containsKey(id);
                if (containsId) {
                    return subTasks.get(id);
                } else return null;
            }
            case EPICTASK: {
                boolean containsId = epicTasks.containsKey(id);
                if (containsId) {
                    return epicTasks.get(id);
                } else return null;
            }
        }
        return null;
    }

    public int createTaskAndReturnID(Task task) {
        int keyID = id;
        task.setId(keyID);

        if (task instanceof SubTask) {
            this.subTasks.put(keyID, (SubTask) task);
            int epicIdOfSubtask = subTasks.get(keyID).getEpicId();
            epicTasks.get(epicIdOfSubtask).addSubTaskId(keyID);
            changeStatus(epicIdOfSubtask);
        }
        else if (task instanceof EpicTask) {
            this.epicTasks.put(keyID, (EpicTask) task);
            changeStatus(keyID);
        }
        else this.tasks.put(keyID, task);

        id++;
        return keyID;
    }

    public void updateTask(int id, Task task) {
        task.setId(id);
        if (task instanceof SubTask) {
            this.subTasks.put(id, (SubTask) task);
            int epicIdOfSubtask = subTasks.get(id).getEpicId();
            changeStatus(epicIdOfSubtask);
        }
        if (task instanceof EpicTask) {

            for(Map.Entry<Integer,SubTask> subTask : subTasks.entrySet()) {
                int epicIdOfSubtask = subTask.getValue().getEpicId();
                if(epicIdOfSubtask == id);
                {
                    ((EpicTask) task).addSubTaskId(epicIdOfSubtask);
                }

            }
            this.epicTasks.put(id, (EpicTask) task);
            changeStatus(id);
        } else
            this.tasks.put(id, task);

    }

    public boolean deleteByIdAndTypeTask(int id, TypeTask typeTask) {
        switch (typeTask) {
            case TASK: {
                boolean containsId = tasks.containsKey(id);
                if (containsId) {
                    tasks.remove(id);
                    return true;
                } else return false;
            }
            case SUBTASK: {
                boolean containsId = subTasks.containsKey(id);
                if (containsId) {
                    int epicIdOfSubTask = subTasks.get(id).getEpicId();
                    subTasks.remove(id);
                    //удаление айди сабтаска из этого эпика
                    epicTasks.get(epicIdOfSubTask).removeSubtuskID(id);
                    changeStatus(epicIdOfSubTask);
                    return true;
                } else return false;
            }
            case EPICTASK: {
                boolean containsId = epicTasks.containsKey(id);
                if (containsId) {
                    epicTasks.remove(id);
                  //Удаление сабтасков
                    for(Map.Entry<Integer,SubTask> subTask: subTasks.entrySet()) {
                        int epicIdOfSubTask = subTask.getValue().getEpicId();
                        if(epicIdOfSubTask == id) {
                            int subTaskId = subTask.getKey();
                            subTasks.remove(subTaskId);
                        }
                    }
                    return true;
                } else return false;
            }
        }
        return false;
    }


    //////////////////ДОПМЕТОДЫ//////////////////////////////////
    public ArrayList<SubTask> getListSubtask(int id) {
        boolean containsId = epicTasks.containsKey(id);
        ArrayList <Integer> subTasksId;
        if(containsId){
            subTasksId = epicTasks.get(id).getSubTasksID();
        }
        else return null;
        ArrayList <SubTask> subTaskArrayList = new ArrayList<>();
        for(Integer Id : subTasksId){
            subTaskArrayList.add(subTasks.get(Id));
        }
        return subTaskArrayList;
    }

    public void changeStatus(int epicId) {
        Status status;
        ArrayList <SubTask> tempListSubtasks = new ArrayList<>();
        for(Map.Entry<Integer,SubTask> subTask : subTasks.entrySet()){
                int epicIdOfSubtask = subTask.getValue().getEpicId();
                if(epicId == epicIdOfSubtask){
                    tempListSubtasks.add(subTask.getValue());
                }
        }
        if (tempListSubtasks.size() == 0) {
            epicTasks.get(epicId).setStatus(Status.NEW);
            return;
        }
        boolean isDone = false;
        boolean isInProgress = false;
        boolean isNew = false;
        for (SubTask subTask : tempListSubtasks) {
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
            epicTasks.get(epicId).setStatus(Status.NEW);
        } else if (!isNew && isDone && !isInProgress) {
            epicTasks.get(epicId).setStatus(Status.DONE);
        } else epicTasks.get(epicId).setStatus(Status.INPROGRESS);
    }
}



