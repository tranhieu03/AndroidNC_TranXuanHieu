package com.example.bai1_thuchanh1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView01);
    }

    public void readWebpage(View view) {
        // Start the AsyncTask to fetch data from the webpage
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute("http://ctms.fithou.net.vn/");
    }

    // AsyncTask to download webpage content in the background
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;

            try {
                // Create URL object
                URL url = new URL(urls[0]);

                // Open a connection
                urlConnection = (HttpURLConnection) url.openConnection();

                // Create an InputStream to read the content
                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                // Read and append the lines from the input
                String line;
                while ((line = reader.readLine()) != null) {
                    response += line;
                }

                // Close the reader
                reader.close();

            } catch (Exception e) {
                e.printStackTrace(); // Log the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect(); // Close the connection
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Update the UI with the downloaded content
            textView.setText(result);
        }
    }
}
