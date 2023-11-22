package com.selvia.absensi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPassword extends AppCompatActivity {
    private EditText inputNama, inputPassword;
    private Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpassword);

        inputNama = findViewById(R.id.inputNama);
        inputPassword = findViewById(R.id.inputPassword);
        btnResetPassword = findViewById(R.id.btnresetpassword);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            class Login {
            }

            @Override
            public void onClick(View v) {
                String nik = inputNama.getText().toString().trim();
                String newPassword = inputPassword.getText().toString().trim();

                // Di sini Anda dapat menambahkan logika untuk mengatur ulang kata sandi pengguna.
                // Contoh sederhana: Menampilkan pesan sukses.
                Toast.makeText(ResetPassword.this, "Kata sandi telah diatur ulang.", Toast.LENGTH_SHORT).show();

                // Setelah mengatur ulang kata sandi, Anda dapat mengarahkan pengguna ke aktivitas lain, jika diperlukan.
                Intent intent = new Intent(ResetPassword.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
