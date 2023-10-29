package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.UUID;
import java.util.function.Consumer;

public class AddCActivity extends AppCompatActivity {

    private EditText et1, et2, et3;

    private Button btn;
    private ImageView back;

    private DatabaseUtil db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclazz);

        db = MyApplication.databaseUtil;

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);

        et2.setOnClickListener(view->{

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,  new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    et2.setText(selectedDate);

                }
            }, year, month, dayOfMonth);
            datePickerDialog.show();

            showTimePicker(this, et2::setText);
        });

        et3.setOnClickListener(view->{
            showTimePicker(this, et3::setText);
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });

        btn = findViewById(R.id.btn_add);
        btn.setOnClickListener(view -> {
            BeanC beanC = new BeanC();

            String name = et1.getText().toString();
            String startDate = et2.getText().toString();
            String endDate = et3.getText().toString();

            beanC.setName(name);
            beanC.setsDate(startDate);
            beanC.seteDate(endDate);
            beanC.setUuid(UUID.randomUUID().toString());

            new Thread(() -> {
                db.mcd().insert(beanC);
            }).start();

            Toast.makeText(this, getResources().getString(R.string.add_success), Toast.LENGTH_SHORT).show();

        });

    }

    private void showTimePicker(Context context, Consumer<String> consumer) {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
            consumer.accept(selectedDate);
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

}