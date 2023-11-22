package com.selvia.absensi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pemanggilan AsyncTask untuk mengambil data dari server
        new GetDataTask().execute("your_entered_nik", "your_entered_password");
    }

    private class GetDataTask extends AsyncTask<String, Void, String> {

        private String enteredNIK;
        private String enteredPassword;
        private String postData;

        @Override
        protected String doInBackground(String... params) {
            enteredNIK = params[0];
            enteredPassword = params[1];

            String result = "";

            try {
                String urlString = "http://192.168.18.32:81/api/GetData.php";
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    OutputStream outputStream = urlConnection.getOutputStream();
                    OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");

                    postData = "enteredNIK=" + URLEncoder.encode(enteredNIK, "UTF-8") + "&" +
                            "enteredPassword=" + URLEncoder.encode(enteredPassword, "UTF-8");

                    Log.d("GetDataTask", "Data sent to server: " + postData);

                    writer.write(postData);
                    writer.flush();
                    writer.close();
                    outputStream.close();

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    result = convertStreamToString(in);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Handle JSON result here
            Log.d("Response", result);

            // Process JSON data
            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    // Access individual fields like this
                    String nik = jsonObject.getString("nik");
                    String password = jsonObject.getString("password");

                    // Do something with the data, e.g., display it in a RecyclerView
                    Log.d("UserData", "NIK: " + nik + ", Password: " + password);
                }

            } catch (JSONException e) {
                Log.e("GetDataTask", "Error parsing JSON", e);
            }
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
