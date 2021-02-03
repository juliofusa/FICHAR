package com.example.fichar;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class firmas extends AppCompatActivity {

    private Canvasview customCanvas;
    private String nombre_firma,FECHA,HORA,TIP,NOMBRECOMPLETO,NOMBRE_PDF,ID_DISPOSITIVO,ID_ACTIVITY,FORMACION,FORMADOR;
    private TextView msg_box;
    private Button GUARDAR;
    private Boolean tipo_personal;
    private Integer ID,numero_firma,GROSOR=2,RESULTADO;
    private String TIPO_LAPIZ="FINO";
    public Integer Int_color= Color.BLUE;
    private RadioButton FINO,MEDIO,GRUESO;
    public static int COLOR = Color.BLUE;
    private final static String NOMBRE_DIRECTORIO = "//fotospuesto/PDF";
    private final static String NOMBRE_DOCUMENTO = "ROPA_ENTREGADA_";
    private final static String ETIQUETA_ERROR = "ERROR";
    private final static String CARPETA_FIRMAS = "//fotospuesto/firmas/";
    private final static String CARPETA_EXPORTACION = "/fotospuesto/EXPORTACIONES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firmas);

        customCanvas =  findViewById(R.id.signature_canvas);

        //FINO=(RadioButton)findViewById(R.id.FINO);
        //MEDIO=(RadioButton)findViewById(R.id.MEDIO);
        //GRUESO=(RadioButton)findViewById(R.id.GRUESO);
        //msg_box=  findViewById(R.id.msg_firma);
       // GUARDAR=  findViewById(R.id.b_GUARDAR);
        //GUARDAR.setEnabled(true);

        //nombre_firma=getIntent().getStringExtra("nombre_firma");
        //tipo_personal=getIntent().getBooleanExtra("tipo_personal", true);
        //FECHA=getIntent().getStringExtra("FECHA");
        //HORA=getIntent().getStringExtra("HORA");
        //FORMADOR=getIntent().getStringExtra("FORMADOR");
        //TIP=getIntent().getStringExtra("TIP");
        //ID_ACTIVITY=getIntent().getStringExtra("ACTIVITY");
        //FORMACION=getIntent().getStringExtra("FORMACION");
        //NOMBRECOMPLETO=getIntent().getStringExtra("NOMBRECOMPLETO");
       // RESULTADO=getIntent().getIntExtra("RESULTADO",0);

        //String activity;
       // if (ID_ACTIVITY.equals("1")){activity=" firmo que recibo ropa: ";}else{activity=" firmo que recibo formacion en: "+FORMACION;}
        //msg_box.setText("Yo, "+NOMBRECOMPLETO+" con fecha "+FECHA);
        //ID_DISPOSITIVO= Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    public void R_fino(View v){
        if (FINO.isChecked()){
            Toast.makeText(getApplicationContext(), "LAPIZ FINO,SELECIONE COLOR", Toast.LENGTH_SHORT).show();
            TIPO_LAPIZ="FINO";
            GROSOR=4;MEDIO.setChecked(false);GRUESO.setChecked(false);}
    }
    public void R_medio(View v){
        if (MEDIO.isChecked()){
            Toast.makeText(getApplicationContext(), "LAPIZ MEDIO,SELECIONE COLOR", Toast.LENGTH_SHORT).show();
            TIPO_LAPIZ="MEDIO";
            GROSOR=8;FINO.setChecked(false);GRUESO.setChecked(false);}
    }
    public void R_grueso(View v){
        if (GRUESO.isChecked()){
            Toast.makeText(getApplicationContext(), "LAPIZ GRUESO,SELECIONE COLOR", Toast.LENGTH_SHORT).show();
            TIPO_LAPIZ="GRUESO";
            GROSOR=11;FINO.setChecked(false);MEDIO.setChecked(false);}
    }
    public void clearCanvas(View v) {
        //BORRAR PANTALLA
        customCanvas.clearCanvas();

    }
    public void color_ROJO(View v){

        Toast.makeText(getApplicationContext(), "COLOR ROJO ,LAPIZ "+TIPO_LAPIZ, Toast.LENGTH_SHORT).show();
        customCanvas.Color_rojo(GROSOR);
    }
    public void color_NEGRO(View v){
        Toast.makeText(getApplicationContext(), "COLOR NEGRO ,LAPIZ "+TIPO_LAPIZ, Toast.LENGTH_SHORT).show();
        customCanvas.Color_NEGRO(GROSOR);
    }
    public void color_VERDE(View v){
        Toast.makeText(getApplicationContext(), "COLOR VERDE ,LAPIZ "+TIPO_LAPIZ, Toast.LENGTH_SHORT).show();
        customCanvas.Color_VERDE(GROSOR);
    }
    public void color_AZUL(View v){
        Toast.makeText(getApplicationContext(), "COLOR AZUL ,LAPIZ "+TIPO_LAPIZ, Toast.LENGTH_SHORT).show();
        customCanvas.Color_azul(GROSOR);
    }
    public void color_VIOLETA(View v){
        Toast.makeText(getApplicationContext(), "COLOR VIOLETA ,LAPIZ "+TIPO_LAPIZ, Toast.LENGTH_SHORT).show();
        customCanvas.Color_VIOLETA(GROSOR);
    }
    //GUARDAR FIRMA
    public void guardar_firma(View v) {

        long l=0;
        String NOM="";

        customCanvas.setDrawingCacheEnabled(true);
        Bitmap bmpBase = customCanvas.getDrawingCache();


        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + ADAPTADORES.R_RUTA);
        if (!dir.exists())
            dir.mkdirs();
        String cola_archivo,mensaje;

        if (ID_ACTIVITY.equals("2")){

            cola_archivo= "_"+FORMACION+".PNG";mensaje="FIRMADA LA FORMACION EN: "+FORMACION;

            NOM=NOMBRECOMPLETO+getCode()+cola_archivo;

        }else{

            cola_archivo= "_VESTUARIO.PNG";mensaje="ENTREGA DE ROPA FIRMADA";
           
            NOM=NOMBRECOMPLETO+"_"+FECHA_FICHERO(FECHA)+cola_archivo;
        }




        File f = new File(dir, NOM);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            bmpBase.compress(Bitmap.CompressFormat.PNG, 50, fos);
            fos.flush();
            fos.close();
            l=f.length();

            if (l<10000){

                f.delete();

                Toast.makeText(getApplicationContext(), "TIENE QUE FIRMAR DE NUEVO ,FIRMA NO VALIDA ", Toast.LENGTH_LONG).show();

                customCanvas.clearCanvas();

                customCanvas.setDrawingCacheEnabled(false);

            }else{

                if (ID_ACTIVITY.equals("2")){

                GUARDAR_REGISTRO(NOM);

                EXPORTAR();}

                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();

                finish(); }


        } catch (IOException e) {

            Toast.makeText(getApplicationContext(), "FIRMA NO GUARDADA", Toast.LENGTH_SHORT).show();
        }



    }

    // exportar datos
    private void EXPORTAR(){

        String sqlORDENES;

        String NOMBREFICHERO="";

        BasedbHelper usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getReadableDatabase();

        sqlORDENES="SELECT * FROM FORMACIONFIRMADAS";

        Cursor c = db.rawQuery(sqlORDENES, null);

        if (c.getCount()!=0){

            c.moveToLast();
            if (tipo_personal==true) {

                 NOMBREFICHERO="FORMACION_FIRMADA_"+getCode()+"_"+ID_DISPOSITIVO+".txt";

            }else{

                 NOMBREFICHERO="FORMACION_AUX"+getCode()+"_"+ID_DISPOSITIVO+".txt";

            }


            try {

                File DIR = new File(Environment.getExternalStorageDirectory().getPath()+ADAPTADORES.R_RUTA_EXPORTACIONES);

                if (!DIR.exists()){DIR.mkdir();}

                File file= new File(DIR,NOMBREFICHERO);

                OutputStreamWriter fout =new OutputStreamWriter(new FileOutputStream(file));

                String linea= System.getProperty("line.separator");

                fout.write("ID" + ";" + "FECHA" + ";" + "HORAINICIO" + ";" + "HORAFIN" + ";" + "FORMACION" + ";" + "PERSONA" + ";" + "TIP" + ";" + "RESULTADO" + ";" + "APROBADO" + ";" + "FIRMA"+ ";" + "FORMADOR"  + linea);

                do {
                    String registro=c.getString(0)+ ";" +c.getString(1)+ ";" +c.getString(2)+ ";" +c.getString(3)+ ";" +c.getString(4)+ ";" +c.getString(5)+ ";" +c.getString(6)+ ";"+c.getString(7)+ ";" +c.getString(8)+ ";" +c.getString(9)+ ";"+c.getString(10)+ ";" ;

                    fout.write(registro);

                } while (c.moveToNext());

                fout.close();

                c.close();

                db.close();

            } catch (Exception ex) {
                Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
            }
        }else{
            Toast.makeText(getBaseContext(), "DATOS NO EXPORTADOS ", Toast.LENGTH_LONG).show();}
    }
    // guargar registro en la tabla, luego se exporta a un archivo
    public void GUARDAR_REGISTRO(String nom){

        BasedbHelper  usdbh = new  BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        if(db != null){
            ContentValues nuevoRegistro = new ContentValues();

            nuevoRegistro.put("FECHA",FECHA);//1
            nuevoRegistro.put("horaINICIO", HORA);//2
            nuevoRegistro.put("horaFIN", HORAconformato());//3
            nuevoRegistro.put("FORMACION", FORMACION);//4
            nuevoRegistro.put("PERSONA",NOMBRECOMPLETO);//5
            nuevoRegistro.put("TIP", TIP);//6
            nuevoRegistro.put("RESULTADO",  RESULTADO);//7
            nuevoRegistro.put("APROBADO", -1);//8
            nuevoRegistro.put("FIRMA", nom);//9
            nuevoRegistro.put("FORMADOR", FORMADOR);//10
            //Insertamos el registro en la base de datos
            db.insert("FORMACIONFIRMADAS", null, nuevoRegistro);
            //Log.i(this.getClass().toString(), "INSERTADO NUEVO REGISTRO");
            // Cerramos la base de datos
            db.close();

        }
    }
    private void CREAR_PDF(String FIRMA_ORDEN){
        // Creamos el documento.


    }
    public String HORAconformato() {
        Long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateString = sdf.format(date);
        return dateString;
    }
    private String WEEKDAY(){
        Calendar CAL = Calendar.getInstance();


        String dia;
        String[] WEEKDAYS=new String[]{"DOMINDO","LUNES","MARTES","MIERCOLES","JUEVES","VIERNES","SABADO"};
        dia=WEEKDAYS[CAL.get(Calendar.DAY_OF_WEEK)-1];
        return dia;
    }
    private void abrir_PDF(){
        final String filename ="Orden1.pdf";

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"//ORDENES/"+ filename);
        //Toast.makeText(getApplicationContext(),file.toString(),Toast.LENGTH_LONG).show();
        if (file.exists()) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");

            try {
                startActivity(intent);
                //Toast.makeText(getBaseContext(), "AYUDA....", Toast.LENGTH_LONG).show();
            } catch (ActivityNotFoundException e) {
// Instruct the user to install a PDF reader here, or something
            }
        }else {
            Toast.makeText(getBaseContext(), "NO ENCUENTRO ARCHIVO CON LA ORDEN DE PUESTO", Toast.LENGTH_LONG).show();}
    }
    public static File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }
    public static File getRuta() {

        // El fichero sera almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File DIR = new File(Environment.getExternalStorageDirectory().getPath()+"/");
            if (!DIR.exists()){DIR.mkdir();}
            ruta= new File(DIR,NOMBRE_DIRECTORIO);
            //ruta = new File(
            //Environment
            //.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            //NOMBRE_DIRECTORIO);

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }
    public String FECHA_FICHERO(String f) {
        String fecha_reunion=f;
        String F_REUNION_FORMAT=fecha_reunion.substring(6,10)+"_"+fecha_reunion.substring(3,5)+"_"+fecha_reunion.substring(0,2);
        return F_REUNION_FORMAT;}
    private String getCode() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = dateFormat.format(new Date());
        String photoCode =  "_"+date ;
        return photoCode;
    }
    private String getID_FIRMA(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = dateFormat.format(new Date());
        String photoCode =  date+ID_DISPOSITIVO+TIP ;
        return photoCode;
    }


}