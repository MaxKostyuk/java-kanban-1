import ru.yandex.model.*;
import ru.yandex.presenter.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        System.out.println("ОБЫЧНЫЕ ТАСКИ:");
        Task task1 = new Task("Посуда", "Помыть посуду,прибрать кухню", Status.NEW, TypeTask.TASK);
        Task task2 = new Task("пол", "Помыть пол,прибрать спальню", Status.INPROGRESS, TypeTask.TASK);
        Task task3 = new Task("Продукты", "Купить хлеб", Status.DONE, TypeTask.TASK);

        manager.createTaskAndReturnId(task1);
        manager.createTaskAndReturnId(task2);
        manager.createTaskAndReturnId(task3);


        System.out.println(manager.getByIdAndTypeTask(0, TypeTask.TASK));
        System.out.println(manager.getByIdAndTypeTask(1, TypeTask.TASK));
        System.out.println(manager.getByIdAndTypeTask(2, TypeTask.TASK));
        System.out.println("//////////////////////////////////////////////////");
        System.out.println("ЭПИК ТАСК:");
        EpicTask epicTask1 = new EpicTask("Ипотека", "Приобрести жилье", TypeTask.EPICTASK);
        int epicId = manager.createTaskAndReturnId(epicTask1);
        System.out.println(manager.getByIdAndTypeTask(3, TypeTask.EPICTASK));
        System.out.println("//////////////////////////////////////////////////");

        System.out.println("САБТАСКИ ПОСЛЕ ДОБАВЛЕНИЯ");
        SubTask subTask1 = new SubTask("Квартира", "Найти квартиру для покупки", Status.NEW, TypeTask.SUBTASK, epicId);
        SubTask subTask2 = new SubTask("Деньги", "Оформить ипотеку", Status.DONE, TypeTask.SUBTASK, epicId);
        SubTask subTask3 = new SubTask("риэлтор", "Найти риэлтора", Status.DONE, TypeTask.SUBTASK, epicId);
        manager.createTaskAndReturnId(subTask1);
        manager.createTaskAndReturnId(subTask2);
        manager.createTaskAndReturnId(subTask3);
        System.out.println(manager.getByIdAndTypeTask(4, TypeTask.SUBTASK));
        System.out.println(manager.getByIdAndTypeTask(5, TypeTask.SUBTASK));
        System.out.println(manager.getByIdAndTypeTask(6, TypeTask.SUBTASK));
        System.out.println("//////////////////////////////////////////////////");

        System.out.println("ЭПИК ТАСК ПОСЛЕ ДОбАВЛЕНИЯ САБ ТАСКОВ");
        System.out.println(manager.getByIdAndTypeTask(3, TypeTask.EPICTASK));
        System.out.println("//////////////////////////////////////////////////");

        System.out.println("ОБНОВЛЕНИЕ ЗАДАЧИ АЙДИ2");
        Task UpdateTask2 = new Task("пол", "Помыть пол", Status.DONE, TypeTask.TASK);
        manager.updateTask(2, UpdateTask2);
        System.out.println(manager.getByIdAndTypeTask(2, TypeTask.TASK));
        System.out.println("//////////////////////////////////////////////////");

        System.out.println("УДАЛЕНИЕ ЗАДАЧИ ПО АЙДИ №1 : " + manager.deleteByIdAndTypeTask(1, TypeTask.TASK));
        System.out.println("//////////////////////////////////////////////////");

        System.out.println("ВЫВОД ЗАДАЧ ТИПА ТАСК :");
        System.out.println(manager.getTasksByType(TypeTask.TASK));
        System.out.println("//////////////////////////////////////////////////");

        System.out.println("УДАЛЕНИЕ САБТАСКИ И ВЫВОД ЭПИКА");
        manager.deleteByIdAndTypeTask(4, TypeTask.SUBTASK);
        System.out.println(manager.getByIdAndTypeTask(3, TypeTask.EPICTASK));
        System.out.println("//////////////////////////////////////////////////");
        System.out.println("ВЫВОД САБТАСКОВ через айди эпика " + manager.getListSubtask(3));
        System.out.println("//////////////////////////////////////////////////");
        System.out.println("ВЫВОД ВСЕХ САБТАСКОВ " + manager.getTasksByType(TypeTask.SUBTASK));
        System.out.println("//////////////////////////////////////////////////");


        System.out.println("УДАЛЕНИЕ АБСОЛЮТНО ВСЕХ САБТАСКОВ И ВЫВОД ЭПИКA");
        manager.deleteTasksByType(TypeTask.SUBTASK);
        System.out.println(manager.getByIdAndTypeTask(3, TypeTask.EPICTASK));
        System.out.println("//////////////////////////////////////////////////");

        System.out.println("СОЗДАНИЕ ЭПИКА 2");
        EpicTask epicTask2 = new EpicTask("Купить аляскинского маламута", "Большого и пушистого!", TypeTask.EPICTASK);
        int epicId2 = manager.createTaskAndReturnId(epicTask2);


        System.out.println("ВЫВОД ЭПИКА 2" + manager.getByIdAndTypeTask(epicId2, TypeTask.EPICTASK));
        System.out.println("//////////////////////////////////////////////////");
        System.out.println("ВЫВОД ВСЕХ ЭПИКОВ");
        System.out.println(manager.getTasksByType(TypeTask.EPICTASK));
        System.out.println("//////////////////////////////////////////////////");


        System.out.println("СПИСОК САБТАСКОВ ПОСЛЕ ДОбАВЛЕНИЯ САБ ТАСКИ ВЭПИК АЙДИ3");
        SubTask subTask4 = new SubTask("Платеж", "плати пока не умрешь", Status.INPROGRESS, TypeTask.SUBTASK, epicId);
        manager.createTaskAndReturnId(subTask4);
        System.out.println(manager.getListSubtask(3));
        System.out.println("//////////////////////////////////////////////////");
        System.out.println("ВЫВОД ВСЕХ ЭПИКОВ ЕЩЕ РАЗ");
        System.out.println(manager.getTasksByType(TypeTask.EPICTASK));

        SubTask UpdateSubTask4 = new SubTask("Платеж", "плати пока не умрешь", Status.DONE, TypeTask.SUBTASK, epicId);
        manager.updateTask(8,UpdateSubTask4);

        System.out.println("ОБНОВИЛИ САБТАСКУ ПО ИПОТЕКЕ , ВЫВОД ВСЕХ ЭПИКОВ ЕЩЕ РАЗОЧЕК )))");
        System.out.println(manager.getTasksByType(TypeTask.EPICTASK));

        System.out.println("ДОБАВИЛИ САБТСАКУ ПО СОБАКЕ и ВЫВЕЛИ ВСЕ ЭПИКИ");
        SubTask subTask5 = new SubTask("Маламут", "найти собаку",Status.NEW,TypeTask.EPICTASK,epicId2);
        System.out.println(manager.getTasksByType(TypeTask.EPICTASK));
        manager.createTaskAndReturnId(subTask5);
        System.out.println("УДАЛЕНИЕ ВСЕХ ОБЫЧНЫХ ТАСКОВ - " + manager.deleteTasksByType(TypeTask.TASK));
        System.out.println("Осталось? " + manager.getTasksByType(TypeTask.TASK));

        System.out.println("Удалили ЭПИК ипотеки?" + manager.deleteByIdAndTypeTask(3,TypeTask.EPICTASK));
        System.out.println("Смотрим сабтаски");
        System.out.println(manager.getTasksByType(TypeTask.SUBTASK));
        System.out.println("Конец");

    }
}
