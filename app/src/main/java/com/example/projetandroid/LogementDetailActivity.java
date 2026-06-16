package com.example.projetandroid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;


public class LogementDetailActivity extends AppCompatActivity {
    private TextView nom;
    private TextView adresse;
    private TextView superficie;
    private TextView pieces;
    private TextView parking;
    private TextView edtCommentaire;
    private TextView txtMoyenne;
    FirebaseFirestore db;
    Button btnEnvoyerAvis;
    Button btnRetour;
    RatingBar ratingBar;
   List<Avis> avisList ;
    Button btnFavori;
    FavorisDao favorisDao;
    boolean estFavori;
    Logement logementActuel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logement_detail);

        nom = findViewById(R.id.detailNom);
        adresse = findViewById(R.id.detailAdresse);
        superficie = findViewById(R.id.detailSuperficie);
        pieces = findViewById(R.id.detailPieces);
        parking = findViewById(R.id.detailParking);
        db= FirebaseFirestore.getInstance();
        btnEnvoyerAvis = findViewById(R.id.btnEnvoyerAvis);
        ratingBar = findViewById(R.id.ratingBar);
        edtCommentaire = findViewById(R.id.txtCommentaire);
        txtMoyenne = findViewById(R.id.txtMoyenne);
        btnFavori = findViewById(R.id.btnFavori);
        favorisDao = new FavorisDao(this);
        btnRetour = findViewById(R.id.btnRetour);
        //récupération de l'ID
        String logementId = getIntent().getStringExtra("LOGEMENT_ID");
        Log.d("DETAIL_LOGEMENT", "ID reçu : " + logementId);

        if (logementId != null) {
            chargerLogementFirestore(logementId);
        }

        //Envoyer des avis
        btnEnvoyerAvis.setOnClickListener(v -> {
            addAvis(logementId);
        });

        // récupérer les avis
        avisList = new ArrayList<>();
        listAvis(logementId);

        // Vérifier état favori
        estFavori = favorisDao.estFavori(logementId);
        updateFavoriButton();

        // Charger données (online ou offline)
        if (isOnline()) {
            chargerLogementFirestore(logementId);
        } else {
            logementActuel = favorisDao.getFavori(logementId);
            if (logementActuel != null) {
                chargerLogementLocal(logementActuel);
            }
        }

        //revenir en arrière
        btnRetour.setOnClickListener(v -> {
            finish();
        });

    }
    private void addAvis(String logementId){
        int note = (int) ratingBar.getRating();
        String commentaire = edtCommentaire.getText().toString();

        Map<String, Object> avis = new HashMap<>();
        avis.put("note", note);
        avis.put("commentaire", commentaire);

        db.collection("logement")
                .document(logementId)
                .collection("avis")
                .add(avis)
                .addOnSuccessListener(docRef -> {
                    Log.d("TAG", "Avis ajouté avec succès. ID du document : " + docRef.getId());
                    ratingBar.setRating(0);
                    edtCommentaire.setText("");
                    Toast.makeText(this, "Avis envoyé", Toast.LENGTH_SHORT).show();
                })
        ;
    }
    private void chargerLogementFirestore(String logementId) {
        db.collection("logement")
                .document(logementId)
                .get()
                .addOnSuccessListener(doc -> {
                    Log.d("DETAIL_LOGEMENT", "onSuccess appelé");
                    if (doc.exists()) {
                        Log.d("DETAIL_LOGEMENT", "Document trouvé : " + doc.getId());
                        Logement logement = doc.toObject(Logement.class);

                        nom.setText(logement.getNomLogement());
                        adresse.setText("Adresse : " + logement.getAdresse());
                        superficie.setText("Superficie : " + logement.getSuperficie() + " m²");
                        pieces.setText("Nombre de pièces : " + logement.getNbPieces());
                        parking.setText("Parking : " + (Boolean.TRUE.equals(logement.getParking()) ? "Oui" : "Non"));
                    }
                    else {
                        Log.e("DETAIL_LOGEMENT", "Logement = null après toObject()");
                    }
                });

    }
    private void chargerLogementLocal(Logement logement) {
        nom.setText(logement.getNomLogement());
        adresse.setText(logement.getAdresse());
        superficie.setText(logement.getSuperficie() + " m²");
        pieces.setText(logement.getNbPieces() + " pièces");
        parking.setText(logement.getParking() ? "Parking : Oui" : "Parking : Non");
    }


    private void listAvis(String logementId){
        db.collection("logement")
                .document(logementId)
                .collection("avis")
                .get()
                .addOnSuccessListener(query -> {

                    avisList.clear();

                    for (DocumentSnapshot doc : query) {
                        Avis avis = doc.toObject(Avis.class);
                        if (avis != null) {
                            avisList.add(avis);
                        }
                    }
                    //calculer la moyenne
                    moyenneAvis(avisList);
                });
    }

    private void moyenneAvis(List<Avis> avisLst){

        double total = 0;
        for (Avis avis : avisLst) {
            total += avis.getNote();
        }
        double moyenne = total / avisLst.size();
        txtMoyenne.setText("Note moyenne : " + moyenne + "/5");
    }
    private void updateFavoriButton() {
        btnFavori.setText(
                estFavori ? "Supprimer des favoris" : "Ajouter aux favoris"
        );
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) return false;

        NetworkCapabilities capabilities =
                cm.getNetworkCapabilities(cm.getActiveNetwork());

        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

}