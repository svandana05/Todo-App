package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todoapp.MVVM.TodoDetailViewModel;
import com.example.todoapp.MVVM.TodoViewModel;
import com.example.todoapp.Models.Todo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CreateTodoActivity extends AppCompatActivity {
    LinedEditText etTodoTitle;
    ImageView ivAddAlarm, ivAlarmOn, ivStarNormal, ivStarHigh;
    String createdDate, createTime, reminderDate, reminderTime, todoTitle;
    int priority=2;
    long todoId;
    Todo todo;
    TextView tvPickDate, tvPickTime;
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
        }

        ivStarNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivStarHigh.setVisibility(View.VISIBLE);
                ivStarNormal.setVisibility(View.GONE);
                priority = 1;
            }
        });

        ivStarHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivStarNormal.setVisibility(View.VISIBLE);
                ivStarHigh.setVisibility(View.GONE);
                priority = 2;
            }
        });


        ivAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmDialog();
            }
        });
        ivAlarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmDialog();
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
                    if (reminderDate!=null&& reminderTime!=null){
                        if (!reminderTime.equals(" ")&&!reminderDate.equals(" ")){
                            ivAlarmOn.setVisibility(View.VISIBLE);
                            ivAddAlarm.setVisibility(View.GONE);
                        }
                    }
                    if (priority==2){
                        ivStarNormal.setVisibility(View.VISIBLE);
                        ivStarHigh.setVisibility(View.GONE);
                    }else if (priority==1){
                        ivStarNormal.setVisibility(View.GONE);
                        ivStarHigh.setVisibility(View.VISIBLE);
                    }
                } catch (NullPointerException e){}
            }
        });

    }

    private void setAlarmDialog(){
        TextView tvCancel, tvOk;
        final AlertDialog.Builder builder = new AlertDialog.Builder(CreateTodoActivity.this);
        View view = LayoutInflater.from(CreateTodoActivity.this).inflate(R.layout.set_reminder, null,false);
        tvPickDate = view.findViewById(R.id.tv_pick_date);
        tvPickTime = view.findViewById(R.id.tv_pick_time);
        tvCancel = view.findViewById(R.id.tv_cancel);
        tvOk = view.findViewById(R.id.tv_ok);
        try {
            if (reminderTime.length()>1 || reminderDate.length()>1){
                tvPickDate.setText(reminderDate);
                tvPickTime.setText(reminderTime);
            }
        }catch (NullPointerException e){}
        builder.setView(view);
        final AlertDialog ad = builder.show();
        ad.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        tvPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        tvPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reminderDate==null){
                    Toast.makeText(CreateTodoActivity.this, "Pick a reminder Date", Toast.LENGTH_SHORT).show();
                }else if (reminderTime==null){
                    Toast.makeText(CreateTodoActivity.this, "Pick a reminder Time", Toast.LENGTH_SHORT).show();
                }else {
                    ad.dismiss();
                    Toast.makeText(CreateTodoActivity.this, "Reminder set successfully!", Toast.LENGTH_SHORT).show();
                    ivAddAlarm.setVisibility(View.GONE);
                    ivAlarmOn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void deleteTodo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                todoViewModel.deleteTodo(todo);
                Toast.makeText(CreateTodoActivity.this, "Tast is deleted.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(CreateTodoActivity.this, MainActivity.class));
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
                setAlarm(millis);
            }catch (NullPointerException r){}


            finish();
            startActivity(new Intent(CreateTodoActivity.this, MainActivity.class));
        }
    }

    private void setAlarm(long time) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent i = new Intent(CreateTodoActivity.this, MyReceiver.class);
        i.putExtra("TITLE", todoTitle);
        PendingIntent pi = PendingIntent.getBroadcast(CreateTodoActivity.this, 0, i, 0);

        am.set(AlarmManager.RTC_WAKEUP, time, pi);

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
            Log.e("TODO item :", " "+createdDate +", "+ createTime+ ", "+reminderDate+", "+reminderTime+ ", "+todoTitle+", "+priority);
            Todo todo = new Todo(createdDate, reminderDate, createTime, reminderTime, todoTitle, priority);
            todo.setTodoId(todoId);
            todoViewModel.updateTodoDetail(todo);
            Toast.makeText(this, "Your task updated successfully.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(CreateTodoActivity.this, MainActivity.class));
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
                tvPickDate.setText(reminderDate);
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
                tvPickTime.setText(reminderTime);
            }
        }, mHour, mMinute, true);//Yes 24 mHour time
        mTimePicker.show();
    }

    private void initializingView(){
        etTodoTitle = findViewById(R.id.todo_text);
        ivAddAlarm = findViewById(R.id.iv_alarm_add);
        ivAlarmOn = findViewById(R.id.iv_alarm_on);
        ivStarNormal = findViewById(R.id.iv_star_normal);
        ivStarHigh = findViewById(R.id.iv_star_high);
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
