package com.iuh.thach.todoapp;



public class ListToDo {
    public String task;
    public String date;
    public String priority;
    public int id;
    public ListToDo(int id,String task,String date,String priority){
        this.id = id;
        this.task = task;
        this.date = date;
        this.priority = priority;
    }
}