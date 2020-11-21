package ca.cmpt276.prj.model.taskManager;

import java.util.ArrayList;

public class TaskManager {

    private static TaskManager taskManager;

    private ArrayList<Task> taskList;

    public int size(){
        return taskList.size();
    }
    public ArrayList<Task> getLens() {
        return taskList;
    }
    public void setLens(ArrayList<Task> lens) {
        this.taskList = taskList;
    }
//    public static TaskManager getInstance() {
//        if (taskManager == null) taskManager = new TaskManager();
//        return taskManager;
//    }
//
//    public static void loadInstance(TaskManager instance) {
//        if (instance != null) taskManager = instance;
//    }
//
//    public void addTask(Task task) {
//        taskList.add(taskList.size(), task);
//    }
//
//    public Task removeTask(Task task) {
//        for (int i=0; i<taskList.size(); i++) {
//            if (taskList.get(i).getName().equals(task.getName())) {
//                return taskList.remove(i);
//            }
//        }
//        return null;
//    }
//
//    public void wipeTasks() {
//        taskList = new ArrayList<>();
//    }
    public void add(Task newTask){
    taskList.add(newTask);
}

    public Task retrieving(int index){
        return taskList.get(index);
    }

    private TaskManager(){
        taskList=new ArrayList<>();
    }

    public static TaskManager getInstance(){
        if(taskManager==null){
            taskManager =new TaskManager();
        }
        return taskManager;
    }

}
