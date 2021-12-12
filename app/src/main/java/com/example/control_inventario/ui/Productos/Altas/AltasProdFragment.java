package com.example.control_inventario.ui.Productos.Altas;

import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
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

import com.example.control_inventario.R;
import com.example.control_inventario.databinding.FragmentAltasProdBinding;

public class AltasProdFragment extends Fragment {

    private AltasProdViewModel altViewModel;
    private FragmentAltasProdBinding binding;
    private Button btnguarda;
    private EditText etID,etProducto, etCantidad, etPrecio;
    private ImageView miImagenView;
    private Uri fotoUri;


    public static AltasProdFragment newInstance() {
        return new AltasProdFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
       // return inflater.inflate(R.layout.fragment_altas_prod, container, false);
        altViewModel= new ViewModelProvider(this).get(AltasProdViewModel.class);
        binding= FragmentAltasProdBinding.inflate(inflater,container,false);
        View root= binding.getRoot();

        return root;
    }
    private void componentes(View root){
        EditTextComponent(root);
        ButtonComponent(root);
    }

    private void ButtonComponent(View root) {
        btnguarda = root.findViewById(R.id.ALTbtnGuardar);
        btnguarda.setOnClickListener((View.OnClickListener)this);
    }

    private void EditTextComponent(View root) {
        etID = root.findViewById(R.id.ALetId);
        etProducto = root.findViewById(R.id.AlTetProductoNom);
        etCantidad = root.findViewById(R.id.AlTetCantidad);
        etPrecio = root.findViewById(R.id.AlTetPrecio);
    }


}