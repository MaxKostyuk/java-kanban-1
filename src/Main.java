import ru.yandex.model.*;
import ru.yandex.presenter.Manager;

import java.sql.SQLOutput;
import java.util.ArrayList;

//1.неправильно дается статус эпикам.
//2.айдишники
public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Посуда", "Помыть посуду,прибрать кухню", Status.NEW, TypeTask.TASK);
        Task task2 = new Task("пол", "Помыть пол,прибрать спальню", Status.NEW, TypeTask.TASK);
        Task task3 = new Task("Продукты", "Купить хлеб", Status.NEW, TypeTask.TASK);
        SubTask subTask1 = new SubTask("Квартира", "Найти квартиру для покупки", Status.NEW, TypeTask.SUBTASK);
        SubTask subTask2 = new SubTask("Деньги", "Оформить ипотеку", Status.DONE, TypeTask.SUBTASK);
        SubTask subTask3 = new SubTask("риэлтор", "Найти риэлтора", Status.DONE, TypeTask.SUBTASK);
        ArrayList<SubTask> subTasks = new ArrayList<>();
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        EpicTask epicTask2 = new EpicTask("Ипотека", "Приобрести жилье", subTasks, TypeTask.EPICTASK);
///////////////////////////////////////////////////////////////////////////////////////////////////////////

        manager.createTask(task1);
        manager.createTask(task2);
        System.out.println( "Вывод после создание 2 обычных тасков");

        System.out.println(manager.getById(0,TypeTask.TASK));
        System.out.println(manager.getById(1,TypeTask.TASK));

        manager.createTask(epicTask2);
        System.out.println("вывод при создании");
        System.out.println(manager.getById(2,TypeTask.EPICTASK));

        subTasks.add(subTask3);
        EpicTask epicTask2Update1 = new EpicTask("Ипотека", "Приобрести жилье", subTasks, TypeTask.EPICTASK);


        manager.updateTask(2, epicTask2Update1);
        System.out.println("вывод после обновления");
        System.out.println(manager.getById(2,TypeTask.EPICTASK));

        manager.getListSubtask(1);
        boolean valid = manager.deleteById(5,TypeTask.SUBTASK);
        System.out.println("Удаление " + valid);

        //Task task2 = manager.getById(1);
    }
}
