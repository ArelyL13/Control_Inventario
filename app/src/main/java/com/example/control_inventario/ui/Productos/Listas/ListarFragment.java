package com.example.control_inventario.ui.Productos.Listas;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.control_inventario.Adaptadores.AdaptadorProducto;
import com.example.control_inventario.MainActivity;
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

import java.util.ArrayList;

public class ListarFragment extends Fragment {

    private DatabaseReference bdReference;
    private FirebaseDatabase bdFire;
    private AdaptadorProducto adaptador;
    FirebaseAuth mAuth;
    ArrayList<Producto> productos = new ArrayList<Producto>();

    ListView lvProductos;
    private ListarViewModel mViewModel;


    public static ListarFragment newInstance() {
        return new ListarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_listar, container, false);

        asignaComponentes(root);
        consultarDatos();

        lvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "Entrando al adapter view", Toast.LENGTH_SHORT).show();
                View dialogoView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_mostrar,null);
                ((TextView)dialogoView.findViewById(R.id.DIAPRODtvNombre)).setText(productos.get(i).getNombre());
                ((TextView)dialogoView.findViewById(R.id.DIAPRODId)).setText(productos.get(i).getId());
                ((TextView)dialogoView.findViewById(R.id.DIAPRODCantidad)).setText(productos.get(i).getCantidad());
                ((TextView)dialogoView.findViewById(R.id.DIAPRODPrecio)).setText(productos.get(i).getPrecio());
                ImageView ivImageView = dialogoView.findViewById(R.id.DIAPRODivFoto);
                Picasso.get().load( productos.get(i).getFoto()).into( ivImageView );


                AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                dialogo.setTitle("Producto");
                dialogo.setView(dialogoView);
                dialogo.show();
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListarViewModel.class);
        // TODO: Use the ViewModel
    }

    public void asignaComponentes(View root){
        lvProductos= root.findViewById(R.id.LISTPRODlvLista);
        mAuth = FirebaseAuth.getInstance();
        bdFire = FirebaseDatabase.getInstance();
        bdReference = bdFire.getReference();
    }



    public void consultarDatos() {


        //tvTituloTem.setText(tema);
        //Abecedario
        FirebaseUser user = mAuth.getCurrentUser();
        bdReference.child("Producto").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productos.clear();
                for (DataSnapshot objectSnapshot : snapshot.getChildren()) {
                    productos.add(objectSnapshot.getValue(Producto.class));
                    Log.d("tema_entro", "Entro "+ objectSnapshot.getKey().toString());
                    //Log.d("tema_key", objectSnapshot.getKey().toString());
                }
                Log.d("tema_tamanio", String.valueOf(productos.size()));
                adaptador = new AdaptadorProducto(productos,getContext());
                lvProductos.setAdapter(adaptador);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("tema_fallo", error.getDetails());
            }
        });
    }

}