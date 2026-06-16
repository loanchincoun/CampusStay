package com.example.projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.*;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ConnexionActivity extends AppCompatActivity {

    EditText etEmail ;
    EditText etPassword ;
    Button btnLogin ;
    Button btnRegister;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.connexion);
        etEmail = findViewById(R.id.txtEmail);
        etPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btn_connexion);
        btnRegister = findViewById(R.id.btn_addUser);
        db= FirebaseFirestore.getInstance();
        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> navigateInscription());
    }

    private void loginUser() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        CollectionReference utilisateursRef = db.collection("user");
        Query query = utilisateursRef
                .whereEqualTo("email", email)
                .whereEqualTo("motDePasse", password);
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // L'utilisateur existe et les deux conditions sont remplies
                            Toast.makeText(this, "Connexion réussi", Toast.LENGTH_SHORT).show();
                            Log.d("LOGIN", "Utilisateur trouvé !");
                            Intent myIntent = new Intent(ConnexionActivity.this, ListeLogementsActivity.class);
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(this, "Aucun utilisateur correspondant.", Toast.LENGTH_SHORT).show();
                            Log.d("LOGIN", "Aucun utilisateur correspondant.");
                        }
                    } else {
                        Log.e("LOGIN", "Erreur : ", task.getException());
                    }
                });
    }

    private void navigateInscription() {
        Intent myIntent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
        startActivity(myIntent);
    }
}