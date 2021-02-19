package com.example.fichar;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ADAPTADORES {

    public static final String R_RUTA_EXPORTACIONES="/COMODIN/EXPORTACIONES/";
    public static final String R_RUTA="/COMODIN/";
    public static final String R_RUTA_IMPORTACIONES="/COMODIN/DATAIMPORT/";
    public static final String C_COLUMNA_ID  = "_id";
    public static final String C_COLUMNA_GESTOR = "GESTOR";
    public static final String C_COLUMNA_ID_ANDROID = "ID_ANDROID";
    public static final String C_COLUMNA_CLIENTE = "CLIENTE";
    public static final String C_COLUMNA_COMODIN = "COMODIN";

    private String[] listaGESTOR = new String[]{C_COLUMNA_ID, C_COLUMNA_GESTOR,C_COLUMNA_ID_ANDROID} ;
    public static final String A_COMODINES="COMODINES.txt";
    public static final String A_CLIENTES="CLIENTES.txt";
    public static final String A_GESTORES="GESTORES.txt";

    private Context contexto;
    private BasedbHelper BasedbHelper;
    private SQLiteDatabase db;

    public ADAPTADORES(Context context)
    {
        this.contexto = context;
    }

    public BasedbHelper abrir(Context cntx) throws SQLException
    {
        BasedbHelper = new BasedbHelper(cntx);
        db = BasedbHelper.getWritableDatabase();
        return BasedbHelper;
    }

    public void cerrar()
    {
        BasedbHelper.close();
    }

    public Cursor getGESTOR() throws SQLException
    {
         Cursor c = db.query( true, C_COLUMNA_GESTOR, listaGESTOR, null, null, null, null,null, null);

        return c;
    }

    public static  String FECHAconformato() {

        Long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        //String dateString =
        return sdf.format(date);
    }


    public static final String HORAconformato() {
        Long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ITALIAN);
        //String dateString = sdf.format(date);
        return sdf.format(date);
    }
    public static final String HORAMINUTO() {
        Long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
       // String dateString = sdf.format(date);
        return sdf.format(date);
    }
    public static final String tiempocompleto() {
        Long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ITALIAN);
       // String dateString = sdf.format(date);
        return sdf.format(date);
    }
    //devuelve String con el MES Y AÑO
    public static  final String mesyano(String FECHA){
        String mmes="",anno="",mesyaNo="";

        anno = FECHA.substring(6);

        mmes = FECHA.substring(3,5);


        switch (mmes){
            case "01":
                mesyaNo="ENERO "+anno;
                break;
            case "02":
                mesyaNo="FEBRERO "+anno;
                break;
            case "03":
                mesyaNo="MARZO "+anno;
                break;
            case "04":
                mesyaNo="ABRIL "+anno;
                break;
            case "05":
                mesyaNo="MAYO "+anno;
                break;
            case "06":
                mesyaNo="JUNIO "+anno;
                break;
            case "07":
                mesyaNo="JULIO "+anno;
                break;
            case "08":
                mesyaNo="AGOSTO "+anno;
                break;
            case "09":
                mesyaNo="SEPTIEMBRE "+anno;
                break;
            case "10":
                mesyaNo="OCTUBRE "+anno;
                break;
            case "11":
                mesyaNo="NOVIEMBRE "+anno;
                break;
            case "12":
                mesyaNo="DICIEMBRE "+anno;
                break;
        }


        return mesyaNo;
    }

    //devuelve String con el dia de semana
    public static  final String Diadelasemana(int dia,int mes,int aNo){

        Calendar now = Calendar.getInstance();

        now.set(aNo,mes,dia);

        String [] diassemana={"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};

        //return diassemana[now.get(Calendar.DAY_OF_WEEK) - 1];
        return diassemana[0];
    }
    public static boolean USUARIOVALIDO(){

        return true;
    }
    public  Cursor getCOMODIN(String COMODIN,String DNI) throws SQLException
    {
        Cursor c=db.rawQuery("SELECT _id,COMODIN,DNI FROM COMODINES WHERE  COMODIN='"+COMODIN+"' AND DNI='"+DNI+"'", null);

        return c;
    }
}
