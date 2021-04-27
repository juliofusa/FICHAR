package com.example.fichar;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by JULIO on 14/11/2017.
 */

public class AnimeAdapter extends RecyclerView.Adapter<com.example.fichar.AnimeAdapter.ViewHolder> {




    private List<FICHAJES> userModelList;



    public AnimeAdapter(List<FICHAJES> userModelList) {
        this.userModelList = userModelList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fichajes_aun_pendientes, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;



    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String s_comodin = userModelList.get(position).getCOMODIN();
        String s_cliente = userModelList.get(position).getCLIENTE();
        String s_fecha = userModelList.get(position).getFECHA();
        String s_hora_entrada = userModelList.get(position).getHORA_ENTRADA();
        String s_DIRECCION = userModelList.get(position).getDireccion();
        String s_GPS = userModelList.get(position).getGps();
        Integer Id=userModelList.get(position).getId();

        holder.comodin.setText(s_comodin);

        holder.cliente.setText(s_cliente);

        holder.fecha.setText(s_fecha);

        holder.hora_entrada.setText(s_hora_entrada);

        holder.GPS.setText(s_GPS);

        holder.DIRECCION.setText(s_DIRECCION);

        holder.ID.setText(String.valueOf(Id));




    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView comodin,fecha,hora_entrada,cliente,ID,GPS,DIRECCION;



        public ViewHolder(View v) {
            super(v);
            comodin =  v.findViewById(R.id.CARD_COMODIN);

            fecha =  v.findViewById(R.id.CARD_FECHA);

            cliente =  v.findViewById(R.id.CARD_CLIENTE);

            hora_entrada =  v.findViewById(R.id.CARD_HE);

            GPS =  v.findViewById(R.id.CARD_COORDENADAS);

            DIRECCION =  v.findViewById(R.id.CARD_DIRECCION);

            ID=  v.findViewById(R.id.CARD_Id);


            // on item click
            itemView.setOnClickListener(v1 -> {
                // get position
                int pos = getAdapterPosition();

                // check if item still exists
                if(pos != RecyclerView.NO_POSITION){

                    Intent intent = new Intent(v1.getContext(), FIRMAR.class);
                    intent.putExtra("id", String.valueOf(ID.getText()));
                    intent.putExtra("DIRECCION_SALIDA", String.valueOf(DIRECCION.getText()));
                    intent.putExtra("GPS_SALIDA", String.valueOf(GPS.getText()));
                    intent.putExtra("NOMBRECOMPLETO", comodin.getText());
                    intent.putExtra("CLIENTE", comodin.getText());
                    v1.getContext().startActivity(intent);


                }
            });



        }

    }

}
