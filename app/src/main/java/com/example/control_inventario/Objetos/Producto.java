package com.example.control_inventario.Objetos;

public class Producto {
    private String id;
    private String nombre;
    private String foto;
    private String cantidad;
    private String precio;

    public Producto(String id, String nombre, String foto, String cantidad, String precio) {
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
        this.cantidad = cantidad;
        this.precio = precio;
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
}
