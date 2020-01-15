package com.example.todoapp.MVVM;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoapp.Models.Todo;
import com.example.todoapp.Models.TodoHistory;

public class TodoDetailViewModel extends AndroidViewModel {
    private TodoRepository mRepository;
    private LiveData<Todo> mTodoDetail;
    private LiveData<TodoHistory> mTodoHistoryDetail;

    public TodoDetailViewModel(Application application, long todoid) {
        super(application);
        mRepository = new TodoRepository(application);
        mTodoDetail = mRepository.getTodoDetail(todoid);
        mTodoHistoryDetail = mRepository.getTodoHistoryDetail(todoid);
    }

    public LiveData<Todo> getmTodoDetails() { return mTodoDetail; }
    public LiveData<TodoHistory> getmTodoHistoryDetails() { return mTodoHistoryDetail; }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application mApplication;
        private final long mTodoId;
        private final TodoRepository mRepository;

        public Factory(@NonNull Application application, long todoId) {
            mApplication = application;
            mTodoId = todoId;
            mRepository = new TodoRepository(application);
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TodoDetailViewModel(mApplication, mTodoId);
        }
    }

}
