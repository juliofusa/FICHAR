package com.example.fichar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public TextView fecha, direccion, hora, cliente, gps;
    private EditText COMODIN,DNI;
    private Spinner CLIENTES;
    private ImageButton FICHAR_INICIO,FICHAR_FIN;
    public boolean b=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BasedbHelper  usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        inicializar();
    }

    private void inicializar() {
       // declaramos controles
        fecha = findViewById(R.id.Txt_fecha);

        fecha.setText(ADAPTADORES.FECHAconformato());

        direccion = findViewById(R.id.Txt_direccion);

        hora = findViewById(R.id.Txt_hora);

        cliente = findViewById(R.id.Txt_cliente);

        gps = findViewById(R.id.Txt_gps);

        COMODIN=findViewById(R.id.ED_comodin);

        DNI=findViewById(R.id.ED_dni);

        CLIENTES=findViewById(R.id.SP_CLIENTE);

        FICHAR_INICIO=findViewById(R.id.CMD_FICHAR_INICIAL);

        FICHAR_FIN=findViewById(R.id.CMD_FICHAR_FIN);

        BasedbHelper  usdbh = new BasedbHelper(this);
        SQLiteDatabase db = usdbh.getWritableDatabase();

        Cursor C_comodines= db.rawQuery("SELECT * FROM COMODINES ", null);


        SimpleCursorAdapter adapterCOMODINES = new SimpleCursorAdapter(this,R.layout.custom_spinner_item1,C_comodines,(new String[] {"COMODIN"}), new int[] {R.id.Spiner_text},0);

        CLIENTES.setAdapter(adapterCOMODINES);

        Comprobar_permisos();

        Timer timer = new Timer();

        TimerTask t = new TimerTask() {

            @Override
            public void run() {


                if (b = false){
                    hora.setTextColor(getResources().getColor(R.color.verde));
                   // hora.setText(ADAPTADORES.HORAconformato());
                    b=true;
                }else{
                   hora.setTextColor(getResources().getColor(R.color.black));
                    hora.setText(ADAPTADORES.HORAconformato());
                    b=false;
                }
            }
        };
        timer.scheduleAtFixedRate(t,1000,1000);





    }
    public void CMD_CONFIGURAR (View V){
        importar_COMODINES();
       // importar_CLIENTES();

    }
    public void importar_COMODINES(){

        File DIR = new File(this.getExternalFilesDir(null)+ADAPTADORES.R_RUTA);

        File f = new File(DIR, ADAPTADORES.A_COMODINES);

        mensaje(f.toString());

        if (f.exists()) {
            mensaje(f.toString());
            String texto;

            int N = 0;

            BasedbHelper usdbh = new BasedbHelper(this);

            SQLiteDatabase db = usdbh.getWritableDatabase();

            if (db != null) {
                db.execSQL("DELETE FROM COMODINES");
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name = '" + "COMODINES" + "'");
                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("COMODIN", "");
                nuevoRegistro.put("DNI", "");
                db.insert("COMODINES", null, nuevoRegistro);


            }

            try
            {
                Toast.makeText(getApplicationContext(), "IMPORTANDO COMODINES... ", Toast.LENGTH_SHORT).show();

                BufferedReader COMODO =
                        new BufferedReader(
                                new InputStreamReader(
                                        new FileInputStream(f)));
                while((texto = COMODO.readLine())!=null){
                    String CODIFICACION= new String(texto.getBytes("UTF-8"),"UTF-8");
                    String [] registro=CODIFICACION.split(";");
                    if (N==0){N += 1;}else{

                        ContentValues nuevoRegistro = new ContentValues();

                        nuevoRegistro.put("COMODIN", registro[0]);

                        nuevoRegistro.put("DNI", registro[1]);

                        db.insert("COMODINES", null, nuevoRegistro);

                        N += 1;

                    }

                }
                COMODO.close();

                db.close();

                f.delete();

                Toast.makeText(getBaseContext(), "ARCHIVO COMODINES IMPORTADO... "+Integer.toString(N-1)+" Registros", Toast.LENGTH_SHORT).show();
            }
            catch (Exception ex)
            {
                Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");

                Toast.makeText(getBaseContext(), "Error al leer fichero", Toast.LENGTH_SHORT).show();
            }
        }else{

            Toast.makeText(getBaseContext(), "NO EXISTE EL ARCHIVO", Toast.LENGTH_SHORT).show();}
    }
    public void importar_CLIENTES(){

        File DIR = new File(Environment.getExternalStorageDirectory().getPath()+ADAPTADORES.R_RUTA);

        File f = new File(DIR, ADAPTADORES.A_CLIENTES);

        if (f.exists()) {

            String texto;

            int N = 0;

            BasedbHelper usdbh = new BasedbHelper(this);

            SQLiteDatabase db = usdbh.getWritableDatabase();

            if (db != null) {
                db.execSQL("DELETE FROM CLIENTES");
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name = '" + "CLIENTES" + "'");
                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("CLIENTE", "");
                db.insert("CLIENTES", null, nuevoRegistro);


            }

            try
            {
                Toast.makeText(getApplicationContext(), "IMPORTANDO CLIENTES... ", Toast.LENGTH_SHORT).show();

                BufferedReader Cli =new BufferedReader(new InputStreamReader(new FileInputStream(f)));

               /** while((texto = Cli.readLine())!=null){
                    String CODIFICACION= new String(texto.getBytes("UTF-8"),"UTF-8");
                    String [] registro=CODIFICACION.split(";");
                    mensaje(registro[0]);
                    if (N==0){N += 1;}else{

                        ContentValues nuevoRegistro = new ContentValues();

                        nuevoRegistro.put("CLIENTE", registro[0]);

                        db.insert("CLIENTES", null, nuevoRegistro);

                        N += 1;

                    }

                }
              **/
                Cli.close();

                db.close();

                f.delete();

                Toast.makeText(getBaseContext(), "ARCHIVO CLIENTES IMPORTADO... "+Integer.toString(N-1)+" Registros", Toast.LENGTH_SHORT).show();
            }
            catch (Exception ex)
            {
                //Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");

                Toast.makeText(getBaseContext(), "Error al leer fichero", Toast.LENGTH_SHORT).show();
            }
        }else{

            Toast.makeText(getBaseContext(), "NO EXISTE EL ARCHIVO", Toast.LENGTH_SHORT).show();}
    }

    private void Comprobar_permisos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            localizar();
        }


    }

    private void localizar() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        gps.setText("Localización agregada");
        direccion.setText("");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                localizar();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion.setText(DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private class Localizacion implements LocationListener {
        MainActivity mainActivity;

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(@NonNull Location location) {
            location.getLatitude();
            location.getLongitude();
            String sLatitud = String.valueOf(location.getLatitude());
            String sLongitud = String.valueOf(location.getLongitude());
            gps.setText(sLatitud +" "+ sLongitud);
            //hora.setText(ADAPTADORES.HORAconformato());
            //longitud.setText(sLongitud);
            this.mainActivity.setLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            gps.setText("GPS Activado");
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            gps.setText("GPS Desactivado");
        }
    }

    private void mensaje(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void exportar(){

        Long date = System.currentTimeMillis();

        String NOMBREFICHERO="FICHAR_"+COMODIN+"_"+String.valueOf(date)+".txt";

        try {

            File DIR = new File(Environment.getExternalStorageDirectory().getPath()+ADAPTADORES.R_RUTA_EXPORTACIONES);
            if (!DIR.exists()){DIR.mkdir();}
            File file= new File(DIR,NOMBREFICHERO);

            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(file));

            String linea=System.getProperty("line.separator");

            fout.write("COMODIN"+ ";" + "CLIENTE" + ";" + "HORARIO ENTRADA" + ";" + "HORARIO SALIDA" + ";" + "HORARIO_REAL_ENTRADA" + ";" + "HORARIO_REAL_SALIDA" + ";" +  "GPS_ENTRADA" + ";" + "GPS_SALIDA" + ";" + "NOTA" + ";" + "FECHA" + ";" + "GESTOR" + ";" + "ID_ANDROID" + linea);

           // String registro= "\""+ COMODIN +"\"" + ";" +"\""+ CLIENTE +"\""+ ";" +""+ ";" + HORA_ENTRADA.getText().toString() + ";" +HORA_SALIDA.getText().toString()+ ";" +"00:00"+ ";" +"00:00"+ ";" +""+ ";" +""+ ";" +"\""+ OBSERV.getText().toString() + "\""+";" +fecha.getText().toString()+ ";" +"\""+GESTOR.getText().toString()+"\""+ ";" +ID_ANDROID+ ";" ;

          //  fout.write(registro+linea);

            fout.close();

            mensaje("DATOS EXPORTADOS");



        } catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
        }



    }
}

