package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todoapp.MVVM.TodoDetailViewModel;
import com.example.todoapp.MVVM.TodoViewModel;
import com.example.todoapp.Models.Todo;
import com.example.todoapp.Models.TodoHistory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateTodoActivity extends AppCompatActivity {
    EditText etTodoTitle;
    ImageView ivAddAlarm, ivStarNormal, ivDone;
    String createdDate, createTime, reminderDate, reminderTime, todoTitle;
    int priority=2;
    long todoId;
    Todo todo;
    TextView etPickDate, etPickTime;
    String shortTitle;

    private TodoDetailViewModel todoDetailViewModel;
    TodoDetailViewModel.Factory factory;
    TodoViewModel todoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        initializingView();

        Intent intent = getIntent();
        todoId = intent.getLongExtra("TODO_ID", 0);
        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);

        factory = new TodoDetailViewModel.Factory(getApplication(), todoId);
        todoDetailViewModel = new ViewModelProvider(this, factory)
                .get(TodoDetailViewModel.class);

        if (todoId==0){
            setTitle("New Task");
        }else {
            setTitle("Edit Task");
            loadTodo();
            showDone();
        }

        ivStarNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priority==2){
                    ivStarNormal.startAnimation(AnimationUtils.loadAnimation(CreateTodoActivity.this, R.anim.zoom_in_anim));
                    ivStarNormal.setImageDrawable(getDrawable(R.drawable.ic_star));
                    priority=1;
                }else if (priority==1){
                    ivStarNormal.startAnimation(AnimationUtils.loadAnimation(CreateTodoActivity.this, R.anim.zoom_in_anim));
                    ivStarNormal.setImageDrawable(getDrawable(R.drawable.ic_star_border));
                    priority=2;
                }
            }
        });

        ivAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivAddAlarm.startAnimation(AnimationUtils.loadAnimation(CreateTodoActivity.this, R.anim.zoom_in_anim));
                setAlarmDialog();
            }
        });

    }

    private void showDone(){
        ivDone.setVisibility(View.VISIBLE);
        ivDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivDone.startAnimation(AnimationUtils.loadAnimation(CreateTodoActivity.this, R.anim.zoom_in_anim));
                ivDone.setColorFilter(getColor(R.color.colorAccent));
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        TodoHistory todoHistory = new TodoHistory();
                        todoHistory.setCreateDate(todo.getCreateDate());
                        todoHistory.setCreateTime(todo.getCreateTime());
                        todoHistory.setReminderDate(todo.getReminderDate());
                        todoHistory.setReminderTime(todo.getReminderTime());
                        todoHistory.setTodoNote(todo.getTodoNote());
                        todoHistory.setTodoPriority(todo.getTodoPriority());
                        todoViewModel.insertTodoHistoryDetail(todoHistory);
                        todoViewModel.deleteTodo(todo);
                        ApplicationClass.enterIntentAnim(CreateTodoActivity.this);
                        startActivity(new Intent(CreateTodoActivity.this, MainActivity.class));
                    }
                }, 1000);
            }
        });
    }
    private void loadTodo(){
        todoDetailViewModel.getmTodoDetails().observe(this, new Observer<Todo>() {
            @Override
            public void onChanged(Todo todoLoaded) {
                todo = todoLoaded;
                try {
                    Log.e("TODO loaded :", " "+todoLoaded.getTodoId()+", "+ todoLoaded.getTodoNote()+ ", "
                            +todoLoaded.getTodoPriority()+", reminder "+ todoLoaded.getReminderDate()+todoLoaded.getReminderTime());
                    etTodoTitle.setText(todoLoaded.getTodoNote());
                    reminderDate = todoLoaded.getReminderDate();
                    reminderTime = todoLoaded.getReminderTime();
                    priority = todoLoaded.getTodoPriority();
                    createdDate = todoLoaded.getCreateDate();
                    createTime = todoLoaded.getCreateTime();
                    try {
                        if (priority==1){
                            ivStarNormal.setImageDrawable(getDrawable(R.drawable.ic_star));
                        }else if (priority==2){
                            ivStarNormal.setImageDrawable(getDrawable(R.drawable.ic_star_border));
                        }
                    }catch (NullPointerException e){}
                    try {
                        if (reminderDate!=null&&reminderTime!=null){
                            if (reminderDate.length()>1&&reminderTime.length()>1){
                                ivAddAlarm.setImageDrawable(getDrawable(R.drawable.ic_alarm_on));
                            }
                        }else {
                            ivAddAlarm.setImageDrawable(getDrawable(R.drawable.ic_add_alarm));
                        }
                    }catch (NullPointerException e){}

                } catch (NullPointerException e){}
            }
        });

    }

    private void setAlarmDialog(){
        ConstraintLayout clCancel, clOk;
        final AlertDialog.Builder builder = new AlertDialog.Builder(CreateTodoActivity.this, R.style.DialogSlideAnimLeftRight);
        View view = LayoutInflater.from(CreateTodoActivity.this).inflate(R.layout.dialog_layout, null,false);
        etPickDate = view.findViewById(R.id.etPickDate);
        etPickTime = view.findViewById(R.id.etPickTime);
        clCancel = view.findViewById(R.id.cl_cancel_btn);
        clOk = view.findViewById(R.id.cl_ok);
        try {
            if (reminderTime.length()>1 || reminderDate.length()>1){
                etPickDate.setText(reminderDate);
                etPickTime.setText(reminderTime);
            }
        }catch (NullPointerException e){}
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog ad = builder.create();
        ad.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ad.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ad.show();

        etPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        etPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime();
            }
        });

        clCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        clOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reminderDate==null){
                    etPickDate.setFocusable(true);
                    etPickDate.setError("Pick a Date");
                }else
                    etPickDate.setError(null);

                if (reminderTime==null){
                    etPickTime.setFocusable(true);
                    etPickTime.setError("Pick a Time");
                }else
                    etPickTime.setError(null);

                if (reminderDate != null && reminderTime != null){
                    ad.dismiss();
                    Toast.makeText(CreateTodoActivity.this, "Reminder set successfully!", Toast.LENGTH_SHORT).show();
                    ivAddAlarm.setImageDrawable(getDrawable(R.drawable.ic_alarm_on));
                }
            }
        });
    }

    private void deleteTodo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogSlideAnimLeftRight);
        builder.setMessage("Are you sure you want to delete?");
        builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelAlarm(todo.getTicks());
                todoViewModel.deleteTodo(todo);
                Toast.makeText(CreateTodoActivity.this, "Task is deleted.", Toast.LENGTH_SHORT).show();
                finish();
                ApplicationClass.backPressIntentAnim(CreateTodoActivity.this);

            }
        });
        builder.show();
    }

    private void createTODO(){
        todoTitle = etTodoTitle.getText().toString();
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat date = new SimpleDateFormat("dd MMM yyyy");
        DateFormat time = new SimpleDateFormat("hh:mm:ss a");
        createdDate = date.format(currentTime);
        createTime = time.format(currentTime);
        if (TextUtils.isEmpty(todoTitle)){
            etTodoTitle.setError("This field is required!");
        }else if (todoTitle.length()<5){
            etTodoTitle.setError("Too small task description!");
            Toast.makeText(this, "Please enter some more description.", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.e("TODO item :", " "+createdDate +", "+ createTime+ ", "+reminderDate+", "+reminderTime+ ", "+todoTitle+", "+priority);
            Todo todo = new Todo(createdDate, reminderDate, createTime, reminderTime, todoTitle, priority);
            shortTitle = todoTitle.trim();
            if (shortTitle.length()>20){
                shortTitle.substring(0, 15);
            }
            int ticks = (int) System.currentTimeMillis();
            todo.setTicks(ticks);
            todoViewModel.insertTodoDetail(todo);
            Toast.makeText(this, "Your task created successfully.", Toast.LENGTH_SHORT).show();


            try {
                String reminder = reminderDate+" "+reminderTime;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy HH:mm");
                Date remindetdate = null;
                try {
                    remindetdate = sdf.parse(reminder);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long millis = remindetdate.getTime();
                Log.e("reminder", "Choosen "+reminder+", parsed "+remindetdate+", millis"+millis);
                setAlarm(millis, ticks);
            }catch (NullPointerException r){}
            finish();
        }
    }

    private void setAlarm(long time, int ticks) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(CreateTodoActivity.this, MyReceiver.class);
        i.putExtra("TITLE", shortTitle);
        PendingIntent pi = PendingIntent.getBroadcast(CreateTodoActivity.this, ticks, i, 0);
        am.set(AlarmManager.RTC_WAKEUP, time, pi);
    }

    private void cancelAlarm(int ticks) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(CreateTodoActivity.this, MyReceiver.class);
        i.putExtra("TITLE", shortTitle);
        PendingIntent pi = PendingIntent.getBroadcast(CreateTodoActivity.this, ticks, i, 0);
        am.cancel(pi);
    }

    private void updateTodo(){
        todoTitle = etTodoTitle.getText().toString();
        if (TextUtils.isEmpty(todoTitle)){
            etTodoTitle.setError("Fill the title of the Task!");
        }else if (todoTitle.length()<5){
            etTodoTitle.setError("Too small task description!");
            Toast.makeText(this, "Please enter some more description.", Toast.LENGTH_SHORT).show();
        }
        else {
            if (todo.getTicks()!=0){
                cancelAlarm(todo.getTicks());
            }
            Log.e("TODO item :", " "+createdDate +", "+ createTime+ ", "+reminderDate+", "+reminderTime+ ", "+todoTitle+", "+priority);
            Todo todo = new Todo(createdDate, reminderDate, createTime, reminderTime, todoTitle, priority);
            todo.setTodoId(todoId);
            int ticks = (int) System.currentTimeMillis();
            todo.setTicks(ticks);
            todoViewModel.updateTodoDetail(todo);
            Toast.makeText(this, "Your task updated successfully.", Toast.LENGTH_SHORT).show();

            try {
                String reminder = reminderDate+" "+reminderTime;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy HH:mm");
                Date remindetdate = null;
                try {
                    remindetdate = sdf.parse(reminder);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long millis = remindetdate.getTime();
                Log.e("reminder", "Choosen "+reminder+", parsed "+remindetdate+", millis"+millis);

                setAlarm(millis, ticks);
            }catch (NullPointerException r){}
            finish();
        }
    }


    private void pickDate(){
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(CreateTodoActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                selectedmonth = selectedmonth + 1;
                reminderDate = ("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                etPickDate.setText(reminderDate);
            }
        }, mYear, mMonth, mDay);
        mDatePicker.show();
    }

    private void pickTime(){
        Calendar mcurrentTime = Calendar.getInstance();
        int mHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int mMinute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CreateTodoActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                reminderTime = ( selectedHour + ":" + selectedMinute);
                etPickTime.setText(reminderTime);
            }
        }, mHour, mMinute, true);//Yes 24 mHour time
        mTimePicker.show();
    }

    private void initializingView(){
        etTodoTitle = findViewById(R.id.todo_text);
        ivAddAlarm = findViewById(R.id.iv_alarm_add);
        ivStarNormal = findViewById(R.id.iv_star_normal);
        ivDone = findViewById(R.id.iv_done);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_todo_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (todoId == 0){
            menu.findItem(R.id.action_delete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done:
                if (todoId==0){
                    createTODO();
                }else {
                    updateTodo();
                }
                return true;
            case R.id.action_delete:
                deleteTodo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
