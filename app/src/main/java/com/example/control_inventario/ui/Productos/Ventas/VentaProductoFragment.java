package com.example.control_inventario.ui.Productos.Ventas;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.control_inventario.Adaptadores.AdaptadorProducto;
import com.example.control_inventario.Objetos.Producto;
import com.example.control_inventario.Objetos.Ventas;
import com.example.control_inventario.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class VentaProductoFragment extends Fragment {

    private VentaProductoViewModel mViewModel;

    Button btnVender,btnBuscar;
    TextView tvNombre,tvPrecio;
    EditText etCantidad,etid;
    DatabaseReference dbReference;
    FirebaseAuth mAuth;
    Producto prod;




    public static VentaProductoFragment newInstance() {
        return new VentaProductoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.venta_producto_fragment, container, false);

        dbReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        tvNombre = root.findViewById(R.id.VENTAtvNombre);
        tvPrecio = root.findViewById(R.id.VENTAtvNPrecio);
        etCantidad = root.findViewById(R.id.VENTAedCantidad);
        etid = root.findViewById(R.id.VENTAetId);



        btnBuscar = root.findViewById(R.id.VENTAbtnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbReference.child("Producto").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Producto p = snapshot.getValue(Producto.class);
                            if(p.getNombre().equals(etid.getText().toString())){
                                prod = snapshot.getValue(Producto.class);
                                tvNombre.setText(prod.getNombre());
                                tvPrecio.setText(prod.getPrecio());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        btnVender = root.findViewById(R.id.VentabtnGuardar);
        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ventas venta  = new Ventas();
                venta.setIdVenta(UUID.randomUUID().toString());
                venta.setIdUsuario(mAuth.getUid());
                venta.setNombre(prod.getNombre());
                venta.setCantidad(prod.getCantidad());
                venta.setPrecio(prod.getPrecio());

                dbReference.child("Venta").child(venta.getIdUsuario()).child(venta.getIdVenta()).setValue(venta);
            }
        });





        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VentaProductoViewModel.class);
        // TODO: Use the ViewModel
    }

}