package com.example.projetandroid;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListeLogementsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LogementAdapter adapter;
    List<Logement> Logements;
    List<Logement> favoris;
    FirebaseFirestore db;
    FavorisDao favorisDao;
    Button btnFavoris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_logements);

        btnFavoris = findViewById(R.id.btnLstFavoris);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerLogements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //charger les logements depuis Firestore
        Logements = new ArrayList<>();

        //Charger les favoris depuis SQL lite
        favorisDao = new FavorisDao(this);
        if(isOnline()){
            loadLogements();
        }
        else {
            LoadFavoris();
        }

        //Afficher les favoris
        btnFavoris.setOnClickListener(v -> {
            LoadFavoris();
        });

    }

    private void loadLogements() {

        db.collection("logement")
                .get()
                .addOnSuccessListener(query -> {

                    Logements.clear();

                    for (DocumentSnapshot doc : query) {

                        Logement logement = doc.toObject(Logement.class);
                        logement.setId(doc.getId());

                        Logements.add(logement);
                    }

                    adapter.notifyDataSetChanged();
                });
        adapter = new LogementAdapter( this,Logements);
        recyclerView.setAdapter(adapter);

    }
    private void LoadFavoris (){
        favoris = favorisDao.getLstFavoris();
        adapter = new LogementAdapter(this, favoris);
        recyclerView.setAdapter(adapter);
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