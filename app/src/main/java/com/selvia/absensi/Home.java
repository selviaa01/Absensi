package com.selvia.absensi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private Button btnAbsenMasuk;
    private Button btnAbsenKeluar;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnAbsenMasuk = findViewById(R.id.btnAbsenMasuk);
        btnAbsenKeluar = findViewById(R.id.btnAbsenKeluar);
        calendarView = findViewById(R.id.calendarView);

        btnAbsenMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, AbsenMasuk.class);
                startActivity(intent);
            }
        });

        btnAbsenKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, AbsenKeluar.class);
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    Toast.makeText(Home.this, "Tanggal dipilih: " + selectedDate, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void logoutClick(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        }
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
