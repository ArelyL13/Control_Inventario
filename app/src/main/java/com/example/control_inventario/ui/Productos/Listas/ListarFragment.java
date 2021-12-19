package com.example.control_inventario.ui.Productos.Listas;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.example.control_inventario.databinding.FragmentListarBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class ListarFragment extends Fragment {

    private DatabaseReference bdReference;
    private FirebaseDatabase bdFire;

    ArrayList<Producto>list;
    RecyclerView rv;
    SearchView searchView;
    AdaptadorProducto adapter;
    LinearLayoutManager lm;
    private ListarViewModel lViewModel;
    FragmentListarBinding binding;

    public static ListarFragment newInstance() {
        return new ListarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       // View root =  inflater.inflate(R.layout.fragment_listar, container, false);
        // lViewModel= new ViewModelProvider(this).get(ListarViewModel.class);
       binding=FragmentListarBinding.inflate(inflater,container,false);
       View root= binding.getRoot();
       adapter= new AdaptadorProducto(list);
       bdReference=FirebaseDatabase.getInstance().getReference().child("Producto");
       rv=root.findViewById(R.id.rv);
       searchView=root.findViewById(R.id.search);
       lm= new LinearLayoutManager(getContext());
       rv.setLayoutManager(lm);

       list=new ArrayList<>();

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




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        for (Producto obj: list){
            if (obj.getNombre().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            }
        }
        AdaptadorProducto adapter= new AdaptadorProducto(milista);
        rv.setAdapter(adapter);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lViewModel = new ViewModelProvider(this).get(ListarViewModel.class);
        // TODO: Use the ViewModel
    }

    public void buscarTodo(){
        bdReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Producto p = snapshot.getValue(Producto.class);
                        list.add(p);
                    }
                    adapter.notifyDataSetChanged();
                AdaptadorProducto adapter= new AdaptadorProducto(list);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }







}