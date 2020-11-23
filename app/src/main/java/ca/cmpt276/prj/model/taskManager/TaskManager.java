package ca.cmpt276.prj.model.taskManager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.coinManager.Flip;

public class TaskManager {

    private static TaskManager taskManager;
    private ArrayList<Child> childrenList;
    private ArrayList<Task> taskList;

    public int size(){
        return taskList.size();
    }
    public ArrayList<Task> getTaskList() {
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

    public void remove(Task newTask) { taskList.remove(newTask); }

    public Task retrieving(int index){
        return taskList.get(index);
    }

    public int returnint(int i){
        return i;
    }

    private TaskManager(){
        this.childrenList = new ArrayList<>();
        taskList=new ArrayList<>();
//        taskList.add(new Task("ff","tt"));
//        taskList.add(new Task("dd","tt"));
//        taskList.add(new Task("gg","tt"));
//        taskList.add(new Task("hh","tt"));
    }

    public void addChild(Child child) {
        childrenList.add(childrenList.size(), child);
    }

    public Child removeChild(Child child) {
        for (int i=0; i<childrenList.size(); i++) {
            if (childrenList.get(i).getName().equals(child.getName())) {
                return childrenList.remove(i);
            }
        }
        return null;
    }

    public Child getChild(int index) {
        return childrenList.get(index);
    }

    public void wipeChildrens() {
        childrenList = new ArrayList<>();
    }



    public static TaskManager getInstance(){
        if(taskManager==null){
            taskManager =new TaskManager();
        }
        return taskManager;
    }

    public void removeByIndex(int index){
        taskList.remove(retrieving(index));
    }

    public void EditTask(String task,String description,int index){
        removeByIndex(index);
        Task editTask=new Task(task,description);
        taskList.add(editTask);
    }

    public ArrayList<Child> getChildrenList() {
        return childrenList;
    }


    public void setChildrenList(ArrayList<Child> list){
        wipeChildrens();
        for(Child child : list){
            addChild(child);
        }
    }


}
