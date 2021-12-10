package com.example.control_inventario.Objetos;

public class Usuario {
    private String id;
    private String usuario;
    private String pass;
    private String nombre;
    private String tipo;
    private Double latitud;
    private Double longitud;
    private String foto;

    public Usuario() {
        this.latitud = 0.0;
        this.longitud = 0.0;
    }

    public Usuario(String id, String usuario, String pass, String nombre, String tipo, Double latitud, Double longitud, String foto) {
        this.id = id;
        this.usuario = usuario;
        this.pass = pass;
        this.nombre = nombre;
        this.tipo = tipo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto = foto;
    }

    public Usuario(String id, String usuario, String pass, String nombre, String tipo, String foto) {
        this.id = id;
        this.usuario = usuario;
        this.pass = pass;
        this.nombre = nombre;
        this.tipo = tipo;
        this.foto = foto;
        this.latitud = 0.0;
        this.longitud = 0.0;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
