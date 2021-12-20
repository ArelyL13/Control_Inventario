package com.example.control_inventario.ui.Productos.Ventas;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.example.control_inventario.Adaptadores.AdaptadorProductovent;
import com.example.control_inventario.Objetos.Producto;
import com.example.control_inventario.R;
import com.example.control_inventario.databinding.FragmentVentasBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VentasFragment extends Fragment {
    private DatabaseReference bdReference;
    ArrayList<Producto> listV;
    RecyclerView rv;
    SearchView searchV;
    AdaptadorProductovent  adapter;
    LinearLayoutManager lm;
    Button btnVenta;
    private VentasViewModel mViewModel;
   FragmentVentasBinding binding;

    public static VentasFragment newInstance() {
        return new VentasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       // View root =  inflater.inflate(R.layout.fragment_ventas, container, false);
        binding= FragmentVentasBinding.inflate(inflater,container,false);
        View root= binding.getRoot();
        adapter= new AdaptadorProductovent(listV);
        bdReference= FirebaseDatabase.getInstance().getReference().child("Producto");
        rv=root.findViewById(R.id.rv2);
        searchV=root.findViewById(R.id.search2);
        lm= new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);

        listV=new ArrayList<>();

        btnVenta = root.findViewById(R.id.VENTbtnVenta);
        btnVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), String.valueOf(adapter.getItemId(1)), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), String.valueOf(adapter.getNombre()), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), String.valueOf(adapter.getId()), Toast.LENGTH_SHORT).show();

            }
        });



        rv.setAdapter(adapter);
        buscarTodo();
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        searchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                buscar(s);
                Log.d("texto",s);
                Log.d("entro","ya entro");
                return false;
            }
        });


        return root;
    }
    private void buscar(String s) {
        ArrayList<Producto>milista = new ArrayList<>();
        for (Producto obj: listV){
            if (obj.getNombre().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            }
        }
        adapter = new AdaptadorProductovent(milista);
        rv.setAdapter(adapter);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VentasViewModel.class);
        // TODO: Use the ViewModel
    }
    public void buscarTodo(){
        bdReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Producto p = snapshot.getValue(Producto.class);
                    listV.add(p);
                }
                adapter.notifyDataSetChanged();
                adapter= new AdaptadorProductovent(listV);
                rv.setAdapter(adapter);
                Toast.makeText(getContext(), "ID_PROD", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }



}