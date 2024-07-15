package com.example.justquit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.justquit.data.ReadAndWrite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNewActivity extends AppCompatActivity {
    ReadAndWrite readAndWrite;

    Button btnReturnToMain, btnAddNewQuit;
    TextView tvHour, tvDate;
    EditText etDate, etThingToQuit;

    String thingToQuit;
    Date dateOfQuit;

    FirebaseAuth mAuth;
    FirebaseUser user;

    int month,day,year;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        readAndWrite = new ReadAndWrite();
        btnReturnToMain = findViewById(R.id.btnReturnToMain);
        btnAddNewQuit = findViewById(R.id.btnAddNewQuit);
        calendar = Calendar.getInstance();
        tvHour = findViewById(R.id.tvHour);
        tvDate = findViewById(R.id.tvDate);
        etDate = findViewById(R.id.etDate);
        etThingToQuit = findViewById(R.id.etThingToQuit);
        btnReturnToMain.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });


        tvDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()));
        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                calendar = Calendar.getInstance();
                tvHour.setText(new SimpleDateFormat("HH:mm:ss", Locale.US).format(calendar.getTime()));
                someHandler.postDelayed(this, 1000);
            }
        }, 10);

        etDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            // To show current date in the datepicker
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(AddNewActivity.this, (datepicker, selectedyear, selectedmonth, selectedday) -> {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                dateOfQuit = myCalendar.getTime();
                etDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(myCalendar.getTime()));
                day = selectedday;
                month = selectedmonth;
                year = selectedyear;
            }, year, month, day);
            //mDatePicker.setTitle("Select date");
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            mDatePicker.show();
        });

        btnAddNewQuit.setOnClickListener(view -> {
            thingToQuit = String.valueOf(etThingToQuit.getText());
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            if (!thingToQuit.isEmpty() && user != null) {
                readAndWrite.writeUserInfo(user.getUid(), thingToQuit, dateOfQuit);
            }
            Toast.makeText(AddNewActivity.this, "Succesfully added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

}