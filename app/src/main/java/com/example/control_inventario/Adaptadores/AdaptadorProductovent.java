package com.example.control_inventario.Adaptadores;

import android.hardware.lights.LightState;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.control_inventario.Objetos.Producto;
import com.example.control_inventario.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorProductovent extends RecyclerView.Adapter<AdaptadorProductovent.viewholderproductosvent> {
    List<Producto>productovntList;

    public AdaptadorProductovent(List<Producto> productovntList) {
        this.productovntList = productovntList;
    }

    @NonNull
    @Override
    public viewholderproductosvent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productov,parent, false);
       viewholderproductosvent holder = new viewholderproductosvent(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholderproductosvent holder, int position) {
        Producto p = productovntList.get(position);
        holder.tv_nombre.setText(p.getNombre());
        holder.tv_precio.setText(p.getPrecio());
        holder.cantidad.setText("");
        Picasso.get().load( p.getFoto() ).into(holder.img1);

    }
    @Override
    public int getItemCount() {
        if(productovntList == null){
            return 0;
        }else{
            return productovntList.size();
        }
    }


    public class viewholderproductosvent extends RecyclerView.ViewHolder {
        TextView tv_nombre, tv_precio;
        EditText cantidad;
        CheckBox cb;
        ImageView img1;
        public viewholderproductosvent(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.ITVENTAivImagen);
            tv_nombre=itemView.findViewById(R.id.ITVENTAtvNombre);
            tv_precio=itemView.findViewById(R.id.ITVENTAtvPrecio);
            cantidad=itemView.findViewById(R.id.ETVENTAcantidad);
            cb=itemView.findViewById(R.id.CBVENTAS);

        }
    }
}


