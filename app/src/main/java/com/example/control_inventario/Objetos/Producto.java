package com.example.control_inventario.Objetos;

public class Producto {
    private String id;
    private String nombre;
    private String foto;
    private String cantidad;
    private String precio;
    private String caducidad;
    private String tipo;

    public Producto(String id, String nombre, String foto, String cantidad, String precio, String caducidad, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.caducidad = caducidad;
        this.tipo = tipo;
    }

    public Producto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(String caducidad) {
        this.caducidad = caducidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
