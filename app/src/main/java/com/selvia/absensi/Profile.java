package com.selvia.absensi;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    private EditText nameEditText, nikEditText, divisionEditText, jobTitleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        // Mendapatkan referensi ke elemen UI
        ImageView profileImage = findViewById(R.id.profileImage);
        nameEditText = findViewById(R.id.nameEditText);
        nikEditText = findViewById(R.id.nikEditText);
        divisionEditText = findViewById(R.id.divisionEditText);
        jobTitleEditText = findViewById(R.id.jobTitleEditText);
        Button saveButton = findViewById(R.id.saveButton);

        // Memberikan fungsi pada tombol simpan
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfileData();
            }
        });
    }

    private void saveProfileData() {
        // Mendapatkan data dari formulir
        String name = nameEditText.getText().toString().trim();
        String nik = nikEditText.getText().toString().trim();
        String division = divisionEditText.getText().toString().trim();
        String jobTitle = jobTitleEditText.getText().toString().trim();

        // Menyimpan data ke SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.putString("nik", nik);
        editor.putString("division", division);
        editor.putString("jobTitle", jobTitle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        }

        // Menampilkan Toast sebagai indikasi bahwa data telah disimpan
        Toast.makeText(this, "Data tersimpan: \nNama: " + name +
                "\nNIK: " + nik + "\nDivisi: " + division + "\nJabatan: " + jobTitle, Toast.LENGTH_SHORT).show();
    }
}


