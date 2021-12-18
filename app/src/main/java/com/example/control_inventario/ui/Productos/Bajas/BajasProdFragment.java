package com.example.control_inventario.ui.Productos.Bajas;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.control_inventario.Objetos.Producto;
import com.example.control_inventario.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BajasProdFragment extends Fragment {

    TextView tvnombre,tvprecio,tvcantidad,tvFecha;
    EditText etid;
    Button btnBuscar,btnEliminar;
    ImageView ivfoto;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference bdReference;
    Producto prod;


    private BajasProdViewModel mViewModel;

    public static BajasProdFragment newInstance() {
        return new BajasProdFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bajas_prod, container, false);
        ivfoto = root.findViewById(R.id.PRODBAJAivFoto);
        tvnombre = root.findViewById(R.id.PRODBAJAtvNombre);
        tvprecio = root.findViewById(R.id.PRODBAJAtvPrecio);
        tvcantidad = root.findViewById(R.id.PRODBAJAtvCantidad);
        tvFecha = root.findViewById(R.id.PRODBAJAtvFecha);
        tvFecha.setVisibility(View.GONE);

        etid = root.findViewById(R.id.PRODBAJAetId);
        bdReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        btnBuscar = root.findViewById(R.id.PRODBAJAbtnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bdReference.child("Producto").child(etid.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        prod = snapshot.getValue(Producto.class);
                        if (prod != null){
                            tvnombre.setText(prod.getNombre());
                            tvcantidad.setText(prod.getCantidad());
                            tvprecio.setText(prod.getPrecio());

                            if(prod.getTipo().equals("Perecedero")){
                                tvFecha.setText(prod.getCaducidad());
                                tvFecha.setVisibility(View.VISIBLE);

                            }
                            Picasso.get().load( prod.getFoto() ).into( ivfoto );
                            //Toast.makeText(getContext(), prod.getFoto(), Toast.LENGTH_SHORT).show();
                        }


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        btnEliminar = root.findViewById(R.id.PRODBAJAbtnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bdReference.child("Producto").child(prod.getId()).removeValue();
                Toast.makeText(getContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                prod= null;
                etid.setText("");
                tvnombre.setText("");
                tvprecio.setText("");
                tvcantidad.setText("");
                ivfoto.setImageResource(android.R.drawable.ic_menu_camera);
            }
        });


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BajasProdViewModel.class);
        // TODO: Use the ViewModel
    }

}