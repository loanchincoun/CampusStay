package com.example.projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AjoutLogement extends AppCompatActivity {

    EditText txtNomLogement;
    EditText txtDescription;
    EditText txtAdresse;
    EditText txtSuperficie;
    EditText txtNbPieces;

    RadioButton rbParkingOui;
    RadioButton rbParkingNon;

    Button btnValider;
    Button btnAnnuler;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajout_logement);

        // Liaison XML -> Java (la partie chiante mais obligatoire)
        txtNomLogement = findViewById(R.id.txtNomLogement);
        txtDescription = findViewById(R.id.txtDescription);
        txtAdresse = findViewById(R.id.txtAdresse);
        txtSuperficie = findViewById(R.id.txtSuperficie);
        txtNbPieces = findViewById(R.id.txtNbPieces);

        rbParkingOui = findViewById(R.id.rbParkingOui);
        rbParkingNon = findViewById(R.id.rbParkingNon);

        btnValider = findViewById(R.id.btnValiderAnnonce);
        btnAnnuler = findViewById(R.id.btnAnnulerAnnonce);

        db = FirebaseFirestore.getInstance();

        btnValider.setOnClickListener(v -> addAnnonce());
        btnAnnuler.setOnClickListener(v -> cancelAnnonce());
    }

    private void addAnnonce() {

        String nomLogement = txtNomLogement.getText().toString().trim();
        String description = txtDescription.getText().toString().trim();
        String adresse = txtAdresse.getText().toString().trim();
        String superficie = txtSuperficie.getText().toString().trim();
        String nbPieces = txtNbPieces.getText().toString().trim();

        Boolean parking = null;
        if (rbParkingOui.isChecked()) parking = true;
        if (rbParkingNon.isChecked()) parking = false;

        // Vérification des champs (oui, on évite les annonces fantômes)
        if (nomLogement.isEmpty() ||
                description.isEmpty() ||
                adresse.isEmpty() ||
                superficie.isEmpty() ||
                nbPieces.isEmpty() ||
                parking == null) {

            Toast.makeText(this, "Merci de remplir tous les champs ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Création de l'annonce
        Map<String, Object> annonce = new HashMap<>();
        annonce.put("nomLogement", nomLogement);
        annonce.put("description", description);
        annonce.put("adresse", adresse);
        annonce.put("superficie", superficie);
        annonce.put("nbPieces", nbPieces);
        annonce.put("parking", parking);

        db.collection("logement")
                .add(annonce)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Annonce publiée ", Toast.LENGTH_SHORT).show();
                    finish(); // retour à l'écran précédent
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erreur lors de l'ajout ", Toast.LENGTH_SHORT).show()
                );
    }

    private void cancelAnnonce() {
        finish(); // ferme simplement l’activité
    }
}