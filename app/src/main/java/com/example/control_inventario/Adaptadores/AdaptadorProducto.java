package com.example.control_inventario.Adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.control_inventario.Objetos.Producto;
import com.example.control_inventario.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AdaptadorProducto extends RecyclerView.Adapter<AdaptadorProducto.viewholderproductos>{
    List<Producto> productoList;

    public AdaptadorProducto(List<Producto> productoList) {
        this.productoList = productoList;

    }

    @NonNull
    @Override
    public viewholderproductos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto,parent, false);
        viewholderproductos holder = new viewholderproductos(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholderproductos holder, int position) {
     Producto p = productoList.get(position);

     holder.tv_nombre.setText(p.getNombre());
     holder.tv_precio.setText(p.getPrecio());
     holder.tv_cantidad.setText(p.getCantidad());
     holder.tv_caducidad.setText(p.getCaducidad());


    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public class viewholderproductos extends RecyclerView.ViewHolder {
        TextView tv_nombre, tv_precio, tv_cantidad, tv_caducidad;
        ImageView img1;


        public viewholderproductos(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.ITMPRODivImagen);
            tv_nombre=itemView.findViewById(R.id.ITMPRODtvNombre);
            tv_precio=itemView.findViewById(R.id.ITMPRODtvPrecio);
            tv_cantidad=itemView.findViewById(R.id.ITMPRODtvCantidad);
            tv_caducidad=itemView.findViewById(R.id.ITMPRODtvCaducidad);



        }
    }
}
