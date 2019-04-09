package com.example.juegopersonajes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class Juego extends AppCompatActivity {

    private ImageView imagenPersonaje2;
    private MainActivity.DatoImagen personajeCorrecto;
    private ArrayList<MainActivity.DatoImagen> personajesActuales;
    private int TOTAL_PERSONAJES = MainActivity.personajes.size();
    private Button opcion1;
    private Button opcion2;
    private Button opcion3;
    private Button opcion4;

    private ImageDownloader2 imageDownloader2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        personajesActuales = new ArrayList<>();
        opcion1 = findViewById(R.id.button);
        opcion2 = findViewById(R.id.button2);
        opcion3 = findViewById(R.id.button3);
        opcion4 = findViewById(R.id.button4);

        imagenPersonaje2 = findViewById(R.id.imagenPersonaje2);
        imageDownloader2 = new ImageDownloader2();

        nuevoPersonaje();
    }

    public void nuevoPersonaje(){
        personajeCorrecto = obtenerPersonajeAleatorio();
        personajesActuales.clear();

        for (int i = 0; i < 3; i++){
            boolean repetido = true;
            while (repetido){
                MainActivity.DatoImagen perso = obtenerPersonajeAleatorio();
                if(!perso.equals(personajeCorrecto.getNombre())){
                    repetido = false;
                    personajesActuales.add(perso);
                }
            }
        }

        Random rm = new Random();
        int indice = rm.nextInt(4);
        personajesActuales.add(indice, personajeCorrecto);
        System.out.println(personajeCorrecto.getSrc());

        Bitmap bitmap = leerImagen(personajeCorrecto.getSrc());
        System.out.println(personajeCorrecto.getSrc());
        imageDownloader2.cancel(true);
        if (bitmap != null){
            imagenPersonaje2.setImageBitmap(bitmap);
        }


        opcion1.setText(personajesActuales.get(0).getNombre());
        opcion2.setText(personajesActuales.get(1).getNombre());
        opcion3.setText(personajesActuales.get(2).getNombre());
        opcion4.setText(personajesActuales.get(3).getNombre());

    }

    private MainActivity.DatoImagen obtenerPersonajeAleatorio(){
        MainActivity.DatoImagen personaje;
        Random rm = new Random();
        return MainActivity.personajes.get(rm.nextInt(TOTAL_PERSONAJES));
    }

    public void verificarGane(View view){
        Button btn = (Button) view;
        String nombreSeleccionado = btn.getText().toString();
        if (nombreSeleccionado.equals(personajeCorrecto.getNombre())){
            Toast.makeText(this,"Correcto", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Falso", Toast.LENGTH_SHORT).show();
        }

        nuevoPersonaje();

    }

    private Bitmap leerImagen(String url){
        try {
            Log.i("imagen", "Imagen inicio");
            ImageDownloader2 imgDown = new ImageDownloader2();
            Bitmap bitmap = imgDown.execute(url).get();
            Log.i("imagen", "Imagen descargada2");
            return bitmap;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static class ImageDownloader2 extends AsyncTask<String, Void, Bitmap> {

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
}
