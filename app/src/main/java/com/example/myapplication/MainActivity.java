package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout l1, l2;

    private TextView title, options;

    private DaoS daoS;

    private DaoC daoC;

    List<BeanS> bsL;

    List<BeanC> bcList;

    ClazzAdapter cA;

    StudentAdapter sA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseUtil db = MyApplication.databaseUtil;

        daoS = db.msd();
        daoC = db.mcd();

        cA = new ClazzAdapter();
        sA = new StudentAdapter();

        LinearLayout b1 = findViewById(R.id.b1);
        LinearLayout b2 = findViewById(R.id.b2);
        l1 = findViewById(R.id.layout1);
        l2 = findViewById(R.id.layout3);

        title = findViewById(R.id.title);
        options = findViewById(R.id.options);
        b1.setOnClickListener(view -> initLayout1());
        b2.setOnClickListener(view -> initLayout3());
        initLayout1();

    }

    // 初始化课程界面
    private void initLayout1() {
        l1.setVisibility(View.VISIBLE);
        l2.setVisibility(View.GONE);
        title.setText(getResources().getString(R.string.clazz_manage));
        options.setVisibility(View.VISIBLE);
        options.setText(getResources().getString(R.string.add));
        options.setOnClickListener(view1 -> {
            Intent intent = new Intent(this, AddCActivity.class);
            startActivity(intent);
        });
        RecyclerView recyclerView1 = findViewById(R.id.recycle1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView1.setAdapter(cA);
        flashLayout1();
    }

    // 刷新界面1
    private void flashLayout1() {
        new Thread(() -> {
            bcList = daoC.getList();
            bsL = daoS.getList();
            cA.setStudentList(bsL);
            runOnUiThread(() -> cA.update(bcList));
        }).start();
    }

    // 初始化课程界面
    private void initLayout3() {
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.student_manage));
        options.setVisibility(View.VISIBLE);
        options.setText(getResources().getString(R.string.add));
        options.setOnClickListener(view1 -> {
            View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_input_student, null, false);
            AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
            inputDialog.setTitle(getResources().getString(R.string.enter_student)).setView(view2);
            inputDialog.setPositiveButton(getResources().getString(R.string.confirm), (dialog, which) -> {
                BeanS beanS = new BeanS();
                String id = ((EditText) view2.findViewById(R.id.edit1)).getText().toString();
                String name = ((EditText) view2.findViewById(R.id.edit2)).getText().toString();
                beanS.setName(id);
                beanS.setId(name);
                beanS.setUuid(UUID.randomUUID().toString());
                new Thread(() -> {
                    daoS.insert(beanS);
                    fL2();
                }).start();
            }).show();
        });

        RecyclerView recyclerView3 = findViewById(R.id.recycle3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView3.setAdapter(sA);

        fL2();
    }

    // 刷新界面1
    private void fL2() {
        new Thread(() -> {
            bsL = daoS.getList();
            runOnUiThread(() -> sA.update(bsL));
        }).start();
    }

}