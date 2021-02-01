package com.example.fichar;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class BasedbHelper extends SQLiteOpenHelper {


    String sqlCreateCLIENTES= "CREATE TABLE CLIENTES (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, CLIENTE TEXT)";
    String sqlCreateCOMODINES= "CREATE TABLE COMODINES (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, COMODIN TEXT)";
    String sqlCreateSOLICITUD = "CREATE TABLE FICHAJE (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, FECHA TEXT, CLIENTE TEXT, HORA_ENTRADA TEXT, HORA_SALIDA TEXT,GPS_ENTRADA TEXT,GPS_SALIDA TEXT, COMODIN TEXT, DIRECCION TEXT)";

    public BasedbHelper(Context context) {
        super(context, "DBFICHAR", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(sqlCreateCLIENTES);
        db.execSQL(sqlCreateCOMODINES);
        db.execSQL(sqlCreateSOLICITUD);

        Log.i(this.getClass().toString(), "BASE COMODINES CREADA");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//Se elimina la version anterior de la tabla

        if (newVersion > oldVersion) {
            db.execSQL(sqlCreateCOMODINES);
            //db.execSQL("ALTER TABLE FORMACIONFIRMADAS ADD COLUMN FORMADOR TEXT");
        }
    }
}
