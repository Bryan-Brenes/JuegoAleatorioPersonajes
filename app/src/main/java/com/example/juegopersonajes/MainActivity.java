package com.example.juegopersonajes;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private DownloadTask downloadTask;
    private String html = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadTask = new DownloadTask();

        Log.i("html", "Inicio descarga");


        downloadTask.execute("https://www.rollingstone.com/tv/tv-lists/40-best-game-of-thrones-characters-ranked-and-updated-29974/the-night-king-227343/");
        //html = downloadTask.execute("http://help.websiteos.com/websiteos/example_of_a_simple_html_page.htm").get();
        //downloadTask.execute("https://www.hbo.com/game-of-thrones/cast-and-crew");
        //downloadTask.execute("http://help.websiteos.com/websiteos/example_of_a_simple_html_page.htm");
        // view-source:http://help.websiteos.com/websiteos/example_of_a_simple_html_page.htm


        //Log.i("html", html);


    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;

            // Agregar permiso en AndroidManifest.xml
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                /*InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                // Esto es muy estilo C
                // Se lee un caracter a la vez (como cuando se hace gets() en C o C++)
                int data = inputStreamReader.read();
                while (data != -1){
                    char character = (char)data;
                    result += character;
                    html += character;
                    data = inputStreamReader.read();
                }*/

                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String linea;

                while((linea = br.readLine()) != null){
                    html += linea;
                }
                br.close();
                Log.i( "html","Termino");
                System.out.println(html);
                System.out.println("----------");
                System.out.println(html.length());
                return result;
            }
            catch (MalformedURLException e){
                e.printStackTrace();
                return "Malformado url";
            } catch (Exception e){
                e.printStackTrace();
                return "error";
            }
        }
    }
}

