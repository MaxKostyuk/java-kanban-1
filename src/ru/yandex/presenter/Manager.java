package ru.yandex.presenter;

import ru.yandex.model.EpicTask;
import ru.yandex.model.SubTask;
import ru.yandex.model.Task;
import ru.yandex.model.TypeTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private final HashMap<Integer, Task> tasks;
    int id;

    public Manager() {
        this.tasks = new HashMap<>();
        id = 0;
    }

    // добавить на вывод эпик айди хз чоттам творится вообще
    ////////////////ОСНОВНЫЕ МЕТОДЫ////////////////////////////////
    ArrayList<Task> getTasksByType(TypeTask typeTask) {
        ArrayList<Task> tasksByType = new ArrayList<>();
        for (HashMap.Entry<Integer, Task> task : tasks.entrySet()) {
            boolean isNeedType = (task.getValue().getTypeTask()).equals(typeTask);
            if (isNeedType) {
                tasksByType.add(task.getValue());
            }
        }
        return tasksByType;
    }

    public void deleteTasksByType(TypeTask typeTask) {
        for (HashMap.Entry<Integer, Task> task : tasks.entrySet()) {
            boolean isNeedType = (task.getValue().getTypeTask()).equals(typeTask);
            if (isNeedType) {
                tasks.remove(task.getKey());
            }
        }
    }

    public Task getById(int id, TypeTask typeTask) {
        boolean validType = typeTask.equals(tasks.get(id).getTypeTask());
        boolean containsId = tasks.containsKey(id);
        if (validType && containsId)
            return tasks.get(id);
        else
            return null;
    }

    public void createTask(Task task) {

        task.setId(id);
        tasks.put(id, task);
        if (task instanceof EpicTask) {
            int epicId = this.id;
            ++id;
            int subTasksSize = ((EpicTask) task).getSubTasks().size();
            for (int i = 0; i < subTasksSize; i++) {
                ((EpicTask) task).getSubTasks().get(i).setId(id);
                ((EpicTask) task).getSubTasks().get(i).setEpicId(epicId);
                ++id;
            }
        } else id++;
    }

    public void updateTask(int id, Task task) {
        task.setId(id);
        tasks.put(id, task);
        if (task instanceof EpicTask) {
            ((EpicTask) task).changeStatus();
            int subTasksSize = ((EpicTask) task).getSubTasks().size();
            for (int i = 0; i < subTasksSize; i++) {
                if (((EpicTask) task).getSubTasks().get(i).getId() == 0) {
                    ((EpicTask) task).getSubTasks().get(i).setId(this.id);
                    ++this.id;
                }
                if (((EpicTask) task).getSubTasks().get(i).getEpicId() == 0) {
                    ((EpicTask) task).getSubTasks().get(i).setEpicId(id);
                }

            }
        }
    }

    public boolean deleteById(int id, TypeTask typeTask) {
        boolean validType = typeTask.equals(tasks.get(id).getTypeTask());
        boolean containsId = tasks.containsKey(id);
        if (validType && containsId) {
            tasks.remove(id);
            return true;
        } else
            return false;
    }

    //////////////////ДОПМЕТОДЫ//////////////////////////////////
    public ArrayList<SubTask> getListSubtask(int id) {
        boolean validType = tasks.get(id).getTypeTask().equals(TypeTask.EPICTASK);
        boolean containsId = tasks.containsKey(id);
        if (validType && containsId) {
            Task task = tasks.get(id);
            return ((EpicTask) task).getSubTasks();
        } else return null;
    }


}



