package com.example.todoapp.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_history_table")
public class TodoHistory {
    String CreateDate, reminderDate;
    String CreateTime, reminderTime;
    String TodoNote;

    @PrimaryKey(autoGenerate = true)
    long TodoId;

    int TodoPriority;
    public TodoHistory() {
    }

    public TodoHistory(String createDate, String reminderDate, String createTime, String reminderTime, String todoNote, int todoPriority) {
        CreateDate = createDate;
        this.reminderDate = reminderDate;
        CreateTime = createTime;
        this.reminderTime = reminderTime;
        TodoNote = todoNote;
        TodoPriority = todoPriority;
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
