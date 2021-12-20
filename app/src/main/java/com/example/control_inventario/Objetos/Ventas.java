package com.example.control_inventario.Objetos;

public class Ventas {
    private String idVenta;
    private String idUsuario;
    private String nombre;
    private String cantidad;
    private String precio;

    public Ventas() {
    }

    public Ventas(String idVenta, String idUsuario, String nombre, String cantidad, String precio) {
        this.idVenta = idVenta;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
