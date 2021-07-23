package com.example.fichar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class PRINCIPAL extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public boolean b=true,coor=false;
    public TextView fecha, direccion,Version, hora, cliente, gps,persona,T_COMODIN,T_DNI;
    public String S_direccion,s_fecha,S_coordenadas,S_usuario, Scliente;
    private String Cliente_selecionado,Usuario_logado,Dni_logado,Hora_logado,msgps;
    public ImageButton Importar_trabajadores;
    public Button Añadir_Usuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_r_i_n_c_i_p_a_l);
        inicializar();
        CREAR_CARPETAS();
        Comprobar_permisos();


      if (Cliente()){
            final Intent i = new Intent(this, MainActivity.class);

            startActivity(i);

            finish();


        }else{Scliente="METRO DE MADRID, SA";}

      PackageManager packageManager =getPackageManager();
        String packageName =getPackageName();
        String myVersionName = "not available"; // initialize String
        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
            Version.setText("Version "+myVersionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        Importar_trabajadores.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
              Boton_Importar();
              return true;
          }
      });

        Añadir_Usuarios.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ALERT_usuarios();
                return true;
            }
        });


    }

    private void inicializar() {
        direccion = findViewById(R.id.txt_direccion);
        Importar_trabajadores = findViewById(R.id.CMD_IMPORTAR);
        Añadir_Usuarios=findViewById(R.id.CMD_AÑADIR_USUARIO);
        Version=findViewById(R.id.txt_version);
    }

    private boolean Cliente(){

        BasedbHelper  usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        Cursor C_comodines= db.rawQuery("SELECT * FROM CLIENTES ", null);

        if (C_comodines.moveToFirst() ){
           // Toast.makeText(getApplicationContext(), "existe.. ", Toast.LENGTH_SHORT).show();
            return true;
        }else{return false;}


    }

    private void CREAR_CARPETAS() {
        // listado de carpetas a crear
        File DIR = new File(this.getExternalFilesDir(null) + ADAPTADORES.R_RUTA_EXPORTACIONES);

        File DIR_Act = new File(this.getExternalFilesDir(null) + ADAPTADORES.R_RUTA_IMPORTACIONES);

        // comprobamoms si existen los directorios "fotopuesto" y "ORDEN DE PUESTO" y creamos carpetas y subcarpetas
        if (!DIR.exists()) {

            DIR.mkdirs();

            DIR_Act.mkdirs();
        }
    }

    private void Comprobar_permisos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            localizar();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, 1000);
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
       // gps.setText(getResources().getString(R.string.LOCALIZACION));
        //msgps=getResources().getString(R.string.LOCALIZACION);
       // direccion.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

        switch (parent.getId()) {
            case -1:
                Cursor COM=(Cursor) parent.getItemAtPosition(i);
                S_usuario=COM.getString(COM.getColumnIndex(ADAPTADORES.C_COLUMNA_COMODIN));
                //Toast.makeText(getApplicationContext(), "Comodin.. "+S_usuario, Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class Localizacion implements LocationListener {
        PRINCIPAL mainActivity;

        public PRINCIPAL getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(PRINCIPAL mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(@NonNull Location location) {
            location.getLatitude();
            location.getLongitude();
            String sLatitud = String.valueOf(location.getLatitude());
            String sLongitud = String.valueOf(location.getLongitude());
            S_coordenadas=(sLatitud +","+ sLongitud);
            coor=true;
            //hora.setText(ADAPTADORES.HORAconformato());
            //longitud.setText(sLongitud);
            //setLocation(location);
            this.setLocation(location);
        }

        public void setLocation(Location loc) {
            //Obtener la direccion de la calle a partir de la latitud y la longitud
            if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
                try {
                    Geocoder geocoder = new Geocoder(this.mainActivity, Locale.getDefault());
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

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            coor=true;

            //gps.setText(getResources().getString(R.string.GPSACTIVADO));
           // ACTIVAR_GPS.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            coor=false;
            //gps.setText(getResources().getString(R.string.GPSDESACTIVADO));
            //ACTIVAR_GPS.setVisibility(View.VISIBLE);

        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                localizar();
                return;
            }
        }
    }

    public void Boton_iniciar_fichaje(View view){
        if (coor){ALERT_FICHAJE();}
    }

    public void Boton_Importar(){
        importar_COMODINES();
        importar_CLIENTES();
    }

    public void Boton_Finalizar_fichajes(View v){
        if (coor){
        final Intent i = new Intent(this, Fichajes_pendientes.class);
        i.putExtra("GPS_SALIDA", S_coordenadas);
        i.putExtra("DIRECCION_SALIDA", direccion.getText().toString());
        startActivity(i);}
    }
    public void Boton_Añadir_usuario(){
        ALERT_usuarios();
    }

    public void importar_COMODINES(){

        File DIR = new File(this.getExternalFilesDir(null)+ADAPTADORES.R_RUTA_IMPORTACIONES);

        File f = new File(DIR, ADAPTADORES.A_COMODINES);

        //mensaje(f.toString());

        if (f.exists()) {
            //mensaje(f.toString());
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

                        nuevoRegistro.put("COMODIN", registro[0].toUpperCase());

                        nuevoRegistro.put("DNI", registro[1].toUpperCase());

                        assert db != null;
                        db.insert("COMODINES", null, nuevoRegistro);

                        N += 1;

                    }

                }
                COMODO.close();

                db.close();

                // f.delete();

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

        File DIR = new File(this.getExternalFilesDir(null)+ADAPTADORES.R_RUTA_IMPORTACIONES);

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

                while((texto = Cli.readLine())!=null){
                    String CODIFICACION= new String(texto.getBytes("UTF-8"),"UTF-8");
                    String [] registro=CODIFICACION.split(";");

                    if (N==0){N += 1;}else{

                        ContentValues nuevoRegistro = new ContentValues();

                        nuevoRegistro.put("CLIENTE", registro[0]);

                        assert db != null;

                        db.insert("CLIENTES", null, nuevoRegistro);

                        N += 1;

                    }

                }

                Cli.close();

                db.close();


                //f.delete();

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

    private void ALERT_FICHAJE(){
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final Spinner editTextNombre = new Spinner(this);
        editTextNombre.setBackground(getDrawable(R.drawable.rectangle_spiner));

        BasedbHelper  usdbh = new BasedbHelper(this);
        SQLiteDatabase db = usdbh.getWritableDatabase();
        Cursor C_comodines= db.rawQuery("SELECT * FROM COMODINES ", null);
        SimpleCursorAdapter adapterCOMODINES = new SimpleCursorAdapter(this,R.layout.custom_spinner_item1,C_comodines,(new String[] {"COMODIN"}), new int[] {R.id.Spiner_text},0);
        editTextNombre.setAdapter(adapterCOMODINES);
        editTextNombre.setOnItemSelectedListener(this);




       // editTextNombre.setHint(R.string.usuario);
        layout.addView(editTextNombre);
        final EditText editTextPass = new EditText(this);
        editTextPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextPass.setHint(R.string.pass);
        editTextPass.setBackground(getDrawable(R.drawable.rectangle));
        editTextPass.setGravity(Gravity.CENTER);
        editTextPass.setTextSize(24);
        layout.addView(editTextPass);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Fichaje:")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Fichar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Usuario_logado=editTextNombre.getText().toString().toUpperCase();
                       Usuario_logado=S_usuario;

                       Dni_logado=editTextPass.getText().toString().toUpperCase();



                        if (Usuario_logado.equals("") || Dni_logado.equals("")){

                            Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrectos", Toast.LENGTH_LONG).show();

                        }else{
                            if (coor){

                                Cursor login=getCOMODIN(Usuario_logado,Dni_logado);

                                Cursor fichaje=getFICHAJE(Usuario_logado);

                                fichaje.moveToLast();

                               // Toast.makeText(getApplicationContext(), login.getCount(), Toast.LENGTH_LONG).show();

                                if (login.getCount()>0){
                                   if (fichaje.getCount()==0){
                                    Hora_logado=ADAPTADORES.HORAMINUTO();
                                    Cliente_selecionado=Scliente;
                                    Fichaje_inicial();
                                    }else{
                                       Toast.makeText(getApplicationContext(), Usuario_logado+" tu fichaje no es valido, ya has fichado!!!", Toast.LENGTH_LONG).show();
                                   }

                                }else{Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrectos", Toast.LENGTH_LONG).show();}

                               }
                        }


                    }
                });
        final AlertDialog alertDialogPersonalizado = builder.create();
        alertDialogPersonalizado.setView(layout);
