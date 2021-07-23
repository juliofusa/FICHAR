package com.example.fichar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class USUARIO_NUEVO extends AppCompatActivity {
    private EditText Usuario, password,old_password;
    private TextView Titulo_old_password;
    private String S_Usuario,S_password,S_old_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_s_u_a_r_i_o__n_u_e_v_o);
        Inicializar();
    }

    private void Inicializar(){
                Usuario= findViewById(R.id.Txt_usuario_nuevo);
                password= findViewById(R.id.Txt_password_nuevo);
                old_password= findViewById(R.id.Txt_password_viejo);
                Titulo_old_password= findViewById(R.id.Txt_Titulo_old_password);
    }

    public void Boton_Añadir_Actializar_Usuario(View V) {

        get_datos();


        if (S_Usuario.equals("") || S_password.equals("")) {

            Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrectos", Toast.LENGTH_LONG).show();

        } else {


                Cursor login = getCOMODIN1(S_Usuario);

                    //comprobamos que existe el usuario
                if (login.getCount() > 0 && S_old_password.equals("")) {

                     Ver_Old_password(View.VISIBLE);

                     Toast.makeText(getApplicationContext(), "El Usuario Ya existe, introduzca la contraseña anterior", Toast.LENGTH_LONG).show();


                    }else{

                        Cursor login1 = getCOMODIN(S_Usuario,S_old_password);

                        if (login1.getCount() > 0) {Update_Datos(S_Usuario, S_password,S_old_password);


                    }else{
                            if (login.getCount()== 0){set_Datos(S_Usuario, S_password);}else{
                                Toast.makeText(getApplicationContext(), "La contraseña anterior no es valida, introduzca la contraseña anterior valida", Toast.LENGTH_LONG).show();
                            }
                        }}}




    }

    public void Boton_reset(View V){
        BasedbHelper usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        if (db != null) {
            db.execSQL("DELETE FROM CLIENTES");
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name = '" + "CLIENTES" + "'");

        }
    }

    public Cursor getCOMODIN(String COMODIN,String DNI) throws SQLException {

        BasedbHelper  usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        return db.rawQuery("SELECT _id,COMODIN,DNI FROM COMODINES WHERE  COMODIN='"+COMODIN+"' AND DNI='"+DNI+"'", null);


        //return c;
    }

    public Cursor getCOMODIN1(String COMODIN) throws SQLException {

        BasedbHelper  usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        return db.rawQuery("SELECT _id,COMODIN FROM COMODINES WHERE  COMODIN='"+COMODIN+"'", null);


        //return c;
    }


    private void get_datos(){
        S_Usuario=Usuario.getText().toString(); S_Usuario= S_Usuario.toUpperCase();
        S_password=password.getText().toString();S_password= S_password.toUpperCase();
        S_old_password=old_password.getText().toString(); S_old_password=  S_old_password.toUpperCase();
    }

    private void set_Datos(String Usuario,String Password){
        BasedbHelper usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();



        if (db != null) {

            ContentValues nuevoRegistro = new ContentValues();


            nuevoRegistro.put("COMODIN",Usuario.toUpperCase());
            nuevoRegistro.put("DNI",Password.toUpperCase());

            db.insert("COMODINES", null, nuevoRegistro);


        }
        Toast.makeText(getApplicationContext(), Usuario+" , ha sido añadido", Toast.LENGTH_LONG).show();

        finish();

    }

    private void Update_Datos(String Usuario,String Password,String Old_Password){
        BasedbHelper usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();



        if (db != null) {

            ContentValues nuevoRegistro = new ContentValues();


            nuevoRegistro.put("COMODIN",Usuario.toUpperCase());
            nuevoRegistro.put("DNI",Password.toUpperCase());

            db.update("COMODINES",  nuevoRegistro,"_id=" + GET_ID(Usuario,Old_Password),null);


        }
        Toast.makeText(getApplicationContext(), Usuario+" , ha sido modificado", Toast.LENGTH_LONG).show();

       finish();

    }

    private String GET_ID(String COMODIN,String DNI){
        BasedbHelper usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();



        Cursor C_comodines= db.rawQuery("SELECT _id,COMODIN,DNI FROM COMODINES WHERE  COMODIN='"+COMODIN+"' AND DNI='"+DNI+"'", null);

        if (C_comodines.moveToLast() ){

            C_comodines.getString(0);

            return  C_comodines.getString(0);
        }else{return "0";}}

    private void Ver_Old_password(Integer visibilidad){

        old_password.setVisibility(visibilidad);

        Titulo_old_password.setVisibility(visibilidad);

    }

}
