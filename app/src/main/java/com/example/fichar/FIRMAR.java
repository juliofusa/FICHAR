package com.example.fichar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FIRMAR extends AppCompatActivity {
    private String FECHA,HORA,NOMBRECOMPLETO,CLIENTE;

    private Canvasview customCanvas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_i_r_m_a_r);
        NOMBRECOMPLETO=getIntent().getStringExtra("NOMBRECOMPLETO");
        CLIENTE=getIntent().getStringExtra("CLIENTE");
        customCanvas =  findViewById(R.id.canvasview_FIRMAR);

    }
    public void clearCanvas(View v) {
        //BORRAR PANTALLA
        customCanvas.clearCanvas();

    }
    public Bitmap getBitmapFromView(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    public void guardar_firma(View v) {
        {

            long l=0;
            String NOM="";

            Bitmap bmpBase;
            View can= findViewById(R.id.canvasview_FIRMAR);
            bmpBase = getBitmapFromView(can);
            // Bitmap bmpBase = customCanvas.getDrawingCache();


            //File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(this.getExternalFilesDir(null) + ADAPTADORES.R_RUTA);

            if (!dir.exists())
                dir.mkdirs();
            String cola_archivo,mensaje;

                Long date = System.currentTimeMillis();

                cola_archivo= date+".PNG";

                NOM="FIRMA DE "+NOMBRECOMPLETO+"_"+CLIENTE+"_"+cola_archivo;

            File f = new File(dir, NOM);
           // Toast.makeText(getApplicationContext(), f.toString(), Toast.LENGTH_LONG).show();
            try {
                FileOutputStream out = new FileOutputStream(f) ;
                bmpBase.compress(Bitmap.CompressFormat.PNG, 50, out);
                //FileOutputStream fos = new FileOutputStream(f);
                //bmpBase.compress(Bitmap.CompressFormat.PNG, 50, fos);
                out.flush();
                out.close();
                l=f.length();

                if (l<15000){

                   f.delete();

                    Toast.makeText(getApplicationContext(), "TIENE QUE FIRMAR DE NUEVO ,FIRMA NO VALIDA ", Toast.LENGTH_LONG).show();

                    customCanvas.clearCanvas();



               }else{



                    Toast.makeText(getApplicationContext(), "GUARDADA LA FIRMA DE "+NOMBRECOMPLETO, Toast.LENGTH_SHORT).show();

                    final Intent i = new Intent(this, MainActivity.class);

                    startActivity(i);

                    finish(); }


            } catch (IOException e) {

                Toast.makeText(getApplicationContext(), "FIRMA NO GUARDADA", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




