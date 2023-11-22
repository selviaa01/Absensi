package com.selvia.absensi;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Notification extends AppCompatActivity {
    private MaterialButton btnoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        btnoke = findViewById(R.id.btnoke);
        btnoke.setVisibility(View.GONE);

        // Simulasi absen selesai dengan mengklik tombol "Masuk" pada AbsenMasuk.java
        // Anda bisa mengganti logika ini sesuai dengan kebutuhan proyek Anda.
        // Pada contoh ini, tombol "Selesai" akan ditampilkan setelah tombol "Masuk" diklik.
        // Anda dapat menggantinya dengan logika sesuai dengan proyek Anda.

        btnoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan saat tombol "Oke" diklik
                Toast.makeText(Notification.this, "Absen selesai!", Toast.LENGTH_SHORT).show();
                finish(); // Selesaikan aktivitas
            }
        });

        absenSelesai(); // Contoh fungsi absen selesai, gantilah sesuai dengan proyek Anda
    }

    private void absenSelesai() {
        // Lakukan operasi absen selesai di sini
        // ...

        // Set visibilitas tombol "Selesai" menjadi VISIBLE
        btnoke.setVisibility(View.VISIBLE);
    }
}
