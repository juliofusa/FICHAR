package com.example.fichar;

public class FICHAJES {

    private String CLIENTE;
    private String COMODIN;
    private String FECHA;
    private String HORA_ENTRADA;
    private String gps;
    private String direccion;
    private Integer Id;

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getCLIENTE(){ return CLIENTE;}

    public String getCOMODIN() {
        return COMODIN;
    }

    public String getFECHA() {
        return FECHA;
    }

    public String getHORA_ENTRADA() {
        return HORA_ENTRADA;
    }

    public void setCLIENTE(String CLIENTE) {
        this.CLIENTE = CLIENTE;
    }

    public void setCOMODIN(String COMODIN) {
        this.COMODIN = COMODIN;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }

    public void setHORA_ENTRADA(String HORA_ENTRADA) {
        this.HORA_ENTRADA = HORA_ENTRADA;
    }
}
