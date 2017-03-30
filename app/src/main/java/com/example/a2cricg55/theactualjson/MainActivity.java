package com.example.a2cricg55.theactualjson;

import android.opengl.EGLDisplay;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submit = (Button) findViewById(R.id.thebutton);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        EditText artistEE = (EditText) findViewById(R.id.editTextartist);
        String artist = artistEE.getText().toString();
        new ReturnSongs().execute(artist);
    }

    class ReturnSongs extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String artist = params[0];
            //replace spaces in name with escape code
            artist = artist.replace(" ", "&#32;");

            try {
                String url = ("http://www.free-map.org.uk/course/mad/ws/hits.php?artist=" + artist + "&format=json");
                URL urlobject = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlobject.openConnection();

                InputStream in = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String jsondata = "";
                String line = br.readLine();
                while (line != null) {
                    jsondata += line;
                    line = br.readLine();
                }

                String result = "";
                JSONArray json = new JSONArray(jsondata);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject song = json.getJSONObject(i);

                    String songTitle = song.getString("song");
                    String artistName = song.getString("artist");
                    String year = song.getString("year");

                    result += "Song title = " + songTitle;
                    result += ", Artist = " + artistName;
                    result += ", Year = " + year + "\n";


                }

                return result;

            } catch (IOException e) {
                return "Error! " + e.getMessage();
            } catch (JSONException e) {
                return "Error! " + e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(s);


        }

    }
}
