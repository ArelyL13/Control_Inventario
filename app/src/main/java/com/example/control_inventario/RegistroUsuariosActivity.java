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

public class RegistroUsuariosActivity extends AppCompatActivity {
    Button btnRegistrarse,btnFoto;
    EditText etCorreo,etPass,etNombre,etTipo;


    ImageView ivFoto;



    private Uri photoUri;
    public static String currentPhotoPath,img="", tamanio, tipSoft, urgencia;
    public static final int Request_TAKE_PHOTO = 1;
    String urlimg;


    FirebaseAuth bdMauth;
    DatabaseReference dbReference;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios);
        asignarComponentes();
        tomarFoto();


        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegistroUsuariosActivity.this, "Iniciando proceso", Toast.LENGTH_LONG).show();
                //mAuth.signOut();
                bdMauth.createUserWithEmailAndPassword(etCorreo.getText().toString(),etPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            StorageReference folder = FirebaseStorage.getInstance().getReference().child("user");
                            StorageReference file_name = folder.child("file"+photoUri.getLastPathSegment());

                            file_name.putFile(photoUri).addOnSuccessListener(taskSnapshot -> file_name.getDownloadUrl()).addOnSuccessListener(uri -> {

                                /**
                                String imagen = file_name.getDownloadUrl().toString();
                                Log.d("direccion1",file_name.getDownloadUrl().toString());
                                Log.d("direccion2",file_name.toString());
                                Log.d("direccion3",file_name.getActiveDownloadTasks().toString());
                                Log.d("direccion4",file_name.getPath());

                                        Usuario us = new Usuario();
                                        us.setId(bdMauth.getUid());
                                        us.setNombre(etNombre.getText().toString().trim());
                                        us.setCorreo(etCorreo.getText().toString().trim());
                                        us.setPass(etPass.getText().toString().trim());
                                        us.setTipo(etTipo.getText().toString().trim());
                                        us.setFoto(imagen);
                                        Bundle extras = RegistroUsuariosActivity.this.getIntent().getExtras();
                                        //String usuario = extras.getString("id_usuario");
                                        Toast.makeText(RegistroUsuariosActivity.this, us.getFoto(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(RegistroUsuariosActivity.this, urlimg, Toast.LENGTH_SHORT).show();
                                        dbReference.child("Usuario").child(us.getId()).setValue(us);
                                        Toast.makeText(RegistroUsuariosActivity.this, "Proyecto agregado", Toast.LENGTH_LONG).show();
                                    **/



                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegistroUsuariosActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if ( task.isSuccessful() ) {
                                        UploadTask.TaskSnapshot downloadUri = null;
                                        downloadUri = task.getResult();
                                        Task<Uri> result = downloadUri.getStorage().getDownloadUrl();

                                         Usuario us = new Usuario();
                                         us.setId(bdMauth.getUid());
                                         us.setNombre(etNombre.getText().toString().trim());
                                         us.setCorreo(etCorreo.getText().toString().trim());
                                         us.setPass(etPass.getText().toString().trim());
                                         us.setTipo(etTipo.getText().toString().trim());
                                         us.setFoto(result.toString());


                                         Bundle extras = RegistroUsuariosActivity.this.getIntent().getExtras();
                                         //String usuario = extras.getString("id_usuario");
                                         Toast.makeText(RegistroUsuariosActivity.this, us.getFoto(), Toast.LENGTH_SHORT).show();
                                         Toast.makeText(RegistroUsuariosActivity.this, urlimg, Toast.LENGTH_SHORT).show();
                                         dbReference.child("Usuario").child(us.getId()).setValue(us);
                                         Toast.makeText(RegistroUsuariosActivity.this, "Proyecto agregado", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });





                            Toast.makeText(RegistroUsuariosActivity.this, "Registro Finalizado", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegistroUsuariosActivity.this, "Error con la bd", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });






    }

    private void asignarComponentes(){
        btnRegistrarse = findViewById(R.id.REGbtnRegistrarse);
        btnFoto = findViewById(R.id.REGbtnFoto);

        etCorreo = findViewById(R.id.REGetcorreo);
        etNombre = findViewById(R.id.REGetnombre);
        etPass = findViewById(R.id.REGetpass);
        etTipo = findViewById(R.id.REGetTipo);

        bdMauth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference();


        ivFoto = findViewById(R.id.REGivFoto);

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


}