// después mostrarla:
        alertDialogPersonalizado.show();
        //alertDialog.show();
    }

    private void ALERT_usuarios(){
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextPass = new EditText(this);
        editTextPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextPass.setHint(R.string.pass);
        editTextPass.setBackground(getDrawable(R.drawable.rectangle));
        editTextPass.setGravity(Gravity.CENTER);
        editTextPass.setTextSize(24);
        layout.addView(editTextPass);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Cambiar/Añadir Usuarios:")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Acceso", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        Dni_logado=editTextPass.getText().toString();



                        if ( Dni_logado.equals("")){

                            Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_LONG).show();

                        }else{
                            if (Dni_logado.equals("Primera08")){ ir_Añadir_usuario();}else{
                                Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                            }
                        }


                    }
                });
        final AlertDialog alertDialogPersonalizado = builder.create();
        alertDialogPersonalizado.setView(layout);
// después mostrarla:
        alertDialogPersonalizado.show();
        //alertDialog.show();
    }

    private void ir_Añadir_usuario(){
        final Intent i = new Intent(this, USUARIO_NUEVO.class);
        startActivity(i);
    }

    public Cursor getCOMODIN(String COMODIN,String DNI) throws SQLException {

        BasedbHelper  usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        return db.rawQuery("SELECT _id,COMODIN,DNI FROM COMODINES WHERE  COMODIN='"+COMODIN+"' AND DNI='"+DNI+"'", null);


        //return c;
    }

    public Cursor getFICHAJE(String COMODIN) throws SQLException {

        BasedbHelper  usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        return db.rawQuery("SELECT _id,COMODIN,HORA_SALIDA FROM FICHAJE WHERE COMODIN='"+COMODIN+"' AND HORA_SALIDA='PENDIENTE'", null);


        //return c;
    }

    private void Fichaje_inicial(){

        BasedbHelper usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        Hora_logado=ADAPTADORES.HORAMINUTO();

        if (db != null) {

            ContentValues nuevoRegistro = new ContentValues();

            nuevoRegistro.put("FECHA", ADAPTADORES.FECHAconformato());
            nuevoRegistro.put("CLIENTE",  Cliente_selecionado);
            nuevoRegistro.put("HORA_ENTRADA", Hora_logado);
            nuevoRegistro.put("HORA_SALIDA", "PENDIENTE");
            nuevoRegistro.put("GPS_ENTRADA", S_coordenadas);
            nuevoRegistro.put("COMODIN", Usuario_logado);
            nuevoRegistro.put("DIRECCION_ENTRADA", direccion.getText().toString());

            db.insert("FICHAJE", null, nuevoRegistro);


        }
        Toast.makeText(getApplicationContext(), Usuario_logado+" ,has fichado la entrada", Toast.LENGTH_LONG).show();
    }
}