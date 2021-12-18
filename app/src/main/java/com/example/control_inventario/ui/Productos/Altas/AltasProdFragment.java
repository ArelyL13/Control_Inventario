package com.example.control_inventario.ui.Productos.Altas;

import static android.app.Activity.RESULT_OK;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
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

import com.example.control_inventario.MainActivity;
import com.example.control_inventario.Objetos.Producto;
import com.example.control_inventario.Objetos.Usuario;
import com.example.control_inventario.R;
import com.example.control_inventario.RegistroUsuariosActivity;
import com.example.control_inventario.databinding.FragmentAltasProdBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AltasProdFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private AltasProdViewModel altViewModel;
    private FragmentAltasProdBinding binding;
    private Button btnguarda;
    private EditText etNombre, etCantidad, etPrecio, etFechaCad;
    private TextInputLayout extFechaCad;
    private RadioButton radPerecedero, radNoperecedero;
    ImageButton ibtnFecha;
    private ImageView ivFoto;
    private Uri fotoUri;
    private Uri photoUri;
    public static String currentPhotoPath,img="", tamanio, tipSoft, urgencia;
    public static final int Request_TAKE_PHOTO = 1;


    DatePickerDialog dpd;
    Calendar c;
    private static int anio,mes,dia;

    DatabaseReference dbReference;
    FirebaseAuth mAuth;
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
        componentes(root);




        return root;
    }
    private void componentes(View root){
        EditTextComponent(root);
        ButtonComponent(root);
        btnguarda.setOnClickListener(this);
        ivFoto.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference();


        etFechaCad.setVisibility(View.GONE);
        ibtnFecha.setVisibility(View.GONE);
        extFechaCad.setVisibility(View.GONE);

    }

    private void ButtonComponent(View root) {
        btnguarda = root.findViewById(R.id.ALTbtnGuardar);
        btnguarda.setOnClickListener(this);
        ibtnFecha = root.findViewById(R.id.ALTibtnFecha);
        ibtnFecha.setOnClickListener(this);
    }

    private void EditTextComponent(View root) {
        etNombre = root.findViewById(R.id.AlTetProductoNom);
        etCantidad = root.findViewById(R.id.AlTetCantidad);
        etPrecio = root.findViewById(R.id.AlTetPrecio);
        etFechaCad = root.findViewById(R.id.ALTetFecha);
        extFechaCad = root.findViewById(R.id.ALTextID);

        ivFoto = root.findViewById(R.id.PALTivFoto);

        radPerecedero = root.findViewById(R.id.ALTradPerecedero);
        radPerecedero.setOnClickListener(this);
        radNoperecedero = root.findViewById(R.id.ALTradNoPerecedero);
        radNoperecedero.setOnClickListener(this);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
        etFechaCad.setText(dayofMonth+"/"+(month+1)+"/"+year);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ALTbtnGuardar:

                if (validarCampos()){
                    //variables para subir imagen
                    StorageReference folder = FirebaseStorage.getInstance().getReference().child("user");
                    StorageReference file_name = folder.child("file"+photoUri.getLastPathSegment());

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

                                    //extraer usuario

                                    //Bundle extras = getActivity().getIntent().getExtras();
                                    //String us = null;

                                    if (true){
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        p.setId(UUID.randomUUID().toString());
                                        p.setNombre(etNombre.getText().toString());
                                        p.setCantidad(etCantidad.getText().toString());
                                        p.setPrecio(etPrecio.getText().toString());
                                        if(radPerecedero.isChecked()){
                                            p.setTipo("Perecedero");
                                            p.setCaducidad(etFechaCad.getText().toString().trim());

                                        }else{
                                            p.setTipo("No Perecedero");
                                            p.setCaducidad("No Aplica");
                                        }
                                        //p.setCaducidad(etFechaCad.getText().toString().trim());
                                        p.setFoto(imagen);
                                        Toast.makeText(getContext(), "Subieron Archivos", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getContext(),  uri.toString(), Toast.LENGTH_SHORT).show();
                                        dbReference.child("Producto").child(p.getId()).setValue(p);
                                        Toast.makeText(getContext(), "Producto Creado", Toast.LENGTH_SHORT).show();
                                        limpiar();
                                    }else{
                                        Toast.makeText(getContext(), "No entro usuario", Toast.LENGTH_SHORT).show();
                                    }
                                    //crear usuario

                                }
                            });
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "llena todos los campos", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.PALTivFoto:
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
            case R.id.ALTibtnFecha:
                c = Calendar.getInstance();
                anio=c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);
                dpd = new DatePickerDialog(getContext(),this,anio,mes,dia);
                dpd.show();
                break;
            case R.id.ALTradPerecedero:
                etFechaCad.setVisibility(View.VISIBLE);
                extFechaCad.setVisibility(View.VISIBLE);
                ibtnFecha.setVisibility(View.VISIBLE);
                break;
            case R.id.ALTradNoPerecedero:
                etFechaCad.setVisibility(View.GONE);
                ibtnFecha.setVisibility(View.GONE);
                extFechaCad.setVisibility(View.GONE);
                break;



            default:
                Toast.makeText(getContext(), "Elemnto no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == Request_TAKE_PHOTO && resultCode == RESULT_OK){
            //Bundle extras = data.getExtras();
            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivFoto.setImageBitmap(imageBitmap);
            //dio errror aqui , Linea 88
            try{
                ivFoto.setImageURI(photoUri);
                img=currentPhotoPath;
                Toast.makeText(getContext(), "img:" + img, Toast.LENGTH_SHORT).show();
            }catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(getContext(),"Fallos de onActivityResult " + img,Toast.LENGTH_LONG).show();
            }
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

    private void limpiar () {
        etNombre.setText("");
        etCantidad.setText("");
        etPrecio.setText("");

    }
}