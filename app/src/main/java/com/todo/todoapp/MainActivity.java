package com.todo.todoapp;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.todo.todoapp.MVVM.TodoViewModel;
import com.todo.todoapp.Models.Todo;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ImageView ivAddTodo;
    TextView tvEmpty;
    RecyclerView recyclerViewTodoList;
    TodoAdapter todoAdapter;
    public TodoViewModel todoViewModel;
    List<Todo> todoList;
    String title = "";
    UpdateManager mUpdateManager;
    ApplicationClass applicationClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewTodoList = findViewById(R.id.rv_todo);
        ivAddTodo = findViewById(R.id.iv_add);
        tvEmpty = findViewById(R.id.tv_empty);

        applicationClass = new ApplicationClass();

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.your_tasks);
        Intent intent = getIntent();
        title = intent.getStringExtra("TODO_TITLE");
        updateApp();

        try {
            if (title.length()>1){
                showDialog(title);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        ivAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationClass.enterIntentAnim(MainActivity.this);
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
            assert manager != null;
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
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void updateApp() {
        // Initialize the Update Manager with the Activity and the Update Mode
        mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.FLEXIBLE);
        mUpdateManager.start();
        mUpdateManager.addUpdateInfoListener(new UpdateManager.UpdateInfoListener() {
            @Override
            public void onReceiveVersionCode(final int code) {
                // You can get the available version code of the apk in Google Play
                // Do something here
                Log.e("PlayStoreUpdateCode", ""+code);
            }

            @Override
            public void onReceiveStalenessDays(final int days) {
                // Number of days passed since the user was notified of an update through the Google Play
                // If the user hasn't notified this will return -1 as days
                // You can decide the type of update you want to call
            }
        });
    }

    private void showDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogSlideAnimLeftRight);
        builder.setTitle("Reminder task detail");
        builder.setMessage(title);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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
                    Todo todo = todoList.get(i);
                    todoViewModel.deleteTodo(todo);
                    applicationClass.cancelAlarm(todo.getTicks(), todo.getTodoNote(), getApplicationContext());
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
                ApplicationClass.enterIntentAnim(MainActivity.this);
                startActivity(new Intent(MainActivity.this, TodoHistoryActivity.class));
                return true;

            case R.id.action_delete_all:
                try {
                    if (todoList.size()<1){
                        Toast.makeText(this, "There is no task to delete!", Toast.LENGTH_SHORT).show();
                    }else {
                        deleteAllTodo();
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ApplicationClass.backPressIntentAnim(this);
    }
}
