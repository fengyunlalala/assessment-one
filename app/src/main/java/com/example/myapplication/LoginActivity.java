package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class LoginActivity extends Activity {

    private EditText n, p;

    DatabaseUtil db = MyApplication.databaseUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        n = findViewById(R.id.et_login_name);
        p = findViewById(R.id.et_login_pwd);
        Button login = findViewById(R.id.btn_login);
        Button register = findViewById(R.id.btn_register);

        login.setOnClickListener(view -> {

            String id = n.getText().toString();
            String pd = p.getText().toString();

            if (n.getText().toString() == null || n.getText().toString().equals("")) {
                Toast.makeText(this, getResources().getString(R.string.login_name_null), Toast.LENGTH_SHORT).show();
                return;
            }

            if (p.getText().toString() == null || p.getText().toString().equals("")) {
                Toast.makeText(this, getResources().getString(R.string.login_pwd_null), Toast.LENGTH_SHORT).show();
                return;
            }

            Thread thread = new Thread(() -> {

                DaoT daoT = db.mtd();
                if (daoT.getUser(id, pd).size() == 0) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, getResources().getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            });
            thread.start();
        });

        register.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

    }
}
