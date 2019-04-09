package com.example.juegopersonajes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private DownloadTask downloadTask;
    private String html = "";
    public static ArrayList<DatoImagen> personajes;
    private ImageView imagenPersonaje;
    private ImageDownloader imageDownloader;

    private Bitmap imagenBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagenPersonaje = findViewById(R.id.imagenPersonajeImageView);
        imageDownloader = new ImageDownloader();

        downloadTask = new DownloadTask();

        Log.i("html", "Inicio descarga");


        //downloadTask.execute("https://www.rollingstone.com/tv/tv-lists/40-best-game-of-thrones-characters-ranked-and-updated-29974/the-night-king-227343/");
        //html = downloadTask.execute("http://help.websiteos.com/websiteos/example_of_a_simple_html_page.htm").get();
        downloadTask.execute("https://www.hbo.com/game-of-thrones/cast-and-crew");
        //downloadTask.execute("http://help.websiteos.com/websiteos/example_of_a_simple_html_page.htm");
        // view-source:http://help.websiteos.com/websiteos/example_of_a_simple_html_page.htm

        //https://www.hbo.com/content/dam/hbodata/series/game-of-thrones/series_logos_game-of-thrones.jpg/_jcr_content/renditions/original

        try {
            Log.i("imagen", "Imagen inicio");
            Bitmap bitmap = imageDownloader.execute("https://www.hbo.com/content/dam/hbodata/series/game-of-thrones/series_logos_game-of-thrones.jpg/_jcr_content/renditions/original").get();
            imagenPersonaje.setImageBitmap(bitmap);
            Log.i("imagen", "Imagen descargada");
        } catch (Exception e){
            e.printStackTrace();
        }
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

                // lectura del html, se almacena en la variable global html
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String linea;

                while((linea = br.readLine()) != null){
                    html += linea;
                }
                br.close();
                Log.i( "html","Termino");

                // Se obtienen las etiquetas del html
                Document doc = Jsoup.parse(html);
                Elements elements = doc.select("img");
                elements.remove(0); // elimina el primer img q no pertenece a ningun personaje

                personajes = new ArrayList<>();

                // es pobla la lista de personajes
                for(Element e : elements){
                    String src = e.attr("src");
                    List<String> separadosPorSlash = Arrays.asList(src.split("/"));
                    String conNombre = separadosPorSlash.get(7);
                    if (conNombre.equals("s5") || conNombre.equals("s6") || conNombre.equals("s7")){
                        conNombre = separadosPorSlash.get(8);
                    }
                    if (conNombre.length() > 2){
                        List<String> separadoPorGuion = Arrays.asList(conNombre.split("-"));

                        char segundoNombre = separadoPorGuion.get(1).charAt(0);
                        String nombre;
                        if (Character.isDigit(segundoNombre)){
                            nombre = separadoPorGuion.get(0);
                        } else {
                            nombre = separadoPorGuion.get(0) + " " + separadoPorGuion.get(1);
                        }
                        String source = "https://www.hbo.com" + src;

                        DatoImagen personaje = new DatoImagen(nombre, source);
                        personajes.add(personaje);
                        System.out.println(String.format("Nombre: %s", personaje.getSrc()));
                    }
                }

                /*try {
                    Log.i("imagen", "Imagen inicio");
                    Bitmap bitmap = imageDownloader.execute("https://www.hbo.com/content/dam/hbodata/series/game-of-thrones/character/s5/jamie-lannister-512x512.jpg/_jcr_content/renditions/cq5dam.web.130.130.jpeg").get();
                    imagenPersonaje.setImageBitmap(bitmap);
                    Log.i("imagen", "Imagen descargada");
                } catch (Exception e){
                    e.printStackTrace();
                }*/

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

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {


            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class DatoImagen {

        private String nombre;
        private String src;

        public DatoImagen(String nombre, String src){
            this.nombre = nombre;
            this.src = src;
        }

        public String getNombre(){
            return this.nombre;
        }

        public String getSrc(){
            return this.src;
        }
    }

    public void iniciarJuego(View view){
        Intent intent = new Intent(getApplicationContext(), Juego.class);
        startActivity(intent);
    }
}

