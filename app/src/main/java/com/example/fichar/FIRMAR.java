package com.example.fichar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
    public void guardar_firma(View v) {}
}