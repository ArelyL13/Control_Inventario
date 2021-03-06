package com.example.control_inventario.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
     Picasso.get().load( p.getFoto() ).into(holder.img1);
     holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            View dialogoView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_producto,null);
            ((TextView)dialogoView.findViewById(R.id.ITDIALOGtvNombre)).setText(p.getNombre());
            ((TextView)dialogoView.findViewById(R.id.ITDIALOGtvPrecio)).setText(p.getPrecio());
            ((TextView)dialogoView.findViewById(R.id.ITDIALOGtvCantidad)).setText(p.getCantidad());
            ((TextView)dialogoView.findViewById(R.id.ITDIALOGtvCaducidad)).setText(p.getCaducidad());
            ImageView ivImageView = dialogoView.findViewById(R.id.DIAPRODivFoto);
            Picasso.get().load( p.getFoto()).into((ImageView) dialogoView.findViewById(R.id.ITDIALOGDivImagen));
            AlertDialog.Builder dialogo = new AlertDialog.Builder(view.getContext());
            dialogo.setTitle("Producto");
            dialogo.setView(dialogoView);
            dialogo.setPositiveButton("Aceptar ", null);
            dialogo.show();
        }
    });
    }
    @Override
    public int getItemCount() {
         if(productoList == null){
             return 0;
         }else{
             return productoList.size();
         }

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
