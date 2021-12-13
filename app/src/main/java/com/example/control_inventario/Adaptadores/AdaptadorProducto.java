package com.example.control_inventario.Adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.control_inventario.Objetos.Producto;
import com.example.control_inventario.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdaptadorProducto extends BaseAdapter {

    ArrayList<Producto> productos;
    Context context;

    public AdaptadorProducto(ArrayList<Producto> productos, Context context) {
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
        Log.d("tema_entro", "recibio"+String.valueOf(getCount()));
        Producto producto = (Producto) getItem(i);
        view = LayoutInflater.from(context).inflate(R.layout.item_producto,null);
        //Toast.makeText(view.getContext(), this.getCount(), Toast.LENGTH_SHORT).show();
        ImageView imgFoto = view.findViewById(R.id.ITMPRODivImagen);
        TextView nombre = view.findViewById(R.id.ITMPRODtvNombre);
        TextView precio = view.findViewById(R.id.ITMPRODtvPrecio);
        TextView cantidad = view.findViewById(R.id.ITMPRODtvCantidad);
        //imgFoto.setImageURI();
        Picasso.get().load( producto.getFoto() ).into( imgFoto );
        nombre.setText(producto.getNombre());
        precio.setText(producto.getPrecio());
        cantidad.setText(producto.getCantidad());


        return view;



    }
}
