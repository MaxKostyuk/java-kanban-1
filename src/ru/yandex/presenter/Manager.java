package ru.yandex.presenter;

import ru.yandex.model.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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



    /**
     * Основные методы бизнес-логики
     */
    public Collection<? extends Task> getTasksByType(TypeTask typeTask) {
        Collection<? extends Task> tempListTasks;
        switch (typeTask) {
            case TASK: {
                if (!tasks.isEmpty()) {
                    tempListTasks = tasks.values();
                    return tempListTasks;
                } else return null;
            }
            case SUBTASK: {
                if (!subTasks.isEmpty()) {
                    tempListTasks = subTasks.values();
                    return tempListTasks;
                } else return null;
            }
            case EPICTASK: {
                if (!epicTasks.isEmpty()) {
                    tempListTasks = epicTasks.values();
                    return tempListTasks;
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
                    /**Вызов обновления эпиков*/
                    for (Map.Entry<Integer, EpicTask> epicTask : epicTasks.entrySet()) {
                        epicTask.getValue().clearAllSubTasksId();
                        updateTask(epicTask.getKey(), epicTask.getValue());
                    }
                    return true;
                } else return false;
            }
            case EPICTASK: {
                if (!epicTasks.isEmpty()) {
                    epicTasks.clear();
                    subTasks.clear();
                    return true;
                } else return false;
            }
        }
        return false;
    }

    public Task getByIdAndTypeTask(int id, TypeTask typeTask) {
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

    public int createTaskAndReturnId(Task task) {
        int keyId = getNextId();
        task.setId(keyId);
        if (task instanceof SubTask) {
            this.subTasks.put(keyId, (SubTask) task);
            int epicIdOfSubtask = subTasks.get(keyId).getEpicId();
            epicTasks.get(epicIdOfSubtask).addSubTaskId(keyId);
            changeStatus(epicIdOfSubtask);
        }
        else if (task instanceof EpicTask) {
            this.epicTasks.put(keyId, (EpicTask) task);
            changeStatus(keyId);
        }
        else
            this.tasks.put(keyId, task);
        return keyId;
    }

    public void updateTask(int id, Task task) {
        task.setId(id);
        if (task instanceof SubTask) {
            this.subTasks.put(id, (SubTask) task);
            int epicIdOfSubtask = subTasks.get(id).getEpicId();
            changeStatus(epicIdOfSubtask);
        }
        if (task instanceof EpicTask) {
            for (Map.Entry<Integer, SubTask> subTask : subTasks.entrySet()) {
                int epicIdOfSubtask = subTask.getValue().getEpicId();
                if (epicIdOfSubtask == id) {
                    ((EpicTask) task).addSubTaskId(epicIdOfSubtask);
                }
            }
            this.epicTasks.put(id, (EpicTask) task);
            changeStatus(id);
        } else
            this.tasks.put(id, task);

    }

    public boolean deleteByIdAndTypeTask(Integer id, TypeTask typeTask) {
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
                    /**Удаление ID SubTask из EpicTask*/
                    epicTasks.get(epicIdOfSubTask).removeSubTaskId(id);
                    changeStatus(epicIdOfSubTask);
                    return true;
                } else return false;
            }
            case EPICTASK: {
                boolean containsId = epicTasks.containsKey(id);
                if (containsId) {
                    EpicTask deletedEpicTask = epicTasks.remove(id);
                    /**Удаление сабтасков в связи удаления эпика*/
                    ArrayList <Integer> tempListId = deletedEpicTask.getSubTasksID();
                    for (Integer subTaskIdFromEpic : tempListId) {
                        if (subTasks.containsKey(subTaskIdFromEpic)) {
                            subTasks.remove(subTaskIdFromEpic);
                        }
                    }
                    return true;
                } else return false;
            }
        }
        return false;
    }





    /** Дополнительные методы бизнес логики */
    public ArrayList<SubTask> getListSubtask(int id) {
        boolean containsId = epicTasks.containsKey(id);
        ArrayList<Integer> subTasksId;
        if (containsId) {
            subTasksId = epicTasks.get(id).getSubTasksID();
        } else return null;
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        for (Integer Id : subTasksId) {
            subTaskArrayList.add(subTasks.get(Id));
        }
        return subTaskArrayList;
    }
 /** Метод отвечающий за состояние EpicTask*/
    public void changeStatus(int epicId) {
        ArrayList<SubTask> listSubtasks = new ArrayList<>();
        EpicTask tempEpicTask = ((EpicTask)getByIdAndTypeTask(epicId,TypeTask.EPICTASK));
        ArrayList<Integer> listId = tempEpicTask.getSubTasksID();
        for (Integer uid : listId) {
            listSubtasks.add(subTasks.get(uid));
        }
        if (listSubtasks.size() == 0) {
            epicTasks.get(epicId).setStatus(Status.NEW);
            return;
        }
        boolean isDone = false;
        boolean isInProgress = false;
        boolean isNew = false;
        for (SubTask subTask : listSubtasks) {
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
        }
        else if (!isNew && isDone && !isInProgress) {
            epicTasks.get(epicId).setStatus(Status.DONE);
        }
        else
            epicTasks.get(epicId).setStatus(Status.INPROGRESS);
    }

    /** Метод для генерации ID*/
    private int getNextId() {
        int keyId = id;
        ++id;
        return keyId;
    }
}




