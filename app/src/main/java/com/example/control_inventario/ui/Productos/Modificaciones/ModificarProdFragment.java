package com.example.control_inventario.ui.Productos.Modificaciones;

import static android.app.Activity.RESULT_OK;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.control_inventario.Objetos.Producto;
import com.example.control_inventario.Objetos.Usuario;
import com.example.control_inventario.R;
import com.example.control_inventario.databinding.FragmentModificarProdBinding;
import com.example.control_inventario.databinding.FragmentModificarProdBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ModificarProdFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener{

    private ModificarProdViewModel modViewModel;
    private FragmentModificarProdBinding binding;
    private Button MoButton,btnbuscar;
    private EditText etNombre, etCantidad, etPrecio, etId, etFechaCad;
    private TextInputLayout extFechaCad;
    private RadioButton radPerecedero, radNoperecedero;
    ImageButton ibtnFecha;
    private ImageView miImagenView;
    private Uri fotoUri;
    private String ProductoB;
    private Uri photoUri;
    public static String currentPhotoPath,img="", tamanio, tipSoft, urgencia;
    public static final int Request_TAKE_PHOTO = 1;
    private ArrayList<Producto> Product = new ArrayList<Producto>();
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference bdReference;
    Producto prod;



    private ModificarProdViewModel mViewModel;

    public static ModificarProdFragment newInstance() {
        return new ModificarProdFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       // return inflater.inflate(R.layout.fragment_modificar_prod, container, false);

        // return inflater.inflate(R.layout.fragment_altas_prod, container, false);
        modViewModel= new ViewModelProvider(this).get(ModificarProdViewModel.class);
        binding= FragmentModificarProdBinding.inflate(inflater,container,false);
        View root= binding.getRoot();
        componentes(root);


        return root;
    }

    private void componentes(View root){
        EditTextComponent(root);
        ButtonComponent(root);
        MoButton.setOnClickListener(this);
        miImagenView.setOnClickListener(this);


        bdReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

    }

    private void ButtonComponent(View root) {
        MoButton = root.findViewById(R.id.MODbtnModifica);
        MoButton.setOnClickListener((View.OnClickListener)this);
        btnbuscar = root.findViewById(R.id.MODbtnBuscar);
        btnbuscar.setOnClickListener(this);
        ibtnFecha = root.findViewById(R.id.MODibtnFecha);
        ibtnFecha.setOnClickListener(this);
    }

    private void EditTextComponent(View root) {
        etNombre = root.findViewById(R.id.MODetProductoNom);
        etCantidad = root.findViewById(R.id.MODetCantidad);
        etPrecio = root.findViewById(R.id.MODetPrecio);
        etId= root.findViewById(R.id.MODetId);
        etFechaCad = root.findViewById(R.id.MODetFecha);
        extFechaCad = root.findViewById(R.id.MODextID);

        miImagenView = root.findViewById(R.id.MODivFoto);

        radPerecedero = root.findViewById(R.id.MODradPerecedero);
        radPerecedero.setOnClickListener(this);
        radNoperecedero = root.findViewById(R.id.MODradNoPerecedero);
        radNoperecedero.setOnClickListener(this);


    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
        etFechaCad.setText(dayofMonth+"/"+(month+1)+"/"+year);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MODbtnModifica:

                if (validarCampos()) {
                    //variables para subir imagen
                    StorageReference folder = FirebaseStorage.getInstance().getReference().child("user");
                    StorageReference file_name = folder.child("file" + photoUri.getLastPathSegment());

                    //metodo para subir imagen
                    file_name.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //proceso
                            Toast.makeText(getContext(), "Subiendo", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //fallo
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            //completado
                            //obtener imagen
                            UploadTask.TaskSnapshot downloadUri = null;
                            downloadUri = task.getResult();
                            Task<Uri> result = downloadUri.getStorage().getDownloadUrl();
                            //si obtubo exitosamente
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //llenado de campos
                                    String imagen = uri.toString();
                                    Producto p = new Producto();
                                    //modificar usuario
                                    p.setId(etId.getText().toString().trim());
                                    p.setNombre(etNombre.getText().toString().trim());
                                    p.setCantidad(etCantidad.getText().toString().trim());
                                    p.setPrecio(etPrecio.getText().toString().trim());
                                    p.setFoto(imagen);
                                    if(radPerecedero.isChecked()){
                                        p.setTipo("Perecedero");
                                        p.setCaducidad(etFechaCad.getText().toString().trim());

                                    }else{
                                        p.setTipo("No Perecedero");
                                        p.setCaducidad("No Aplica");
                                    }

                                    Toast.makeText(getContext(), "Modificaron Archivos", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                                    //generar alta
                                    bdReference.child("Producto").child(user.getUid()).child(p.getId()).setValue(p);
                                    Toast.makeText(getContext(), "Producto Modificado", Toast.LENGTH_SHORT).show();
                                    limpiar();
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "llena todos los campos", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.MODivFoto:
                //Toast.makeText(getContext(), "Entrnado a tomar foto", Toast.LENGTH_SHORT).show();
                Intent tomarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (tomarFoto.resolveActivity(getActivity().getPackageManager()) != null){
                    File photoFile = null;
                    try{
                        photoFile = createImageFile();
                        /**
                         if (photoFile == null){
                         Toast.makeText(getContext(), "photoFile Null", Toast.LENGTH_SHORT).show();
                         }else{
                         Toast.makeText(getContext(), "photoFile LLenado", Toast.LENGTH_SHORT).show();
                         }
                         **/
                    }catch (Exception e){
                        Toast.makeText(getContext(), "Error en la fotografia", Toast.LENGTH_SHORT).show();
                    }
                    if (photoFile != null){

                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
                        }

                        photoUri = FileProvider.getUriForFile(getContext(),"com.example.control_inventario",photoFile);
                        tomarFoto.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                        startActivityForResult(tomarFoto,Request_TAKE_PHOTO);
                    }
                }else{
                    Toast.makeText(getContext(), "Fotografia, No entra al proceso", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.MODbtnBuscar:
                bdReference.child("Producto").child(user.getUid()).child(etId.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        prod = snapshot.getValue(Producto.class);
                        etNombre.setText(prod.getNombre());
                        etCantidad.setText(prod.getCantidad());
                        etPrecio.setText(prod.getPrecio());
                        etFechaCad.setText(prod.getCaducidad());
                        if(prod.getTipo().equals("Perecedero")){
                            radPerecedero.setChecked(true);
                            activaFecha(true);
                        }else{
                            radNoperecedero.setChecked(true);
                            activaFecha(false);
                        }
                        Picasso.get().load( prod.getFoto() ).into( miImagenView );
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case R.id.MODradPerecedero:
                activaFecha(true);

                break;
            case R.id.MODradNoPerecedero:
                activaFecha(false);
                break;
            default:
                Toast.makeText(getContext(), "Elemnto no encontrado", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFilename = "FP_"+timestamp+"_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFilename,".jpg",storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean validarCampos () {
        boolean validado=true;
        String producto = etNombre.getText().toString();
        String cantidad = etCantidad.getText().toString();
        String precio = etPrecio.getText().toString();

        if (producto.equals("")) {
            etNombre.setError("Campo o Nombre obligatorio");
            validado = false;
        } else if (cantidad.equals("")) {
            etCantidad.setError("Campo o Nombre obligatorio");
            validado = false;
        } else if (precio.equals("")) {
            etPrecio.setError("Campo o Nombre obligatorio");
            validado = false;
        }else if (photoUri==null) {
            Toast.makeText(getContext(), "imagen vacia", Toast.LENGTH_SHORT).show();
            validado = false;
        }
        return validado;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == Request_TAKE_PHOTO && resultCode == RESULT_OK){
            //Bundle extras = data.getExtras();
            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            miImagenView.setImageBitmap(imageBitmap);
            //dio errror aqui , Linea 88
            try{
                miImagenView.setImageURI(photoUri);
                img=currentPhotoPath;
                Toast.makeText(getContext(), "img:" + img, Toast.LENGTH_SHORT).show();
            }catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(getContext(),"Fallos de onActivityResult " + img,Toast.LENGTH_LONG).show();
            }
        }
    }


    private void limpiar () {
        etNombre.setText("");
        etCantidad.setText("");
        etPrecio.setText("");

    }

    private void activaFecha(boolean act){
        if(act){
            etFechaCad.setVisibility(View.VISIBLE);
            extFechaCad.setVisibility(View.VISIBLE);
            ibtnFecha.setVisibility(View.VISIBLE);
        }else{
            etFechaCad.setVisibility(View.GONE);
            ibtnFecha.setVisibility(View.GONE);
            extFechaCad.setVisibility(View.GONE);
        }
    }
}