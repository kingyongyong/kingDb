package com.sqy.kingdbdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sqy.kingdbdemo.entity.Student;

import java.util.List;

public class DbMainAdapter extends RecyclerView.Adapter<DbMainViewHolder> {
    private List<Student> mData;
    private Context context;

    public DbMainAdapter(List<Student> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @NonNull
    @Override
    public DbMainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_db_main_item,viewGroup,false);
        return new DbMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DbMainViewHolder dbMainViewHolder, int i) {
        if(i == 0){
            dbMainViewHolder.tv1.setText("学号");
            dbMainViewHolder.tv11.setText("姓名");
            dbMainViewHolder.tv2.setText("年龄");
            dbMainViewHolder.tv3.setText("性别");
            dbMainViewHolder.tv4.setText("语文");
            dbMainViewHolder.tv5.setText("数学");
            dbMainViewHolder.tv6.setText("英语");
        }else{
            Student student = getItem(i);
            dbMainViewHolder.tv1.setText(String.valueOf(student.getStudentId()));
            dbMainViewHolder.tv11.setText(String.valueOf(student.getName()));
            dbMainViewHolder.tv2.setText(String.valueOf(student.getAge()));
            dbMainViewHolder.tv3.setText(student.getGender() ? "男" : "女");
            dbMainViewHolder.tv4.setText(String.valueOf(student.getChinesScore()));
            dbMainViewHolder.tv5.setText(String.valueOf(student.getMathScore()));
            dbMainViewHolder.tv6.setText(String.valueOf(student.getEnglishScore()));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public Student getItem(int position){
        return mData.get(position);
    }

    public List<Student> getData() {
        return mData;
    }
}
