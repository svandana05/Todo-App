package com.example.todoapp.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "todo_table")
public class Todo {
    private String CreateDate, reminderDate;
    private String CreateTime, reminderTime;
    private String TodoNote;
    private int ticks;

    @PrimaryKey(autoGenerate = true)
    long TodoId;

    int TodoPriority;
    static final int URGENT = 1;
    static final int NORMAL = 2;

    public Todo() {
    }

    public Todo(String createDate, String reminderDate, String createTime, String reminderTime, String todoNote, int todoPriority) {
        CreateDate = createDate;
        this.reminderDate = reminderDate;
        CreateTime = createTime;
        this.reminderTime = reminderTime;
        TodoNote = todoNote;
        TodoPriority = todoPriority;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public int getTodoPriority() {
        return TodoPriority;
    }

    public void setTodoPriority(int todoPriority) {
        this.TodoPriority = todoPriority;
    }

    public long getTodoId() {
        return TodoId;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public String getTodoNote() {
        return TodoNote;
    }

    public void setTodoId(long todoId) {
        TodoId = todoId;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public void setTodoNote(String todoNote) {
        TodoNote = todoNote;
    }
}
