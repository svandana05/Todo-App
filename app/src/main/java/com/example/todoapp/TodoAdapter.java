package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Models.Todo;
import com.example.todoapp.Models.TodoHistory;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.DataViewHolder> {
    MainActivity context;
    List<Todo> todoList;

    public TodoAdapter(MainActivity context, List<Todo> todoList) {
        this.context = context;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        TodoAdapter.DataViewHolder dataViewHolder = new TodoAdapter.DataViewHolder(view);
        return dataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DataViewHolder holder, int position) {
        final Todo todo = todoList.get(position);
        Log.e("Adapter", "Todo- "+position+ " "+ todo);
        String todoNote = todo.getTodoNote();
        holder.tvTodoTitle.setText(todoNote);
        holder.tvTodoTitle.setMaxLines(3);
        holder.tvTodoCreatedTime.setText(todo.getCreateTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateTodoActivity.class);
                intent.putExtra("TODO_ID", todo.getTodoId());
                context.startActivity(intent);
                ApplicationClass.enterIntentAnim(context);
            }
        });

        if (todo.getTodoPriority()==1){
            holder.ivStar.setVisibility(View.VISIBLE);
        }else {
            holder.ivStar.setVisibility(View.GONE);
        }

        holder.tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvDone.setCompoundDrawableTintList(context.getColorStateList(R.color.colorAccent));
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        holder.tvDone.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_anim));
                        TodoHistory todoHistory = new TodoHistory();
                        todoHistory.setCreateDate(todo.getCreateDate());
                        todoHistory.setCreateTime(todo.getCreateTime());
                        todoHistory.setReminderDate(todo.getReminderDate());
                        todoHistory.setReminderTime(todo.getReminderTime());
                        todoHistory.setTodoNote(todo.getTodoNote());
                        todoHistory.setTodoPriority(todo.getTodoPriority());
                        context.todoViewModel.insertTodoHistoryDetail(todoHistory);
                        context.todoViewModel.deleteTodo(todo);
                    }
                }, 100);
            }
        });
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
        ImageView ivStar;

        public DataViewHolder(final View itemView) {
            super(itemView);
            tvTodoTitle = itemView.findViewById(R.id.tv_todo_title);
            tvTodoCreatedTime = itemView.findViewById(R.id.tv_created_time);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDone = itemView.findViewById(R.id.tv_done);
            ivStar = itemView.findViewById(R.id.iv_star);
        }

    }
}
