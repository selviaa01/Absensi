package com.selvia.absensi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AbsenMasuk extends AppCompatActivity {
    private ImageView imageView;
    private String currentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_CAMERA_PERMISSION = 102;
    private static final int REQUEST_STORAGE_PERMISSION = 103;
    private static final int REQUEST_LOCATION_PERMISSION = 104;
    private FusedLocationProviderClient fusedLocationClient;
    private Button selesaiButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.absenmasuk);

        imageView = findViewById(R.id.imageSelfie);
        selesaiButton = findViewById(R.id.btnAbsen);

        // Memanggil setCurrentDateAndTime()
        setCurrentDateAndTime();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            setCurrentLocation();
        }

        selesaiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AbsenMasuk.this, "Terima kasih telah melakukan Absen Masuk hari ini", Toast.LENGTH_SHORT).show();
                try {
                    takePicture();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Setelah selesai, kembali ke aktivitas "Home"
                Intent intent = new Intent(AbsenMasuk.this, Notification.class);
                startActivity(intent);
                finish();
            }
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        // Tindakan klik untuk ImageView
        ImageView imageSelfie = findViewById(R.id.imageSelfie);
        imageSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    takePicture();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Inisialisasi spinner dan adapter
        Spinner spinnerKeterangan = findViewById(R.id.spinnerKeterangan);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.keterangan_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKeterangan.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void takePicture() throws IOException {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() throws IOException {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            setCurrentLocation();
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = createImageFile();
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.selvia.absensi.fileprovider.masuk",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void setCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            Geocoder geocoder = new Geocoder(AbsenMasuk.this, Locale.getDefault());
                            String locationString = null;
                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    Address address = addresses.get(0);
                                    locationString = address.getAddressLine(0);
                                    String city = address.getLocality(); // Nama kota
                                    String country = address.getCountryName(); // Nama negara
                                    String postalCode = address.getPostalCode(); // Kode pos

                                    // Gabungkan semua informasi
                                    locationString = String.format(Locale.getDefault(), "%s, %s, %s %s", locationString, city, country, postalCode);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            EditText lokasiEditText = findViewById(R.id.inputLokasi);
                            lokasiEditText.setText(locationString);
                        }
                    }
                });
    }

    private String getProfileName() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return ((SharedPreferences) preferences).getString("name", "");
    }

    private void setCurrentDateAndTime() {
        // Mendapatkan nama dari SharedPreferences
        String profileName = getProfileName();

        // Mendapatkan tanggal dan waktu saat ini
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        String currentDateTime = dateFormat.format(Calendar.getInstance().getTime());

        // Menetapkan teks ke EditText
        EditText namaEditText = findViewById(R.id.inputNama);
        namaEditText.setText(profileName);

        EditText tanggalWaktuEditText = findViewById(R.id.inputTanggal);
        tanggalWaktuEditText.setText(currentDateTime);
    }
}

