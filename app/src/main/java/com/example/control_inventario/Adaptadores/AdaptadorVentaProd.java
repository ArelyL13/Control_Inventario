package com.example.control_inventario.Adaptadores;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.control_inventario.Objetos.Producto;
import com.example.control_inventario.Objetos.Ventas;
import com.example.control_inventario.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorVentaProd extends BaseAdapter {
    ArrayList<Producto> productos;
    Context context;

    public AdaptadorVentaProd(ArrayList<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return productos.size();
    }

    @Override
    public Object getItem(int i) {
        return productos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Producto producto = (Producto) getItem(i);
        view = LayoutInflater.from(context).inflate(R.layout.item_venta_producto,null);
        ImageView imgFoto = view.findViewById(R.id.ITEMVENTivImagen);
        TextView nombre = view.findViewById(R.id.ITEMVENTtvNombre);
        TextView precio = view.findViewById(R.id.ITEMVENTtvPrecio);
        EditText cantidad = view.findViewById(R.id.ITEMVENTetcantidad);



        nombre.setText(producto.getNombre());
        precio.setText(producto.getPrecio());

        //((Producto)getItem(i)).setCantidad(cantidad.getText().toString());
        cantidad.setText(cantidad.getText());

        Picasso.get().load( producto.getFoto()).into(imgFoto);

        return view;

    }




}
