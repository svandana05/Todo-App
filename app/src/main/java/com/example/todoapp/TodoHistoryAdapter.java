package com.example.todoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Models.Todo;
import com.example.todoapp.Models.TodoHistory;

import java.util.List;

public class TodoHistoryAdapter extends RecyclerView.Adapter<TodoHistoryAdapter.DataViewHolder> {
    Context context;
    List<TodoHistory> todoList;

    public TodoHistoryAdapter(Context context, List<TodoHistory> todoList) {
        this.context = context;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        TodoHistoryAdapter.DataViewHolder dataViewHolder = new TodoHistoryAdapter.DataViewHolder(view);
        return dataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DataViewHolder holder, int position) {
        final TodoHistory todo = todoList.get(position);
        Log.e("Adapter", "TodoHistory- "+position+ " "+ todo);
        final String todoNote = todo.getTodoNote();
        holder.tvTodoTitle.setText(todoNote);
        holder.tvTodoTitle.setMaxLines(3);
        holder.tvTodoCreatedTime.setText(todo.getCreateTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String priority;
                if (todo.getTodoPriority()==1){
                    priority="Urgent";
                }else {
                    priority = "Normal";
                }
                String reminder = "";
                try {
                    if (!todo.getReminderDate().equals("")&& !todo.getReminderTime().equals("")){
                        reminder="Reminder Date-"+todo.getReminderDate()+" and time-"+todo.getReminderTime()+"\n";
                    }
                }catch (NullPointerException e){}
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogSlideAnimLeftRight);
                builder.setTitle("Detail of Task ");
                builder.setMessage("Priority: "+ priority + "\n"+reminder+"Task: "+todoNote);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.show();
            }
        });

        holder.tvDone.setVisibility(View.INVISIBLE);
        // group item by date
        String date = todo.getCreateDate();
        if (position==0){
            holder.tvDate.setVisibility(View.VISIBLE);
            holder.tvDate.setText(date);
        }else {
            String previousDate = todoList.get(position-1).getCreateDate();
            if (date.contains(previousDate)){
                holder.tvDate.setVisibility(View.GONE);
            }else {
                holder.tvDate.setText(date);
                holder.tvDate.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tvTodoTitle, tvTodoCreatedTime, tvDate, tvDone;

        public DataViewHolder(final View itemView) {
            super(itemView);
            tvTodoTitle = itemView.findViewById(R.id.tv_todo_title);
            tvTodoCreatedTime = itemView.findViewById(R.id.tv_created_time);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDone = itemView.findViewById(R.id.tv_done);
        }

    }
}
