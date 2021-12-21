package com.example.control_inventario.Objetos;

public class VentasP {

    private String idVenta;
    private String producto;
    private String cantidad;
    private String total;

 public VentasP(String idVenta,String producto, String cantidad, String total){
     this.idVenta=idVenta;
     this.producto=producto;
     this.cantidad=cantidad;
     this.total=total;

 }
 public VentasP(){

 }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
