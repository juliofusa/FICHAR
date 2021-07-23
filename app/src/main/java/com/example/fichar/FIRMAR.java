package com.example.fichar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Timer;
import java.util.TimerTask;

public class FIRMAR extends AppCompatActivity {
    private String DIRECCION_SALIDA,GPS_SALIDA,HORA,NOMBRECOMPLETO,CLIENTE,ID_;
    private Integer Tiempo=90;
    private TextView MENSAJE;
    private Canvasview customCanvas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_i_r_m_a_r);
        NOMBRECOMPLETO=getIntent().getStringExtra("NOMBRECOMPLETO");
        CLIENTE=getIntent().getStringExtra("CLIENTE");
        DIRECCION_SALIDA=getIntent().getStringExtra("DIRECCION_SALIDA");
        GPS_SALIDA=getIntent().getStringExtra("GPS_SALIDA");
        ID_=getIntent().getStringExtra("id");
        MENSAJE=findViewById(R.id.Txt_mensajes);
        customCanvas =  findViewById(R.id.canvasview_FIRMAR);

      //Toast.makeText(getApplicationContext(), "nombre: "+NOMBRECOMPLETO+ "id: "+ID_, Toast.LENGTH_LONG).show();

        Timer timer = new Timer();

        TimerTask t = new TimerTask() {

            @Override
            public void run() {

                Tiempo-=1;

                //MENSAJE.setText("CONTANDO: "+ADAPTADORES.HORAconformato());

                if (Tiempo==0){
                    fin();

                }else{
                    MENSAJE.setText(getResources().getString(R.string.TIENE)+" "+String.valueOf(Tiempo)+ " " +getResources().getString(R.string.segundos));

                }
            }
        };
        timer.scheduleAtFixedRate(t,1,1000);

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


            long l=0;
            //String NOM="";

            Bitmap bmpBase;

            View can= findViewById(R.id.canvasview_FIRMAR);

            bmpBase = getBitmapFromView(can);
            // Bitmap bmpBase = customCanvas.getDrawingCache();


            //File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(this.getExternalFilesDir(null)+ADAPTADORES.R_RUTA_EXPORTACIONES);

            if (!dir.exists())
                dir.mkdirs();
            String cola_archivo,mensaje;

                Long date = System.currentTimeMillis();

                cola_archivo= date+".PNG";

            String NOM="FIRMA DE "+NOMBRECOMPLETO+"_"+CLIENTE+"_"+cola_archivo;

            File f = new File(dir, NOM);
           //Toast.makeText(getApplicationContext(), f.toString(), Toast.LENGTH_LONG).show();
            try {
                FileOutputStream out = new FileOutputStream(f) ;
                bmpBase.compress(Bitmap.CompressFormat.PNG, 50, out);
                //FileOutputStream fos = new FileOutputStream(f);
                //bmpBase.compress(Bitmap.CompressFormat.PNG, 50, fos);
                out.flush();
                out.close();
                l=f.length();

                if (l<=15000){

                   f.delete();

                    Toast.makeText(getApplicationContext(), "TIENE QUE FIRMAR DE NUEVO ,FIRMA NO VALIDA ", Toast.LENGTH_LONG).show();

                    customCanvas.clearCanvas();



               }else{




                    //final Intent i = new Intent(this, MainActivity.class);

                    Fichaje_final(NOM);

                    Toast.makeText(getApplicationContext(), "FICHAJE SALIDA DE "+NOMBRECOMPLETO+" A LAS "+HORA, Toast.LENGTH_SHORT).show();

                    fin();
                    //startActivity(i);

                    //finish();
                     }


            } catch (IOException e) {

                Toast.makeText(getApplicationContext(), "FIRMA NO GUARDADA", Toast.LENGTH_SHORT).show();
            }

    }
    private Integer ID(){
        Integer ID;
        BasedbHelper usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        Cursor c_fichaje=db.rawQuery("SELECT * FROM FICHAJE ", null);

        c_fichaje.moveToLast();

        ID=c_fichaje.getInt(0);

        return ID;
    }
    private void Fichaje_final( String Firma){


        BasedbHelper usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        HORA=ADAPTADORES.HORAMINUTO();

        Integer nºID=0;

        if (ID_.equals("0")){nºID=ID();}else{nºID=Integer.parseInt(ID_);}

        if (db != null) {

            ContentValues nuevoRegistro = new ContentValues();


            nuevoRegistro.put("HORA_SALIDA", HORA);
            nuevoRegistro.put("GPS_SALIDA", GPS_SALIDA);
            nuevoRegistro.put("DIRECCION_SALIDA", DIRECCION_SALIDA);
            nuevoRegistro.put("FIRMA", Firma);

            db.update("FICHAJE",nuevoRegistro,"_id="+nºID, null);


        }
        exportar();


    }
    public void exportar(){

        Long date = System.currentTimeMillis();

        String NOMBREFICHERO="FICHAR_"+NOMBRECOMPLETO+"_"+"_"+String.valueOf(date)+".txt";

        BasedbHelper usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        Cursor c_fichaje=db.rawQuery("SELECT * FROM FICHAJE WHERE _id="+ID_, null);

        c_fichaje.moveToLast();

        try {

            File DIR = new File(this.getExternalFilesDir(null)+ADAPTADORES.R_RUTA_EXPORTACIONES);
            if (!DIR.exists()){DIR.mkdir();}
            File file= new File(DIR,NOMBREFICHERO);

            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(file));

            String linea=System.getProperty("line.separator");

            fout.write("COMODIN"+ ";" + "CLIENTE" + ";" + "HORARIO ENTRADA" + ";" + "HORARIO SALIDA" + ";" + "HORARIO_REAL_ENTRADA" + ";" + "HORARIO_REAL_SALIDA" + ";" +  "GPS_ENTRADA" + ";" + "GPS_SALIDA" +";" +   "DIRECCION_ENTRADA" + ";" + "DIRECCION_SALIDA"+ ";" + "NOTA" + ";" + "FECHA" + ";" + "GESTOR" + ";" + "ID_ANDROID" + linea);

            String registro= "\""+ c_fichaje.getString(7) +"\"" + ";"+""+ ";" +"\""+ c_fichaje.getString(2) +"\""+ ";" +""+ ";"+""+ ";" +"\""+ c_fichaje.getString(3)+"\"" + ";"+"\"" +c_fichaje.getString(4)+"\""+ ";"+"\"" +c_fichaje.getString(5)+"\""+ ";" +c_fichaje.getString(6)+ ";" +c_fichaje.getString(8)+ ";"+"\"" +c_fichaje.getString(9)+"\""+ ";"+""+ ";" +c_fichaje.getString(1)+ ";"+""+";" +""+";"+"\"" +c_fichaje.getString(10)+"\"";

              fout.write(registro+linea);

            fout.close();

           // Toast.makeText(getApplicationContext(), "exportado "+NOMBRECOMPLETO, Toast.LENGTH_SHORT).show();



        } catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
        }



    }
    public void Temporizador(){


    }
    private void fin (){

        final Intent i = new Intent(this, PRINCIPAL.class);

        startActivity(i);

        finish();
    }
}




