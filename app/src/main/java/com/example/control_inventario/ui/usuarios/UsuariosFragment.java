package com.example.control_inventario.ui.usuarios;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.control_inventario.Objetos.Producto;
import com.example.control_inventario.Objetos.Usuario;
import com.example.control_inventario.R;
import com.example.control_inventario.RegistroUsuariosActivity;
import com.example.control_inventario.databinding.FragmentAltasProdBinding;
import com.example.control_inventario.databinding.FragmentModificarProdBinding;
import com.example.control_inventario.databinding.UsuariosFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Date;

public class UsuariosFragment extends Fragment implements View.OnClickListener {
    FirebaseAuth mAuth;
    DatabaseReference dbReference;
    FirebaseUser user ;
    Usuario  us;
    EditText etCorreo,etPass,etNombre,etApellidoP, etApellidoM;
    Button btnModificar,btnFoto;
    RadioButton radAdministrador,radUsuario;
    ImageView ivFoto;
    private Uri photoUri;
    public static String currentPhotoPath,img="", tamanio, tipSoft, urgencia;
    public static final int Request_TAKE_PHOTO = 1;
    private UsuariosViewModel mViewModel;
    UsuariosFragmentBinding binding;

    public static UsuariosFragment newInstance() {
        return new UsuariosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.usuarios_fragment, container, false);
        mViewModel=new ViewModelProvider(this).get(UsuariosViewModel.class);
        binding= UsuariosFragmentBinding.inflate(inflater,container,false);
        View root= binding.getRoot();
        componentes(root);
        buscarUsuario();

        return root;
    }

    private void buscarUsuario() {
        dbReference.child("Usuario").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                us= snapshot.getValue(Usuario.class);
                etCorreo.setText(us.getCorreo());
                etPass.setText(us.getPass());
                etNombre.setText(us.getNombre());
                etApellidoP.setText(us.getApellidoP());
                etApellidoM.setText(us.getApellidoM());
                if (us.getTipo().equals("Administrador")) {
                radAdministrador.setChecked(true);
                }else {
                    radUsuario.setChecked(true);
                }
                Picasso.get().load(us.getFoto()).into(ivFoto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void componentes(View root){
        EditTextcomponent(root);
        ButtonComponent(root);
        btnModificar.setOnClickListener(this);
        btnFoto.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        dbReference = FirebaseDatabase.getInstance().getReference();

        user = mAuth.getCurrentUser();

    }

    private void EditTextcomponent(View root) {

        etCorreo =root.findViewById(R.id.MODUsuarioCorreo);
        etNombre =root.findViewById(R.id.MODUsuarioNombre);
        etPass = root.findViewById(R.id.MODUsuarioPass);
        etApellidoP =root.findViewById(R.id.MODUsuarioApellidoP);
        etApellidoM =root.findViewById(R.id.MODUsuarioApellidoM);



        ivFoto =root.findViewById(R.id.MODUsuarioivFoto);

        radAdministrador = root.findViewById(R.id.RBMODUsuarioAdmin);
        radAdministrador.setOnClickListener(this);
        radUsuario = root.findViewById(R.id.RBMODUsuarioVendedor);
        radUsuario.setOnClickListener(this);

    }
    private void ButtonComponent(View root){
        btnModificar = root.findViewById(R.id.BTNMODUsuarios);
        btnModificar.setOnClickListener((View.OnClickListener)this);
        btnFoto = root.findViewById(R.id.MODUsuariobtnFoto);
        btnFoto.setOnClickListener((View.OnClickListener)this);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UsuariosViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onClick(View view) {

            switch (view.getId()) {
                case R.id.BTNMODUsuarios:

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
                                        Usuario p = new Usuario();
                                        //modificar usuario
                                        p.setId(mAuth.getUid());
                                        p.setCorreo(etCorreo.getText().toString().trim());
                                        p.setPass(etPass.getText().toString().trim());
                                        p.setNombre(etNombre.getText().toString().trim());
                                        p.setApellidoP(etApellidoP.getText().toString().trim());
                                        p.setApellidoM(etApellidoM.getText().toString().trim());
                                        p.setFoto(imagen);


                                        Toast.makeText(getContext(), "Modificaron Archivos", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                                        //generar alta
                                        dbReference.child("Usuario").child(p.getId()).setValue(p);
                                        Toast.makeText(getContext(), "Usuario Modificado", Toast.LENGTH_SHORT).show();
                                        limpiar();
                                    }
                                });
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "llena todos los campos", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.MODUsuariobtnFoto:
                    //Toast.makeText(getContext(), "Entrnado a tomar foto", Toast.LENGTH_SHORT).show();
                    Intent tomarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (tomarFoto.resolveActivity(getActivity().getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                            /**
                             if (photoFile == null){
                             Toast.makeText(getContext(), "photoFile Null", Toast.LENGTH_SHORT).show();
                             }else{
                             Toast.makeText(getContext(), "photoFile LLenado", Toast.LENGTH_SHORT).show();
                             }
                             **/
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error en la fotografia", Toast.LENGTH_SHORT).show();
                        }
                        if (photoFile != null) {

                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                            }

                            photoUri = FileProvider.getUriForFile(getContext(), "com.example.control_inventario", photoFile);
                            tomarFoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(tomarFoto, Request_TAKE_PHOTO);
                        }
                    } else {
                        Toast.makeText(getContext(), "Fotografia, No entra al proceso", Toast.LENGTH_SHORT).show();
                    }
                    break;



                 case R.id.RBMODUsuarioAdmin:


                    break;

                case R.id.RBMODUsuarioVendedor:

                    break;
                default:
                    Toast.makeText(getContext(), "Elemnto no encontrado", Toast.LENGTH_SHORT).show();
            }


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
        String correo =etCorreo.getText().toString();
        String pass = etCorreo.getText().toString();
        String nombre =etNombre.getText().toString();



        if (correo.equals("")) {
            etCorreo.setError("correo obligatorio");
            validado=false;
        } else if (pass.equals("")) {
            etPass.setError("Contraseña obligatoria");
            validado=false;
        }else if (nombre.equals("")){
            etNombre.setError("nombre Obligatorio");
            validado=false;
        } else if (photoUri==null){
            Toast.makeText(getContext(), "imagen vacia", Toast.LENGTH_SHORT).show();
            validado = false;

        }

        return validado;
    }
    private void limpiar(){
        etNombre.setText("");

        etCorreo.setText("");
        etPass.setText("");
    }
}