package com.example.control_inventario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.control_inventario.Objetos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText etCorreo, etPass;
    TextView tvRecuperar ;
    Button btnIniciar, btnCrear;

    private FirebaseAuth bdMauth;
    private FirebaseUser bdCurrentUser;
    DatabaseReference bdReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        asignaComponentes();

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistroUsuariosActivity.class));
                //crearUsuario(etCorreo.getText().toString(), etPass.getText().toString(), view);
            }
        });


        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logear(etCorreo.getText().toString(),etPass.getText().toString(),view);

            }
        });
    }

    public void asignaComponentes(){
        btnIniciar = findViewById(R.id.LOGbtnIniciar);
        etCorreo = findViewById(R.id.LOGetcorreo);
        etPass = findViewById(R.id.LOGetpass);
        btnCrear = findViewById(R.id.LOGbtncrear);
        bdMauth = FirebaseAuth.getInstance();
        bdCurrentUser = bdMauth.getCurrentUser();
        bdReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");
    }

    public void logear(String correo, String pass,View view){

        if (validarCampos()){
            bdMauth.signInWithEmailAndPassword(correo,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(LoginActivity.this, "Se ha logeado", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = bdMauth.getCurrentUser();
                        bdReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Usuario usuario = snapshot.getValue(Usuario.class);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("usuario",user.getUid());
                                //Bundle bundle = new Bundle();
                                //bundle.putSerializable("usuario",usuario);
                                //intent.putExtras(bundle);
                                startActivity(intent);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Correo no encontrado", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "Verifique los campos", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validarCampos () {
        boolean validado=true;
        String correo =etCorreo.getText().toString();
        String pass = etCorreo.getText().toString();
        if (correo.equals("")) {
            etCorreo.setError("correo obligatorio");
            validado = false;
        } else if (pass.equals("")) {
            etPass.setError("Contraseña obligatoria");
            validado = false;
        }
        return validado;
    }
}