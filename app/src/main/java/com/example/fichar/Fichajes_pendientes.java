package com.example.fichar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Fichajes_pendientes extends AppCompatActivity  {
    public RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private String DIRECCION_SALIDA,GPS_SALIDA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichajes_pendientes);
        DIRECCION_SALIDA=getIntent().getStringExtra("DIRECCION_SALIDA");
        GPS_SALIDA=getIntent().getStringExtra("GPS_SALIDA");
        Inicializar();
    }
    private void Inicializar(){
        recycler = findViewById(R.id.recliclado_CARD);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        // lManager = new ConstraintLayout(this);
        recycler.setLayoutManager(mLayoutManager);
        List listaItemsCursos = getCursorList();

        adapter = new AnimeAdapter(listaItemsCursos);
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new DefaultItemAnimator());



    }

    public  List<FICHAJES> getCursorList(){

        List<FICHAJES>  list= new ArrayList<>();

        String QUERY="";


            QUERY="SELECT * FROM FICHAJE WHERE HORA_SALIDA='PENDIENTE'";

        BasedbHelper  usdbh = new BasedbHelper(this);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        Cursor c=db.rawQuery(QUERY,null);



        while (c.moveToNext()){
            FICHAJES Anime = new FICHAJES();

            Anime.setFECHA(c.getString(1));

            Anime.setCLIENTE(c.getString(2));

            Anime.setCOMODIN(c.getString(7));

            Anime.setHORA_ENTRADA(c.getString(3));

            Anime.setGps(GPS_SALIDA);

            Anime.setDireccion(DIRECCION_SALIDA);

            Anime.setId(c.getInt(0));



            list.add(Anime);
        }

        return list;}



}