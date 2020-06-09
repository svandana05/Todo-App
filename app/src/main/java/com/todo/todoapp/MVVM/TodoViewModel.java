package com.todo.todoapp.MVVM;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.todo.todoapp.Models.Todo;
import com.todo.todoapp.Models.TodoHistory;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private TodoRepository mRepository;
    private LiveData<List<Todo>> mAllTodo;
    private LiveData<List<TodoHistory>> mAllTodoHistory;

    public TodoViewModel(Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        mAllTodo = mRepository.getAllTodo();
        mAllTodoHistory = mRepository.getAllTodoHistory();
    }

    public LiveData<List<Todo>> getAllTodo() { return mAllTodo; }
    public void insertTodoDetail(Todo data) { mRepository.insertTodoDetail(data); }
    public void updateTodoDetail(Todo data) { mRepository.updateTodoDetail(data); }
    public void deleteTodo(Todo data) { mRepository.deleteTodo(data); }


    public LiveData<List<TodoHistory>> getAllTodoHistory() { return mAllTodoHistory; }
    public void insertTodoHistoryDetail(TodoHistory data) { mRepository.insertTodoHistoryDetail(data); }
    public void deleteTodoHistory(TodoHistory data) { mRepository.deleteTodoHistory(data); }
}
