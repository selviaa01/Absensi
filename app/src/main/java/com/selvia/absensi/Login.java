package com.selvia.absensi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private EditText MasukkanNIK, MasukkanPassword;
    private Button BtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MasukkanNIK = findViewById(R.id.inputNIK);
        MasukkanPassword = findViewById(R.id.inputPassword);
        BtnLogin = findViewById(R.id.btnLogin);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View v) {
                String nik = MasukkanNIK.getText().toString().trim();
                String password = MasukkanPassword.getText().toString().trim();

                if (nik.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "NIK dan Password harus diisi", Toast.LENGTH_SHORT).show();
                } else {
                    loginWithVolley(nik, password);
                }
            }
        });
    }

    private void loginWithVolley(String nik, String password) {
        String apiUrl = "http://192.168.18.32:81/api/GetData.php";

        // Membuat objek JSON untuk dikirim ke server
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nik", nik);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Membuat permintaan JSON menggunakan Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                apiUrl,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("loginSuccess")) {
                                boolean loginSuccess = response.getBoolean("loginSuccess");
                                if (loginSuccess) {
                                    Toast.makeText(Login.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, Home.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Login.this, "NIK atau Password salah", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "Format respons tidak sesuai", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this, "Terjadi kesalahan saat melakukan login: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            Log.e("Volley Error", "HTTP Status Code: " + statusCode);

                            if (error.networkResponse.data != null) {
                                String responseString = new String(error.networkResponse.data);
                                Log.e("Volley Error", "Response String: " + responseString);
                                try {
                                    JSONObject jsonResponse = new JSONObject(responseString);
                                    Log.e("Volley Error", "JSON Response: " + jsonResponse.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Log.e("Volley Error", "Network Response is null");
                        }
                    }
                }
        );

// Menambahkan permintaan ke antrian Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
