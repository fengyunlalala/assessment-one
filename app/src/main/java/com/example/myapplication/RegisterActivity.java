package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    DatabaseUtil db;

    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regtister);

        register = findViewById(R.id.btn_register);

        db = MyApplication.databaseUtil;

        register.setOnClickListener(view -> {
            BeanT beanT = new BeanT();

            String name = ((EditText) findViewById(R.id.et_register_name)).getText().toString();
            String pwd = ((TextView) findViewById(R.id.et_register_pwd)).getText().toString();
            String repwd = ((TextView) findViewById(R.id.et_register_repwd)).getText().toString();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, getResources().getString(R.string.login_name_null), Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(this, getResources().getString(R.string.login_pwd_null), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pwd.equals(repwd)) {
                Toast.makeText(this, getResources().getString(R.string.login_password_different), Toast.LENGTH_SHORT).show();
                return;
            }

            beanT.setName(name);
            beanT.setPassword(pwd);
            beanT.setUuid(UUID.randomUUID().toString());

            new Thread(() -> {
                DaoT daoT = db.mtd();
                daoT.insert(beanT);
            }).start();

            Toast.makeText(this, getResources().getString(R.string.register_success), Toast.LENGTH_SHORT).show();
        });

    }
}