package com.todo.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.todo.todoapp.MVVM.TodoViewModel;
import com.todo.todoapp.Models.TodoHistory;

import java.util.List;

public class TodoHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerViewTodoList;
    TodoHistoryAdapter todoAdapter;
    public TodoViewModel todoViewModel;
    List<TodoHistory> todoList;
    TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_history);
        recyclerViewTodoList = findViewById(R.id.rv_todo_history);
        tvEmpty = findViewById(R.id.tv_empty);

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        todoViewModel.getAllTodoHistory().observe(this, new Observer<List<TodoHistory>>() {
            @Override
            public void onChanged(List<TodoHistory> todos) {
                todoList = todos;
                todoAdapter = new TodoHistoryAdapter(TodoHistoryActivity.this, todos);
                recyclerViewTodoList.setLayoutManager(new LinearLayoutManager(TodoHistoryActivity.this));
                recyclerViewTodoList.setAdapter(todoAdapter);
                try {
                    if (todoList==null || todoList.size()<1){
                        tvEmpty.setVisibility(View.VISIBLE);
                        recyclerViewTodoList.setVisibility(View.GONE);
                    }else {
                        tvEmpty.setVisibility(View.GONE);
                        recyclerViewTodoList.setVisibility(View.VISIBLE);
                    }
                }catch (NullPointerException e){}
            }
        });


    }


    private void deleteAllTodo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogSlideAnimLeftRight);
        builder.setMessage("Are you sure you want to delete all Task?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                for (int i=0; i<todoList.size(); i++){
                    todoViewModel.deleteTodoHistory(todoList.get(i));
                }
                Toast.makeText(TodoHistoryActivity.this, "You have deleted all Task.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.action_view_done).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete_all:
                try {
                    if (todoList.size()<1){
                        Toast.makeText(this, "There is no task to delete!", Toast.LENGTH_SHORT).show();
                    }else {
                        deleteAllTodo();
                    }
                }catch (NullPointerException e){}
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
