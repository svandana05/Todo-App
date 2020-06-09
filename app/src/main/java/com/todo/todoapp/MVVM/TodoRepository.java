package com.todo.todoapp.MVVM;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import com.todo.todoapp.Models.Todo;
import com.todo.todoapp.Models.TodoHistory;

import java.util.List;

public class TodoRepository {
    private TodoDao todoDao;
    private LiveData<List<Todo>> mAllTodo;
    private LiveData<Todo> todoDetail;
    private LiveData<List<TodoHistory>> mAllTodoHistory;
    private LiveData<TodoHistory> todoHistoryDetail;

    public TodoRepository(Application application) {
        TodoDatabase db = TodoDatabase.getDatabase(application);
        todoDao = db.todoDao();
        mAllTodo = todoDao.getAllTodo();
        mAllTodoHistory = todoDao.getAllTodoHistory();
    }

    LiveData<List<Todo>> getAllTodo() {
        return mAllTodo;
    }
    LiveData<Todo> getTodoDetail(long id) {
        Log.e("TodoRepo", "ID is-"+id);
        return todoDetail = todoDao.getTodoDetailByID(id);
    }

    LiveData<List<TodoHistory>> getAllTodoHistory() {
        return mAllTodoHistory;
    }
    LiveData<TodoHistory> getTodoHistoryDetail(long id) {
        Log.e("TodoRepo", "ID is-"+id);
        return todoHistoryDetail = todoDao.getTodoHistoryDetailByID(id);
    }

    void insertTodoDetail(Todo todo) {
        new insertAsyncTask1(todoDao).execute(todo);
    }
     void updateTodoDetail(Todo todo) {
        new updateAsyncTask1(todoDao).execute(todo);
    }
     void deleteTodo(Todo todo) {
        new deleteAsyncTask1(todoDao).execute(todo);
    }

    void insertTodoHistoryDetail(TodoHistory todo) {
        new insertAsyncTask2(todoDao).execute(todo);
    }
    void deleteTodoHistory(TodoHistory todo) {
        new deleteAsyncTask2(todoDao).execute(todo);
    }


    private static class insertAsyncTask1 extends AsyncTask<Todo, Void, Void> {
        private TodoDao mAsyncTaskDao;
        insertAsyncTask1(TodoDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Todo... params) {
            mAsyncTaskDao.insertTodoDetail(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask1 extends AsyncTask<Todo, Void, Void> {
        private TodoDao mAsyncTaskDao;
        updateAsyncTask1(TodoDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Todo... params) {
            mAsyncTaskDao.updateTodoDetail(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask1 extends AsyncTask<Todo, Void, Void> {
        private TodoDao mAsyncTaskDao;
        deleteAsyncTask1(TodoDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Todo... params) {
            mAsyncTaskDao.deleteTodo(params[0]);
            return null;
        }
    }


    private static class insertAsyncTask2 extends AsyncTask<TodoHistory, Void, Void> {
        private TodoDao mAsyncTaskDao;
        insertAsyncTask2(TodoDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final TodoHistory... params) {
            mAsyncTaskDao.insertTodoHistoryDetail(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask2 extends AsyncTask<TodoHistory, Void, Void> {
        private TodoDao mAsyncTaskDao;
        deleteAsyncTask2(TodoDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final TodoHistory... params) {
            mAsyncTaskDao.deleteTodoHistory(params[0]);
            return null;
        }
    }

}
