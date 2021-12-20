package com.example.control_inventario.ui.Productos.Ventas;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.control_inventario.Adaptadores.AdaptadorProductovent;
import com.example.control_inventario.Adaptadores.AdaptadorVentaProd;
import com.example.control_inventario.Objetos.Producto;
import com.example.control_inventario.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VentasProdFragment extends Fragment {

    private VentasProdViewModel mViewModel;
    ListView lvProductos;
    DatabaseReference dbReference;
    private AdaptadorVentaProd adaptador;
    ArrayList<Producto> productos = new ArrayList<Producto>();
    FirebaseDatabase dbFire;
    Button btnGenerarCompra;



    public static VentasProdFragment newInstance() {
        return new VentasProdFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_ventas_prod, container, false);

        btnGenerarCompra = root.findViewById(R.id.VENTASbtnVenta);
        btnGenerarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<adaptador.getCount(); i++){
                    Producto p=(Producto) adaptador.getItem(i);
                    Toast.makeText(getContext(), p.getNombre()+"Cant: "+p.getCantidad(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        dbFire = FirebaseDatabase.getInstance();
        dbReference = dbFire.getReference();
        lvProductos = root.findViewById(R.id.VENTASlvProductos);
        listarDatos();

        lvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "Venta"+String.valueOf(i), Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VentasProdViewModel.class);
        // TODO: Use the ViewModel
    }

    public void listarDatos() {
        dbReference.child("Producto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productos.clear();
                for (DataSnapshot objectSnapshot : snapshot.getChildren()) {
                    productos.add(objectSnapshot.getValue(Producto.class));

                }

                adaptador = new AdaptadorVentaProd(productos,getContext());
                lvProductos.setAdapter(adaptador);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}