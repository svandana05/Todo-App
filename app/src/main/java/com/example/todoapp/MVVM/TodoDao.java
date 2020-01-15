package com.example.todoapp.MVVM;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoapp.Models.Todo;
import com.example.todoapp.Models.TodoHistory;

import java.util.List;

@Dao
public interface TodoDao {

    @Query("SELECT * FROM todo_table")
    LiveData<List<Todo>> getAllTodo();

    @Query("SELECT * FROM  todo_table WHERE TodoId = :todoId")
    LiveData<Todo> getTodoDetailByID(final long todoId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTodoDetail(Todo todoDetail);

    @Update
    void updateTodoDetail(Todo todoDetail);

    @Delete
    void  deleteTodo(Todo todo);


    // history
    @Query("SELECT * FROM todo_history_table")
    LiveData<List<TodoHistory>> getAllTodoHistory();

    @Query("SELECT * FROM  todo_history_table WHERE TodoId = :todoId")
    LiveData<TodoHistory> getTodoHistoryDetailByID(final long todoId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTodoHistoryDetail(TodoHistory todoDetail);

    @Delete
    void  deleteTodoHistory(TodoHistory todo);

}
