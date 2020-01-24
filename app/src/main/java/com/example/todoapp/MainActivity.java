package com.example.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoapp.MVVM.TodoViewModel;
import com.example.todoapp.Models.Todo;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView ivAddTodo;
    TextView tvEmpty;
    RecyclerView recyclerViewTodoList;
    TodoAdapter todoAdapter;
    public TodoViewModel todoViewModel;
    List<Todo> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewTodoList = findViewById(R.id.rv_todo);
        ivAddTodo = findViewById(R.id.iv_add);
        tvEmpty = findViewById(R.id.tv_empty);

        ivAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateTodoActivity.class));
            }
        });

        //creating channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.notification_channel),
                    getString(R.string.notification_channel),
                    NotificationManager.IMPORTANCE_HIGH
            );
             NotificationManager manager = getSystemService(NotificationManager.class);
             manager.createNotificationChannel(channel);
        }

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        todoViewModel.getAllTodo().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                todoList = todos;
                todoAdapter = new TodoAdapter(MainActivity.this, todos);
                recyclerViewTodoList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    private void deleteAllTodo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                    todoViewModel.deleteTodo(todoList.get(i));
                }
                Toast.makeText(MainActivity.this, "You have deleted all Task.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_view_done:
                startActivity(new Intent(MainActivity.this, TodoHistoryActivity.class));
                return true;

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
