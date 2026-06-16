package com.example.projetandroid;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LogementAdapter extends RecyclerView.Adapter<LogementAdapter.ViewHolder> {

    private List<Logement> Logements;
    private Context context;

    public LogementAdapter(Context context, List<Logement> logements) {
        this.context = context;
        this.Logements = logements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_annonce, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Logement a = Logements.get(position);

        holder.nom.setText(a.getNomLogement());
        holder.adresse.setText(a.getAdresse());

        String details = a.getSuperficie() + " m² • "
                + a.getNbPieces() + " pièces • "
                + (Boolean.TRUE.equals(a.getParking()) ? "Avec Parking " : "Sans parking");

        holder.details.setText(details);

        holder.itemView.setOnClickListener(v -> {

            //  ID Firestore du logement cliqué
            String logementId = a.getId();
            Log.d("DETAIL_LOGEMENT", "ID reçu : " + logementId);

            Intent intent = new Intent(context, LogementDetailActivity.class);
            intent.putExtra("LOGEMENT_ID", logementId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return Logements.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nom, adresse, details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.txtItemNom);
            adresse = itemView.findViewById(R.id.txtItemAdresse);
            details = itemView.findViewById(R.id.txtItemDetails);
        }
    }
}

