package com.example.control_inventario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.control_inventario.Objetos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class RegistroUsuariosActivity extends AppCompatActivity {
    Button btnRegistrarse,btnFoto;
    EditText etCorreo,etPass,etNombre,etApellidoP, etApellidoM;
    RadioButton radAdministrador,radUsuario;


    ImageView ivFoto;



    private Uri photoUri;
    public static String currentPhotoPath,img="", tamanio, tipSoft, urgencia;
    public static final int Request_TAKE_PHOTO = 1;
    String urlimg;


    FirebaseAuth bdMauth;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios);
        asignarComponentes();
        tomarFoto();


        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()){
                    Toast.makeText(RegistroUsuariosActivity.this, "Iniciando proceso", Toast.LENGTH_LONG).show();
                    //mAuth.signOut();
                    bdMauth.createUserWithEmailAndPassword(etCorreo.getText().toString(),etPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                StorageReference folder = FirebaseStorage.getInstance().getReference().child("user");
                                StorageReference file_name = folder.child("file"+photoUri.getLastPathSegment());


                                file_name.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        Toast.makeText(RegistroUsuariosActivity.this, "Subiendo", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(RegistroUsuariosActivity.this, "Error", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        UploadTask.TaskSnapshot downloadUri = null;
                                        downloadUri = task.getResult();
                                        Task<Uri> result = downloadUri.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Toast.makeText(RegistroUsuariosActivity.this, "Subieron Archivos", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(RegistroUsuariosActivity.this,  uri.toString(), Toast.LENGTH_SHORT).show();
                                                String imagen = uri.toString();
                                                Usuario us = new Usuario();
                                                //Log.d("id_us",bdMauth.getUid());
                                                us.setId(bdMauth.getUid());
                                                us.setNombre(etNombre.getText().toString().trim());
                                                us.setApellidoP(etApellidoP.getText().toString().trim());
                                                us.setApellidoM(etApellidoM.getText().toString().trim());
                                                us.setCorreo(etCorreo.getText().toString().trim());
                                                us.setPass(etPass.getText().toString().trim());
                                                if(radAdministrador.isChecked()){
                                                    us.setTipo("Administrador");
                                                }else{
                                                    us.setTipo("Vendedor");
                                                }
                                                us.setFoto(imagen);
                                                dbReference.child("Usuario").child(us.getId()).setValue(us);

                                                Intent intent = new Intent(RegistroUsuariosActivity.this,MainActivity.class);
                                                intent.putExtra("usuario",us);
                                                startActivity(intent);
                                                limpiar();
                                            }
                                        });
                                    }
                                });

                                /**
                                 file_name.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                                /**
                                Log.d("rutaImg1",taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                                Log.d("rutaImg2",taskSnapshot.getMetadata().getReference().getPath());
                                Log.d("rutaImg3",taskSnapshot.getMetadata().getReference().getBucket());
                                Log.d("rutaImg4",taskSnapshot.getMetadata().getReference().getPath());
                                Log.d("rutaImg5",taskSnapshot.getUploadSessionUri().toString());

                                Toast.makeText(RegistroUsuariosActivity.this, "Imagen Subida", Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegistroUsuariosActivity.this, "1"+taskSnapshot.getMetadata().getReference().getDownloadUrl().toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegistroUsuariosActivity.this, "2"+taskSnapshot.getMetadata().getReference().getPath(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegistroUsuariosActivity.this, "3"+taskSnapshot.getMetadata().getReference().getBucket(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegistroUsuariosActivity.this, "4"+taskSnapshot.getUploadSessionUri().toString(), Toast.LENGTH_SHORT).show();


                                String imagen = taskSnapshot.getUploadSessionUri().toString();
                                Usuario us = new Usuario();
                                Log.d("id_us",bdMauth.getUid());
                                us.setId(UUID.randomUUID().toString());
                                us.setNombre(etNombre.getText().toString().trim());
                                us.setCorreo(etCorreo.getText().toString().trim());
                                us.setPass(etPass.getText().toString().trim());
                                us.setTipo(etTipo.getText().toString().trim());
                                us.setFoto(imagen);
                                //Log.d("ruta",generatedFilePath);
                                dbReference.child("Usuario").child(us.getId()).setValue(us);
                                }
                                });
                                 **/




                                Toast.makeText(RegistroUsuariosActivity.this, "Registro Finalizado", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegistroUsuariosActivity.this, "Error con la bd", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                }

        });






    }

    private void asignarComponentes(){
        btnRegistrarse = findViewById(R.id.REGbtnRegistrarse);
        btnFoto = findViewById(R.id.REGbtnFoto);

        etCorreo = findViewById(R.id.REGetcorreo);
        etNombre = findViewById(R.id.REGetnombre);
        etPass = findViewById(R.id.REGetpass);
        etApellidoP = findViewById(R.id.REGetApellidoP);
        etApellidoM = findViewById(R.id.REGetApellidoM);

        bdMauth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference();

        ivFoto = findViewById(R.id.REGivFoto);

        radAdministrador = findViewById(R.id.REGradAdmin);
        radUsuario = findViewById(R.id.REGradVendedor);


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
                Toast.makeText(RegistroUsuariosActivity.this, "img:" + img, Toast.LENGTH_SHORT).show();
            }catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(RegistroUsuariosActivity.this,"Fallos de onActivityResult " + img,Toast.LENGTH_LONG).show();
            }
        }
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFilename = "FP_"+timestamp+"_";
        File storageDir = RegistroUsuariosActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFilename,".jpg",storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void tomarFoto(){
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//Toast.makeText(getContext(), "Entrnado a tomar foto", Toast.LENGTH_SHORT).show();
                Intent tomarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (tomarFoto.resolveActivity(RegistroUsuariosActivity.this.getPackageManager()) != null){
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
                        Toast.makeText(RegistroUsuariosActivity.this, "Error en la fotografia", Toast.LENGTH_SHORT).show();
                    }
                    if (photoFile != null){

                        if (ContextCompat.checkSelfPermission(RegistroUsuariosActivity.this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(RegistroUsuariosActivity.this,new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
                        }

                        photoUri = FileProvider.getUriForFile(RegistroUsuariosActivity.this,"com.example.control_inventario",photoFile);
                        tomarFoto.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                        startActivityForResult(tomarFoto,Request_TAKE_PHOTO);
                    }
                }else{
                    Toast.makeText(RegistroUsuariosActivity.this, "Fotografia, No entra al proceso", Toast.LENGTH_SHORT).show();
                }

            }
        });
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
            Toast.makeText(this, "imagen vacia", Toast.LENGTH_SHORT).show();
            validado = false;
        }

        if (!radUsuario.isChecked() && !radAdministrador.isChecked()){
            Toast.makeText(this, "Seleccione un tipo de usuario", Toast.LENGTH_SHORT).show();
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