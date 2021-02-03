package com.example.fichar;

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
    private Canvasview customCanvas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_i_r_m_a_r);

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

            bmpBase = getBitmapFromView(customCanvas.getRootView());
            // Bitmap bmpBase = customCanvas.getDrawingCache();


            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + ADAPTADORES.R_RUTA);

            if (!dir.exists())
                dir.mkdirs();
            String cola_archivo,mensaje;



                cola_archivo= "_.PNG";

                NOM="firma"+cola_archivo;

            File f = new File(dir, NOM);
            Toast.makeText(getApplicationContext(), f.toString(), Toast.LENGTH_LONG).show();
            try {
                FileOutputStream out = new FileOutputStream(f) ;
                bmpBase.compress(Bitmap.CompressFormat.PNG, 50, out);
                //FileOutputStream fos = new FileOutputStream(f);
                //bmpBase.compress(Bitmap.CompressFormat.PNG, 50, fos);
                out.flush();
                out.close();
                //l=f.length();

                if (l<10){

                    f.delete();

                    Toast.makeText(getApplicationContext(), "TIENE QUE FIRMAR DE NUEVO ,FIRMA NO VALIDA ", Toast.LENGTH_LONG).show();

                    customCanvas.clearCanvas();

                    //customCanvas.setDrawingCacheEnabled(false);

                }else{



                    Toast.makeText(getApplicationContext(), "nu se", Toast.LENGTH_SHORT).show();

                    finish(); }


            } catch (IOException e) {

                Toast.makeText(getApplicationContext(), "FIRMA NO GUARDADA", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




