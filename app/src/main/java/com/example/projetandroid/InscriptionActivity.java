package com.example.projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InscriptionActivity extends AppCompatActivity {

    EditText txtEmail ;
    EditText txtFullname ;
    EditText txtPassword ;
    EditText txtPhone ;
    Button btnRegister;
    Button btnCancelRegister;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.inscription);
        txtEmail = findViewById(R.id.txtGiveMail);
        txtFullname = findViewById(R.id.txtGiveName);
        txtPassword = findViewById(R.id.txtGivePassword);
        txtPhone = findViewById(R.id.txtGivePhone);
        db= FirebaseFirestore.getInstance();
        btnRegister = findViewById(R.id.btn_inscription);
        btnCancelRegister = findViewById(R.id.btn_cancel_inscription);
        btnRegister.setOnClickListener(v -> addUser());
        btnCancelRegister.setOnClickListener(v -> navigateInscription());
    }

    private void addUser() {
        String newEmail = txtEmail.getText().toString();
        String newFullname = txtFullname.getText().toString();
        String newPassword = txtPassword.getText().toString();
        String newPhone = txtPhone.getText().toString();

       if ( newPassword.isBlank() || newFullname.isBlank() || newEmail.isBlank() || newPhone.isBlank() ){
           Toast.makeText(this, "Paramètre manquant", Toast.LENGTH_SHORT).show();
        }
       else{
           Map<String, Object> newUser = new HashMap<>();
           newUser.put("email", newEmail );
           newUser.put("fullname", newFullname );
           newUser.put("password", newPassword );
           newUser.put("phone", newPhone );
           db.collection("user").document().set(newUser)
                   .addOnSuccessListener(unused -> {
                               Toast.makeText(this, "Utilisateur ajouté", Toast.LENGTH_SHORT).show();
                               Intent myIntent = new Intent(InscriptionActivity.this, ConnexionActivity.class);
                               startActivity(myIntent);
                           }
                   )
                   .addOnFailureListener(
                           unused ->
                                   Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show());
       }
    }

    private void navigateInscription() {
        Intent myIntent = new Intent(InscriptionActivity.this, ConnexionActivity.class);
        startActivity(myIntent);
    }
}