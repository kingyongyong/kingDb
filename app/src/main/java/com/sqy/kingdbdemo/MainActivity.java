package com.sqy.kingdbdemo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sqy.kingdb.KingDbManager;
import com.sqy.kingdbdemo.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mList;
    private DbMainAdapter mDbMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mDbMainAdapter = new DbMainAdapter(KingDbManager.newQueryBuilder().query(Student.class), this);
        mList.setAdapter(mDbMainAdapter);

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList();
            }
        });

    }

    /**
     * 列表 dialog
     */
    private void showList() {
        final String[] items = {"清空数据",
                "恢复到最初状态",
                "新增10条数据",
                "删除年龄为20岁的同学",
                "删除年龄大于22岁的同学",
                "删除年龄在19到21岁的同学",
                "学号为1的同学名字改成张三",
                "查询所有男生信息",
                "查询学号小于9并且语文成绩大于70的学生",
                "查询是年龄在19到21岁，并且语文成绩大于70的女生或者数学成绩大于70的男生",
                "数学成绩数字包含9的同学",
                "数学成绩数字9开头的同学",
                "数学成绩数字9结尾的同学",
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher)
                .setTitle("KingDb")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        switch (position){
                            case 0://清空数据
                                KingDbManager.newDeleteBuilder().delete(Student.class);
                                mDbMainAdapter.getData().clear();
                                mDbMainAdapter.getData().add(new Student());
                                mDbMainAdapter.notifyDataSetChanged();
                                break;
                            case 1://恢复到最初状态
                                refresh();
                                break;
                            case 2://新增10条数据
                                List<Student> data1 = KingDbManager.newQueryBuilder().query(Student.class);
                                List<Student> newD = new ArrayList<>();
                                for(int i = data1.size();i<data1.size() + 10;i++){
                                    Student student = new Student();
                                    student.setStudentId((long) i);
                                    student.setName("学生" + i);
                                    student.setAge(new Random().nextInt(7) + 18);
                                    student.setGender(new Random().nextInt(10) % 2 == 0);
                                    student.setChinesScore(new Random().nextInt(100));
                                    student.setMathScore(new Random().nextInt(100));
                                    student.setEnglishScore(new Random().nextInt(100));
                                    KingDbManager.newInsertBuilder().insert(student);
                                    newD.add(student);
                                }
                                mDbMainAdapter.getData().addAll(newD);
                                mDbMainAdapter.notifyDataSetChanged();
                                break;
                            case 3://删除年龄为20岁的同学
                                KingDbManager.newDeleteBuilder().setCondition(
                                        KingDbManager.newQueryBuilder().addEqual("age",20).buildQueryCondition()
                                ).delete(Student.class);
                                refresh();
                                break;
                            case 4://删除年龄大于22岁的同学
                                KingDbManager.newDeleteBuilder().setCondition(
                                        KingDbManager.newQueryBuilder().addMoreThan("age",22).buildQueryCondition()
                                ).delete(Student.class);
                                refresh();
                                break;
                            case 5://删除年龄在19到21岁的同学
                                KingDbManager.newDeleteBuilder().setCondition(
                                        KingDbManager.newQueryBuilder().addBetween("age",19,21).buildQueryCondition()
                                ).delete(Student.class);
                                refresh();
                                break;
                            case 6://学号为1的同学名字改成张三
                                KingDbManager.newUpdateBuilder().updateValue("name","张三").setCondition(
                                        KingDbManager.newQueryBuilder().addEqual("id",1).buildQueryCondition()
                                ).update(Student.class);
                                refresh();
                                break;
                            case 7://查询所有男生信息
                                mDbMainAdapter.getData().clear();
                                mDbMainAdapter.getData().add(new Student());
                                mDbMainAdapter.getData().addAll(
                                        KingDbManager.newQueryBuilder().addEqual("gender",true).query(Student.class));
                                mDbMainAdapter.notifyDataSetChanged();
                                break;
                            case 8://查询学号小于9并且语文成绩大于70的学生
                                mDbMainAdapter.getData().clear();
                                mDbMainAdapter.getData().add(new Student());
                                mDbMainAdapter.getData().addAll(
                                        KingDbManager.newQueryBuilder().addLessThan("id",9).addMoreThan("ChinesScore",70).query(Student.class));
                                mDbMainAdapter.notifyDataSetChanged();
                                break;
                            case 9://查询是年龄在19到21岁，并且语文成绩大于70的女生或者数学成绩大于70的男生
                                mDbMainAdapter.getData().clear();
                                mDbMainAdapter.getData().add(new Student());
                                mDbMainAdapter.getData().addAll(KingDbManager.newQueryBuilder()
                                        .addBetween("age",19,21)
                                        .addOr(
                                                KingDbManager.newQueryBuilder()
                                                        .addMoreThan("ChinesScore",70)
                                                        .addEqual("gender",false)
                                                        .buildQueryCondition(),
                                                KingDbManager.newQueryBuilder()
                                                        .addMoreThan("MathScore",70)
                                                        .addEqual("gender",true)
                                                        .buildQueryCondition()
                                        ).query(Student.class));
                                mDbMainAdapter.notifyDataSetChanged();
                                break;
                            case 10://数学成绩数字包含9的同学
                                mDbMainAdapter.getData().clear();
                                mDbMainAdapter.getData().add(new Student());
                                mDbMainAdapter.getData().addAll(
                                        KingDbManager.newQueryBuilder()
                                                .addMatching("MathScore","%9%").query(Student.class));
                                mDbMainAdapter.notifyDataSetChanged();
                                break;
                            case 11://数学成绩数字9开头的同学
                                mDbMainAdapter.getData().clear();
                                mDbMainAdapter.getData().add(new Student());
                                mDbMainAdapter.getData().addAll(
                                        KingDbManager.newQueryBuilder()
                                                .addMatching("MathScore","9%").query(Student.class));
                                mDbMainAdapter.notifyDataSetChanged();
                                break;
                            case 12://数学成绩数字9结尾的同学
                                mDbMainAdapter.getData().clear();
                                mDbMainAdapter.getData().add(new Student());
                                mDbMainAdapter.getData().addAll(
                                        KingDbManager.newQueryBuilder()
                                                .addMatching("MathScore","%9").query(Student.class));
                                mDbMainAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    private void refresh(){
        mDbMainAdapter.getData().clear();
        mDbMainAdapter.getData().add(new Student());
        mDbMainAdapter.getData().addAll(KingDbManager.newQueryBuilder().query(Student.class));
        mDbMainAdapter.notifyDataSetChanged();
    }
}
